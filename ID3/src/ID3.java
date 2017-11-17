import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Object;


public class ID3 
{
	public enum Classification{unacc, acc, good, vgood}
	public enum Buying{vhigh, high, med, low}
	public enum Maint{vhigh, high, med, low}
	public enum Doors{Two, Four, Fivemore}
	public enum Persons{Two, Four, More}
	public enum Lug_Boot{small, med, big}
	public enum Safety{low, med, high}
	
	TreeNode<String> test = new TreeNode<String>("root");
	
	
	private int TreeDepth = 100; //just for testing, to be filled with correct treeDepth
	Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);

	public static void main(String[] args)
	{
		ArrayList<Car> Cars = new ArrayList<Car>();
		Cars = read("Cardaten/Car.data");
		Car[] temp = new Car[Cars.size()];
		Cars.toArray(temp);
		for(int i = 0 ; i< temp.length;i++)
		{
			System.out.println(temp[i].ToString());
		}
		
		// ID3 laufen lassen
		// if pure > break;
		
		
		// XML-Ausgabe
	}

public static ArrayList<Car> read(String filepath)
{
	ArrayList<Car> Cardata = new ArrayList<Car>();
	try (BufferedReader br = new BufferedReader(new FileReader(new File(filepath)))) {
	    String line;
	    while ((line = br.readLine()) != null) {
	        String delims = "[,]";
	    	String[] tokens = line.split(delims);	    	
	    	Cardata.add(new Car(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]));
	    }
	} catch (FileNotFoundException e) {
		System.out.println("no such file: "+filepath);
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("somethng went wrong ( "+e+")");
		e.printStackTrace();
	}
	
	return Cardata;
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
}