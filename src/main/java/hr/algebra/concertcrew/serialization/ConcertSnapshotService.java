package hr.algebra.concertcrew.serialization;

import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
public class ConcertSnapshotService {

    private final ConcertRepository concertRepository;
    private final Path serializationDir;

    public ConcertSnapshotService(
            ConcertRepository concertRepository,
            @Value("${app.serialization.dir:./serialized}") String serializationDir) {
        this.concertRepository = concertRepository;
        this.serializationDir = Paths.get(serializationDir).toAbsolutePath().normalize();
    }

    public byte[] serialize(Long concertId) throws IOException {
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new NoSuchElementException("Concert not found: " + concertId));
        ConcertSnapshot snapshot = ConcertSnapshot.from(concert);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(snapshot);
        }
        return bos.toByteArray();
    }

    public Path exportToFile(Long concertId) throws IOException {
        byte[] data = serialize(concertId);
        Files.createDirectories(serializationDir);
        Path target = serializationDir.resolve("concert-" + concertId + ".ser");
        Files.write(target, data);
        return target;
    }

    public ConcertSnapshot importSnapshot(byte[] data) throws IOException, ClassNotFoundException {
        return SecureDeserializer.deserialize(data);
    }
}
