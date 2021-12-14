let reqData = {
    departureFrequencyWeight: 5,
    mittelzentrumDistanceWeight: 7,
    oberzentrumDistanceWeight: 9,
    stationDistanceWeight: 3,
    populationDataWeight: 8
};

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

// structure of response

let response = {
    status: "created", // "created" or "cached"
    geoserverUrl: "raster_depf2.0_mzdist7.0_ozdist9.0_statdist3.0_pop1.0" // ID of the WMS/WCS layer
}
