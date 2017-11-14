import java.util.ArrayList;
import java.util.List;

public class TreeNode<T>
{
    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    
    public boolean isRoot() 
    {
		return parent == null;
	}

	public boolean isLeaf() 
	{
		return children.size() == 0;
	}

    public TreeNode(T input)
    {
    	this.data = input;
    	this.children = new ArrayList<TreeNode<T>>();
    }
    
    public TreeNode<T> addChild(T child)
    {
    	TreeNode<T> childNode = new TreeNode<T>(child);
    	childNode.parent = this;
    	this.children.add(childNode);
    	return childNode;
    }   
    
}