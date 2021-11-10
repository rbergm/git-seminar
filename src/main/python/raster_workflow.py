## regelmaessiges punktgitter über sachsen (bbox)
points = processing.run("qgis:regularpoints", {
'EXTENT':'278325.548200000,502920.016800000,5561091.335200000,5728189.790900000 [EPSG:25833]',
'SPACING':500,
'INSET':0,
'RANDOMIZE':False,
'IS_SPACING':True,
'CRS':QgsCoordinateReferenceSystem('EPSG:25833'),
'OUTPUT':'memory:'})['OUTPUT']

## auf ausdehnung clippen
clip_points = processing.run("native:clip", {
'INPUT':points,
'OVERLAY':'D:/Uni/GID/Data/Sachsen_RasterClipper.shp',
'OUTPUT':'memory:'})['OUTPUT']

## polygone erzeugen: quadratische puffer um punkte (250m => 500 polyone) 
polygons = processing.run("native:buffer", {
'INPUT': clip_points,
'DISTANCE':250,
'SEGMENTS':5,
'END_CAP_STYLE':2,
'JOIN_STYLE':1,
'MITER_LIMIT':2,
'DISSOLVE':False,
'OUTPUT':'memory:'})['OUTPUT']

## nearest neighbour von jedem polygonmittelpunkt + distance
nearest_neighbour = processing.run("qgis:distancetonearesthubpoints", {
'INPUT':polygons,
'HUBS':'D:/Uni/GID/Data/Sachsen_DB_Haltestellen.shp',
'FIELD':'EVA_NR',
'UNIT':0, # meter
'OUTPUT':'memory:'})['OUTPUT']

## nearest neighbour mittelpunkte mit polygonen verknüpfen
joined_polygon_nn = processing.run("native:joinattributestable", {
'INPUT':polygons,
'FIELD':'id',
'INPUT_2':nearest_neighbour,
'FIELD_2':'id',
'FIELDS_TO_COPY':[],
'METHOD':1,
'DISCARD_NONMATCHING':False,
'PREFIX':'',
'OUTPUT':'memory:'})['OUTPUT']

## polygone rastern (distance)
processing.run("gdal:rasterize", {
'INPUT':joined_polygon_nn,
'FIELD':'HubDist',
'BURN':0,
'USE_Z':False,
'UNITS':1,
'WIDTH':500,
'HEIGHT':500,
'EXTENT':'278075.548200000,503075.548200000,5560939.790900000,5728439.790900000 [EPSG:25833]',
'NODATA':0,
'OPTIONS':'',
'DATA_TYPE':5,
'INIT':None,
'INVERT':False,
'EXTRA':'',
'OUTPUT':'D:/Uni/GID/Data/dist_raster.tif'})
