import java.io.*; 
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Connect{

Bialko pdb;
GB gb;
int exNr;

	public Connect(String PDBpath, String GBpath,String savePath){
		int MolNr=0;
		try{
			pdb = new Bialko();
			pdb.molecule(PDBpath);
			if(pdb.getMol().size()>1)
				MolNr=wybor(pdb.getMol());
			pdb.wczytaj(PDBpath,MolNr);
		}
		catch(Exception e){
			Gui.wyswietl("blad odczytu pliku PDB");
		}
		try{
			gb = new GB(GBpath);
			exon();
			save(savePath);
		}
		catch(Exception e){
			Gui.wyswietl("blad odczytu pliku GB");
		}
	}
	
	
	public Connect(String PDBpath, String savePath,boolean del){
		int MolNr=0;
		ArrayList<String> GBfiles = new ArrayList<String>();
		boolean p=true;
		Gui.wyswietl("trwa pobieranie pliku "+PDBpath);
		try{
			Downloading.go(PDBpath);
			if(Bialko.sprawdz(PDBpath+".pdb"))
				Gui.wyswietl("plik zostal poprawnie pobrany");
			PDBpath+=".pdb";
			pdb = new Bialko();
			pdb.molecule(PDBpath);
			if(pdb.getMol().size()>1)
				MolNr=wybor(pdb.getMol());
			pdb.wczytaj(PDBpath,MolNr);
			}
		catch(Exception e){
			Gui.wyswietl("blad pobierania pliku PDB");
		}
		Gui.wyswietl("trwa pobieranie plkiow GenBank");
		try{
			GBfiles=Downloading.UP(pdb.getUP());
			if(GBfiles.size()>0)
				Gui.wyswietl("pliki zostaly poprawnie pobrane");
			for(int j=0;j<GBfiles.size();j++){
				gb=new GB(GBfiles.get(j)+".gb");
				p=spr(gb.getTranslation(), pdb.getSeqres(), pdb.getBegin(MolNr)-1, pdb.getEnd(MolNr));
				if(p){
					exon();
					save(savePath);
					break;
				}
			}
		}
		catch(Exception e){
			Gui.wyswietl("blad pobierania pliku GenBank");
		}
		if(del){
			new File(PDBpath.toLowerCase()).delete();
			for(int i=0;i<GBfiles.size();i++){
				new File(GBfiles.get(i)+".gb").delete();
			}
		}
	}
	
	private boolean spr(String gb, String pdb, int begin, int end){
		if(gb.length()<pdb.length()) return false;
		int tak=0;
		for(int i=begin;i<end;i++){
			if(gb.charAt(i)==pdb.charAt(i-begin)) tak++;
		}
		double div=((double)tak/(end-begin));
		if(div>=0.9) return true;
		else return false;
	}
	
	private int wybor(ArrayList<String> mol){
		String[] tab = new String[mol.size()];
		tab = mol.toArray(tab);
		Object selection = JOptionPane.showInputDialog(null, "Plik PDB zawiera wiecej niz jedna makromolekule:",
        "Wybierz jedno", JOptionPane.QUESTION_MESSAGE, null, tab, tab[0]);
		return mol.indexOf(selection);
	}
	
	private void exon(){
		exNr=0;
		while(exNr<gb.getExonyP().size()&&gb.getCds1()>gb.getExonyP().get(exNr))
			exNr++;
	}
	
	private void save(String path){
		try{
			PrintWriter pw = new PrintWriter(path+".txt");
			String cds = gb.getSequence().substring(gb.getCds1()-1,gb.getCds2());
			int nrSeq=gb.getCds1();
			int k=0;
			for(int i=0; i<gb.getTranslation().length(); i++){
				if(exNr<gb.getExonyP().size()&&nrSeq>=gb.getExonyP().get(exNr))
					exNr++;
				
				if(cds.length()>=3*i+3){
					pw.print(cds.substring(3*i,3*i+3)+"    "+exNr);
					nrSeq+=3;
					for(int j=0;j<4-Math.log10((double)exNr);j++)
						pw.print(" ");
				}
				else pw.print("    ");
				if(gb.getTranslation().length()>=i)
					pw.print(gb.getTranslation().charAt(i));
					
				if(pdb.getNr().size()>k&&pdb.getNr().get(k)==i+1){ 
					pw.print("    "+pdb.getNr().get(k));                       
					for(int j=0;j<6-Math.log10((double)pdb.getNr().get(k));j++)
						pw.print(" ");
						pw.print(pdb.getResidue().get(k++));
				}
					pw.println();
			}
			pw.print(pdb.getLuki());
			pw.close();	
		}
		catch(Exception e){
			Gui.wyswietl("Wystapil blad zapisu");
		}
	}
	public static void main(String args[])throws Exception{
		Connect dupa = new Connect("3uec","wynik",true);
	}
} 