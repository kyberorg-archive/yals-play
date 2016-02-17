package struct;

/**
 * Reply struct
 *
 * @since 1.0
 */
public class IdentReply {
    private String longUrl;

    public static IdentReply init() {
        return new IdentReply();
    }

    public String getLongUrl() {
        return this.longUrl;
    }

    public IdentReply setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }
}
