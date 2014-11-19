package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.Charsets;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;

@Service
public class CsvService {

    public List<Map<String, String>> readCsvFile(File file, String separator, String encoding) throws IOException {
        return readCsvFile(new FileInputStream(file), separator, encoding);
    }

    public List<Map<String, String>> readCsvFile(InputStream stream, String separator, String encoding) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(stream, Charsets.UTF_8)) {
            String content = CharStreams.toString(isr);
            List<String> lines = Splitter.on(System.lineSeparator()).splitToList(content);
            List<String> header = null;
            List<Map<String, String>> csvContent = new Vector<Map<String, String>>(lines.size());
            for (String line : lines) {
                String[] parts = line.split(separator);
                if (header == null) {
                    header = new ArrayList<String>();
                    for (String part : parts) {
                        header.add(cleanup(part));
                    }
                } else {
                    int index = 0;
                    Map<String, String> linemap = new HashMap<String, String>(header.size());
                    for (String column : header) {
                        linemap.put(column, access(parts, index++));
                    }
                    csvContent.add(linemap);
                }
            }
            return csvContent;
        }
    }

    private String access(String[] parts, int index) {
        if (parts.length <= index) {
            return null;
        }
        return cleanup(parts[index]);
    }

    private String cleanup(String part) {
        String value = part.trim();
        if (value.startsWith("\"")) {
            value = value.replaceAll("\"", "");
        }
        return value.trim();
    }
}
