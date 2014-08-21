package trees.park.cal.smoke;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <E> E deserialize(InputStream stream, Class<E> clzazz) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        E result = null;
        try {
            IOUtils.copy(stream, outputStream);

            result = MAPPER.readValue(outputStream.toString(), clzazz);
        } catch (IOException e) {
            System.out.println("There was an IO exception during deserializtion");
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(outputStream);
        }

        return result;
    }

}
