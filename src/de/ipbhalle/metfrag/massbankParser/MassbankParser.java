package de.ipbhalle.metfrag.massbankParser;

import java.io.*;
import java.util.Vector;
import java.util.TreeMap;


public class MassbankParser{
	
	public static Vector<Spectrum> Read(String filename) {
		ElementTable et = new ElementTable();
		et.add(new Element("C", 12.0, 4));
		et.add(new Element("H", 1.007825, 1));
		et.add(new Element("N", 14.003074, 3));
		et.add(new Element("O", 15.994915, 2));
		et.add(new Element("P", 30.973762, 3));
		et.add(new Element("S", 31.972071, 2));
		et.add(new Element("Cl", 35.4527, 1));
	
		int compoundsDone = 0;
		BufferedReader reader;
		ObjectOutputStream oostream;
		String formula = "";
		String line, name = "", instrument = "";
		String nameTrivial = "";
		int linkPubChem = 0;
		String linkKEGG = "none";
		String[] array;
		String IUPAC = "";
		int mode = 0, collisionEnergy;
		double mass = 0.0, focusedMass = 0.0;
		Vector<Peak> peaks;
		Vector<Spectrum> spectra = new Vector<Spectrum>();
		//Vector<Compound> compounds = new Vector<Compound>(); not used....
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		boolean errorFlag = true; // starts with true, so no compound is added in the first loop.
		
		for (int i= 1; i < 2; ++i){ //disabled loop ... read only 1 spectra
			try {
			//Skip first entries (Accession, Record_Title, Date, Authors, Copyright)
			//	reader = new BufferedReader(new FileReader("/home/basti/WorkspaceJava/TandemMSLookup/massbank_parser/test.TXT"));
			reader = new BufferedReader(new FileReader(filename));
		  	line = reader.readLine();
		  	while (line != null && !line.contains("CH$NAME")){
		  	  line = reader.readLine();
		  	}
		  	if (!line.substring(line.indexOf("CH$NAME")+9).equals(name)){
		  		
	  			spectra = new Vector<Spectrum>();
	  			errorFlag = false;
	  			
	  			//Use only the first name....skip synonymes
			  	name = line.substring(line.indexOf("CH$NAME")+9);
			  	line = reader.readLine();
			  	
			  	if(line.contains("CH$NAME"))
			  	{
				  	nameTrivial = line.substring(line.indexOf("CH$NAME")+9);
				  	while (line != null && !line.contains("CH$FORMULA")){
				  	  line = reader.readLine();
				  	}
			  	}
			  	else
			  	{
			  		nameTrivial = name;
			  	}
			  	
				formula = line.substring(line.indexOf("CH$FORMULA")+12);
			  	while (line != null && !line.contains("CH$EXACT_MASS")){
			  	  line = reader.readLine();
			  	}
			  	//exact mass
			  	mass = Double.parseDouble(line.substring(line.indexOf("CH$EXACT_MASS")+15));
			  	
			  	
			  	//skipped CH$SMILES - SMILES code
			  	//IUPAC INCHI
			  	while (line != null && !line.contains("CH$IUPAC")){
				  	  line = reader.readLine();
				}
				//Inchi
				IUPAC = line.substring(line.indexOf("CH$IUPAC")+10);
				 
			    //CH$LINK - ID of other database with link
			  	while (line != null && !line.contains("CH$LINK: PUBCHEM CID:")){
				  	  line = reader.readLine();
				}
				//pubchem id
				linkPubChem = Integer.parseInt(line.substring(line.indexOf("CH$LINK: PUBCHEM CID:")+21).split("\\ ")[0]);
			  	
				
				//CH$LINK - KEGG ID --> optional
				line = reader.readLine();
			  	if(line.startsWith("CH$LINK: KEGG"))
			  	{
					//kegg id
					linkKEGG = line.substring(line.indexOf("CH$LINK: KEGG")+14);
			  	}
			  	
			  	
			  	while (line != null && !line.contains("AC$INSTRUMENT")){
			  	  line = reader.readLine();
			  	}
			  	
			  	//Experimental condition
			  	//Equipment
				instrument = line.substring(line.indexOf("AC$INSTRUMENT")+15);
			  	while (line != null && !line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE")){
			  	  line = reader.readLine();
			  	}	
			  		// PRECURSOR_TYPE: POSITIVE (1) or NEGATIVE (-1)
					if (line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE")+40).contains("[M+H]+")) mode = 1;
					else mode = -1;
				}
		  	
		  		//skipped PRECURSER SELECTION, FRAGMENTATION_EQUIPMENT, SPECTRUM_TYPE.....
		  	while (line != null && !line.contains("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")){
		  	  line = reader.readLine();
		  	}
			try
			{
				collisionEnergy = Integer.parseInt(line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")+42, line.length()-3));
			}
			catch(NumberFormatException e)
			{
				//error in source file
				collisionEnergy = 0;
			}
		  	
		  	
			while (line != null && !line.contains("PK$PEAK")){
		  	  line = reader.readLine();
		  	}
				line = reader.readLine();
				peaks = new Vector<Peak>();
				while (line != null && !line.contains("//")){
					array = line.split(" ");
					// array[2] is mass, array[3] abs. intensity, array[4] rel. intensity.
					// spectra.size shows how many spectra had a lower energy than the spectrum this peak belongs to.
					peaks.add(new Peak(Double.parseDouble(array[2]), Double.parseDouble(array[3]), Double.parseDouble(array[4]), collisionEnergy));
					line = reader.readLine();
				}
				spectra.add(new Spectrum(collisionEnergy, peaks, mass, mode, IUPAC, linkPubChem, linkKEGG, nameTrivial, formula));
			
				
			}
			catch (IOException e) {
				System.out.println("File not found!!! Error: " +e.getMessage());
				errorFlag = true;
			}
		}
		return spectra;
	}
}
