package util;

import org.apache.commons.io.IOUtils;
import play.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Provides correct application version
 *
 * @author Alexander Muravya {@literal <asm@virtalab.net>}
 * @since 1.0
 */
public class VersionHelper {
    
    public static String getLatestCommitHash() {
        try {
            String ref = IOUtils.toString(new FileInputStream("./.git/HEAD")).replace("ref:", "").trim();
            String latestCommit = IOUtils.toString(new FileInputStream("./.git/" + ref));
            return (Objects.nonNull(latestCommit) && !latestCommit.isEmpty() ? latestCommit : "");
        } catch(Exception e) {
            Logger.warn(e, "Exception while getting latest commit hash");
            return "";
        }
    }

    public static String getLatestTag() {
        try {
            File tagsDir = new File("./.git/refs/tags");
            if(tagsDir.isDirectory() && tagsDir.canRead()) {
                File[] tags = tagsDir.listFiles();
                if(tags == null || tags.length == 0) {
                    Logger.warn("No tags in git found");
                    return "";
                }
                List<File> tagsList = Arrays.asList(tags);
                Collections.sort(tagsList, (f1, f2) -> {
                    if(f1.lastModified() > f1.lastModified()) {
                        return 1;
                    } else if(f1.lastModified() == f2.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
                });

                return tagsList.get(0).getName();
            } else {
                Logger.warn("Git directory is not readable");
                return "";
            }
        } catch(Exception e) {
            Logger.warn(e, "Exception while getting latest tag");
            return "";
        }
    }
}
