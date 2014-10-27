package com.zdatainc.rts.storm;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import cmu.arktweetnlp.POSTagger;
import cmu.arktweetnlp.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.BinarySparseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;

/**
 *
 * @author girish
 */
public class WekaTutorial {
    
    private ArrayList<String> featureWords;
    private ArrayList<Attribute> attributeList;
    private Instances inputDataset;
    private POSTagger posTagger;
    private Classifier classifier;
    private ArrayList<String> sentimentClassList;
    private Instances testingInstances0;
    
    File file = new File("/home/user/output.txt");
    
    File file2 = new File("/home/user/output2.txt");
     
         
     
    public WekaTutorial()
    {
    	
        attributeList = new ArrayList<>();
        posTagger = new POSTagger();
        
        FileWriter fw = null;
        BufferedWriter bw = null;
    	try {
    	if (!file.exists()) {
			
			 file.createNewFile();
    	}
			
		 fw = new FileWriter(file.getAbsoluteFile(),true);
		 bw = new BufferedWriter(fw);
		 bw.write("before init");
		 bw.close();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
                
      initialize();
    }
    
    
    private void initialize()
    {
    	
    	 FileWriter fw = null;
         BufferedWriter bw = null;
     	try {
     	if (!file.exists()) {
 			
 				file.createNewFile();
     	}
 			
 		 fw = new FileWriter(file.getAbsoluteFile(),true);
 		 bw = new BufferedWriter(fw);
 		 bw.write("init");
 		 bw.close();
 		
 		
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}   
    	
        ObjectInputStream ois = null;
        try {
            //reads the feature words list to a hashset
            ois = new ObjectInputStream(new FileInputStream("/home/user/FeatureWordsList.dat"));
            featureWords = (ArrayList<String>) ois.readObject();
        } catch (Exception ex) {
            System.out.println("Exception in Deserialization");
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                System.out.println("Exception while closing file after Deserialization");
            }
        }
        
        //creating an attribute list from the list of feature words
        sentimentClassList = new ArrayList<>();
        sentimentClassList.add("positive");
        sentimentClassList.add("negative");
        for(String featureWord : featureWords)
        {
            attributeList.add(new Attribute(featureWord));
        }
        //the last attribute reprsents ths CLASS (Sentiment) of the tweet
        attributeList.add(new Attribute("Sentiment",sentimentClassList)); 
    }
    
    
    public void trainClassifier(final String INPUT_FILENAME)
    {
    	
    	 FileWriter fw = null;
         BufferedWriter bw = null;
     	try {
     	if (!file.exists()) {
 			
 				file.createNewFile();
     	}
 			
 		 fw = new FileWriter(file.getAbsoluteFile(),true);
 		 bw = new BufferedWriter(fw);
 		 bw.write("trainClassifier");
 		 bw.close();
 		
 		
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}  
    	
            getTrainingDataset(INPUT_FILENAME);
            
            //trainingInstances consists of feature vector of every input
            Instances trainingInstances = createInstances("TRAINING_INSTANCES");
            
            for(Instance currentInstance : inputDataset)
            {
                //extractFeature method returns the feature vector for the current input
                Instance currentFeatureVector = extractFeature(currentInstance);
                
                //Make the currentFeatureVector to be added to the trainingInstances
                currentFeatureVector.setDataset(trainingInstances);
                trainingInstances.add(currentFeatureVector);
            }
            
        //You can create the classifier that you want. In this tutorial we use NaiveBayes Classifier
        //For instance classifier = new SMO;
        classifier = new NaiveBayes();
            
        try {
            //classifier training code
            classifier.buildClassifier(trainingInstances);
            
            //storing the trained classifier to a file for future use
            weka.core.SerializationHelper.write("NaiveBayes.model",classifier);
        } catch (Exception ex) {
            System.out.println("Exception in training the classifier.");
        }
    }
    
   
    
    
    public void testClassifier(final String INPUT_FILENAME)
    {
    	
    	 FileWriter fw = null;
         BufferedWriter bw = null;
     	try {
     	if (!file.exists()) {
 			
 				file.createNewFile();
     	}
 			
 		 fw = new FileWriter(file.getAbsoluteFile(),true);
 		 bw = new BufferedWriter(fw);
 		 bw.write("testClassifier");
 		 bw.close();
 		
 		
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}  
    	
        getTrainingDataset(INPUT_FILENAME);
            
        //trainingInstances consists of feature vector of every input
        Instances testingInstances = createInstances("TESTING_INSTANCES");
 
        testingInstances0 = testingInstances;
        
        for(Instance currentInstance : inputDataset)
        {
            //extractFeature method returns the feature vector for the current input
            Instance currentFeatureVector = extractFeature(currentInstance);

            //Make the currentFeatureVector to be added to the trainingInstances
            currentFeatureVector.setDataset(testingInstances);
            testingInstances.add(currentFeatureVector);
        }
            
        
        try {
			classifier = (Classifier) weka.core.SerializationHelper.read("NaiveBayes.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
       try {
            //Classifier deserialization
           
            
            //classifier testing code
            for(Instance testInstance : testingInstances)
            {
                double score = classifier.classifyInstance(testInstance);
                System.out.println(testingInstances.attribute("Sentiment").value((int)score));
                bw.write(testingInstances.attribute("Sentiment").value((int)score));
        		bw.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception in testing the classifier.");
        }
    }
    
    
    
    
    private void getTrainingDataset(final String INPUT_FILENAME)
    {
        try{
            //reading the training dataset from CSV file
            CSVLoader trainingLoader =new CSVLoader();
            trainingLoader.setSource(new File(INPUT_FILENAME));
            inputDataset = trainingLoader.getDataSet();
        }catch(IOException ex)
        {
            System.out.println("Exception in getTrainingDataset Method");
        }
    }
    
    
    private Instances createInstances(final String INSTANCES_NAME)
    {
        
        //create an Instances object with initial capacity as zero 
        Instances instances = new Instances(INSTANCES_NAME,attributeList,0);
        
        //sets the class index as the last attribute (positive or negative)
        instances.setClassIndex(instances.numAttributes()-1);
            
        return instances;
    }
    
    
    private Instance extractFeature(Instance inputInstance)
    {
        Map<Integer,Double> featureMap = new TreeMap<>();
        List<Token> tokens = posTagger.runPOSTagger(inputInstance.stringValue(0));

        for(Token token : tokens)
        {
            switch(token.getPOS())
            {
                case "A":
                case "V":
                case "R":   
                case "#":   
//                	System.out.println(token.getWord());
                	String word = token.getWord().replaceAll("#","");
                            if(featureWords.contains(word))
                            {
                                //adding 1.0 to the featureMap represents that the feature word is present in the input data
                                featureMap.put(featureWords.indexOf(word),1.0);
                            }
            }
        }
        
        
        
        
        
        int indices[] = new int[featureMap.size()+1];
        double values[] = new double[featureMap.size()+1];
        int i=0;
        for(Map.Entry<Integer,Double> entry : featureMap.entrySet())
        {
            indices[i] = entry.getKey();
            values[i] = entry.getValue();
            i++;
        }
        indices[i] = featureWords.size();
        values[i] = (double)sentimentClassList.indexOf(inputInstance.stringValue(1));
        return new SparseInstance(1.0,values,indices,featureWords.size());
    }
    
    
/*    public static void main(String[] args) throws Exception
    {
          WekaTutorial wekaTutorial = new WekaTutorial();
          wekaTutorial.trainClassifier("training.csv");
          wekaTutorial.testClassifier("testing.csv");
    }*/
}    

