package duda.meneses.odiometro.bolt;

import duda.meneses.odiometro.dictionary.DictionaryWords;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SentimentAnalysisBolt extends BaseRichBolt {

    @NonNull
    private final long logIntervalSec;

    @NonNull
    private final long clearIntervalSec;

    private Map<String, Long> sentimentScoreCounter;
    private long lastLogTime;
    private long lastClearTime;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        sentimentScoreCounter = new HashMap<>();
        lastLogTime = System.currentTimeMillis();
        lastClearTime = System.currentTimeMillis();
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
