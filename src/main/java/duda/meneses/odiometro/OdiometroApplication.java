package duda.meneses.odiometro;

import duda.meneses.odiometro.bolt.MentionBolt;
import duda.meneses.odiometro.bolt.SentimentAnalysisBolt;
import duda.meneses.odiometro.bolt.TweetWordSplitterBolt;
import duda.meneses.odiometro.spout.TwitterSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class OdiometroApplication {

    private static final String TOPOLOGY_NAME = "odiometro-twitter-sentiment-analysis";

    public static void main(String[] args) {
        SpringApplication.run(OdiometroApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        Set<String> languages = new HashSet<>(Collections.singletonList("pt"));
        Set<String> hashtags = new HashSet<>(Arrays.asList("bbb", "bbb21"));
        Set<String> mentions = new HashSet<>(Arrays.asList("arthurpicoli", "AfiuneCaio", "camilladelucas",
                "carladiaz", "fiuk", "gilnogueiraofc", "joaoluizpedrosa", "FreireJuliette", "karolconka",
                "kerlinecardoso", "koka_lucas", "LumenaAleluia", "negodioficial", "Pocah", "Projota",
                "iRodolffo", "ssarahandrade", "PortalThaisof", "viihtube"));

        Config config = new Config();
        config.setMessageTimeoutSecs(120);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("TwitterSpout", new TwitterSpout());
        builder.setBolt("MentionBolt", new MentionBolt(languages, hashtags, mentions)).shuffleGrouping("TwitterSpout");
        builder.setBolt("TweetWordSplitterBolt", new TweetWordSplitterBolt(3)).shuffleGrouping("MentionBolt");
        builder.setBolt("SentimentAnalysisBolt", new SentimentAnalysisBolt(10, 10 * 60)).shuffleGrouping("TweetWordSplitterBolt");

        final LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cluster.killTopology(TOPOLOGY_NAME);
            cluster.shutdown();
        }));
    }
}
