package de.hse.swb8.parkingSystem.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class JSONHandler {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static void saveDataBaseInfo(final @NotNull DataBaseInfo info, final @NotNull  String filePath) {
            try {
                File file = new File(filePath);
                File parentDirectory = file.getParentFile();

                // Check if the parent directory exists; if not, create it
                if (parentDirectory != null && !parentDirectory.exists()) {
                    if (parentDirectory.mkdirs()) {
                        System.out.println("Directories created successfully: " + parentDirectory.getAbsolutePath());
                    } else {
                        System.err.println("Failed to create directories: " + parentDirectory.getAbsolutePath());
                    }
                }

                // Write the JSON file, overriding it if it already exists
                objectMapper.writeValue(file, info);
                System.out.println("DataBaseInfo saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving DataBaseInfo: " + e.getMessage());
            }
        }

        // Read DataBaseInfo from JSON
        public static DataBaseInfo readDataBaseInfo(final @NotNull String filePath) {
            try {
                System.out.println("ReadFile " + filePath);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules(); // Automatically registers support for Java 16+ features

                return objectMapper.readValue(new File(filePath), DataBaseInfo.class);
            } catch (IOException e) {
                System.err.println("Error reading DataBaseInfo: " + e.getMessage());
                return null;
            }
        }
    }