package com.example.csv2json;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class Csv2jsonController {
    @GetMapping("/file-content")
    public ResponseEntity<List<Map<String, String>>> getFileContent() {
        try {
            // Read the content of the uploaded CSV file
            // Replace "path/to/destination/example.csv" with the actual file path
            InputStream resource = new ClassPathResource("static/input.csv").getInputStream();
            Reader reader = new BufferedReader(
                    new InputStreamReader(resource));
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> rows = csvReader.readAll();

            // Extract the header row
            String[] headers = rows.get(0);

            List<Map<String, String>> jsonData = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) {
                String[] fields = rows.get(i);
                Map<String, String> data = new LinkedHashMap<>();

                // Map each field to its corresponding header
                for (int j = 0; j < headers.length; j++) {
                    String header = headers[j].trim();
                    String field = (j < fields.length) ? fields[j].trim() : "";
                    data.put(header, field);
                }

                jsonData.add(data);
            }

            csvReader.close();
            reader.close();

            return ResponseEntity.ok(jsonData);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
