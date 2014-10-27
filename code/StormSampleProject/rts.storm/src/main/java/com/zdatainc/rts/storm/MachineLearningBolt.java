package com.zdatainc.rts.storm;


import org.apache.log4j.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.BinarySparseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cmu.arktweetnlp.POSTagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.BinarySparseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;

public class MachineLearningBolt extends BaseBasicBolt
{
    private static final long serialVersionUID = 42L;
    private static final Logger LOGGER =
        Logger.getLogger(MachineLearningBolt.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    File file = new File("/home/user/output.txt");
    
    File file2 = new File("/home/user/output2.txt");
    
    private POSTagger postagger;
    private Classifier classifier;
    
    WekaTutorial wekaTutorial;
    
   
   public void getWekaObj(WekaTutorial wekaTutorial0)
   {
	   wekaTutorial = wekaTutorial0;
	   wekaTutorial.trainClassifier("/home/user/training.csv");
 	   wekaTutorial.testClassifier("/home/user/testing.csv");
   }
   
  
   
    
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("tweet_id", "tweet_text"));
        
      
    }

    public void execute(Tuple input, BasicOutputCollector collector)
    {
    	
     
    	
    	//wekaTutorial.trainClassifier("/home/user/training.csv");
    	
    /*	
    	long id = 0;
        String text = null;
        
       
        FileWriter fw = null;
        BufferedWriter bw = null;
    	try {
    	if (!file.exists()) {
			
				file.createNewFile();
    	}
			
		 fw = new FileWriter(file.getAbsoluteFile(),true);
		 bw = new BufferedWriter(fw);
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        
        
        
        LOGGER.debug("filttering incoming tweets");
        String json = input.getString(0);
        try
        {
            JsonNode root = mapper.readValue(json, JsonNode.class);
            
        
                if (root.get("id") != null && root.get("text") != null)
                {
                    id = root.get("id").longValue();
                    text = root.get("text").textValue();

        			bw.write(id+","+text+"\n");
                   // bw.write("11111");
        			bw.close();
        			
        			
        			
        			
        		
                    collector.emit(new Values(id, text));
                }
                else
                    LOGGER.debug("tweet id and/ or text was null");
            
          
        }
        catch (IOException ex)
        {
            LOGGER.error("IO error while filtering tweets", ex);
            LOGGER.trace(null, ex);
        }*/
        
       
    }

    public Map<String, Object> getComponenetConfiguration() { return null; }
    
    
 
    
  
}
