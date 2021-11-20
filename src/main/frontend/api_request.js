async function get_time_table(eva, plus_x_hours) {

  let DateTime = new Date(); // current DateTime
  DateTime.setHours(DateTime.getHours() + plus_x_hours);  // Adding user input "in how many hours do you want to depart?"
  let Day = (DateTime.getDate() < 10 ? '0' : '') + (DateTime.getDate().toString()); // add leading 0 to numbers < 10
  let Month = DateTime.getMonth() + 1; // jan = 0
  Month = Month.toString();
  let Year = DateTime.getFullYear().toString().substring(2); // 2 digits representation of year
  let time = (DateTime.getHours() < 10 ? '0' : '') + (DateTime.getHours().toString()); // add leading 0 to numbers < 10


  let date = Year+Month+Day; // concatanate to YYMMDD
  console.log(date);
  console.log(time);

  let url = "https://api.deutschebahn.com/timetables/v1/plan/" + eva +"/" + date +"/" + time;

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
  
      let stops = xmlDoc.getElementsByTagName('s');         // s = stop(Haltestelle)

      let stopsList = [];    

      for (let i = 0; i < stops.length; i++) {
        let stop = [],
        category = stops[i].getElementsByTagName('tl')[0].getAttribute('c'),   // ICE, RE, S, Bus,... 
        departure = stops[i].getElementsByTagName('dp')[0];             

        if (departure != undefined) {                                          // only consider departure, no arrivals
          let depTime = departure.getAttribute('pt'),                          // YYMMDDHHmm
          platform = departure.getAttribute('pp'),                             
          plannedPath = departure.getAttribute('ppth');

          stop.push(category, depTime, platform, plannedPath)
          stopsList.push(stop)                                                 // append stop[i] information (strings) to stoplist
        }     
      }
    
      return stopsList;
    
  })
  .then(function(timetables) { 

    let table = document.getElementsByTagName("tbody")[0];

    console.log(timetables);
    for (let index = 0; index < timetables.length; index++) {
     
      let traintype = timetables[index][0];
      let datetime = timetables[index][1];
      let platform = timetables[index][2];
      let route = timetables[index][3];

      let new_text= traintype + " auf Gleis " + platform + " um " + datetime + " richtung " +(route.substring(route.lastIndexOf("|")+1));

      let row = table.insertRow();
      let cell = row.insertCell();
  
      let newText = document.createTextNode(new_text);
      cell.appendChild(newText);
      

      document.getElementById("params_div").style.display = "inline";
      // $("#params_div").show();

    }
  
  
    
  });

  // print in nice format 
}