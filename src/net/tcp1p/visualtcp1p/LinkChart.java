package net.tcp1p.visualtcp1p;

import java.util.ArrayList;

public class LinkChart {

	ArrayList<GraphElement> items = new ArrayList<GraphElement>();
	
	public ArrayList<GraphElement> getItems (){
		return items;
	}
	
	//Return String representation of LinkChart
	public String toString(){
		String result = "";
		
		for (GraphElement item: items){
			result += item.toString() + "\n"; 
		}
		
		return result;
	}
	
}
