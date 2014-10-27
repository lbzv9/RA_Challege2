package com.zdatainc.rts.storm;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class JoinSentimentsBolt extends BaseBasicBolt
{
    private static final long serialVersionUID = 42L;
    private static final Logger LOGGER =
        Logger.getLogger(JoinSentimentsBolt.class);
    private HashMap<Long, Triple<String, Float, String>> tweets;
    
   // File outputFile = new File("/home/user/kafkaFolder/outputFile.txt");
    
   

    public JoinSentimentsBolt()
    {
        this.tweets = new HashMap<Long, Triple<String, Float, String>>();
    }

    public void execute(Tuple input, BasicOutputCollector collector)
    {
    	/* if (!outputFile.exists()) {
    	    	try {
					outputFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}*/
    	 
    	/*    FileWriter fw = null;
			try {
				fw = new FileWriter(outputFile.getAbsoluteFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedWriter bw = new BufferedWriter(fw);*/
    	
        Long id = input.getLong(input.fieldIndex("tweet_id"));
        String text = input.getString(input.fieldIndex("tweet_text"));
       
        if (input.contains("pos_score"))
        {
            Float pos = input.getFloat(input.fieldIndex("pos_score"));
            if (this.tweets.containsKey(id))
            {
                Triple<String, Float, String> triple = this.tweets.get(id);
                if ("neg".equals(triple.getCar()))
                    emit(collector, id, triple.getCaar(), pos, triple.getCdr());
                else
                {
                    LOGGER.warn("one sided join attempted");
                    this.tweets.remove(id);
                }
            }
            else
                this.tweets.put(
                    id,
                    new Triple<String, Float, String>("pos", pos, text));
            
      /*      try {
				bw.write(id+","+pos+","+text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            
        }
        else if (input.contains("neg_score"))
        {
            Float neg = input.getFloat(input.fieldIndex("neg_score"));
            if (this.tweets.containsKey(id))
            {
                Triple<String, Float, String> triple = this.tweets.get(id);
                if ("pos".equals(triple.getCar())){
                    emit(collector, id, triple.getCaar(), neg, triple.getCdr());
                    
              /*      try {
        				bw.write(id+","+neg+","+text);
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}*/
                }
                else
                {
                    LOGGER.warn("one sided join attempted");
                    this.tweets.remove(id);
                }
            }
            else{
            	
           /* 	  try {
      				bw.write(id+","+neg+","+text);
      			} catch (IOException e) {
      				// TODO Auto-generated catch block
      				e.printStackTrace();
      			}*/
            	
                this.tweets.put(
                    id,
                    new Triple<String, Float, String>("neg", neg, text));
                
            }
            
          
        }
        else
            throw new RuntimeException("wat");
        
        
        
        
        
    }

    private void emit(
        BasicOutputCollector collector,
        Long id,
        String text,
        float pos,
        float neg)
    {
        collector.emit(new Values(id, pos, neg, text));
        this.tweets.remove(id);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("tweet_id",
                                    "pos_score",
                                    "neg_score",
                                    "tweet_text"));
    }
}
