async function getRoute(marker_lng, marker_lat, station_lng, station_lat, profile) {


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


    while(instructions.hasChildNodes()){
        instructions.removeChild(instructions.firstChild);
    }
    for (const step of steps) {

            let li = document.createElement('li');
            li.className = 'list-group-item';
            li.appendChild(document.createTextNode(step.maneuver.instruction));
            instructions.appendChild(li);             
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

