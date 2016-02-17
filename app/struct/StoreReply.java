package struct;

import com.google.gson.annotations.Since;

/**
 * Struct which is reply on successfully proceeded store request
 *
 * @version 1
 * @since 1.0
 */
@Since(1.0)
public class StoreReply {
    @Since(1.0)
    private String shortUrl;

    public String getShortUrl() {
        return this.shortUrl;
    }

    public static StoreReply get(){
        return new StoreReply();
    }

    public StoreReply setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

}
