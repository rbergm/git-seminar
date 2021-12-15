async function raster_calc() {
    let sliders = document.getElementById('slider_div').getElementsByTagName('input');

    let frequency  = parseInt(sliders[0].value),
    prox_oz  = parseInt(sliders[1].value),
    prox_mz       = parseInt(sliders[2].value),
    prox_station = parseInt(sliders[3].value);
    count_citizens = parseInt(sliders[4].value);

    reqData = {
            departureFrequencyWeight: frequency,
            mittelzentrumDistanceWeight: prox_oz,
            oberzentrumDistanceWeight: prox_mz,
            stationDistanceWeight: prox_station,
            populationDataWeight: count_citizens
    }

    console.log(reqData);

    let init = {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify(reqData)
    };
    
    let req = new Request("http://localhost:8000/raster/calculate");
    
    const layer = await fetch(req, init).then((response) => {
        if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
        return response.json();
    });
    console.log(layer);
    url_end = layer.geoserverUrl;
    
    // structure of response
    
    if (map.getSource('weighted-raster-source')) {
        map.removeSource('weighted-raster-source');
    }












    console.log('vor add source');

    map.addSource('weighted-raster-source', {
        'type': 'raster', 
        'tiles': ['http://localhost:8080/geoserver/V-GDI/wms?service=WMS&version=1.1.0&request=GetMap&layers=raster_depf4.0_mzdist6.0_ozdist10.0_statdist10.0_pop10.0&bbox={bbox-epsg-25833}&width=768&height=571&srs=EPSG:25833&styles=&transparent=true&format=image/png'],
        'tileSize': 256
    })
    console.log(map.getSource('weighted-raster-source'));

    map.addLayer({
        'id': 'weighted-raster-layer',
        'type': 'raster',
        'source': 'weighted-raster-source',

        'paint': {
        }
    });

    console.log('nach add layer');


    // map.addSource("wms-test-source", {
    //     "type": 'image',

    //     "url": "http://localhost:8080/geoserver/V-GDI/wms?service=WMS&version=1.1.0&request=GetMap&layers=V-GDI%3Araster_depf5.0_mzdist7.0_ozdist9.0_statdist3.0_pop8.0&bbox=278075.5482%2C5560939.7909%2C503075.5482%2C5728439.7909&width=768&height=571&transparent=true&srs=EPSG%3A25833&styles=&format=image%2Fpng",
    //             'coordinates': [
    //         [11.7909799230130741, 51.7068931722358016],
    //         [15.0453037544106447,51.7068931722358016],
    //         [15.0453037544106447, 50.1560044713354003],
    //         [11.7909799230130741,50.1560044713354003]
    //     ]

        
    // }); 

//     map.addLayer(
//         {
//             "id": 'wms-test-layer',
//             "type": 'raster',
//             "source": 'wms-test-source',
//             "paint": {
//             }
//         }                
//     );
}