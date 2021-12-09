function raster_calc() {
    let sliders = document.getElementById('slider_div').getElementsByTagName('input');
    
    let proximity  = parseInt(sliders[0].value),
    count_changes  = parseInt(sliders[1].value),
    duration       = parseInt(sliders[2].value),
    count_citizens = parseInt(sliders[3].value);

    request_params = {
        weights: {
            proximity: proximity,
            count_changes: count_changes,
            duration: duration,
            count_citizens: count_citizens
        }
            
    }

    console.log(request_params);
}