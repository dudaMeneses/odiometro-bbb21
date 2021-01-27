package duda.meneses.odiometro.bolt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IWindowedBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class TweetWordSplitterBolt extends BaseRichBolt {

    @NonNull
    private final int minWordLength;

    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topology, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String mention = (String) input.getValueByField("mention");
        String text = (String) input.getValueByField("text");

        Stream.of(text.split(" ")).forEach(word -> {
            if (word.length() >= minWordLength) {
                collector.emit(new Values(mention, word));
            }
        });
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("mention", "word"));
    }
}
