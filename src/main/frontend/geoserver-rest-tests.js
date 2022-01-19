/*
 * geoserver-rest-tests.js
 *
 */

// basic inputs
const geoserverAddress = document.getElementById('geoserver-address');
const workspaceName = document.getElementById('workspace-name');

// buttons
const loadCoverageStores = document.getElementById('fetch-coverage-stores');
const loadStoreDetails = document.getElementById('fetch-coverage-store-details');

// form data
const coverageStoreName = document.getElementById('coverage-store-name');

// result containers
const storeOverview = document.getElementById('coverage-stores-content');
const storeDetails = document.getElementById('coverage-store-details-content');

// actions
const requestHeaders = new Headers();
requestHeaders.append('Accept', 'application/json');
const requestInit = {
    method: 'GET',
    headers: requestHeaders
};

async function fetchAvailableCoverageStores(geoserverAddress, workspaceName) {
    const finalAddress = `${geoserverAddress}/workspaces/${workspaceName}/coverages`;
    const storesReq = new Request(finalAddress);
    fetch(storesReq, requestInit).then(resp => resp.json()).then(resp => storeOverview.textContent = JSON.stringify(resp, null, 2));
}

async function fetchCoverageStoreDetails(geoserverAddress, workspaceName, storeName) {
    const finalAddress = `${geoserverAddress}/workspaces/${workspaceName}/coverages/${storeName}.json`;
    const detailsReq = new Request(finalAddress);
    fetch(detailsReq, requestInit).then(resp => resp.json()).then(resp => storeDetails.textContent = JSON.stringify(resp, null, 2));
}

// action handlers
loadCoverageStores.addEventListener('click', ev => fetchAvailableCoverageStores(geoserverAddress.value, workspaceName.value));
loadStoreDetails.addEventListener('click', ev => fetchCoverageStoreDetails(geoserverAddress.value, workspaceName.value, coverageStoreName.value));
