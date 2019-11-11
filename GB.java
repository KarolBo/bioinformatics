import java.io.*;
import java.util.*;

public class GB{
	
	private ArrayList<Integer> exonyP;
	private ArrayList<Integer> exonyK;
	private String sequence;
	private int cds1, cds2;
	private String translation;
	
	public GB(String path)throws Exception{
		exonyP = new ArrayList<Integer>();
		exonyK = new ArrayList<Integer>();
		String linia="";
		File plik = new File(path);
		//plik.setReadOnly();
		Scanner scan = new Scanner(plik);
		while(scan.hasNextLine()){
			linia=scan.nextLine();
			if(linia.length()>8&&linia.substring(5,8).equals("CDS")){
				cds1=Integer.parseInt(linia.substring(21,linia.indexOf(".")));
				cds2=Integer.parseInt(linia.substring(linia.indexOf(".")+2,linia.length()));
			}
			else if(linia.length()>9&&linia.substring(5,9).equals("exon")){
				exonyP.add(Integer.parseInt(linia.substring(21,linia.indexOf("."))));
				exonyK.add(Integer.parseInt(linia.substring(linia.indexOf(".")+2,linia.length())));
			}
			else if(linia.trim().equals("ORIGIN")){
				sequence="";
				linia=scan.nextLine();
				while(!linia.trim().equals("//")){
				sequence+=linia.substring(10,linia.length()).replace(" ","");
				linia=scan.nextLine();
				}
			}
			else if(linia.length()>33&&linia.substring(21,33).equals("/translation")){
				translation="";
				translation+=linia.substring(linia.indexOf("\""),linia.length()).replace("\"","").trim();
				do{
					linia=scan.nextLine();
					translation+=linia.replace("\"","").trim();
				}while(!linia.endsWith("\""));
			}
		}
		scan.close();
	}
	
	public ArrayList<Integer> getExonyP(){
		return exonyP;
	}
	
	public ArrayList<Integer> getExonyK(){
		return exonyK;
	}
	
	public String getSequence(){
		return sequence;
	}

	public int getCds1(){
		return cds1;
	}
	
	public String getTranslation(){
		return translation;
	}
	
	public int getCds2(){
		return cds2;
	}
	public static void main(String[] args)throws Exception{
		GB dupa = new GB("sequence.gb");
		System.out.println(dupa.getTranslation());
	}	
	
}