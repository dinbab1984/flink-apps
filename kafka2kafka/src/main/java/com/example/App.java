package com.example;

/**
 * Kafka flink example!
 *
 */

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


public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
    //required properties
    final String bootstrapServers = args.length > 0 ? args[0] : "localhost:29092";
    final String consumerGroupId = "flink-consumer";
    final String inputTopic = "input-topic";
    final String outputTopic = "output-topic";
    
		//flink stream processor
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

      try{
        //Adding KafkaSource
        KafkaSource<String> source = KafkaSource.<String>builder()
          .setBootstrapServers(bootstrapServers)
          .setTopics(inputTopic)
          .setGroupId(consumerGroupId)
          .setStartingOffsets(OffsetsInitializer.earliest())
          .setValueOnlyDeserializer(new SimpleStringSchema())
          .build();
        //Adding KafkaSink
        KafkaSink<String> sink = KafkaSink.<String>builder()
          .setBootstrapServers(bootstrapServers)
          .setRecordSerializer(KafkaRecordSerializationSchema.builder()
            .setTopic(outputTopic)
            .setValueSerializationSchema(new SimpleStringSchema())
            .build()
          )
          .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
          .build();
        //Consume messages
        DataStream<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        //Publishing messages
        stream.sinkTo(sink);
        
        env.execute("Kafka2KafkaApp");

        } catch (Exception e) 
        { 
          System.out.println("Errors {}" + e.fillInStackTrace().toString());
          LOG.info("Errors {}" + e.fillInStackTrace().toString());
        } 
	}

}
