package app;

/**
 * Application wide Storage
 *
 * @author Alexander Muravya {@literal <asm@virtalab.net>}
 * @since 1.0
 */
public class Storage {

    private static Storage SELF = null;
    private String latestTag;
    private String latestCommit;

    private Storage() {
    }

    public static synchronized Storage get() {
        if(SELF == null) {
            SELF = new Storage();
        }
        return SELF;
    }

    public String getLatestTag() {
        return latestTag;
    }

    public void setLatestTag(String applicationVersion) {
        this.latestTag = applicationVersion;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }
}
