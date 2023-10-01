package com.example;

/**
 * Kafka flink actico example!
 *
 */

//import java.util.Objects;
import java.util.Properties;

//flink packages
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//User defined packages
//import realtime_examples.kafka_flink_actico.DataStreamMap;

public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {

		//flink stream processor
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "kafka:9092");
		properties.setProperty("group.id", "flink-consumer");
		LOG.info("Properties set {}", properties);
      try{
        //Adding KafkaSource
        KafkaSource<String> source = KafkaSource.<String>builder()
          .setBootstrapServers(properties.getProperty("bootstrap.servers", null))
          .setTopics("input-topic")
          .setGroupId(properties.getProperty("group.id", null))
          .setStartingOffsets(OffsetsInitializer.earliest())
          .setValueOnlyDeserializer(new SimpleStringSchema())
          .build();
        //Adding KafkaSink
        KafkaSink<String> sink = KafkaSink.<String>builder()
          .setBootstrapServers(properties.getProperty("bootstrap.servers", null))
          .setRecordSerializer(KafkaRecordSerializationSchema.builder()
            .setTopic("output-topic")
            .setValueSerializationSchema(new SimpleStringSchema())
            .build()
          )
          .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
          .build();
        //Consume messages
        DataStream<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        //Publishing messages
        stream.sinkTo(sink);
        
          
	    env.execute("KafkaFlinkExampleApp");
        } catch (Exception e) 
        { 
          System.out.println("Errors {}" + e);
          LOG.info("Errors {}", e);
        } 
	}

}
