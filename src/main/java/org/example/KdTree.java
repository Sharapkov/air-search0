package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KdTree {
    private static final int K = 2;

    private Node root;

    private static class Node {
        private final double[] point;
        private final String value;
        private Node left;
        private Node right;
        private double distance;

        Node(double[] point, String value) {
            this.point = point;
            this.value = value;
        }
    }

    public void insert(double[] point, String value) {
        root = insert(root, point, value, 0);
    }

    private Node insert(Node node, double[] point, String value, int depth) {
        if (node == null) {
            return new Node(point, value);
        }

        int axis = depth % K;
        if (point[axis] < node.point[axis]) {
            node.left = insert(node.left, point, value, depth + 1);
        } else {
            node.right = insert(node.right, point, value, depth + 1);
        }

        return node;
    }

    public List<String> kNearestNeighbors(double[] point, int k) {
        List<Node> nearestNeighbors = new ArrayList<>(k);
        kNearestNeighbors(root, point, k, 2, nearestNeighbors);
        List<String> result = new ArrayList<>(k);
        for (Node node : nearestNeighbors) {
            result.add(node.value);
        }
        return result;
    }

    private void kNearestNeighbors(Node node, double[] point, int k, int depth, List<Node> nearestNeighbors) {
        if (node == null) {
            return;
        }

        double distance = distance(point, node.point);
        node.distance = distance;

        if (nearestNeighbors.size() < k) {
            nearestNeighbors.add(node);
        } else {
            Collections.sort(nearestNeighbors, (n1, n2) -> Double.compare(n2.distance, n1.distance));
            double farthest = nearestNeighbors.get(nearestNeighbors.size() - 1).distance;
            if (distance < farthest) {
                nearestNeighbors.remove(nearestNeighbors.size() - 1);
                nearestNeighbors.add(node);
            }
        }

        int axis = depth % K;
        double planeDistance = Math.abs(point[axis] - node.point[axis]);

        if (planeDistance < nearestNeighbors.get(nearestNeighbors.size() - 1).distance || nearestNeighbors.size() < k) {
            kNearestNeighbors(node.left, point, k, depth + 1, nearestNeighbors);
            kNearestNeighbors(node.right, point, k, depth + 1, nearestNeighbors);
        } else if (point[axis] < node.point[axis]) {
            kNearestNeighbors(node.left, point, k, depth + 1, nearestNeighbors);
        } else {
            kNearestNeighbors(node.right, point, k, depth + 1, nearestNeighbors);
        }
    }

    private static double distance(double[] point1, double[] point2) {
        double radius = 6371;
        double lat1 = (point1[0]);
        double lon1 = (point1[1]);
        double lat2 = (point2[0]);
        double lon2 = (point2[1]);

        double dlat = Math.toRadians(lat2 - lat1);
        double dlon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c;

        return distance;
    }
}
