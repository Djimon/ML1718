import java.util.ArrayList;

public class ID3 
{
	TreeNode<String> test = new TreeNode<String>("root");
	
	
	private int TreeDepth = 100; //just for testing, to be filled with correct treeDepth
	Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);

	public static void main(String[] args)
	{

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