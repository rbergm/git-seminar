let reqData = {
    departureFrequencyWeight: 5,
    mittelzentrumDistanceWeight: 7,
    oberzentrumDistanceWeight: 9,
    stationDistanceWeight: 3,
    populationDataWeight: 1
};

let init = {
    method: "POST",
    headers: { "Content-type": "application/json" },
    body: JSON.stringify(reqData)
};



let req = new Request("http://localhost:8000/raster/calculate");


const route = await fetch(req, init).then((response) => {
    if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
    }
    return response.json();
});
