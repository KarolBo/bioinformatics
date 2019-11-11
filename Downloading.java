import java.io.*;
import java.net.*;
import java.util.*;

 public class Downloading {
	public static void go(String name) throws Exception{
         URL url; //represents the location of the file we want to dl.
         URLConnection con; // represents a connection to the url we want to dl.
         DataInputStream dis; // input stream that will read data from the file.
         FileOutputStream fos; //used to write data from inut stream to file.
		 ArrayList<Byte> fileData;
		 String end="";
         if(name.length()<5){
			url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/msd/pdb_uncompressed/pdb"+name+".ent");
			end=".pdb";
		}
		else{
			url = new URL("http://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?tool=portal&sendto=on&log$=seqview&db=nuccore&dopt=genbank&sort=&val="+name+"&extrafeat=976&maxplex=1");
			end=".gb";
			}
		con = url.openConnection(); // open the url connection.
        dis = new DataInputStream(con.getInputStream()); // get a data stream from the url connection.
		fileData = new ArrayList<Byte>();
		while(true){
			try{
				fileData.add(dis.readByte());
			}
			catch(EOFException e){
				break;
			}
		}
        dis.close(); // close the data input stream
        fos = new FileOutputStream(new File(name+end)); //create an object representing the file we want to save
        for(int i=0;i<fileData.size();i++)
			fos.write(fileData.get(i)); // write out the file we want to save.
		fos.close(); // close the output stream writer
	}
	
	public static ArrayList<String> UP(String up) throws Exception {
	
		ArrayList<String> GBfiles = new ArrayList<String>();
		int a=0;
        URL oracle = new URL("http://www.uniprot.org/uniprot/"+up+".txt"); //Q16539
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            if(inputLine.contains("RefSeq")){
			GBfiles.add(inputLine.substring(inputLine.indexOf('M')-1,inputLine.length()-1));
				Downloading.go(GBfiles.get(a++));
			}
        in.close();
		return GBfiles;
    }
	
	public static void main(String args[])throws Exception{
		go("P00742");
	}
 } 