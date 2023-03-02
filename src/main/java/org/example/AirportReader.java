package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AirportReader {
    static List<Airport> readAirports(String csfFile) {

        String line;
        String delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; //мы ищем запятую, за которой следует четное количество
        // кавычек (или нечетное количество, если запятая находится внутри кавычек)
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/airports.dat"))) {
            while ((line = br.readLine()) != null) {
                // обрабатываем строку с аэропортом
                String[] fields = line.split(delimiter);
                // сохраняем в лист имя и координаты x, y;
                String name = fields[1];
                double latitude = Double.parseDouble(fields[6]);
                double longitude = Double.parseDouble(fields[7]);
                airports.add(new Airport(name, latitude, longitude));
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла airports.dat: " + e.getMessage());
        }
        return airports;
    }
}

