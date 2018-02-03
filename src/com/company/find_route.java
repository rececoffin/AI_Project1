package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class find_route {
    //node and edge classes to store information
    private class Node {
        public String name;
        public Node(String name){
            this.name = name;
        }
        //methods to make node compareable
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
        public String dest;
        public int cost;
        public Edge(String dest, int cost){
            this.dest = dest;
            this.cost = cost;
        }
    }
    //adjacency list to represent the graph
    private Map<Node, ArrayList<Edge>> the_map;
    private String fileName;
    private Node originCity;
    private Node destinationCity;

    public find_route(String[] args){
        the_map = new HashMap<>();
        //read the file and initialize the map
        this.fileName = args[0];
        this.originCity = new Node(args[1]);
        this.destinationCity = new Node(args[2]);
        initMap();
    }

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
                Edge e = new Edge( lineArr[1], Integer.parseInt(lineArr[2]));
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
                e = new Edge(lineArr[0], Integer.parseInt(lineArr[2]));
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
        printMap();
    }

    //print the adj list to verify
    private void printMap() {
        for(Node n : the_map.keySet()){
            System.out.print(n.name + ": ");
            for(Edge e : the_map.get(n)){
                System.out.print(e.dest + "->" + e.cost + " ");
            }
            System.out.println("\n");
        }
    }

    //run the program
    public static void main(String[] args){
       find_route us = new find_route(args);
    }
}
