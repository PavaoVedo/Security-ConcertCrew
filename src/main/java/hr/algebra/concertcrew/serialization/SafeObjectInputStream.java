package hr.algebra.concertcrew.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Set;


public class SafeObjectInputStream extends ObjectInputStream {


    private static final Set<String> ALLOWED_CLASSES = Set.of(
        ConcertSnapshot.class.getName(),
        String.class.getName()
    );

    public SafeObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {
        String className = desc.getName();
        if (!ALLOWED_CLASSES.contains(className)) {
            throw new InvalidClassException(
                "Blocked deserialization of non-whitelisted class: " + className);
        }
        return super.resolveClass(desc);
    }

    @Override
    protected Class<?> resolveProxyClass(String[] interfaces)
            throws IOException, ClassNotFoundException {
        throw new InvalidClassException("Proxy classes are not allowed");
    }
}
