package net.tcp1p.visualtcp1p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.*;
import org.graphstream.ui.spriteManager.*;
import org.graphstream.ui.view.Viewer;

import com.google.gson.Gson;

public class Main {
	
	public static SpriteManager spriteManager = null;
	public static Graph graph = null;
	
	public static int ANIMATION_DELAY;
	public static boolean ANIMATION_ENABLED;
	public static boolean DEBUG_ENABLED;
	public static boolean STYLES_ENABLED;
	public static String MODE;
	public static String JSON_FILE_PATH;
	public static String STYLESHEET_PATH;

	public static boolean CONFIG_DONE;
	
	//GROSS COUNTER TO BE REPLACED
	static int nodeCount = 1;
	
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
			DEBUG_ENABLED = Boolean.parseBoolean(config.getProperty("debug"));
			MODE = config.getProperty("mode");
			JSON_FILE_PATH = config.getProperty("JSONpath");
			STYLES_ENABLED = Boolean.parseBoolean(config.getProperty("styles"));
			STYLESHEET_PATH = config.getProperty("stylesheetPath");
			
			CONFIG_DONE = true; //Mark config operation complete
			
		}catch (IOException e){
			System.out.println("Error loading config file!");
			e.printStackTrace();
		}
		
		//Create Graph
		graph = new SingleGraph("solution graph");
		
		//Enable Styling 
		if (STYLES_ENABLED){
			spriteManager = new SpriteManager(graph);
			
			//Verify the stylesheet can be found
			try{
				File stylesheet = new File ("D:/workspace/Eclipse Projects/visual-tcp1p/assets/tcp1p.css");
				FileReader fr = new FileReader (stylesheet);
			
			}catch (IOException e){
			
				System.out.println("Error finding stylesheet!");
			}
			
			//graph.addAttribute("ui.stylehseet", "url('D:/workspace/Eclipse Projects/visual-tcp1p/assets/tcp1p.css');");
		} 
		
		String jsonData = getStringFromJSONFile();
		
		//Convert JSON to POJO through GSON
		Gson gson = new Gson();
		LinkChart linkChart = gson.fromJson(jsonData, LinkChart.class);
		
		//Debug Block
		if (DEBUG_ENABLED){
			System.out.println("Extracted JSON data in POJO:");
			System.out.println(linkChart);
		}

		switch(MODE){
		
		case "Fast":	
			generateFastGraph(graph,linkChart);
			break;
		case "Chronological":
			generateChronologicalGraph(graph,linkChart);
			break;
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
			renderGraph();
		}
	
		try {
		
		//Create Nodes
		for (GraphElement item: items){
			
			if (item.getType().equals("node")){
				graph = addNode(graph, item);
			}
		}		
		
		//Create Edges
		for (GraphElement item: items){	
			if (item.getType().equals("link")){
				graph = addLink(graph, item);
			}	
		}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//Animation Disabled Block
		if (!ANIMATION_ENABLED){
			renderGraph();	
		}
	}
	
	public static void generateChronologicalGraph(Graph graph, LinkChart data){
		
		if (!CONFIG_DONE){
			System.out.println("Configuration failed, or not done! \nCannot proceed. Exiting...");
			System.exit(1);
		}
		
		if (ANIMATION_ENABLED){
			
			renderGraph();
		}
		
		ArrayList<GraphElement> items = data.getItems();
		
		try {
			
			//Iterate through graph elements
			for (GraphElement item: items){	
				
				//If it's a node add a node to the graph
				if (item.getType().equals("node")){
					graph = addNode(graph, item);
				}
				
				//If it's a link add an edge to the graph
				if (item.getType().equals("link")){
					graph = addLink(graph, item);
				}
				
			}
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		
		//Animation Disabled Block
		if (!ANIMATION_ENABLED){
			renderGraph();	
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
				
				File json = new File (JSON_FILE_PATH);
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
	
	
	private static Graph addNode (Graph graph, GraphElement element) throws InterruptedException{
		
		if(DEBUG_ENABLED){
			System.out.println("Added Node: " + element);
		}
		
		graph.addNode(element.getId());
		
		if(STYLES_ENABLED){
			
		}
		
		
		if (ANIMATION_ENABLED){
			TimeUnit.MILLISECONDS.sleep(ANIMATION_DELAY);
		}
		
		return graph;
	}
	
	private static Graph addLink (Graph graph, GraphElement element) throws InterruptedException{
		
		if (DEBUG_ENABLED){
			System.out.println("Added Link: " + element);
		}
		
		graph.addEdge(element.getId(), Integer.toString(element.getId1()), Integer.toString(element.getId2()));
		
		if (ANIMATION_ENABLED){
			TimeUnit.MILLISECONDS.sleep(ANIMATION_DELAY);
		}
		
		return graph;
	}
	
	//Renders the graph with appropriate styles if applicable
	private  static void renderGraph(){
		
		if (STYLES_ENABLED){
			System.setProperty("org.graphstream.ui.renderer",
	                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
			graph.addAttribute("ui.stylesheet", "url('" + STYLESHEET_PATH + "');");
		}

		graph.display();
	}

}
