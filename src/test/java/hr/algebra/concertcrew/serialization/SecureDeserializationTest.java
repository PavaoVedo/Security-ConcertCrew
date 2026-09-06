package hr.algebra.concertcrew.serialization;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class SecureDeserializationTest {

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
        return bos.toByteArray();
    }

    @Test
    void deserializesWhitelistedSnapshot() throws Exception {
        ConcertSnapshot original = new ConcertSnapshot(
            "Taylor Swift", "The Eras Tour", "Wembley Stadium",
            "London", "UK", "2024-06-21", 5, 44);

        byte[] data = serialize(original);
        ConcertSnapshot restored = SecureDeserializer.deserialize(data);

        assertEquals("Taylor Swift", restored.getMainArtist());
        assertEquals("Wembley Stadium", restored.getVenue());
        assertEquals(5, restored.getRating());
        assertEquals(44, restored.getSongsPlayed());
    }

    @Test
    void rejectsNonWhitelistedClass() throws Exception {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("attack", "gadget-chain");
        byte[] data = serialize(payload);

        InvalidClassException ex = assertThrows(
            InvalidClassException.class,
            () -> SecureDeserializer.deserialize(data));
        assertTrue(ex.getMessage().contains("non-whitelisted"));
    }

    @Test
    void rejectsInvalidMagicBytes() {
        byte[] notSerialized = "this is just a plain text file".getBytes();

        assertThrows(StreamCorruptedException.class,
            () -> SecureDeserializer.deserialize(notSerialized));
    }
}
