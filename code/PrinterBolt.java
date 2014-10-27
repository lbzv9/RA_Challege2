
package technicalelvis.habakkuk.bolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import java.io.*;
public class PrinterBolt extends BaseBasicBolt {
    File file = new File("/home/group8/Twitter.txt");
    File file2 = new File("/home/group8/Image.txt");
    FileWriter fw = null;
     BufferedWriter bw = null;
     FileWriter fw2 = null;
     BufferedWriter bw2 = null;
     long id;
     String text = null;
	  @Override
	    public void execute(Tuple tuple, BasicOutputCollector collector) {
 try{ 
		if(!file.exists())
                {
                        file.createNewFile();
                }
                fw = new FileWriter(file.getAbsoluteFile(),true);
                 bw = new BufferedWriter(fw);
                bw.write("\n"+tuple);
                bw.close();
 
    		    		    			    		     		    		    	            	            	         	             	}catch(IOException e){
    		    		    			    		     		    		    	            	            	         	             	    		e.printStackTrace();
    		    		    			    		     		    		    	            	            	         	             	    		    	}
    		
	    }
	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer ofd) {
	    }	    
}

