import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Attributes;

public class TreeNode<Car>
{
    private ArrayList<Car> data;
    private TreeNode<Car> parent;
    private ArrayList<TreeNode<Car>> children;
    private double entropy;
    private Attributes attribute;
    private String attributevalue;
    private int depth;
    
	public enum Classification{unacc, acc, good, vgood}
	public enum Buying{vhigh, high, med, low}
	public enum Maint{vhigh, high, med, low}
	public enum Doors{Two, Three, Four, Fivemore}
	public enum Persons{Two, Four, More}
	public enum Lug_Boot{small, med, big}
	public enum Safety{low, med, high}
	public enum Attributes (Buying, Maint, Doors, Persons, Lug_Boot, Safety);
    
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
		for(int i = 1; i < data.Size(); i++)
		{
			if(data[i].classification != data[i-1].classification) return false;
		}
		
		return true;
	}

    public TreeNode(ArrayList<Car> input)
    {
    	this.data = input;
    	this.children = new ArrayList<TreeNode<Car>>();
    	this.entropy = ID3.getEntropy(this.data);
    }
    
    public void addChild(ArrayList<Car> child, Attributes attr, String val)
    {
    	TreeNode<Car> childNode = new TreeNode<Car>(child);
    	childNode.depth = this.depth + 1;
    	childNode.parent = this;
    	childNode.entropy = ID3.getEntropy(child);
    	childNode.attribute = attr;
    	childNode.attributevalue = val;
    	this.children.add(childNode);
    	return;
    }
    
    // initiliaze tree build
    public void buildTree()
    {
    	this.depth = 1;
    	this.entropy = ID3.getEntropy(this.data);
    	Attributes att = ID3.getMaxGain(this.entropy, this.data);
    	this.split(att);
    	for(TreeNode<Car> t : this.children)
    	{ 
    		if(!t.isPure) t._buildTree();
    	}  	
    	return;
    }
    
    // recursive tree building
    public void _buildTree()
    {    	
    		Attributes att = ID3.getMaxGain(this.entropy, this.data)
    		this.split(att);
    		for(TreeNode<Car> t : this.children)
        	{ 
        		if(!t.isPure) t._buildTree();
        	} 
    		return;
    }
    
    // Splits data according to splitting attribute and adds children to the tree
    public void split(Attributes attr)
    {
    	switch(attr)
    	{
    		case Attributes.Buying:
    			ArrayList<Car> b_vhigh = new ArrayList<Car>();
    			ArrayList<Car> b_high = new ArrayList<Car>();
    			ArrayList<Car> b_med = new ArrayList<Car>();
    			ArrayList<Car> b_low = new ArrayList<Car>();
    			
    			for (int i = 0; i < this.data.Size(); i++)
    			{
    				if(cars[i].buying == Buying.vhigh) b_vhigh.add(cars[i]);
    				else if(cars[i].buying == Buying.high) b_high.add(cars[i]);
    				else if(cars[i].buying == Buying.med) b_med.add(cars[i]);
    				else b_low.add(cars[i]);
    			}
    			
    			if(!b_vhigh.isEmpty()) this.addChild(b_vhigh, Attributes.Buying, "vhigh");
    			if(!b_high.isEmpty()) this.addChild(b_high, Attributes.Buying, "high");
    			if(!b_med.isEmpty()) this.addChild(b_med, Attributes.Buying, "med");
    			if(!b_low.isEmpty()) this.addChild(b_low, Attributes.Buying, "low");
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
    			}
    			
    			if(!m_vhigh.isEmpty()) this.addChild(m_vhigh, Attributes.Maint, "vhigh");
    			if(!m_high.isEmpty()) this.addChild(m_high, Attributes.Maint, "high");
    			if(!m_med.isEmpty()) this.addChild(m_med, Attributes.Maint, "med");
    			if(!m_low.isEmpty()) this.addChild(m_low, Attributes.Maint, "low");
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
    			}
    			
    			if(!d_two.isEmpty()) this.addChild(d_two, Attributes.Doors, "Two");
    			if(!d_three.isEmpty()) this.addChild(d_three, Attributes.Doors, "Three");
    			if(!d_four.isEmpty()) this.addChild(d_four, Attributes.Doors, "Four");
    			if(!d_fivemore.isEmpty()) this.addChild(d_fivemore, Attributes.Doors, "Fivemore");
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
    			}
    			
    			if(!p_two.isEmpty()) this.addChild(p_two, Attributes.Persons, "Two");
    			if(!p_four.isEmpty()) this.addChild(p_four, Attributes.Persons, "Four");
    			if(!p_more.isEmpty()) this.addChild(p_more, Attributes.Persons, "More");
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
    			}
    			
    			if(!l_small.isEmpty()) this.addChild(l_small, Attributes.Persons, "small");
    			if(!l_med.isEmpty()) this.addChild(l_med, Attributes.Persons, "med");
    			if(!l_big.isEmpty()) this.addChild(l_big, Attributes.Persons, "big");
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
    			}
    			
    			if(!s_low.isEmpty()) this.addChild(s_low, Attributes.Persons, "low");
    			if(!s_med.isEmpty()) this.addChild(s_med, Attributes.Persons, "med");
    			if(!s_high.isEmpty()) this.addChild(s_high, Attributes.Persons, "high");
    			break;
    		
    		default:
    			ArrayList<Car> b_vhigh = new ArrayList<Car>();
    			ArrayList<Car> b_high = new ArrayList<Car>();
    			ArrayList<Car> b_med = new ArrayList<Car>();
    			ArrayList<Car> b_low = new ArrayList<Car>();
    			
    			for (int i = 0; i < this.data.Size(); i++)
    			{
    				if(cars[i].buying == Buying.vhigh) b_vhigh.add(cars[i]);
    				else if(cars[i].buying == Buying.high) b_high.add(cars[i]);
    				else if(cars[i].buying == Buying.med) b_med.add(cars[i]);
    				else b_low.add(cars[i]);
    			}
    			
    			if(!b_vhigh.isEmpty()) this.addChild(b_vhigh, Attributes.Buying, "vhigh");
    			if(!b_high.isEmpty()) this.addChild(b_high, Attributes.Buying, "high");
    			if(!b_med.isEmpty()) this.addChild(b_med, Attributes.Buying, "med");
    			if(!b_low.isEmpty()) this.addChild(b_low, Attributes.Buying, "low");
    			break;
    	}
    	
    	return;
    }
    
}