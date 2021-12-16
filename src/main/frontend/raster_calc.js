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
        console.log(response);
        return response.json();
    });
    console.log(layer);
    let url= layer.geoserverUrl;
    // if (map.getSource('weighted-raster-source')) {
    //     console.log( map.getSource('weighted-raster-source'));
    //     // console.log( map.getLayer('weighted-raster-layer'));
    //     // map.removeLayer('weighted-raster-layer');
    //     map.removeSource('weighted-raster-source');    
    //     console.log( map.getSource('weighted-raster-source'));
    //     // console.log( map.getLayer('weighted-raster-layer'));
    // }
    // map.addSource('weighted-raster-source', {
    //     'type': 'raster', 
    //     'tiles': [
    //         `http://localhost:8080/geoserver/gwc/service/wmts?REQUEST=GetTile&SERVICE=WMTS&VERSION=1.0.0&LAYER=V-GDI:${url}&STYLE=&TILEMATRIX=EPSG:900913:{z}&TILEMATRIXSET=EPSG:900913&FORMAT=image/png&TILECOL={x}&TILEROW={y}`
    //     ],
    //     'minZoom': 0,
    //     'maxZoom': 14
    // })
    // console.log( map.getSource('weighted-raster-source'));

    //     // map.addLayer({
    //     //     'id': 'weighted-raster-layer',
    //     //     'type': 'raster',
    //     //     'source': 'weighted-raster-source',
    //     // 
    //     // });
    

    
}

