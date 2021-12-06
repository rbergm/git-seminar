const init = {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
        startStation: "Freiberg(Sachs)",
        target: "oberzentrum"
    })
}

const req = new Request("http://localhost:8000/routing/calculate");

fetch(req, init).then((resp) => {
    if (!resp.ok) {
        throw new Error(`Error: ${resp.status} - ${resp.statusText}`);
    }
    return resp.json();
}).then((route) => console.log(route));
