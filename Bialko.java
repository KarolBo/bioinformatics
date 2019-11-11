import java.io.*; 
import java.util.*;
import java.util.regex.*;

class Bialko { 

private ArrayList<Integer> nr;
private ArrayList<String> residue;
private ArrayList<Integer> luki;
private String name;
private ArrayList<Integer> chainLength;
private ArrayList<String> molekula;
private ArrayList<String> chains;
private ArrayList<Integer> begin;
private ArrayList<Integer> end;
private ArrayList<Integer> beginPDB;
private ArrayList<Integer> nrPDB;
private ArrayList<Integer> dif;
private ArrayList<String> seqres;
private String UniProt = "";
	
	public void molecule(String path)throws Exception{
		molekula = new ArrayList<String>();
		chains = new ArrayList<String>();
		File file = new File(path); 
		file.setReadOnly();
		Scanner scan = new Scanner(file); 
		String linia="",mm="";
		
		while(scan.hasNextLine()){
			linia=scan.nextLine();
			if(linia.length()>6&&linia.substring(10,21).trim().equals("MOLECULE:")){
				mm=linia.substring(21,linia.length()).trim();
			}
			else if(linia.length()>6&&linia.substring(10,17).trim().equals("CHAIN:")){
				chains.add(linia.substring(18,linia.length()));
				molekula.add(mm+"   "+linia.substring(18,linia.length()-1));
			}			
		}
		scan.close();
	}
	
	private String single(String aa){
		switch(aa){
			case "ALA": return "A";
			case "ARG": return "R";
			case "ASN": return "N";
			case "ASP": return "D";
			case "CYS": return "C";
			case "GLU": return "E";
			case "GLN": return "Q";
			case "GLY": return "G";
			case "HIS": return "H";
			case "ILE": return "I";
			case "LEU": return "L";
			case "LYS": return "K";
			case "MET": return "M";
			case "PHE": return "F";
			case "PRO": return "P";
			case "SER": return "S";
			case "THR": return "T";
			case "TRP": return "W";
			case "TYR": return "Y";
			case "VAL": return "V";
			default: return aa;
		}
	}
	
	public static boolean pasuje(String input, String reg){ 
		Pattern wzorzec=Pattern.compile(reg);
		Matcher sekwencja=wzorzec.matcher(input);
		return sekwencja.find();
	}
	
	public void wczytaj(String name, int MolNr) throws Exception{
		File file = new File(name); 
		//file.setReadOnly();
		Scanner scan = new Scanner(file); 
		String linia="";
		int j=1,k=0,a=0,add=0,sNr=1;
		chainLength = new ArrayList<Integer>();
		nr = new ArrayList<Integer>();
		residue = new ArrayList<String>();
		luki = new ArrayList<Integer>();
		begin = new ArrayList<Integer>();
		end = new ArrayList<Integer>();
		beginPDB = new ArrayList<Integer>();
		char znak=0,chain=0;
		nrPDB = new ArrayList<Integer>();
		seqres = new ArrayList<String>();
		ArrayList<Integer> eTag = new ArrayList<Integer>();
		dif = new ArrayList<Integer>();
		
		while(scan.hasNextLine()){
		linia=scan.nextLine();
			if(linia.length()>5&&linia.substring(0,5).equals("DBREF")&&chains.get(MolNr).contains(linia.substring(12,13))){
				UniProt = linia.substring(32,40).trim();
				chainLength.add(numer(linia,20,25));
				begin.add(numer(linia,55,62));
				end.add(numer(linia,63,69));
				beginPDB.add(numer(linia,14,20));
				j=beginPDB.get(k);
			}
			else if(linia.length()>6&&linia.substring(0,6).equals("SEQADV")&&
			chains.get(MolNr).contains(linia.substring(11,12))){
				if(linia.substring(40,43).equals("   "))
					eTag.add(numer(linia,18,22));
				else
					dif.add(numer(linia,18,22)-beginPDB.get(k));
			}
			else if(linia.length()>6&&linia.substring(0,6).equals("SEQRES")&&chains.get(MolNr).contains(linia.substring(11,12))){	
				if(linia.charAt(11)!=chain) 
					sNr=1;
				chain=linia.charAt(11);
				for(int i=19;i<70;i+=4){
					if(!linia.substring(i,i+3).equals("   ")&&
					!eTag.contains(beginPDB.get(k)+sNr-(eTag.size()/begin.size())-1)){
							seqres.add(linia.substring(i,i+3));
					}
					sNr++;
				}
			}
			else if(linia.length()>4&&linia.substring(0,4).equals("ATOM")&&chains.get(MolNr).contains(linia.substring(21,22))){
				if(numer(linia,22,26)>j){
					for(int i=j;i<numer(linia,22,26)&&i<=chainLength.get(k);i++){
						if(!seqres.get(j-beginPDB.get(k)).equals(linia.substring(16,20).trim())){
							residue.add(single(seqres.get(j-beginPDB.get(k)))+" - brak 3D");
							nr.add(begin.get(k)+a++);
							nrPDB.add(j);
							luki.add(i);
							j++;
						}
					}
				}
				else if(linia.charAt(26)>=65&&linia.charAt(26)!=znak){
					znak=linia.charAt(26);
					residue.add(single(linia.substring(16,20).trim()));
					nr.add(begin.get(k)+a++);
					nrPDB.add(numer(linia,22,26));
					add++;
				}
				else if(numer(linia,22,26)==j&&linia.charAt(26)<65){
					residue.add(single(linia.substring(16,20).trim()));
					nr.add(begin.get(k)+a++);
					nrPDB.add(numer(linia,22,26));
					j++;
					znak=0;
				}
			}
			else if(k<chainLength.size()&&j<chainLength.get(k)&&linia.length()>6&&linia.substring(0,6).equals("HETATM")
			&&numer(linia,22,26)==j&&chains.get(MolNr).contains(linia.substring(21,22))){
				residue.add("HETATM");
				nr.add(begin.get(k)+a++);
				j++;
			}
			else if(linia.substring(0,3).equals("TER")&&chains.get(MolNr).contains(linia.substring(21,22))){
				for(int i=j;i<=chainLength.get(k);i++){
					residue.add(single(seqres.get(i-beginPDB.get(k)+add))+" - brak 3D");
					nr.add(begin.get(k)+a++);
					luki.add(i);
					j++;
				}
				k++;
				if(beginPDB.size()>k) j=beginPDB.get(k);
				a=0;
			}
		}
		scan.close();
	}
	
	private static int numer(String lin, int pocz, int kon){
		String liczba=lin.substring(pocz,kon).trim();
		int ret=0;
		try{
			ret=Integer.parseInt(liczba);
		}
		catch(NumberFormatException e){
			ret=0;
		}
		return ret;
	}
	
	public static boolean sprawdz(String name){
		File plik = new File(name);
		return plik.exists();
	}
	
	public ArrayList<String> getResidue(){
		return residue;
	}
	
	public ArrayList<Integer> getNr(){
		return nr;
	}
	
	public ArrayList<Integer> getLuki(){
		return luki;
	}
	
	public ArrayList<Integer> getDif(){
		return dif;
	}
	
	public ArrayList<String> getMol(){
		return molekula;
	}
	
	public String getUP(){
		return UniProt;
	}
	
	public int getBegin(int a){
		return begin.get(a);
	}
	
	public int getEnd(int a){
		return end.get(a);
	}
	
	public String getSeqres(){
		String seq="";
		for(int i=0;i<seqres.size();i++)
			seq+=single(seqres.get(i));
		return seq;
	}
	
	public static void main(String args[]) throws Exception{
		Bialko dupa = new Bialko(); 
		dupa.molecule("3LCT.PDB");
		dupa.wczytaj("3LCT.PDB",0);
		System.out.println(dupa.dif);
		/*PrintWriter pw = new PrintWriter("wynikBialko.txt");
		for(int i=0; i<dupa.getResidue().size(); i++){
			pw.print(dupa.getNr().get(i));
			pw.print("   ");
			try{
			pw.print(dupa.nrPDB.get(i)+"   ");
			} catch(Exception e){}
			pw.print(dupa.getResidue().get(i));
			pw.println("  "+dupa.single(dupa.seqres.get(i)));
		}
		pw.print(dupa.getLuki());
		pw.close();*/
	}
}