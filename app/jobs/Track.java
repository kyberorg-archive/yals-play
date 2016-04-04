package jobs;

import play.Logger;
import play.jobs.Job;
import play.mvc.Http;

/**
 * Job which tracks statistics for clicked short link
 *
 * @author Alexander Muravya {@literal <asm@virtalab.net>}
 * @since 1.1
 */
public class Track extends Job {

    private Http.Request request = null;

    public Track(Http.Request request) {
        this.request = request;
    }

    public static Track my(Http.Request request) {
        return new Track(request);
    }

    public static boolean isTrackingAllowedFor(Http.Request request) {
        //Constants meanful for method only
        final String DO_NOT_TRACK_HEADER = "DNT";
        final boolean DEFAULT_IS_TRACKING_ALLOWED = true;
        String DNTValue = Integer.toString(1);

        if(request == null) {
            Logger.error("Cannot analyze DNT: got empty request");
            return DEFAULT_IS_TRACKING_ALLOWED;
        }
        if(request.headers == null || request.headers.size() == 0) {
            Logger.warn("Cannot analyze DNT: no headers is request");
            return DEFAULT_IS_TRACKING_ALLOWED;
        }
        if(request.headers.containsKey(DO_NOT_TRACK_HEADER)) {
            //noinspection RedundantConditionalExpression
            return (request.headers.get(DO_NOT_TRACK_HEADER).value().equals(DNTValue)) ? false : true;
        } else {
            return DEFAULT_IS_TRACKING_ALLOWED;
        }
    }

    @Override
    public void doJob() throws Exception {
        //TODO do something useful here
    }

}
