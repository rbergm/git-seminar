function get_time_table(eva, date, time) {

  let url = "https://api.deutschebahn.com/timetables/v1/plan/" + eva +"/" + date +"/" + time;


  fetch(url, {
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
      xmlDoc = parser.parseFromString(data, 'text/xml');
  
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
      
      console.log(stopsList)
  });
}
