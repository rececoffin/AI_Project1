/*
  Author: Rece Coffin
  AI Project 1
  Implement an uninformed search algorithm on a map to
  find a path between two cities
 */
package com.company;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class find_route {
    //node and edge classes to store information
    private class Node {
        String name;
        Node parent;
        Node(String name){
            this.name = name;
        }
        //methods to make node comparable
        @Override
        public boolean equals(Object obj){
            if(!(obj instanceof Node)) return false;
            if(obj == this) return true;
            //check equal names
            return this.name.equalsIgnoreCase(((Node) obj).name);
        }
        @Override
        public int hashCode(){
            return name.length();
        }
    }
    private class Edge{
        Node dest;
        int cost;
        Edge(Node dest, int cost){
            this.dest = dest;
            this.cost = cost;
        }
    }
    //adjacency list to represent the graph
    private Map<Node, ArrayList<Edge>> the_map;
    private String fileName;
    private Node originCity;
    private Node destinationCity;

    private find_route(String[] args){
        the_map = new HashMap<>();
        //read the file and initialize the map
        this.fileName = args[0];
        this.originCity = new Node(args[1]);
        this.destinationCity = new Node(args[2]);
        initMap();
        //find a path using breadth first search
        search();
    }

    private void search() {
        //queue for BFS
        LinkedList<Node> queue = new LinkedList<>();
        Map<Node, Boolean> visited = initVisited();
        Map<Node, Node> parents = new HashMap<>();
        visited.put(originCity, true);
        //begin the queue
        queue.add(originCity);

        //search
        Node city;
        while(!queue.isEmpty()){
            city = queue.poll();
//            System.out.println(city.name);
            //get all adj
            for(Edge e : the_map.get(city)){
                Node n = e.dest;
                //check if visited and add
                if(!visited.get(n)){
                    visited.put(n, true);
                    n.parent = city;
                    parents.put(n, city);
                    queue.add(n);
                }
            }
        }
        //get the solution
        getSolution(parents);
    }

    //helper function to init a map
    private Map<Node, Boolean> initVisited() {
        Map<Node, Boolean> q = new HashMap<>();
        for(Node n : the_map.keySet()){
            q.put(n, false);
        }
        return q;
    }

    //get the desired solution
    private void getSolution(Map<Node, Node> parents) {
        ArrayList<Node> solution = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        Node n = destinationCity;
        //trace back through parent pointers
        while(!n.equals(originCity)){
            solution.add(n);
            distances.add(getDistance(n, parents.get(n)));
            n = parents.get(n);
            //if null, there is not path
            if(n == null){
                noPath();
                System.exit(0);
            }
        }
        //print the solution
        solution.add(n);
        printSolution(solution, parents, distances);
    }

    //function is called if not path is found
    private void noPath() {
        System.out.println("distance: infinity");
        System.out.println("route:");
        System.out.println("none");
    }

    private void printSolution(ArrayList<Node> solution,
                               Map<Node, Node> parents,
                               ArrayList<Integer> distances) {
        //reverse the solution
        int size = solution.size();
        Collections.reverse(solution);
        Collections.reverse(distances);

        //print the total distance first
        System.out.println("distance: "
                            + distances.stream().mapToInt(Integer::intValue).sum() + " km");

        //print the complete route
        System.out.println("route:");
        for(int i = 0; i < size - 1; i++){
            Node start = solution.get(i);
            Node dest = solution.get(i + 1);
            System.out.println(start.name + " to " + dest.name + ", "
                                + distances.get(i) + " km");
        }
    }

    //get the distance from the map
    private int getDistance(Node start, Node dest) {
        for(Edge e : the_map.get(start)){
            if(e.dest.equals(dest)) return e.cost;
        }
        return 0;
    }


    //init the adj lists
    private void initMap() {
        //read the file, stop at terminating line
        try {
            FileReader r = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(r);
            String line;
            while ((line = reader.readLine()) != null){
                //check for terminating line
                if(line.equalsIgnoreCase("END OF INPUT")){
                    break;
                }
                //parse and build map
                String[] lineArr = line.split(" ");
                Node n = new Node(lineArr[0]);
                Edge e = new Edge(new Node(lineArr[1]), Integer.parseInt(lineArr[2]));
                //add to the map
                if(the_map.containsKey(n)){
                    the_map.get(n).add(e);
                }
                else {
                    ArrayList<Edge> edges = new ArrayList<>();
                    edges.add(e);
                    the_map.put(n, edges);
                }
                //add the reverse edge too
                n = new Node(lineArr[1]);
                e = new Edge(new Node(lineArr[0]), Integer.parseInt(lineArr[2]));
                if(the_map.containsKey(n)){
                    the_map.get(n).add(e);
                }
                else {
                    ArrayList<Edge> edges = new ArrayList<>();
                    edges.add(e);
                    the_map.put(n, edges);
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        printMap();
    }

    //run the program
    public static void main(String[] args){
       find_route us = new find_route(args);
    }
}
