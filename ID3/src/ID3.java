import java.util.ArrayList;

import ID3.Tree2XMLConverter.Buying;
import ID3.Tree2XMLConverter.Classification;
import ID3.Tree2XMLConverter.Doors;
import ID3.Tree2XMLConverter.Lug_Boot;
import ID3.Tree2XMLConverter.Maint;
import ID3.Tree2XMLConverter.Persons;
import ID3.Tree2XMLConverter.Safety;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Object;


public enum Classification{unacc, acc, good, vgood}
public enum Buying{vhigh, high, med, low}
public enum Maint{vhigh, high, med, low}
public enum Doors{Two, Four, Fivemore}
public enum Persons{Two, Four, More}
public enum Lug_Boot{small, med, big}
public enum Safety{low, med, high}

public class ID3 
{
	TreeNode<String> test = new TreeNode<String>("root");
	
	
	private int TreeDepth = 100; //just for testing, to be filled with correct treeDepth
	Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);

	public static void main(String[] args)
	{
		ArrayList<car> cars = new ArrayList<car>();
		cars = read("cardaten/car.data");
		
		// ID3 laufen lassen
		
		// XML-Ausgabe
	}
}

public static ArrayList<car> read(String filepath)
{
	ArrayList<car> cardata = new ArrayList<car>();
	try (BufferedReader br = new BufferedReader(new FileReader(new File(filepath)))) {
	    String line;
	    while ((line = br.readLine()) != null) {
	        String delims = "[,]";
	    	String[] tokens = line.split(delims);
	    	cardata.add(new car(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]));
	    }
	}
	
	return cardata;
}


class car
{
	Classification classification;
	Buying buying;
	Maint maint;
	Doors doors;
	Persons persons;
	Lug_Boot lug_boot;
	Safety safety;
	
	private car(String classi, String buy, String mai, String door, String pers, String lug, String saf )
	{
		if(classi.equals(Classification.acc.toString())) classification = Classification.acc;
		if(classi.equals(Classification.good.toString())) classification = Classification.good;
		if(classi.equals(Classification.vgood.toString())) classification = Classification.vgood;
		else classification = Classification.unacc;
		
		if(buy.equals(Buying.vhigh.toString())) buying = Buying.vhigh;
		if(buy.equals(Buying.high.toString())) buying = Buying.high;
		if(buy.equals(Buying.med.toString())) buying = Buying.med;
		else buying = Buying.low;
		
		if(mai.equals(Maint.vhigh.toString())) maint = Maint.vhigh;
		if(mai.equals(Maint.high.toString())) maint = Maint.high;
		if(mai.equals(Maint.med.toString())) maint = Maint.med;
		else maint = Maint.low;
		
		if(door.equals("4"))) doors = Doors.Four;
		if(door.equals("5more")) doors = Doors.Fivemore;
		else doors = Doors.Two;
		
		if(pers.equals("4")) persons = Persons.Four;
		if(pers.equals("more")) persons = Persons.More;
		else persons = Persons.Two;
		
		if(lug.equals(Lug_Boot.med.toString())) lug_boot = Lug_Boot.med;
		if(lug.equals(Lug_Boot.big.toString())) lug_boot = Lug_Boot.big;
		else lug_boot = Lug_Boot.small;
		
		if(saf.equals(Safety.med.toString())) safety = Safety.med;
		if(saf.equals(Safety.high.toString())) safety = Safety.high;
		else safety = Safety.low;
	}
	
}

class Node 
{
	private String name;
	private String value;
	
	public Node(String n, String v)
	{
		this.name = n;
		this.value = v;
	}
}

class Tree2XMLConverter
{
	private String XML;
	private char I = '"';
	private int[] level;
	private String lnEnd = "\n";
	
	public Tree2XMLConverter(int TreeDepth)
	{
		makeHeader();
		level = new int[TreeDepth];
	}
	
	public void makeHeader()
	{
		XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";		
	}
	
	public void AddLineTree(String classes, String entropy)
	{
		XML+= "<tree> classes="+I+classes+I+ " entropy="+I+entropy+I+">"+lnEnd;
	}
	
	public void AddLineNode(int depth, String classes, String entropy, String attribute, String value)
	{
		CheckOpenNodes(depth);
		XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+lnEnd;
		this.level[depth]++;		
	}

	public void AddLineLeaf(int depth,String classes, String entropy, String attribute, String value, String Class)
	{
		CheckOpenNodes(depth);
		XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+Class+"</node>"+lnEnd;	
	}
	
	void CheckOpenNodes(int depth) 
	{
		for (int i = depth; i <= this.level.length ; i++)
		{
			if (this.level[i] > 0)
				CloseNode();
		}
	}

	void CloseNode() 
	{
		XML += "</node>";
	}
	
	public void FinishTree()
	{
		XML += "</tree>";
	}
	

}