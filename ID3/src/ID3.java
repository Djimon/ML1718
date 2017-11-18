import java.util.ArrayList;

import sun.reflect.generics.tree.Tree;

import java.lang.Math;
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
	public enum Doors{Two, Three, Four, Fivemore}
	public enum Persons{Two, Four, More}
	public enum Lug_Boot{small, med, big}
	public enum Safety{low, med, high}
	public enum Attributes (Buying, Maint, Doors, Persons, Lug_Boot, Safety);
	
	TreeNode<String> test = new TreeNode<String>("root");
	
	
	private int TreeDepth = 100; //just for testing, to be filled with correct treeDepth
	Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);

	public static void main(String[] args)
	{
		ArrayList<Car> Cars = new ArrayList<Car>();
		Cars = read("Cardaten/Car.data");
		/*
		Car[] temp = new Car[Cars.size()];
		Cars.toArray(temp);
		for(int i = 0 ; i< temp.length;i++)
		{
			System.out.println(temp[i].ToString());
		}
		*/
		
		TreeNode<Car> Tree = new TreeNode<Tree>(Cars);
		Tree.buildTree();
			
		Tree2XMLConverter printer = new Tree2XMLConverter(Tree);
		printer.AddLineTree();
		printer.write(this.tree, 0);
		printer.FinishTree();
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

// calculates gain for all attributes and returns attribute with highest gain
public static Attributes getMaxGain(double latest_Entropy, ArrayList<Car> cars)
{
	double[] gain = new double[6];
	gain[0] = getGain(latest_Entropy, cars, Attributes.Buying);
	gain[1] = getGain(latest_Entropy, cars, Attributes.Maint);
	gain[2] = getGain(latest_Entropy, cars, Attributes.Doors);
	gain[3] = getGain(latest_Entropy, cars, Attributes.Persons);
	gain[4] = getGain(latest_Entropy, cars, Attributes.Lug_Boot);
	gain[5] = getGain(latest_Entropy, cars, Attributes.Safety);
	
	double maxgaintempvalue = gain[0];
	double maxgaintempindex = 0;
	for(int i= 1; i < gain.length; i++)
	{
		if(gain[i]>maxgaintempvalue)
		{
			maxgaintempvalue = gain[i];
			maxgaintempindex = i;
		}
	}
	
	return Attributes.values()[maxgaintempindex];
}

// gets gain for specified Attribute
public static double getGain(double latest_Entropy, ArrayList<Car> cars, Attributes attr)
{
	double gain = latest_Entropy;

	// calculates gain for specified Attribute
	switch(attr)
	{
	case Attributes.Buying:
		ArrayList<Car> b_vhigh = new ArrayList<Car>();
		ArrayList<Car> b_high = new ArrayList<Car>();
		ArrayList<Car> b_med = new ArrayList<Car>();
		ArrayList<Car> b_low = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].buying == Buying.vhigh) b_vhigh.add(cars[i]);
			else if(cars[i].buying == Buying.high) b_high.add(cars[i]);
			else if(cars[i].buying == Buying.med) b_med.add(cars[i]);
			else b_low.add(cars[i]);
			
			gain -= ((b_vhigh.Size()/cars.Size()) * getEntropy(b_vhigh))
					- ((b_high.Size()/cars.Size()) * getEntropy(b_high))
					- ((b_med.Size()/cars.Size()) * getEntropy(b_med))
					- ((b_low.Size()/cars.Size()) * getEntropy(b_low));
		}
		break;
		
	case Attributes.Maint:
		ArrayList<Car> m_vhigh = new ArrayList<Car>();
		ArrayList<Car> m_high = new ArrayList<Car>();
		ArrayList<Car> m_med = new ArrayList<Car>();
		ArrayList<Car> m_low = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].maint == Maint.vhigh) m_vhigh.add(cars[i]);
			else if(cars[i].maint == Maint.high) m_high.add(cars[i]);
			else if(cars[i].maint == Maint.med) m_med.add(cars[i]);
			else m_low.add(cars[i]);
			
			gain -= ((m_vhigh.Size()/cars.Size()) * getEntropy(m_vhigh))
					- ((m_high.Size()/cars.Size()) * getEntropy(m_high))
					- ((m_med.Size()/cars.Size()) * getEntropy(m_med))
					- ((m_low.Size()/cars.Size()) * getEntropy(m_low));
		}
		break;
		
	case Attributes.Doors:
		ArrayList<Car> d_two = new ArrayList<Car>();
		ArrayList<Car> d_three = new ArrayList<Car>();
		ArrayList<Car> d_four = new ArrayList<Car>();
		ArrayList<Car> d_fivemore = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].doors == Doors.Two) d_two.add(cars[i]);
			else if(cars[i].doors == Doors.Three) d_three.add(cars[i]);
			else if(cars[i].doors == Doors.Four) d_four.add(cars[i]);
			else d_fivemore.add(cars[i]);
			
			gain -= ((d_two.Size()/cars.Size()) * getEntropy(d_two))
					- ((d_three.Size()/cars.Size()) * getEntropy(d_three))
					- ((d_four.Size()/cars.Size()) * getEntropy(d_four))
					- ((d_fivemore.Size()/cars.Size()) * getEntropy(d_fivemore));
		}
		break;
		
	case Attributes.Persons:
		ArrayList<Car> p_two = new ArrayList<Car>();
		ArrayList<Car> p_four = new ArrayList<Car>();
		ArrayList<Car> p_more = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].persons == Persons.Two) p_two.add(cars[i]);
			else if(cars[i].persons == Persons.Four) p_four.add(cars[i]);
			else p_more.add(cars[i]);
			
			gain -= ((p_two.Size()/cars.Size()) * getEntropy(p_two))
					- ((p_four.Size()/cars.Size()) * getEntropy(p_four))
					- ((p_more.Size()/cars.Size()) * getEntropy(p_more));
		}
		break;
		
	case Attributes.Lug_Boot:
		ArrayList<Car> l_small = new ArrayList<Car>();
		ArrayList<Car> l_med = new ArrayList<Car>();
		ArrayList<Car> l_big = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].lug_boot == Lug_Boot.small) l_small.add(cars[i]);
			else if(cars[i].lug_boot == Lug_Boot.med) l_med.add(cars[i]);
			else l_big.add(cars[i]);
			
			gain -= ((l_small.Size()/cars.Size()) * getEntropy(l_small))
					- ((l_med.Size()/cars.Size()) * getEntropy(l_med))
					- ((l_big.Size()/cars.Size()) * getEntropy(l_big));
		}
		break;
		
	case Attributes.Safety:
		ArrayList<Car> s_low = new ArrayList<Car>();
		ArrayList<Car> s_med = new ArrayList<Car>();
		ArrayList<Car> s_high = new ArrayList<Car>();
		
		for (int i = 0; i < cars.Size(); i++)
		{
			if(cars[i].safety == Safety.low) s_low.add(cars[i]);
			else if(cars[i].safety == Safety.med) s_med.add(cars[i]);
			else s_high.add(cars[i]);
			
			gain -= ((s_low.Size()/cars.Size()) * getEntropy(s_low))
					- ((s_med.Size()/cars.Size()) * getEntropy(s_med))
					- ((s_high.Size()/cars.Size()) * getEntropy(s_high));
		}
		break;
		
		default:
			ArrayList<Car> b_vhigh = new ArrayList<Car>();
			ArrayList<Car> b_high = new ArrayList<Car>();
			ArrayList<Car> b_med = new ArrayList<Car>();
			ArrayList<Car> b_low = new ArrayList<Car>();
			
			for (int i = 0; i < cars.Size(); i++)
			{
				if(cars[i].buying == Buying.vhigh) b_vhigh.add(cars[i]);
				else if(cars[i].buying == Buying.high) b_high.add(cars[i]);
				else if(cars[i].buying == Buying.med) b_med.add(cars[i]);
				else b_low.add(cars[i]);
				
				gain -= ((b_vhigh.Size()/cars.Size()) * getEntropy(b_vhigh))
						- ((b_high.Size()/cars.Size()) * getEntropy(b_high))
						- ((b_med.Size()/cars.Size()) * getEntropy(b_med))
						- ((b_low.Size()/cars.Size()) * getEntropy(b_low));
			}
			break;
	}
	

	return gain;
}

// calculate Entropy of a given data set
public static double getEntropy(ArrayList<Car> cars)
{
	int count = cars.size();
	int count_unacc = 0;
	int count_acc = 0;
	int count_good = 0;
	int count_vgood = 0;
	
	// count absolute amounts of each class
	for(Car c : cars)
	{
		switch(c.classification)
		{
		case Classification.unacc:
			count_acc++;
			break;
		case Classification.acc:
			count_unacc++;
			break;
		case Classification.good:
			count_good++;
			break;
		case Classification.vgood:
			count_vgood++;
			break;
		default:
			count_unacc++;
			break;
		}
	}
	// calculate proportions
	double p_unacc = (double)count_unacc / (double) count; 
	double p_acc = (double)count_acc / (double) count; 
	double p_good = (double)count_good / (double) count; 
	double p_vgood = (double)count_vgood / (double) count; 
	
	// calculate entropy
	return double entropy = -1*p_unacc * getlogn(p_unacc, 4) 
					 -1*p_acc * getlogn(p_acc, 4) 
					 -1*p_good * getlogn(p_good, 4) 
					 -1*p_vgood * getlogn(p_vgood, 4);	
}


// to get logarithm of another base than e
public static double getlogn(int number, int base)
{
	return Math.log(number)/Math.log(base);
}


// nicht benötigt
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
	//private int[] level;
	private String lnEnd = "\n";
	private TreeNode<Car> tree;
	
	public Tree2XMLConverter(TreeNode<Car> _tree)
	{
		tree = _tree;
		makeHeader();
		
	//	level = new int[TreeDepth];
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
		//CheckOpenNodes(depth);
		for (int i = 0; i < depth; i++)
		{
			XML += "    "
		}
		XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+lnEnd;
		//this.level[depth]++;		
	}

	public void AddLineLeaf(int depth,String classes, String entropy, String attribute, String value, String Class)
	{
		//CheckOpenNodes(depth);
		for (int i = 0; i < depth; i++)
		{
			XML += "    "
		}
		XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+Class+"</node>"+lnEnd;	
	}
	
	//nicht benötigt
	void CheckOpenNodes(int depth) 
	{
		for (int i = depth; i <= this.level.length ; i++)
		{
			//if (this.level[i] > 0)
			//	CloseNode();
		}
	}

	void CloseNode(int depth) 
	{
		for (int i = 0; i < depth; i++)
		{
			XML += "    "
		}
		XML += "</node>"+lnEnd;
	}
	
	public void FinishTree()
	{
		XML += "</tree>";
	}
	
	public void write(TreeNode<Car> tree, int _depth)
	{
		if(tree.isPure())
		{
			AddLineLeaf(_depth, getclasses(tree), tree.entropy.toString(), tree.attribute.toString(), tree.attributevalue, getClass(tree));
		}
		else
		{
			for(TreeNode<Car> t : tree.children)
			{
				AddLineNode(_depth, getclasses(t), t.entropy.toString(), t.attribute.toString(), t.attributevalue);
				write(t, _depth+1);
				CloseNode(_depth); 
			}
		}
		
	}
	public String getClass(TreeNode<Car> _tree)
	{
		return _tree.data[0].classification.toString();
	}
	
	public String getclasses(TreeNode<Car> _tree)
	{
		ArrayList<Car> unacc = new ArrayList<Car>();
		ArrayList<Car> acc = new ArrayList<Car>();
		ArrayList<Car> good = new ArrayList<Car>();
		ArrayList<Car> vgood = new ArrayList<Car>();
		
		for(int i = 0; i < this.tree; i++)
		{
			switch(_tree.data[i].classification)
			{
				case Classification.unacc:
					unacc.add(_tree.data[i]);
					break;
				case Classification.acc:
					acc.add(_tree.data[i]);
					break;
				case Classification.good:
					good.add(_tree.data[i]);
					break;
				case Classification.vgood:
					vgood.add(_tree.data[i]);
					break;
				default:
					unacc.add(_tree.data[i]);
					break;
			}
		}
		
		return "unacc:" + unacc.Size().toString() + ",acc:" + acc.Size().toString() + ",good:" + good.Size().toString() + ",vgood:" + vgood.Size().toString();
	}
}
}