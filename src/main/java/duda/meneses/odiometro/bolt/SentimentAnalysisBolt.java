package duda.meneses.odiometro.bolt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SentimentAnalysisBolt extends BaseRichBolt {

    @NonNull
    private final SentimentRepository repository;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
    }

    @Override
    public void execute(Tuple input) {
        String mention = (String) input.getValueByField("mention");
        String word = (String) input.getValueByField("word");

        // get score from the mention member
        // increase index if word is positive
        // decrease index if word is negative
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        /*
         *  FINAL BOLT!!!
         */
    }
}
