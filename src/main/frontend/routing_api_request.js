async function getRoute(marker_lng, marker_lat, station_lng, station_lat, profile, station_name) {


    const query = await fetch(
        `https://api.mapbox.com/directions/v5/mapbox/${profile}/${marker_lng},${marker_lat};${station_lng},${station_lat}?steps=true&language=de&geometries=geojson&access_token=${mapboxgl.accessToken}`,
        { method: 'GET' }
    );
    const json = await query.json();
    const data = json.routes[0];
    const route = data.geometry.coordinates;
    const instructions = document.getElementById('navig_steps_listgroup');
    const steps = data.legs[0].steps;
    const geojson = {
        type: 'Feature',
        properties: {},
        geometry: {
            type: 'LineString',
            coordinates: route
        }
    };


    while (instructions.hasChildNodes()) {
        instructions.removeChild(instructions.firstChild);
    }

    for (let index = 0; index < steps.length -1; index++) {
        let numbering = index + 1;
        let li = document.createElement('li');
        li.className = 'list-group-item';
        li.appendChild(document.createTextNode(numbering.toString() + '. ' + steps[index].maneuver.instruction));
        instructions.appendChild(li);
    }

    train_route = await getRouteFromProcessingService(station_name);
    console.log(train_route);
    if (train_route.status == 'ok') {
        
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let start_station = train_route.route.startStation.name;
        let instruction_start_station = '  Sie haben den Bahnhof ' + start_station + ' erreicht.';
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-train";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions.appendChild(li_start);

        let li_line = document.createElement('li');
        li_line.className = 'list-group-item';
        let start_line = train_route.route.changes[0].targetLine.name;
        let instruction_line = '  Steigen Sie in die Linie ' + start_line + ' ein.';
        let entry_icon = document.createElement('i');
        entry_icon.className = "fa fa-sign-in";
        entry_icon.style = "font-size:18px";
        li_line.appendChild(entry_icon)
        li_line.appendChild(document.createTextNode(instruction_line));
        instructions.appendChild(li_line);

        
        for (const change of train_route.route.effectiveChanges) {
            let li_change = document.createElement('li');
            li_change.className = 'list-group-item';
            let change_station = change.station.name;
            let change_line = change.targetLine.name;
            let instruction_change = '  Steigen Sie in ' + change_station + ' in die Linie ' + change_line + ' um.';
            let change_icon = document.createElement('i');
            change_icon.className = "fa fa-retweet";
            change_icon.style = "font-size:18px";
            li_change.appendChild(change_icon)
            li_change.appendChild(document.createTextNode(instruction_change));
            instructions.appendChild(li_change);
        }

        let li_target = document.createElement('li');
        li_target.className = 'list-group-item';
        let target_station = train_route.route.finalStop.name;
        let instruction_target = '  Sie haben das Oberzentrum ' + target_station + ' erreicht.';
        let flag_icon = document.createElement('i');
        flag_icon.className = "fa fa-flag";
        flag_icon.style = "font-size:18px";
        li_target.appendChild(flag_icon);
        li_target.appendChild(document.createTextNode(instruction_target));
        instructions.appendChild(li_target);


    }





    // if the route already exists on the map, we'll reset it using setData
    if (map.getSource('route')) {
        map.getSource('route').setData(geojson);
    }
    // otherwise, we'll make a new request
    else {
        map.addLayer({
            id: 'route',
            type: 'line',
            source: {
                type: 'geojson',
                data: geojson
            },
            layout: {
                'line-join': 'round',
                'line-cap': 'round'
            },
            paint: {
                'line-color': '#3887be',
                'line-width': 5,
                'line-opacity': 0.75
            }
        });


    }


}

async function getRouteFromProcessingService(stationName) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            startStation: stationName,
            target: "oberzentrum" // "mittelzentrum"
        })
    }

    const req = new Request("http://localhost:8000/routing/calculate");

    const route = await fetch(req, init).then((response) => {
        if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
        return response.json();
    });

    return route;
}

// structure of the processing service response
const exampleRoute = {
    status: "ok",
    route: {
        // normal route info
        startStation: {
            name: "Freiberg(Sachs)",
            eva: "",
            mittelzentrum: true,
            oberzentrum: false
        },
        finalStop: {
            name: "Chemnitz(Hbf)",
            eva: "",
            mittelzentrum: false,
            oberzentrum: true
        },
        changes: [
            {
                station: {
                    name: "Freiberg(Sachs)",
                    eva: "",
                    mittelzentrum: true,
                    oberzentrum: false
                },
                targetLine: {
                    name: "3"
                },
                trainEntry: true
            }
        ],
        effectiveChanges: []
    }
}
