package jobs;

import app.Storage;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import util.VersionHelper;

/**
 * Application startup sequence
 *
 * @author Alexander Muravya {@literal <asm@virtalab.net>}
 * @since 1.0
 */
@OnApplicationStart
public class Startup extends Job {
    @Override
    public void doJob() throws Exception {
        Logger.info("Initiating startup sequence");

        Storage.get().setLatestCommit(VersionHelper.getLatestCommitHash());
        Storage.get().setLatestTag(VersionHelper.getLatestTag());

        Play.configuration.setProperty("java.net.preferIPv4Stack","true");

    }
}
