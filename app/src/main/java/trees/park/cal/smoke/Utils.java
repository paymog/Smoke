package trees.park.cal.smoke;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Utils {

    public static final String SERVER_URL = "http://192.168.100.111:8181";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <E> E deserialize(InputStream stream, Class<E> clazz) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        E result = null;
        try {
            IOUtils.copy(stream, outputStream);

            result = MAPPER.readValue(outputStream.toString(), clazz);
        } catch (IOException e) {
            System.out.println("There was an IO exception during deserializtion");
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(outputStream);
        }

        return result;
    }

    public static byte[] serialize(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsBytes(object);
    }

}
