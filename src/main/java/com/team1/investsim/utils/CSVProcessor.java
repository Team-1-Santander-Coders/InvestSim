package com.team1.investsim.utils;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

public class CSVProcessor {
    public static Optional<List<String[]>> processCSV (String path) {
        Optional<List<String[]>> processedData = Optional.empty();

        try (Reader inputStreamReader = new InputStreamReader(new FileInputStream(path))) {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            settings.getFormat().setDelimiter(",");
            settings.setHeaderExtractionEnabled(true);

            CsvParser parser = new CsvParser(settings);
            processedData = Optional.ofNullable(parser.parseAll(inputStreamReader));

        }catch(Exception e) { e.printStackTrace(); }

        return processedData;
    }
}
