import json

path = r"C:\Users\juliu\Desktop\GIT\GID\git-seminar-main\git-seminar-main\src\main\python\timetables\timetables.json"

with open(path, encoding='UTF-8') as file:
    timetables_json = json.load(file)

tt_data = timetables_json["data"]

class Node:
    def __init__(self, name):
        self.name = name
        self.neighbors = []

    def add_neighbor(self, node):
        if node not in self.neighbors:
            self.neighbors.append(node)
            self.neighbors.sort()

class Graph:
    nodes = {}

    def add_node(self, node):
        if isinstance(node, Node) and node.name not in self.nodes:
            self.nodes[node.name] = node
            return True

        else:
            return False

    def add_edge(self, u, v):
        if u in self.nodes and v in self.nodes:
            for key, value in self.nodes.items():
                if key == u:
                    value.add_neighbor(v)
                if key == v:
                    value.add_neighbor(u)
            return True
        else:
            return False

    def print_graph(self):
        for key in sorted(list(self.nodes.keys())):
            print(key + str(self.nodes[key].neighbors))


g = Graph()

all_stations = []

for tt in tt_data:
    route = tt["route_normalized"]
    for stop in route:
        if stop not in all_stations:
            all_stations.append(stop)

# print(len(all_stations))

for i in all_stations:
    n = Node(i)
    g.add_node(n)




for tt in tt_data:
    route = tt["route_normalized"]
    for i, stop in enumerate(route):

        if i == 0:
            previous_stop = None
        else:
            previous_stop = route[i - 1]
            g.add_edge(previous_stop, stop)

        if i == len(route) - 1:
            next_stop = None
        else:
            next_stop = route[i + 1]
            g.add_edge(stop, next_stop)


g.print_graph()



### test ###

# for tt in tt_data:
#     route = tt["route_normalized"]
#     for i, stop in enumerate(route):

#         if i == 0:
#             previous_stop = None
#         else:
#             previous_stop = route[i - 1]
            

#         if i == len(route) - 1:
#             next_stop = None
#         else:
#             next_stop = route[i + 1]
        
#         if stop == "Dresden Hbf":
#             if previous_stop == "Coswig(b Dresden)" or next_stop == "Coswig(b Dresden)" :
#                 print(route)
