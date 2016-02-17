package struct;


import com.google.gson.annotations.Since;
import play.data.validation.*;

/**
 * JSON Object struct for storing object
 *
 * @version 1
 * @since 1.0
 */
public class Store {

    @Required(message = "public.url.absent")
    @MinSize(value = 5, message = "public.url.minsize")
    @MaxSize(value = 15613, message = "public.url.maxsize")
    @URL(message = "public.url.noturl")
    @Since(1.0)
    public String longUrl;

    public static Store get() {
        return new Store();
    }

    public Store setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

}
