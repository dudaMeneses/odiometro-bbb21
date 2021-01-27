package duda.meneses.odiometro.bolt;

import duda.meneses.odiometro.dictionary.DictionaryWords;
import duda.meneses.odiometro.model.Sentiment;
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

    private Map<String, Sentiment> sentiments;
    private long lastLogTime;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        sentiments = new HashMap<>();
        lastLogTime = System.currentTimeMillis();
    }

    @Override
    public void execute(Tuple input) {
        String mention = (String) input.getValueByField("mention");
        String word = (String) input.getValueByField("word");

        Sentiment sentiment = sentiments.getOrDefault(mention, defaultSentiment(mention));

        sentiments.put(mention, sentiment.withTotal(sentiment.getTotal() + 1)
                                         .withNegative(calculateSentiment(word, sentiment)));

        long now = System.currentTimeMillis();
        long logPeriodSec = (now - lastLogTime) / 1000;
        if (logPeriodSec > logIntervalSec) {
            log.info("###################################################################");

            sentiments.forEach((key, value) ->
                   log.info("{}: {} negatives from {}", value.getName(), value.getNegative(), value.getTotal()));

            log.info("###################################################################");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        /*
         *  FINAL BOLT!!!
         */
    }

    private int calculateSentiment(String word, Sentiment sentiment) {
        if(DictionaryWords.NEGATIVE_WORDS.contains(word)){
            return sentiment.getNegative() + 1;
        } else {
            return sentiment.getNegative();
        }
    }

    private Sentiment defaultSentiment(String mention) {
        return Sentiment.builder().name(mention).build();
    }
}
