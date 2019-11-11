import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FileDialog;
import java.util.*;

public class Gui extends JFrame implements ActionListener{

	private String pdb=""; 
	private String gb="";
	private String path="";
	private String newFile="";
	
    JButton wczytajPDB;
    JButton wczytajGenBank;
    JButton pobierz;
    JTextField GB_path;
    JTextField PDB_path;
    JTextField downloadPDB;
    JCheckBox checkbox_1;
    static JTextArea textarea_1;
    JScrollPane sp_textarea_1;
    JLabel label_1;
    JLabel label_2;
    JLabel label_3;
    JButton save;
    JButton start;
    JLabel label_5;
    JTextField save_path;
    JLabel label_4;
    JLabel label_6;
    JLabel label_7;

    public Gui() {
        guiLayout customLayout = new guiLayout();

        getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 12));
        getContentPane().setLayout(customLayout);

        wczytajPDB = new JButton("browse...");
        getContentPane().add(wczytajPDB);
		wczytajPDB.addActionListener(this);

        wczytajGenBank = new JButton("browse...");
        getContentPane().add(wczytajGenBank);
		wczytajGenBank.addActionListener(this);

        pobierz = new JButton("go");
        getContentPane().add(pobierz);
		pobierz.addActionListener(this);

        PDB_path = new JTextField("");
        getContentPane().add(PDB_path);

        GB_path = new JTextField("");
        getContentPane().add(GB_path);

        downloadPDB = new JTextField("");
        getContentPane().add(downloadPDB);

        checkbox_1 = new JCheckBox("remove downloaded files");
        getContentPane().add(checkbox_1);

        textarea_1 = new JTextArea("");
        sp_textarea_1 = new JScrollPane(textarea_1);
        getContentPane().add(sp_textarea_1);

        label_1 = new JLabel("load PDB file");
        getContentPane().add(label_1);

        label_2 = new JLabel("enter PDB record to download and execute");
        getContentPane().add(label_2);

        label_3 = new JLabel("load GenBank file");
        getContentPane().add(label_3);

        save = new JButton("browse...");
        getContentPane().add(save);
		save.addActionListener(this);

        start = new JButton(new ImageIcon("logo.jpg"));
        getContentPane().add(start);
		start.addActionListener(this);

        label_5 = new JLabel("Select where to save");
        getContentPane().add(label_5);

        save_path = new JTextField("");
        getContentPane().add(save_path);

        label_4 = new JLabel("After you click the logo below");
        getContentPane().add(label_4);

        label_6 = new JLabel("output file will be generated");
        getContentPane().add(label_6);

        label_7 = new JLabel("  at the end");
        getContentPane().add(label_7);

        setSize(getPreferredSize());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == wczytajPDB){
			try{
				loadPDB();
			}
			catch (Exception ex){
				wyswietl("blad odczytu PDB"+ex);
			}
		}
		else if(source == wczytajGenBank){
			try{
				loadGB();
			}
			catch (Exception ex){
				wyswietl("blad odczytu GB");
			}
		}
		else if(source == start){
			if(pdb.length()>0&&path.length()>0&&newFile.length()>0&&gb.length()>0)
			try{
				Connect con = new Connect(pdb,gb,path+newFile);
				if(Bialko.sprawdz(path+newFile+".txt")) 
					wyswietl("zapis zakonczony sukcesem");
				else 
					wyswietl("zapis NIE zakonczony sukcesem");
			}
			catch (Exception ex){
			wyswietl("blad CON"+ex);
			}
			else 
				wyswietl("nie wczytales pliku lub nie wybrales lokalizacji do zapisu");
		}
		else if(source == save)
			save();
		else if(source == pobierz){
			if(path.length()>0&&newFile.length()>0){
				pdb=downloadPDB.getText().toLowerCase();
				try{
					Connect conD = new Connect(pdb,path+newFile,checkbox_1.isSelected());
				}
				catch(Exception n){
					wyswietl("Blad pobierania pliku GenBank: "+n);
				}
			}
			else
				wyswietl("nie wybrales lokalizacji do zapisu");
			if(Bialko.sprawdz(path+newFile+".txt"))
				wyswietl("\nplik \""+newFile+"\" zostal poprawnie zapisany");
			else	
				wyswietl("plik NIE zostal poprawnie zapisany");
		}
	}

	private void loadPDB() throws Exception { 
			FileDialog load =new FileDialog(this,"Wczytaj",FileDialog.LOAD);
			load.setVisible(true);
			pdb=load.getDirectory()+load.getFile();
			PDB_path.setText(pdb);
		}
		
	private void loadGB() throws Exception { 
			FileDialog load =new FileDialog(this,"Wczytaj",FileDialog.LOAD);
			load.setVisible(true);
			gb=load.getDirectory()+load.getFile();
			GB_path.setText(gb);
		}
	
	private void save(){
			FileDialog save = new FileDialog(this,"Zapisz",FileDialog.SAVE);
			save.setVisible(true);
			path=save.getDirectory();
			newFile=save.getFile();
			save_path.setText(path+newFile);
		}
		
		public static void wyswietl(String napis){
			textarea_1.append(napis+"\n");
		}
	
    public static void main(String args[]) {
        Gui window = new Gui();

        window.setTitle("Witam, witam!!!");
        window.pack();
        window.show();
    }
}

class guiLayout implements LayoutManager {

    public guiLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 663 + insets.left + insets.right;
        dim.height = 461 + insets.top + insets.bottom;

        return dim;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+40,96,32);}
        c = parent.getComponent(1);
        if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+112,96,32);}
        c = parent.getComponent(2);
        if (c.isVisible()) {c.setBounds(insets.left+472,insets.top+56,96,32);}
        c = parent.getComponent(3);
        if (c.isVisible()) {c.setBounds(insets.left+112,insets.top+40,336,32);}
        c = parent.getComponent(4);
        if (c.isVisible()) {c.setBounds(insets.left+112,insets.top+112,336,32);}
        c = parent.getComponent(5);
        if (c.isVisible()) {c.setBounds(insets.left+576,insets.top+56,72,32);}
        c = parent.getComponent(6);
        if (c.isVisible()) {c.setBounds(insets.left+472,insets.top+96,176,24);}
        c = parent.getComponent(7);
        if (c.isVisible()) {c.setBounds(insets.left+16,insets.top+232,416,208);}
        c = parent.getComponent(8);
        if (c.isVisible()) {c.setBounds(insets.left+40,insets.top+8,240,24);}
        c = parent.getComponent(9);
        if (c.isVisible()) {c.setBounds(insets.left+488,insets.top+8,160,24);}
        c = parent.getComponent(10);
        if (c.isVisible()) {c.setBounds(insets.left+40,insets.top+80,240,24);}
        c = parent.getComponent(11);
        if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+184,96,32);}
        c = parent.getComponent(12);
        if (c.isVisible()) {c.setBounds(insets.left+464,insets.top+224,176,216);}
        c = parent.getComponent(13);
        if (c.isVisible()) {c.setBounds(insets.left+40,insets.top+152,240,24);}
        c = parent.getComponent(14);
        if (c.isVisible()) {c.setBounds(insets.left+112,insets.top+184,336,32);}
        c = parent.getComponent(15);
        if (c.isVisible()) {c.setBounds(insets.left+472,insets.top+168,176,24);}
        c = parent.getComponent(16);
        if (c.isVisible()) {c.setBounds(insets.left+472,insets.top+184,176,24);}
        c = parent.getComponent(17);
        if (c.isVisible()) {c.setBounds(insets.left+488,insets.top+112,152,24);}
    }
}
