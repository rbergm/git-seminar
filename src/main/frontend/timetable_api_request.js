async function get_time_table(eva, name, plus_x_hours) {

  document.getElementById('departure_table_head').innerHTML = 'Abfahrten von ' + name;

  map.setPaintProperty('haltestellen-layer', 'circle-stroke-opacity', ['match', ['get', 'EVA_NR'], eva, 1, 0]);
  map.setPaintProperty('haltestellen-layer', 'circle-opacity', ['match', ['get', 'EVA_NR'], eva, 1, 0.6]);
  map.setPaintProperty('haltestellen-layer', 'circle-color', ['match', ['get', 'EVA_NR'], eva, 'darkred', 'red']);
  
  let DateTime = new Date(); // current DateTime
  DateTime.setHours(DateTime.getHours() + plus_x_hours);  // Adding user input "in how many hours do you want to depart?"
  let Day = (DateTime.getDate() < 10 ? '0' : '') + (DateTime.getDate().toString()); // add leading 0 to numbers < 10
  let Month = DateTime.getMonth() + 1; // jan = 0
  Month = Month.toString();
  let Year = DateTime.getFullYear().toString().substring(2); // 2 digits representation of year
  let time = (DateTime.getHours() < 10 ? '0' : '') + (DateTime.getHours().toString()); // add leading 0 to numbers < 10
  let date = Year+Month+Day; // concatanate to YYMMDD
  let url  = "https://api.deutschebahn.com/timetables/v1/plan/" + eva +"/" + date +"/" + time;

await fetch(url, {
    headers: {
      Accept: "application/xml",
      Authorization: "Bearer fc4f82d6be092177015ebbb25b56563a" // API Key
    }
  })
  .then(function(resp) {        // handles the API GET-response
        return resp.text();     // get the Body of the Response (omit meta data)
  }) 
  .then(function(data){
      let parser = new DOMParser();
      let xmlDoc = parser.parseFromString(data, 'text/xml');
      let stops  = xmlDoc.getElementsByTagName('s');         // s = stop(Haltestelle)
    
      let stopsList = [];    

      for (let i = 0; i < stops.length; i++) {
        let stop  = [],
        category  = stops[i].getElementsByTagName('tl')[0].getAttribute('c'),   // ICE, RE, S, Bus,... 
        departure = stops[i].getElementsByTagName('dp')[0];             

        if (departure != undefined) {                                          // only consider departure, no arrivals
          let depTime = departure.getAttribute('pt'),                          // YYMMDDHHmm
          platform    = departure.getAttribute('pp'),                             
          plannedPath = departure.getAttribute('ppth');

          stop.push(category, depTime, platform, plannedPath)
          stopsList.push(stop)                                                 // append stop[i] information (strings) to stoplist
        }     
      }
    
      return stopsList;

    
  })
  .then(function(timetables) { 
    
    let table = document.getElementsByTagName("tbody")[0];
    while(table.hasChildNodes()){
      table.removeChild(table.firstChild);
    }
    
    timetables.sort((a, b) => {
      if (a[1]> b[1]) {
          return 1;
      }
      if (a[1] < b[1]) {
          return -1;
      }
      return 0;
    });

    for (let index = 0; index < timetables.length; index++) {


      
      let traintype = timetables[index][0];
      let datetime  = timetables[index][1];
      let platform  = timetables[index][2];
      let route     = timetables[index][3]
      
      let formatted_date = datetime.substring(4,6) + '.' + datetime.substring(2,4) + '.' + datetime.substring(0,2);
      let formatted_time = datetime.substring(6,8) + ':' + datetime.substring(8);

      let tr_text= traintype + " auf Gleis " + platform + ' am ' + formatted_date + " um " + formatted_time + " Richtung " + (route.substring(route.lastIndexOf("|")+1));

      let row = table.insertRow();
      row.id = 'row_departure_table';
      let hidden_row = table.insertRow();
      hidden_row.style.display = 'none'
      
      let cell         = row.insertCell();
      let hidden_cell  = hidden_row.insertCell();
      let tr_textnode  = document.createTextNode(tr_text);
      let hidden_route = document.createTextNode(route);

      row.onclick = function() {
        if (hidden_row.style.display == 'none') {
          hidden_row.style.display = 'inline';
        }else if (hidden_row.style.display == 'inline'){
          hidden_row.style.display = 'none';
        }
      };
    
      cell.appendChild(tr_textnode);
      hidden_cell.appendChild(hidden_route);

    }
  
  });

}