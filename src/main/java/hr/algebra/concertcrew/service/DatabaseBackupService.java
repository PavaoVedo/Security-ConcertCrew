package hr.algebra.concertcrew.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@Service
public class DatabaseBackupService {

    private static final String BACKUP_FILE = "concertcrew-backup.sql";

    private final DataSource dataSource;
    private final Path backupDir;

    public DatabaseBackupService(DataSource dataSource,
                                 @Value("${app.backup.dir:./backups}") String backupDir) {
        this.dataSource = dataSource;
        this.backupDir = Paths.get(backupDir).toAbsolutePath().normalize();
    }

    public Path backup() throws SQLException, IOException {
        Files.createDirectories(backupDir);
        Path target = backupDir.resolve(BACKUP_FILE);
        String h2Path = target.toString().replace('\\', '/');
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("SCRIPT TO '" + h2Path + "'");
        }
        return target;
    }

    public Path restore() throws SQLException {
        Path source = backupDir.resolve(BACKUP_FILE);
        if (!Files.exists(source)) {
            throw new IllegalStateException(
                    "No backup file found at " + source + ". Run a backup first.");
        }
        String h2Path = source.toString().replace('\\', '/');
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS");
            statement.execute("RUNSCRIPT FROM '" + h2Path + "'");
        }
        return source;
    }
}