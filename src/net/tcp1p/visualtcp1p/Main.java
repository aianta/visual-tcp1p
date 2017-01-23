package net.tcp1p.visualtcp1p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Graph graph = new SingleGraph("Test");
		
		/*
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		
		graph.display();
		*/
		
		String jsonData = "";
		
		//Read input JSON file
		try{
			String fileName = "3jars.json";
			
			File json = new File ("./data/" + fileName);
			FileReader reader = new FileReader (json);
			BufferedReader bufferedReader = new BufferedReader (reader);

			
			String line = bufferedReader.readLine();
			
			while (line != null){
				jsonData += line;
				
				line = bufferedReader.readLine();
			}
			
			System.out.println(jsonData);
			
			
		}catch (IOException e){
			System.out.println("Error reading file!");
		}
		
		//Convert JSON to POJO through Gson
		Gson gson = new Gson();
		LinkChart linkChart = gson.fromJson(jsonData, LinkChart.class);
		
		//System.out.println(linkChart);
		
		ArrayList<GraphElement> items = linkChart.getItems();
		
		graph.display();
		
		//Create Nodes
		for (GraphElement item: items){
			
			if (item.getType().equals("node")){
				System.out.println(item);
				graph.addNode(item.getId());
			}
			
			
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Create Edges
		for (GraphElement item: items){
			
			
			
			if (item.getType().equals("link")){
				System.out.println(item);
				graph.addEdge(item.getId(), Integer.toString(item.getId1()), Integer.toString(item.getId2()));
			}
			
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//graph.display();
		
	}

}
