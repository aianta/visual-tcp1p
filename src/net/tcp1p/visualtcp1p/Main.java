package net.tcp1p.visualtcp1p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import com.google.gson.Gson;

public class Main {
	
	public static int ANIMATION_DELAY;
	public static boolean ANIMATION_ENABLED;
	public static boolean DEBUG_ENABLED;
	public static boolean CHRONOLOGICAL;
	public static String JSON_FILE_NAME;
	public static String JSON_FILE_PATH;

	public static boolean CONFIG_DONE;
	
	public static void main(String[] args) {
	
		CONFIG_DONE = false; //Set config flag
		
		//Load config file
		Properties config = new Properties();
		
		try{
			File configFile = new File ("config.properties");
			FileReader configReader = new FileReader(configFile);
			BufferedReader  configBufferedReader = new BufferedReader(configReader);
			config.load(configBufferedReader);
			
			ANIMATION_DELAY = Integer.parseInt(config.getProperty("delay"));
			ANIMATION_ENABLED = Boolean.parseBoolean(config.getProperty("animation"));
			DEBUG_ENABLED = Boolean.parseBoolean(config.getProperty("debugs"));
			CHRONOLOGICAL = Boolean.parseBoolean(config.getProperty("chronological"));
			JSON_FILE_NAME = config.getProperty("JSONfilename");
			JSON_FILE_PATH = config.getProperty("JSONfilepath");
			
			CONFIG_DONE = true; //Mark config operation complete
			
		}catch (IOException e){
			System.out.println("Error loading config file!");
			e.printStackTrace();
		}
		
		Graph graph = new SingleGraph("solution graph");
		
		String jsonData = getStringFromJSONFile();
		
		//Convert JSON to POJO through GSON
		Gson gson = new Gson();
		LinkChart linkChart = gson.fromJson(jsonData, LinkChart.class);
		
		//Debug Block
		if (Boolean.parseBoolean(config.getProperty("debug"))){
			System.out.println("Extracted JSON data in POJO:");
			System.out.println(linkChart);
		}

		if (CHRONOLOGICAL){
			generateChronologicalGraph(graph,linkChart);
		}else{
			generateFastGraph(graph,linkChart);
		}
		
	}
	
	public static void generateFastGraph(Graph graph, LinkChart data){
		
		if (!CONFIG_DONE){
			System.out.println("Configuration failed, or not done! \nCannot proceed. Exiting...");
			System.exit(1);
		}
		
		ArrayList<GraphElement> items = data.getItems();
		
		//Animation Block
		if(ANIMATION_ENABLED){
			graph.display();
		}

	
		try {
		
		//Create Nodes
		for (GraphElement item: items){
			
			if (item.getType().equals("node")){
				
				if(DEBUG_ENABLED){
					System.out.println(item);
				}

				graph.addNode(item.getId());
			}
			
			if (ANIMATION_ENABLED){
				TimeUnit.MILLISECONDS.sleep(ANIMATION_DELAY);
			}
		}		
		
		//Create Edges
		for (GraphElement item: items){	
			
			if (item.getType().equals("link")){
				
				if (DEBUG_ENABLED){
					System.out.println(item);
				}

				graph.addEdge(item.getId(), Integer.toString(item.getId1()), Integer.toString(item.getId2()));
			}
		
			//Animation delay block
			if (ANIMATION_ENABLED){
				TimeUnit.MILLISECONDS.sleep(ANIMATION_DELAY);
			}
			
		}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//Animation Disabled Block
		if (!ANIMATION_ENABLED){
			graph.display();	
		}
	}
	
	public static void generateChronologicalGraph(Graph graph, LinkChart data){
		
		if (!CONFIG_DONE){
			System.out.println("Configuration failed, or not done! \nCannot proceed. Exiting...");
			System.exit(1);
		}
		
		if (ANIMATION_ENABLED){
			graph.display();
		}
		
		ArrayList<GraphElement> items = data.getItems();
		
		try {

			for (GraphElement item: items){	
				
				if (item.getType().equals("node")){
					
					if (DEBUG_ENABLED){
						System.out.println(item);
					}

					graph.addNode(item.getId());
				}
				
				if (item.getType().equals("link")){
					
					if (DEBUG_ENABLED){
						System.out.println(item);
					}

					graph.addEdge(item.getId(), Integer.toString(item.getId1()), Integer.toString(item.getId2()));
				}
			
				//Animation delay block
				if (ANIMATION_ENABLED){
					TimeUnit.MILLISECONDS.sleep(ANIMATION_DELAY);
				}
				
			}
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		
		//Animation Disabled Block
		if (!ANIMATION_ENABLED){
			graph.display();	
		}
	}
	
	public static String getStringFromJSONFile (){
		
		if (!CONFIG_DONE){
			System.out.println("Configuration failed, or not done! \nCannot proceed. Exiting...");
			System.exit(1);
		}
		
		String jsonData = "";
		
		//Read input JSON file
		try{
				String filepath = JSON_FILE_PATH + JSON_FILE_NAME;
				
				File json = new File (filepath);
				FileReader reader = new FileReader (json);
				BufferedReader bufferedReader = new BufferedReader (reader);
				
				//Get the first line
				String line = bufferedReader.readLine();
				
				//While the next line is not null
				while (line != null){ 
					jsonData += line; //Add the line to jsonData	
					line = bufferedReader.readLine(); //Get the next line
				}
					
				if (DEBUG_ENABLED){
					System.out.println(jsonData);
				}
					
				}catch (IOException e){
					System.out.println("Error reading file!");
				}
		
		return jsonData;
	}

}
