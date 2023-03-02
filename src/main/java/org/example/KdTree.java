package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class KdTree {
    private static final int K = 2;
    private Node root;

    private static class Node {
        private Airport airport;
        private Node left;
        private Node right;
        private int level;

        public Node(Airport airport, int level) {
            this.airport = airport;
            this.left = null;
            this.right = null;
            this.level = level;
        }

        public double getDistance(Airport target) {
            if (level % K == 0) {
                return target.getLongitude() - airport.getLongitude();
            } else {
                return target.getLatitude() - airport.getLatitude();
            }
        }
    }

    public KdTree(List<Airport> airports) {
        root = buildTree(airports, 0);
    }

    private void insert(Node node, Node newNode) {
        double distance = newNode.airport.distanceTo(node.airport);
        if (distance < 0) {
            if (node.left == null) {
                node.left = newNode;
            } else {
                insert(node.left, newNode);
            }
        } else {
            if (node.right == null) {
                node.right = newNode;
            } else {
                insert(node.right, newNode);
            }
        }
    }

    private Node buildTree(List<Airport> airports, int level) {
        if (airports.isEmpty()) {
            return null;
        }
        int medianIndex = airports.size() / 2;
        Node node = new Node(airports.get(medianIndex), level);
        List<Airport> leftSubtree = new ArrayList<>(airports.subList(0, medianIndex));
        List<Airport> rightSubtree = new ArrayList<>(airports.subList(medianIndex + 1, airports.size()));
        node.left = buildTree(leftSubtree, level + 1);
        node.right = buildTree(rightSubtree, level + 1);
        return node;
    }

    public List<Airport> findNearestAirports(Airport target, int k) {

        PriorityQueue<Airport> pq = new PriorityQueue<>(k, (a1, a2) -> Double.compare(a2.getDistance(), a1.getDistance()));
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        int depth = 0;
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (node == null) {
                continue;
            }
            double distance = node.airport.distanceTo(target);
            node.airport.setDistance(distance);
            if (pq.size() < k) {
                pq.offer(node.airport);
            } else {
                Airport farthestAirport = pq.peek();
                if (distance < farthestAirport.getDistance()) {
                    pq.poll();
                    pq.offer(node.airport);
                }
            }
            double planeDistance = node.getDistance(target);
            if (planeDistance < 0) {
                stack.push(node.right);
                if (Math.abs(planeDistance) < pq.peek().getDistance()) {
                    stack.push(node.left);
                }
            } else {
                stack.push(node.left);
                if (Math.abs(planeDistance) < pq.peek().getDistance()) {
                    stack.push(node.right);
                }
            }
            depth++;
            if (depth >= 1000) {
                break; // max depth reached
            }
        }
        List<Airport> nearestAirports = new ArrayList<>();
        while (!pq.isEmpty()) {
            nearestAirports.add(pq.poll());
        }
        return nearestAirports;

    }

    private void findNearestAirports(Node node, Airport target, int k, PriorityQueue<Airport> pq) {
        if (node == null) {
            return;
        }
        double distance = node.airport.distanceTo(target);
        node.airport.setDistance(distance);
        if (pq.size() < k) {
            pq.offer(node.airport);
        } else {
            Airport farthestAirport = pq.peek();
            if (distance < farthestAirport.getDistance()) {
                pq.poll();
                pq.offer(node.airport);
            }
        }
        double planeDistance = node.getDistance(target);
        if (planeDistance < 0) {
            findNearestAirports(node.left, target, k, pq);
            if (pq.size() < k || Math.abs(planeDistance) < pq.peek().getDistance()) {
                findNearestAirports(node.right, target, k, pq);
            }
        } else {
            findNearestAirports(node.right, target, k, pq);
            if (pq.size() < k || Math.abs(planeDistance) < pq.peek().getDistance()) {
                findNearestAirports(node.left, target, k, pq);
            }
        }
    }
}
