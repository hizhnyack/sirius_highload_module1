package ru.hpclab.hl.module1.util;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.Map;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class PythonScriptRunner {
    private static final Logger logger = LoggerFactory.getLogger(PythonScriptRunner.class);
    
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public PythonScriptRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runScript() {
        try {
            // Создаем временную директорию для скрипта
            Path tempDir = Files.createTempDirectory("python-script");
            File scriptFile = new File(tempDir.toFile(), "generate_initdb.py");
            
            logger.info("Temporary directory created: {}", tempDir);
            
            // Копируем скрипт из ресурсов во временную директорию
            try (InputStream inputStream = getClass().getResourceAsStream("/generate_initdb.py")) {
                if (inputStream == null) {
                    throw new RuntimeException("Python script not found in resources");
                }
                Files.copy(inputStream, scriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Python script copied to: {}", scriptFile.getAbsolutePath());
            }

            // Делаем скрипт исполняемым
            scriptFile.setExecutable(true);
            
            // Запускаем Python-скрипт
            ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptFile.getAbsolutePath());
            processBuilder.directory(tempDir.toFile());
            processBuilder.redirectErrorStream(true);
            
            // Устанавливаем переменную окружения для выходной директории
            Map<String, String> env = processBuilder.environment();
            env.put("OUTPUT_DIR", tempDir.toString());
            
            Process process = processBuilder.start();
            
            // Читаем вывод скрипта
            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    logger.info("Python script output: {}", line);
                }
            }
            
            int exitCode = process.waitFor();
            logger.info("Python script finished with exit code: {}", exitCode);
            
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
            }

            // Копируем сгенерированный файл в текущую директорию
            File generatedFile = new File(tempDir.toFile(), "initdb.sql");
            if (generatedFile.exists()) {
                Files.copy(generatedFile.toPath(), new File("initdb.sql").toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Generated initdb.sql copied to current directory");
                
                // Выполняется SQL-скрипт
                try (Connection connection = dataSource.getConnection()) {
                    logger.info("Executing SQL script from {}", generatedFile.getAbsolutePath());
                    ScriptUtils.executeSqlScript(connection, new FileSystemResource(generatedFile));
                    logger.info("SQL script executed successfully");
                }
            } else {
                throw new RuntimeException("Generated initdb.sql not found in " + tempDir);
            }

        } catch (Exception e) {
            logger.error("Failed to run Python script", e);
            throw new RuntimeException("Failed to run Python script", e);
        }
    }
} 