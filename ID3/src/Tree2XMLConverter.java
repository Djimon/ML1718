import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Tree2XMLConverter
{
		private String XML;
		private char I = '"';
		//private int[] level;
		private String lnEnd = "\n";
		private TreeNode<SubCar> tree;
		
		public Tree2XMLConverter(TreeNode<SubCar> tree2)
		{
			tree = tree2;
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
				XML += "    ";
			}
			XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+lnEnd;
			//this.level[depth]++;		
		}

		public void AddLineLeaf(int depth,String classes, String entropy, String attribute, String value, String Class)
		{
			//CheckOpenNodes(depth);
			for (int i = 0; i < depth; i++)
			{
				XML += "    ";
			}
			XML+= "<node> classes="+I+classes+I+ " entropy="+I+entropy+I+" "+attribute+"="+I+value+I+">"+Class+"</node>"+lnEnd;	
		}
		
		//nicht benötigt
		void CheckOpenNodes(int depth) 
		{
			for (int i = depth; i <= 0 ; i++)
			{
				//if (this.level[i] > 0)
				//	CloseNode();
			}
		}

		void CloseNode(int depth) 
		{
			for (int i = 0; i < depth; i++)
			{
				XML += "    ";
			}
			XML += "</node>"+lnEnd;
		}
		
		public void FinishTree()
		{
			XML += "</tree>";
		}
		
		public void write(TreeNode<SubCar> tree2, int _depth)
		{
			if(tree2.isPure())
			{
				AddLineLeaf(_depth, getclasses(tree2), Double.toString(tree2.entropy) , tree2.attribute.toString(), tree2.attributevalue, getClass(tree2));
			}
			else
			{
				for(TreeNode<SubCar> t : tree2.children)
				{
					AddLineNode(_depth, getclasses(t), Double.toString(t.entropy), t.attribute.toString(), t.attributevalue);
					write(t, _depth+1);
					CloseNode(_depth); 
				}
			}
			
		}
		public String getClass(TreeNode<SubCar> tree2)
		{
			return tree2.data.get(0).classification.toString();
		}
		
		public String getclasses(TreeNode<SubCar> tree2)
		{
			ArrayList<Car> unacc = new ArrayList<Car>();
			ArrayList<Car> acc = new ArrayList<Car>();
			ArrayList<Car> good = new ArrayList<Car>();
			ArrayList<Car> vgood = new ArrayList<Car>();
			
			for(int i = 0; i < 100 ; i++) //TODO: Größe de Baumes einfügen
			{
				switch(tree2.data.get(i).classification)
				{
					case unacc:
						unacc.add(tree2.data.get(i));
						break;
					case acc:
						acc.add(tree2.data.get(i));
						break;
					case good:
						good.add(tree2.data.get(i));
						break;
					case vgood:
						vgood.add(tree2.data.get(i));
						break;
					default:
						unacc.add(tree2.data.get(i));
						break;
				}
			}
			
			return "unacc:" + unacc.size() + ",acc:" + acc.size() + ",good:" + good.size() + ",vgood:" + vgood.size();
		}
		
	public void SaveToFile() throws IOException
	{
		FileWriter writer = new FileWriter("XMLTree_CD_KP.csv");
		writer.append(XML);	
		writer.close();
	}
}