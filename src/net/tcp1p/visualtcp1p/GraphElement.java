package net.tcp1p.visualtcp1p;

public class GraphElement {
	
	private String id;
	private String type;
	private int id1;
	private int id2;
	
	public GraphElement (String id){
		this.id = id;
	}
	
	public int getId1 (){
		return id1;
	}
	
	public int getId2 (){
		return id2;
	}
	
	public void setId1 ( int id1 ){
		this.id1 = id1;
	}
	
	public void setId2 ( int id2 ){
		this.id2 = id2;
	}
	
	public String getType (){
		return type;
	}
	
	public void setType (String type){
		this.type = type;
	}
	
	public String getId (){
		return id;
	}
	
	public void setId (String id){
		this.id = id;
	}

	public String toString(){
		
		if (this.id1 != 0){
			return "[" + id + " " + type + " id1: " + id1 + " id2: " + id2 + "]"; 
		}else{
			return "[" + id + " " + type + "]";
		}
		
		
		
	}
}
