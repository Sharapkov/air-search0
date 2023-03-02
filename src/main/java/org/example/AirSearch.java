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
        KdTree kdTree = new KdTree();
        for (Airport airport : airports) {
            double[] point = {airport.getLatitude(), airport.getLongitude()};
            kdTree.insert(point, airport.getName());
        }
        // координаты пользователя
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите широту: ");
        double lat = scanner.nextDouble();
        System.out.print("Введите долготу: ");
        double lon = scanner.nextDouble();
        Airport target = new Airport("Target Airport",lat,lon);
        // добавление нового узла в дерево
        //List<Airport> airportsWithTarget = new ArrayList<>(airports);
        //airportsWithTarget.add(target);

        long start = System.currentTimeMillis();
        //поиск 5 ближайших аэропортов к указанным координатам
        double[] targetPoint = {target.getLatitude(), target.getLongitude()};
        List<String> nearestAirports = kdTree.kNearestNeighbors(targetPoint, 5);
        // вывод результатов
        for (String airportName : nearestAirports) {

            System.out.println(airportName);
        }
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("Затраченное время: " + totalTime + " мс");
    }

}
