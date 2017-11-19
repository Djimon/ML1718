import java.util.ArrayList;

import sun.reflect.generics.tree.Tree;

import java.lang.Math;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Object;


@SuppressWarnings("restriction")
public class ID3 
{

	
	//TreeNode<String> test = new TreeNode<String>("root");
	
	
	private int TreeDepth = 100; //just for testing, to be filled with correct treeDepth
	//Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);

	public static void main(String[] args) throws IOException
	{
		System.out.println("Reading the data...");
		ArrayList<SubCar> CarZ = new ArrayList<SubCar>();
		SubCar[] Cars;
		Cars = read("Cardaten/Car.data");
		CarZ = read2("Cardaten/Car.data");
		/*
		Car[] temp = new Car[cars.size()];
		Cars.toArray(temp);
		for(int i = 0 ; i< temp.length;i++)
		{
			System.out.println(temp[i].ToString());
		}
		*/
		
		System.out.println("Building the tree...");
		TreeNode<SubCar> Tree = new TreeNode<SubCar>(Cars,CarZ);
		Tree.buildTree();

		System.out.println("Converting tree to XML");	
		Tree2XMLConverter printer = new Tree2XMLConverter(Tree);
		printer.AddLineTree("unacc, acc, good, vgood", Double.toString(getEntropy(CarZ)));
		printer.write(Tree, 0);
		printer.FinishTree();
		printer.SaveToFile();
		System.out.println("Saving XML to file...");
	}

	public static SubCar[] read(String filepath)
	{
		ArrayList<SubCar> Cardata = new ArrayList<SubCar>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(filepath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				String delims = "[,]";
				String[] tokens = line.split(delims);	    	
				Cardata.add(new SubCar(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]));
			}
		} catch (FileNotFoundException e) {
			System.out.println("no such file: "+filepath);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("somethng went wrong ( "+e+")");
			e.printStackTrace();
		}
		SubCar results[] = new SubCar[Cardata.size()];
		
		for(int i=0; i<Cardata.size();i++)
		{
			results[i] = Cardata.get(i);
		}
		return results;
	}

	public static ArrayList<SubCar> read2(String filepath)
	{
		ArrayList<SubCar> Cardata = new ArrayList<SubCar>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(filepath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				String delims = "[,]";
				String[] tokens = line.split(delims);	    	
				Cardata.add(new SubCar(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]));
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

	// calculate Entropy of a given data set
	public static double getEntropy(ArrayList<SubCar> cars)
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


	// to get logarithm of another base than e
	public static double getlogn(double p_unacc, int base)
	{
		return Math.log(p_unacc)/Math.log(base);
	}

//Tree2XMLConverter XMLConverter = new Tree2XMLConverter(TreeDepth);
}
