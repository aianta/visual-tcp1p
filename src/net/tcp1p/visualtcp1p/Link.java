package net.tcp1p.visualtcp1p;

public class Link extends GraphElement{
	
	int id1; //From node id
	int id2; //To node id

	public Link (String id){
		super (id);
	}
	
	public int getId1(){
		return id1;
	}
	
	public int getId2(){
		return id2;
	}
}
