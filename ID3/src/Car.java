

public class Car 
{
	public enum Classification{unacc, acc, good, vgood}
	public enum Buying{vhigh, high, med, low}
	public enum Maint{vhigh, high, med, low}
	public enum Doors{Two, Four, Fivemore}
	public enum Persons{Two, Four, More}
	public enum Lug_Boot{small, med, big}
	public enum Safety{low, med, high}

	Classification classification;
	Buying buying;
	Maint maint;
	Doors doors;
	Persons persons;
	Lug_Boot lug_boot;
	Safety safety;
	
	public Car(String buy, String mai, String door, String pers, String lug, String saf,String classi )
	{
		if(classi.equals(Classification.acc.toString())) classification = Classification.acc;
		else if(classi.equals(Classification.good.toString())) classification = Classification.good;
		else if(classi.equals(Classification.vgood.toString())) classification = Classification.vgood;
		else classification = Classification.unacc;
		
		if(buy.equals(Buying.vhigh.toString())) buying = Buying.vhigh;
		else if(buy.equals(Buying.high.toString())) buying = Buying.high;
		else if(buy.equals(Buying.med.toString())) buying = Buying.med;
		else buying = Buying.low;
		
		if(mai.equals(Maint.vhigh.toString())) maint = Maint.vhigh;
		else if(mai.equals(Maint.high.toString())) maint = Maint.high;
		else if(mai.equals(Maint.med.toString())) maint = Maint.med;
		else maint = Maint.low;
		
		if(door.equals("4")) doors = Doors.Four;
		else if(door.equals("5more")) doors = Doors.Fivemore;
		else doors = Doors.Two;
		
		if(pers.equals("4")) persons = Persons.Four;
		else if(pers.equals("more")) persons = Persons.More;
		else persons = Persons.Two;
		
		if(lug.equals(Lug_Boot.med.toString())) lug_boot = Lug_Boot.med;
		if(lug.equals(Lug_Boot.big.toString())) lug_boot = Lug_Boot.big;
		else lug_boot = Lug_Boot.small;
		
		if(saf.equals(Safety.med.toString())) safety = Safety.med;
		else if(saf.equals(Safety.high.toString())) safety = Safety.high;
		else safety = Safety.low;
	}
	
	public String ToString()
	{
		return( "Class: " + classification.toString() + "; Doors: "+ doors.toString());
	}
	
}



