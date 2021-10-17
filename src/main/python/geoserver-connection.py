#!/usr/env/python

import requests

geoserver_base_url = "http://localhost:8080/geoserver/rest"
auth = ("admin", "geoserver")

geoserver_ws = requests.get(geoserver_base_url + "/workspaces", auth=auth).json()
workspaces = [ws for ws in geoserver_ws["workspaces"]["workspace"]]
print("Available workspaces:", workspaces)
print("Using workspace", workspaces[0]["name"])

ws_base_url = geoserver_base_url + "/workspaces/" + workspaces[0]["name"]
print("Setting base URL", ws_base_url)

geoserver_ds = requests.get(ws_base_url + "/datastores", auth=auth).json()
print("Available datastores:", geoserver_ds)
datastores = [ds for ds in geoserver_ds["dataStores"]["dataStore"]]

ds_info = [requests.get(ds["href"], auth=auth).json()["dataStore"] for ds in datastores]

shapefile_ds = [ds for ds in ds_info if ds["type"] == "Shapefile"]

print("Shapefile datastores", [ds["name"] for ds in shapefile_ds])

shape_ds = shapefile_ds[0]
shape_urls = [par["$"] for par in shape_ds["connectionParameters"]["entry"] if par["@key"] == "url"]

print(shape_ds["name"], shape_urls)

print(requests.get(ws_base_url + "/datastores/" + shape_ds["name"], auth=auth, headers={"type:": "application/zip"}))
