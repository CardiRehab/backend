package com.healthcare.herplatform.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class StandalonePopulateAttachments {
    
    public static void main(String[] args) {
        System.out.println("\n==========================================");
        System.out.println("Populate Attachments - Standalone Script");
        System.out.println("==========================================\n");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                System.err.println("ERROR: MySQL JDBC Driver not found. Make sure mysql-connector is in classpath.");
                System.err.println("Tried: com.mysql.cj.jdbc.Driver and com.mysql.jdbc.Driver");
                System.exit(1);
            }
        }
        
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null) {
            dbUrl = "jdbc:mysql://localhost:3306/herplat?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            System.out.println("⚠ Warning: DB_URL not set, using default: " + dbUrl);
        } else {
            System.out.println("Using database URL: " + dbUrl);
        }
        String dbUser = System.getenv("DB_USERNAME");
        if (dbUser == null) {
            dbUser = "root";
        }
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null) {
            dbPassword = "123123123";
        }
        
        String documentsDir = System.getenv("ATTACHMENTS_DIR");
        if (documentsDir == null || documentsDir.isEmpty()) {
            documentsDir = "/home/hitesh/projects/per/backend/resources/downloads";
        }
        
        Map<String, String> uuidToActualFile = new HashMap<>();
        uuidToActualFile.put("e8be9383-2700-4171-9754-5889eee8da13", "chester step test.pdf");
        uuidToActualFile.put("7b75b456-93e5-40c7-a4ec-d47ff11c1330", "CR. Exercise Diary.pdf");
        uuidToActualFile.put("a7232baf-41d6-40de-a442-2767ee0cb290", "CR. Intial Advice.pdf");
        uuidToActualFile.put("b79f5569-d772-44d6-9856-4473cd04abb7", "CR. Specialist First assessment.pdf");
        uuidToActualFile.put("a501c279-a698-42cb-a293-9f30ab7eb7e9", "CR.Initial Assessment.pdf");
        uuidToActualFile.put("15168ee2-92e1-44fb-a267-43540e0cff75", "CRM.CABG.1.pdf");
        uuidToActualFile.put("b662d822-f8af-4c1c-b487-6f260addbf55", "CRM.CABG.2.pdf");
        uuidToActualFile.put("0c41c564-5746-4e15-b7ca-a4203d8d8830", "CRM.Sample of intial assessment and Exercise prescribing.pdf");
        uuidToActualFile.put("rpe-scale-0c41c564-5746-4e15-b7ca-a4203d8d8830", "RPE.Scale.pdf");
        uuidToActualFile.put("8886f20c-dcee-4981-b057-42c2a246a5a9", "CR.Home Exercise routine and advice.pdf");
        
        Map<String, String> uuidToDisplayName = new HashMap<>();
        uuidToDisplayName.put("e8be9383-2700-4171-9754-5889eee8da13", "Chester Step Test.pdf");
        uuidToDisplayName.put("7b75b456-93e5-40c7-a4ec-d47ff11c1330", "Exercise Diary.pdf");
        uuidToDisplayName.put("a7232baf-41d6-40de-a442-2767ee0cb290", "Initial Advice.pdf");
        uuidToDisplayName.put("b79f5569-d772-44d6-9856-4473cd04abb7", "Specialist First Assessment.pdf");
        uuidToDisplayName.put("a501c279-a698-42cb-a293-9f30ab7eb7e9", "Initial Assessment.pdf");
        uuidToDisplayName.put("15168ee2-92e1-44fb-a267-43540e0cff75", "CABG 1.pdf");
        uuidToDisplayName.put("b662d822-f8af-4c1c-b487-6f260addbf55", "CABG 2.pdf");
        uuidToDisplayName.put("0c41c564-5746-4e15-b7ca-a4203d8d8830", "Sample of IA and EP.pdf");
        uuidToDisplayName.put("rpe-scale-0c41c564-5746-4e15-b7ca-a4203d8d8830", "RPE Scale.pdf");
        uuidToDisplayName.put("8886f20c-dcee-4981-b057-42c2a246a5a9", "Home Exercise routine and advice.pdf");
        
        File dir = new File(documentsDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("ERROR: Directory does not exist: " + documentsDir);
            System.exit(1);
        }
        
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".pdf"));
        Map<String, File> fileMap = new HashMap<>();
        for (File file : files) {
            fileMap.put(file.getName().toLowerCase(), file);
        }
        
        int successCount = 0;
        int errorCount = 0;
        int skipCount = 0;
        
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Connected to database successfully!\n");
            
            for (Map.Entry<String, String> entry : uuidToActualFile.entrySet()) {
                String uuid = entry.getKey();
                String actualFileName = entry.getValue();
                String displayName = uuidToDisplayName.get(uuid);
                
                File file = fileMap.get(actualFileName.toLowerCase());
                
                if (file == null) {
                    System.err.println("ERROR: Could not find file: " + actualFileName + " for UUID: " + uuid);
                    errorCount++;
                    continue;
                }
                
                // Check if already exists
                try (PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM private_attachments WHERE id = ?")) {
                    checkStmt.setString(1, uuid);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("⊘ Skipped (already exists): " + uuid + " -> " + displayName);
                            skipCount++;
                            continue;
                        }
                    }
                }
                
                // Insert the file
                try {
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    System.out.println("Reading file: " + file.getName() + " (size: " + fileData.length + " bytes)");
                    
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO private_attachments (id, file_name, file_type, data) VALUES (?, ?, ?, ?)")) {
                        insertStmt.setString(1, uuid);
                        insertStmt.setString(2, displayName);
                        insertStmt.setString(3, "application/pdf");
                        insertStmt.setBytes(4, fileData);
                        
                        int rowsAffected = insertStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("✓ Inserted: " + uuid + " -> " + displayName + " (from: " + file.getName() + ")");
                            successCount++;
                        } else {
                            System.err.println("ERROR: No rows affected for UUID: " + uuid);
                            errorCount++;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("ERROR reading file " + file.getName() + ": " + e.getMessage());
                    errorCount++;
                } catch (Exception e) {
                    System.err.println("ERROR inserting UUID " + uuid + ": " + e.getMessage());
                    e.printStackTrace();
                    errorCount++;
                }
            }
            
        } catch (Exception e) {
            System.err.println("ERROR connecting to database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("\n=== Summary ===");
        System.out.println("Successfully inserted: " + successCount);
        System.out.println("Skipped (already exists): " + skipCount);
        System.out.println("Errors: " + errorCount);
        System.out.println("\n==========================================");
        System.out.println("Script COMPLETED");
        System.out.println("==========================================\n");
    }
}
