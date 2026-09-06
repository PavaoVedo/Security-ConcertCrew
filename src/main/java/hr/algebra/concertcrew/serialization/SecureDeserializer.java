package hr.algebra.concertcrew.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectStreamConstants;
import java.io.StreamCorruptedException;


public final class SecureDeserializer {


    static final byte[] EXPECTED_HEADER = {
        (byte) (ObjectStreamConstants.STREAM_MAGIC >>> 8),
        (byte)  ObjectStreamConstants.STREAM_MAGIC,
        (byte) (ObjectStreamConstants.STREAM_VERSION >>> 8),
        (byte)  ObjectStreamConstants.STREAM_VERSION
    };

    private SecureDeserializer() {
    }


    public static void validateMagicBytes(byte[] data) throws IOException {
        if (data == null || data.length < EXPECTED_HEADER.length) {
            throw new StreamCorruptedException("File too short to be a serialized object");
        }
        for (int i = 0; i < EXPECTED_HEADER.length; i++) {
            if (data[i] != EXPECTED_HEADER[i]) {
                throw new StreamCorruptedException(
                    "Invalid serialization header: expected magic bytes 0xACED0005");
            }
        }
    }

    public static ConcertSnapshot deserialize(byte[] data)
            throws IOException, ClassNotFoundException {

        validateMagicBytes(data);

        try (SafeObjectInputStream in =
                 new SafeObjectInputStream(new ByteArrayInputStream(data))) {
            Object obj = in.readObject();
            if (!(obj instanceof ConcertSnapshot snapshot)) {
                throw new InvalidClassException(
                    "Unexpected deserialized type: " + obj.getClass().getName());
            }
            return snapshot;
        }
    }
}
