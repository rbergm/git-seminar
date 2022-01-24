async function getRoute(marker_lng, marker_lat, station_lng, station_lat, profile, station_name) {


    const query = await fetch(
        `https://api.mapbox.com/directions/v5/mapbox/${profile}/${marker_lng},${marker_lat};${station_lng},${station_lat}?steps=true&language=de&geometries=geojson&access_token=${mapboxgl.accessToken}`,
        { method: 'GET' }
    );
    const json = await query.json();
    const data = json.routes[0];
    const route = data.geometry.coordinates;
    const instructions = document.getElementById('navig_steps_listgroup');
    const instructions_mz = document.getElementById('navig_steps_listgroup_mz');
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

    train_route = await getRouteFromProcessingService(station_name, "oberzentrum");
    train_route_mz = await getRouteFromProcessingService(station_name, "mittelzentrum");
    console.log(train_route);
    console.log(train_route_mz);
    if (train_route.status == 'ok') {

        for (let index = 0; index < steps.length -1; index++) {
            let numbering = index + 1;
            let li = document.createElement('li');
            li.className = 'list-group-item';
            li.appendChild(document.createTextNode(numbering.toString() + '. ' + steps[index].maneuver.instruction));
            instructions.appendChild(li);
        }

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


    } else if (train_route.status == "start-matches-target") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Bahnhof ist bereits ein Oberzentrum";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-train";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions.appendChild(li_start);
    } else if (train_route.status == "no-route") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Keine Route gefunden";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions.appendChild(li_start);
    }  else if (train_route.status == "station-not-found") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Haltepunkt wurde in der Datenbank nicht gefunden";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions.appendChild(li_start);
    }  else {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Ein unbekannter Fehler ist aufgetreten";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions.appendChild(li_start);
    }

    //same functions for Mittelzentren route

    while (instructions_mz.hasChildNodes()) {
        instructions_mz.removeChild(instructions_mz.firstChild);
    }

    if (train_route_mz.status == 'ok') {

        for (let index = 0; index < steps.length -1; index++) {
            let numbering = index + 1;
            let li = document.createElement('li');
            li.className = 'list-group-item';
            li.appendChild(document.createTextNode(numbering.toString() + '. ' + steps[index].maneuver.instruction));
            instructions_mz.appendChild(li);
        }

        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let start_station = train_route_mz.route.startStation.name;
        let instruction_start_station = '  Sie haben den Bahnhof ' + start_station + ' erreicht.';
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-train";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions_mz.appendChild(li_start);

        let li_line = document.createElement('li');
        li_line.className = 'list-group-item';
        let start_line = train_route_mz.route.changes[0].targetLine.name;
        let instruction_line = '  Steigen Sie in die Linie ' + start_line + ' ein.';
        let entry_icon = document.createElement('i');
        entry_icon.className = "fa fa-sign-in";
        entry_icon.style = "font-size:18px";
        li_line.appendChild(entry_icon)
        li_line.appendChild(document.createTextNode(instruction_line));
        instructions_mz.appendChild(li_line);


        for (const change of train_route_mz.route.effectiveChanges) {
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
            instructions_mz.appendChild(li_change);
        }

        let li_target = document.createElement('li');
        li_target.className = 'list-group-item';
        let target_station = train_route_mz.route.finalStop.name;
        let instruction_target = '  Sie haben das Mittelzentrum ' + target_station + ' erreicht.';
        let flag_icon = document.createElement('i');
        flag_icon.className = "fa fa-flag";
        flag_icon.style = "font-size:18px";
        li_target.appendChild(flag_icon);
        li_target.appendChild(document.createTextNode(instruction_target));
        instructions_mz.appendChild(li_target);


    } else if (train_route_mz.status == "start-matches-target") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Bahnhof ist bereits ein Mittelzentrum";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-train";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions_mz.appendChild(li_start);
    } else if (train_route_mz.status == "no-route") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Keine Route gefunden";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions_mz.appendChild(li_start);
    }  else if (train_route_mz.status == "station-not-found") {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Haltepunkt wurde in der Datenbank nicht gefunden";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions_mz.appendChild(li_start);
    }  else {
        let li_start = document.createElement('li');
        li_start.className = 'list-group-item';
        let instruction_start_station = "   Ein unbekannter Fehler ist aufgetreten";
        let textnode = document.createTextNode(instruction_start_station);
        let train_icon = document.createElement('i');
        train_icon.className = "fa fa-times";
        train_icon.style = "font-size:18px";
        li_start.appendChild(train_icon);
        li_start.appendChild(textnode);
        instructions_mz.appendChild(li_start);
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

async function getRouteFromProcessingService(stationName, target) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            startStation: stationName,
            target: target // "mittelzentrum"
        })
    }

    const req = new Request("http://localhost:8000/routing/calculate");

    const route = await fetch(req, init).then((response) => {
        if (!response.ok) {
            console.log(`Error loading route: ${response.status} - ${response.statusText}`);
        }
        return response.json();
    });

    return route;
}
