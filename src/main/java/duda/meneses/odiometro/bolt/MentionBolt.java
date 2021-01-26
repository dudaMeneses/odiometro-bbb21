package duda.meneses.odiometro.bolt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import twitter4j.Status;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MentionBolt extends BaseRichBolt {

    private OutputCollector collector;

    @NonNull
    private final Set<String> languages;

    @NonNull
    private final Set<String> hashtags;

    @NonNull
    private final Set<String> mentions;

    @Override
    public void prepare(Map map, TopologyContext topology, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Status tweet = (Status) input.getValueByField("tweet");
        String lang = tweet.getUser().getLang();
        String text = tweet.getText()
                .replaceAll("\\p{Punct}", " ")
                .replaceAll("\\r|\\n", "")
                .toLowerCase();

        if (this.languages.contains(lang)) {
            Stream.of(tweet.getUserMentionEntities()).forEach(mention -> {
                String mentionUserText = mention.getName().toLowerCase();

                if (this.mentions.contains(mentionUserText)) {
                    collector.emit(new Values(mentionUserText, text));
                }
            });


            Stream.of(tweet.getHashtagEntities()).forEach(hashtag ->{
                String hashtagText = hashtag.getText().toLowerCase();

                if (this.hashtags.contains(hashtagText)) {
                    collector.emit(new Values(hashtagText, text));
                }
            });
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("mention", "text"));
    }
}
