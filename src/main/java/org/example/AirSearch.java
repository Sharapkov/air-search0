package org.example;

import org.example.KdTree;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import static org.example.AirportReader.readAirports;

public class AirSearch {

    public static void main(String[] args) throws IOException {
        // загрузка данных из файла
        List<Airport> airports = readAirports("src/main/resources/airports.dat");
        // создание kdtree
        KdTree kdTree = new KdTree(airports);

        // координаты пользователя
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите широту: ");
        double lat = scanner.nextDouble();
        System.out.print("Введите долготу: ");
        double lon = scanner.nextDouble();
        Airport target = new Airport("Target Airport",lat,lon);
        // добавление нового узла в дерево
        List<Airport> airportsWithTarget = new ArrayList<>(airports);
        airportsWithTarget.add(target);
        kdTree = new KdTree(airportsWithTarget);

        long start = System.currentTimeMillis();
        //поиск 5 ближайших аэропортов к указанным координатам
        List<Airport> nearestAirports = kdTree.findNearestAirports(target, 5);
        // вывод результатов
        for (Airport airport : nearestAirports) {
            System.out.println(airport.getName() + " Координаты: " + airport.getLatitude() + ", " + airport.getLongitude());
        }
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("Затраченное время: " + totalTime + " мс");
    }

}
