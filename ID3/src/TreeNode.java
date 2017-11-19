import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


class TreeNode<Car>
{
	private SubCar dataList[];
    ArrayList<SubCar> data;
    private TreeNode<Car> parent;
    ArrayList<TreeNode<Car>> children;
    double entropy;
    Attribute attribute;
    String attributevalue;
    private int depth;

    public int StackCounter = 0;
    public int k = 0;
    
    public boolean isRoot() 
    {
		return parent == null;
	}

	public boolean isLeaf() 
	{
		return children.size() == 0;
	}
	
	public boolean isPure()
	{
		for(int i = 1; i < dataList.length; i++)
		{
			if(dataList[i].classification != dataList[i-1].classification) return false;
		}
		
		return true;
	}
	public TreeNode(ArrayList<SubCar> input)
	{
		this.data = input;
		this.children = new ArrayList<TreeNode<Car>>();
    	this.entropy = getEntropy(this.data);
	}

    public TreeNode (Car[] input, ArrayList<SubCar> input2)
    {
    	this.data = input2;
    	this.dataList = (SubCar[]) input.clone();
    	this.children = new ArrayList<TreeNode<Car>>();
    	this.entropy = getEntropy(this.data);
    }
    
    public void addChild(ArrayList<SubCar> child, Attribute buying, String val)
    {
    	SubCar results[] = new SubCar[child.size()];
		
		for(int i=0; i<child.size();i++)
		{
			results[i] = child.get(i);
		}
    	TreeNode<Car> childNode = new TreeNode<Car>(child);
    	childNode.dataList = results;
    	childNode.depth = this.depth + 1;
    	childNode.parent = (TreeNode<Car>) this;
    	childNode.entropy = getEntropy(child);
    	childNode.attribute = buying;
    	childNode.attributevalue = val;
    	this.children.add(childNode);
    	return;
    }
    
    // initiliaze tree build
    public void buildTree()
    {
    	this.depth = 1;
    	this.entropy = getEntropy(this.data);
    	Attribute att = getMaxGain(this.entropy, this.data);
    	this.split(att);
    	for(TreeNode<Car> t : this.children)
    	{ 
    		if(!t.isPure()) t._buildTree();
    	}  	
    	return;
    }
    
    // recursive tree building
    public void _buildTree()
    { 
    	StackCounter += 1;
		Attribute att = getMaxGain(this.entropy, this.data);
		this.split(att);
		Iterator<TreeNode<Car>> IT = this.children.iterator();
		while (IT.hasNext())
		{
			k += 1;
			TreeNode<Car> temp = IT.next();
    		if(! temp.isPure()) temp._buildTree();			
		}
		for(TreeNode<Car> t : this.children)
    	{ 
			
    	} 
		System.out.println("StackDepth= " + StackCounter);
		return;
    }
    
    // Splits data according to splitting attribute and adds children to the tree
    public void split(Attribute attr)
    {
    	switch(attr)
    	{
    		case Buying:
    			ArrayList<SubCar> b_vhigh = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_high = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_med = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_low = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < this.dataList.length; i++)
    			{
    				if(dataList[i].buying == Buying.vhigh) b_vhigh.add( dataList[i]);
    				else if(dataList[i].buying == Buying.high) b_high.add(dataList[i]);
    				else if(dataList[i].buying == Buying.med) b_med.add(dataList[i]);
    				else b_low.add(dataList[i]);
    			}
    			
    			if(!b_vhigh.isEmpty()) this.addChild(b_vhigh, Attribute.Buying, "vhigh");
    			if(!b_high.isEmpty()) this.addChild(b_high, Attribute.Buying, "high");
    			if(!b_med.isEmpty()) this.addChild(b_med, Attribute.Buying, "med");
    			if(!b_low.isEmpty()) this.addChild(b_low, Attribute.Buying, "low");
    			break;
    		
    		case Maint:
    			ArrayList<SubCar> m_vhigh = new ArrayList<SubCar>();
    			ArrayList<SubCar> m_high = new ArrayList<SubCar>();
    			ArrayList<SubCar> m_med = new ArrayList<SubCar>();
    			ArrayList<SubCar> m_low = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < data.size(); i++)
    			{
    				if(dataList[i].maint == Maint.vhigh) m_vhigh.add(dataList[i]);
    				else if(dataList[i].maint == Maint.high) m_high.add(dataList[i]);
    				else if(dataList[i].maint == Maint.med) m_med.add(dataList[i]);
    				else m_low.add(dataList[i]);
    			}
    			
    			if(!m_vhigh.isEmpty()) this.addChild(m_vhigh, Attribute.Maint, "vhigh");
    			if(!m_high.isEmpty()) this.addChild(m_high, Attribute.Maint, "high");
    			if(!m_med.isEmpty()) this.addChild(m_med, Attribute.Maint, "med");
    			if(!m_low.isEmpty()) this.addChild(m_low, Attribute.Maint, "low");
    			break;
    		
    		case Doors:
    			ArrayList<SubCar> d_two = new ArrayList<SubCar>();
    			ArrayList<SubCar> d_three = new ArrayList<SubCar>();
    			ArrayList<SubCar> d_four = new ArrayList<SubCar>();
    			ArrayList<SubCar> d_fivemore = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < data.size(); i++)
    			{
    				if(dataList[i].doors == Doors.Two) d_two.add(dataList[i]);
    				else if(dataList[i].doors == Doors.Three) d_three.add(dataList[i]);
    				else if(dataList[i].doors == Doors.Four) d_four.add(dataList[i]);
    				else d_fivemore.add(dataList[i]);
    			}
    			
    			if(!d_two.isEmpty()) this.addChild(d_two, Attribute.Doors, "Two");
    			if(!d_three.isEmpty()) this.addChild(d_three, Attribute.Doors, "Three");
    			if(!d_four.isEmpty()) this.addChild(d_four, Attribute.Doors, "Four");
    			if(!d_fivemore.isEmpty()) this.addChild(d_fivemore, Attribute.Doors, "Fivemore");
    			break;
    		
    		case Persons:
    			ArrayList<SubCar> p_two = new ArrayList<SubCar>();
    			ArrayList<SubCar> p_four = new ArrayList<SubCar>();
    			ArrayList<SubCar> p_more = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < data.size(); i++)
    			{
    				if(dataList[i].persons == Persons.Two) p_two.add(dataList[i]);
    				else if(dataList[i].persons == Persons.Four) p_four.add(dataList[i]);
    				else p_more.add(dataList[i]);
    			}
    			
    			if(!p_two.isEmpty()) this.addChild(p_two, Attribute.Persons, "Two");
    			if(!p_four.isEmpty()) this.addChild(p_four, Attribute.Persons, "Four");
    			if(!p_more.isEmpty()) this.addChild(p_more, Attribute.Persons, "More");
    			break;
    		
    		case Lug_Boot:
    			ArrayList<SubCar> l_small = new ArrayList<SubCar>();
    			ArrayList<SubCar> l_med = new ArrayList<SubCar>();
    			ArrayList<SubCar> l_big = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < data.size(); i++)
    			{
    				if(dataList[i].lug_boot == Lug_Boot.small) l_small.add(dataList[i]);
    				else if(dataList[i].lug_boot == Lug_Boot.med) l_med.add(dataList[i]);
    				else l_big.add(dataList[i]);
    			}
    			
    			if(!l_small.isEmpty()) this.addChild(l_small, Attribute.Persons, "small");
    			if(!l_med.isEmpty()) this.addChild(l_med, Attribute.Persons, "med");
    			if(!l_big.isEmpty()) this.addChild(l_big, Attribute.Persons, "big");
    			break;
    		
    		case Safety:
    			ArrayList<SubCar> s_low = new ArrayList<SubCar>();
    			ArrayList<SubCar> s_med = new ArrayList<SubCar>();
    			ArrayList<SubCar> s_high = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < data.size(); i++)
    			{
    				if(dataList[i].safety == Safety.low) s_low.add(dataList[i]);
    				else if(dataList[i].safety == Safety.med) s_med.add(dataList[i]);
    				else s_high.add(dataList[i]);
    			}
    			
    			if(!s_low.isEmpty()) this.addChild(s_low, Attribute.Persons, "low");
    			if(!s_med.isEmpty()) this.addChild(s_med, Attribute.Persons, "med");
    			if(!s_high.isEmpty()) this.addChild(s_high, Attribute.Persons, "high");
    			break;
    		
    		default:
    			ArrayList<SubCar> b_vhigh2 = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_high2 = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_med2 = new ArrayList<SubCar>();
    			ArrayList<SubCar> b_low2 = new ArrayList<SubCar>();
    			
    			for (int i = 0; i < this.data.size(); i++)
    			{
    				if(dataList[i].buying == Buying.vhigh) b_vhigh2.add( dataList[i]);
    				else if(dataList[i].buying == Buying.high) b_high2.add(dataList[i]);
    				else if(dataList[i].buying == Buying.med) b_med2.add(dataList[i]);
    				else b_low2.add(dataList[i]);
    			}
    			
    			if(!b_vhigh2.isEmpty()) this.addChild(b_vhigh2, Attribute.Buying, "vhigh");
    			if(!b_high2.isEmpty()) this.addChild(b_high2, Attribute.Buying, "high");
    			if(!b_med2.isEmpty()) this.addChild(b_med2, Attribute.Buying, "med");
    			if(!b_low2.isEmpty()) this.addChild(b_low2, Attribute.Buying, "low");
    			break;
    	}
    	
    	return;
    }
    

	public double getEntropy(ArrayList<SubCar> cars)
	{
		int count = cars.size();
		int count_unacc = 0;
		int count_acc = 0;
		int count_good = 0;
		int count_vgood = 0;

		// count absolute amounts of each class
		for(SubCar c : cars)
		{
			switch(c.classification)
			{
			case unacc:
				count_acc++;
				break;
			case acc:
				count_unacc++;
				break;
			case good:
				count_good++;
				break;
			case vgood:
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
		double entropy = -1*p_unacc * getlogn(p_unacc, 4) 
						 -1*p_acc * getlogn(p_acc, 4) 
						 -1*p_good * getlogn(p_good, 4) 
						 -1*p_vgood * getlogn(p_vgood, 4);	
		return entropy;
	}
	
	public double getlogn(double p_unacc, int base)
	{
		return Math.log(p_unacc)/Math.log(base);
	}
	
	public double getGain(double latest_Entropy, ArrayList<SubCar> cars, Attribute attr)
	{
		double gain = latest_Entropy;

		// calculates gain for specified Attribute
		switch(attr)
		{
		case Buying :
			ArrayList<SubCar> b_vhigh = (ArrayList<SubCar>) new ArrayList<SubCar>();
			ArrayList<SubCar> b_high = new ArrayList<SubCar>();
			ArrayList<SubCar> b_med = new ArrayList<SubCar>();
			ArrayList<SubCar> b_low = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				//TODO: global enums
				if(T.buying == Buying.vhigh) b_vhigh.add(cars.get(i));
				else if(T.buying == Buying.high) b_high.add(cars.get(i));
				else if(T.buying == Buying.med) b_med.add(cars.get(i));
				else b_low.add(cars.get(i));
				
				gain -= ((b_vhigh.size()/cars.size()) * getEntropy(b_vhigh))
						- ((b_high.size()/cars.size()) * getEntropy(b_high))
						- ((b_med.size()/cars.size()) * getEntropy(b_med))
						- ((b_low.size()/cars.size()) * getEntropy(b_low));
			}
			break;
			
		case Maint:
			ArrayList<SubCar> m_vhigh = new ArrayList<SubCar>();
			ArrayList<SubCar> m_high = new ArrayList<SubCar>();
			ArrayList<SubCar> m_med = new ArrayList<SubCar>();
			ArrayList<SubCar> m_low = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				if(T.maint == Maint.vhigh) m_vhigh.add(cars.get(i));
				else if(T.maint == Maint.high) m_high.add(cars.get(i));
				else if(T.maint == Maint.med) m_med.add(cars.get(i));
				else m_low.add(cars.get(i));
				
				gain -= ((m_vhigh.size()/cars.size()) * getEntropy(m_vhigh))
						- ((m_high.size()/cars.size()) * getEntropy(m_high))
						- ((m_med.size()/cars.size()) * getEntropy(m_med))
						- ((m_low.size()/cars.size()) * getEntropy(m_low));
			}
			break;
			
		case Doors:
			ArrayList<SubCar> d_two = new ArrayList<SubCar>();
			ArrayList<SubCar> d_three = new ArrayList<SubCar>();
			ArrayList<SubCar> d_four = new ArrayList<SubCar>();
			ArrayList<SubCar> d_fivemore = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				if(T.doors == Doors.Two) d_two.add(cars.get(i));
				else if(T.doors == Doors.Three) d_three.add(cars.get(i));
				else if(T.doors == Doors.Four) d_four.add(cars.get(i));
				else d_fivemore.add(cars.get(i));
				
				gain -= ((d_two.size()/cars.size()) * getEntropy(d_two))
						- ((d_three.size()/cars.size()) * getEntropy(d_three))
						- ((d_four.size()/cars.size()) * getEntropy(d_four))
						- ((d_fivemore.size()/cars.size()) * getEntropy(d_fivemore));
			}
			break;
			
		case Persons:
			ArrayList<SubCar> p_two = new ArrayList<SubCar>();
			ArrayList<SubCar> p_four = new ArrayList<SubCar>();
			ArrayList<SubCar> p_more = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				if(T.persons == Persons.Two) p_two.add(cars.get(i));
				else if(T.persons == Persons.Four) p_four.add(cars.get(i));
				else p_more.add(cars.get(i));
				
				gain -= ((p_two.size()/cars.size()) * getEntropy(p_two))
						- ((p_four.size()/cars.size()) * getEntropy(p_four))
						- ((p_more.size()/cars.size()) * getEntropy(p_more));
			}
			break;
			
		case Lug_Boot:
			ArrayList<SubCar> l_small = new ArrayList<SubCar>();
			ArrayList<SubCar> l_med = new ArrayList<SubCar>();
			ArrayList<SubCar> l_big = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				if(T.lug_boot == Lug_Boot.small) l_small.add(cars.get(i));
				else if(T.lug_boot == Lug_Boot.med) l_med.add(cars.get(i));
				else l_big.add(cars.get(i));
				
				gain -= ((l_small.size()/cars.size()) * getEntropy(l_small))
						- ((l_med.size()/cars.size()) * getEntropy(l_med))
						- ((l_big.size()/cars.size()) * getEntropy(l_big));
			}
			break;
			
		case Safety:
			ArrayList<SubCar> s_low = new ArrayList<SubCar>();
			ArrayList<SubCar> s_med = new ArrayList<SubCar>();
			ArrayList<SubCar> s_high = new ArrayList<SubCar>();
			
			for (int i = 0; i < cars.size(); i++)
			{
				SubCar T = (SubCar) cars.get(i);
				if(T.safety == Safety.low) s_low.add(cars.get(i));
				else if(T.safety == Safety.med) s_med.add(cars.get(i));
				else s_high.add(cars.get(i));
				
				gain -= ((s_low.size()/cars.size()) * getEntropy(s_low))
						- ((s_med.size()/cars.size()) * getEntropy(s_med))
						- ((s_high.size()/cars.size()) * getEntropy(s_high));
			}
			break;
			
			default:
				ArrayList<SubCar> b_vhigh2 = new ArrayList<SubCar>();
				ArrayList<SubCar> b_high2 = new ArrayList<SubCar>();
				ArrayList<SubCar> b_med2 = new ArrayList<SubCar>();
				ArrayList<SubCar> b_low2 = new ArrayList<SubCar>();
				
				for (int i = 0; i < cars.size(); i++)
				{
					SubCar T = (SubCar) cars.get(i);
					if(T.buying == Buying.vhigh) b_vhigh2.add(cars.get(i));
					else if(T.buying == Buying.high) b_high2.add(cars.get(i));
					else if(T.buying == Buying.med) b_med2.add(cars.get(i));
					else b_low2.add(cars.get(i));
					
					gain -= ((b_vhigh2.size()/cars.size()) * getEntropy(b_vhigh2))
							- ((b_high2.size()/cars.size()) * getEntropy(b_high2))
							- ((b_med2.size()/cars.size()) * getEntropy(b_med2))
							- ((b_low2.size()/cars.size()) * getEntropy(b_low2));
				}
				break;
		}	
		return gain;
	}
	
	public Attribute getMaxGain(double latest_Entropy, ArrayList<SubCar> cars)
	{
		double[] gain = new double[6];
		gain[0] = getGain(latest_Entropy, cars, Attribute.Buying);
		gain[1] = getGain(latest_Entropy, cars, Attribute.Maint);
		gain[2] = getGain(latest_Entropy, cars, Attribute.Doors);
		gain[3] = getGain(latest_Entropy, cars, Attribute.Persons);
		gain[4] = getGain(latest_Entropy, cars, Attribute.Lug_Boot);
		gain[5] = getGain(latest_Entropy, cars, Attribute.Safety);
		
		double maxgaintempvalue = gain[0];
		int maxgaintempindex = 0;
		for(int i= 1; i < gain.length; i++)
		{
			if(gain[i]>maxgaintempvalue)
			{
				maxgaintempvalue = gain[i];
				maxgaintempindex = i;
			}
		}
		
		return Attribute.values()[maxgaintempindex];
	}
	
}