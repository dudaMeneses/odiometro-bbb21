package duda.meneses.odiometro.spout;

import duda.meneses.odiometro.listener.TwitterListener;
import org.apache.storm.Config;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private LinkedBlockingQueue<Status> queue;
    private TwitterStream twitterStream;

    @Override
    public void open(Map conf, TopologyContext topology, SpoutOutputCollector collector) {
        queue = new LinkedBlockingQueue<>(1000);
        this.collector = collector;

        TwitterStreamFactory factory = new TwitterStreamFactory();
        twitterStream = factory.getInstance();
        twitterStream.addListener(new TwitterListener(queue));
        twitterStream.sample();
    }

    @Override
    public void nextTuple() {
        Status ret = queue.poll();

        if (ret == null) {
            Utils.sleep(50);
        } else {
            collector.emit(new Values(ret));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }

    @Override
    public void close() {
        twitterStream.shutdown();
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        config.setMaxTaskParallelism(1);

        return config;
    }
}
