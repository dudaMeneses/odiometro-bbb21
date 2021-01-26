package duda.meneses.odiometro.listener;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.util.concurrent.LinkedBlockingQueue;

public class TwitterListener implements StatusListener {
    private final LinkedBlockingQueue<Status> queue;

    public TwitterListener(LinkedBlockingQueue<Status> queue) {
        this.queue = queue;
    }

    @Override
    public void onStatus(Status status) {
        queue.offer(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {

    }
}
