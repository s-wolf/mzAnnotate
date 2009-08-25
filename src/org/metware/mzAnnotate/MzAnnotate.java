package org.metware.mzAnnotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openscience.cdk.interfaces.IMolecularFormulaSet;

import de.ipbhalle.metfrag.massbankParser.*;



// TODO: Auto-generated Javadoc
/**
 * Wrapper for Massbank parser.
 */
public class MzAnnotate {
	
	private Vector<Spectrum> spectra;
	private Vector<Peak> peaks;
	private double exactMass;
	private int mode;
	private int collisionEnergy;
	private String InchI;
	private int CID;
	private String KEGG;
	private String nameTrivial;
	private String filename;
	private String formula;
	private HashMap<Double, List<Candidate>> assignedPeakToStructure = null;
	private String MassBankAccession = "";
	
	
	/**
	 * Reads in a MassBank flat file from a given location.
	 * 
	 * @param filename the filename
	 */
	public MzAnnotate(String filename){
		this.spectra = MassbankParser.Read(filename);
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.collisionEnergy = spectra.get(0).getCollisionEnergy();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		String[] fileTemp = filename.split("\\/");
		this.filename = fileTemp[fileTemp.length - 1];
		this.setFormula(spectra.get(0).getFormula());
		this.assignedPeakToStructure = new HashMap<Double, List<Candidate>>();
	}
	
	/**
	 * Reads in a MassBank flat file from a given location.
	 * 
	 * @param filename the filename
	 */
	public MzAnnotate(Spectrum spectrum, String MassBankAccession)
	{
		Vector<Spectrum> vec = new Vector<Spectrum>();
		vec.add(spectrum);
		this.spectra = vec;
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.collisionEnergy = spectra.get(0).getCollisionEnergy();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		this.MassBankAccession = MassBankAccession;
		this.filename = "";
		this.setFormula(spectra.get(0).getFormula());
		this.assignedPeakToStructure = new HashMap<Double, List<Candidate>>();
	}
	
	
	/**
	 * Creates a new Spectrum with a given peaklist...used for the web interface
	 * Ignores inchi, keggID, CID (-1), trivial name, collision energy (set to -1)
	 * 
	 * @param Peaks the peaks
	 * @param mode the mode
	 * @param exactMass the exact mass
	 */
	public MzAnnotate(String peakString, int mode, double exactMass){
		this.spectra = new Vector<Spectrum>();
		this.collisionEnergy = -1;
		spectra.add(new Spectrum(-1, parsePeaks(peakString), exactMass, mode, "none", -1, "none", "none",""));
		
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		this.setFormula(spectra.get(0).getFormula());
		this.assignedPeakToStructure = new HashMap<Double, List<Candidate>>();
	}
	
	
	/**
	 * Parses the peaks String from the web interface.
	 * 
	 * @param peakString the peak string
	 * 
	 * @return the vector<Peak>
	 */
	private Vector<Peak> parsePeaks(String peakString)
	{
		// Compile regular expression --> remove spaces in front of lines
        Pattern pattern = Pattern.compile("^[ \t]+");
        //replace all tabs with 1 space
        //Pattern patternTabs = Pattern.compile("[\t]+");
        
		Vector<Peak> parsedPeaks = new Vector<Peak>();
		String[] lines = peakString.split("\\\n");
		for (String line : lines) {
	        // Replace all occurrences of pattern in input
	        Matcher matcher = pattern.matcher(line);
	        String output = matcher.replaceAll("");
	        
	        output = output.replace("  ", " ");
	        
	        //Matcher matcherTabs = patternTabs.matcher(output);
	        //output = matcherTabs.replaceAll("");
			String[] array = output.split(" ");
			//no absolute intensity is given
			if(array.length == 2)
				parsedPeaks.add(new Peak(Double.parseDouble(array[0]), 0.0, Double.parseDouble(array[1]), collisionEnergy));
			//rel intensity is given
			else if(array.length == 3)
				parsedPeaks.add(new Peak(Double.parseDouble(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), collisionEnergy));
		}
		return parsedPeaks;
	}
	
	
	private void prepareAssignedPeaksToStructure()
	{
		for (int i = 0; i < peaks.size(); i++) {
			if(this.assignedPeakToStructure.containsKey(peaks.get(i).getMass()))
				continue;
			else
				this.assignedPeakToStructure.put(peaks.get(i).getMass(), new ArrayList<Candidate>());
		}
	}
	
	/**
	 * Gets the assigned fragments to the specified peak mass.
	 * 
	 * @param exactMass the exact mass
	 * 
	 * @return the assigned frags
	 */
	public List<Candidate> getAssignedFrags(double exactMass)
	{
		return this.assignedPeakToStructure.get(exactMass);
	}
	
	
	/**
	 * Assign peak to a structure.
	 * 
	 * @param peak the peak
	 * @param candidateStructure the candidate structure
	 */
	public void assignPeak(Peak peak, List<Candidate> candidateStructure)
	{
		this.assignedPeakToStructure.put(peak.getMass(), candidateStructure);
	}
	
	
	/**
	 * Set a new peakList.
	 * 
	 * @param peakList the peak list
	 * 
	 */
	public void setPeakList(Vector<Peak> peakList)
	{
		spectra.get(0).setPeaks(peakList);
		this.peaks = peakList;
	}
	
	
	/**
	 * Returns the filename.
	 * 
	 * @return the string
	 */
	public String getFilename()
	{
		return this.filename.split("\\.")[0];
	}
	
	
	/**
	 * Sets the filename. (Removes the file extension)
	 * 
	 * @param filename the new filename
	 */
	public void setFilename(String filename)
	{
		this.filename = filename.split("\\.")[0];
	}
	
	
	/**
	 * Sets the exact mass. Fix for bug in records.
	 * 
	 * @param Mass the new exact mass
	 */
	public void setExactMass(double Mass)
	{
		this.exactMass = Mass;
	}
	
	/**
	 * Gets the InchI.
	 * 
	 * @return the InchI
	 */
	public String getInchI()
	{
		return this.InchI;
	}
	
	/**
	 * Gets the KEGG ID.
	 * 
	 * @return the KEGG ID
	 */
	public String getKEGG()
	{
		return this.KEGG;
	}
	
	/**
	 * Gets the Pubchem CID.
	 * 
	 * @return the Pubchem CID
	 */
	public int getCID()
	{
		return this.CID;
	}
	
	/**
	 * Gets the collision energy.
	 * 
	 * @return the int
	 */
	public int getCollisionEnergy()
	{
		return this.collisionEnergy;
	}
	
	/**
	 * Identify peak. not used
	 * TODO: Use peak identifier from Jena.
	 * 
	 * @param mass the mass
	 * 
	 * @return the i molecular formula set
	 */
	public IMolecularFormulaSet IdentifyPeak(double mass){
		return null;
	}	
	
	
	/**
	 * Gets the peakList.
	 * 
	 * @return the peaks
	 */
	public Vector<Peak> getPeakList()
	{
		return this.peaks;
	}
	
	/**
	 * Gets the parent peak.
	 * 
	 * @return the parent peak
	 */
	public double getExactMass()
	{
		return this.exactMass;
	}
	
	/**
	 * Gets the mode. +1 or -1.
	 * 
	 * @return the mode
	 */
	public int getMode()
	{
		return this.mode;
	}
	
	/**
	 * Gets the trivial name.
	 * 
	 * @return the trivial name
	 */
	public String getTrivialName()
	{
		return this.nameTrivial;
	}


	/**
	 * Sets the formula.
	 * 
	 * @param formula the new formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}


	/**
	 * Gets the formula.
	 * 
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}


	/**
	 * Sets the assigned peak to structure.
	 * 
	 * @param assignedPeakToStructure the assigned peak to structure
	 */
	public void setAssignedPeakToStructure(HashMap<Double, List<Candidate>> assignedPeakToStructure) {
		this.assignedPeakToStructure = assignedPeakToStructure;
	}


	/**
	 * Gets the assigned peak to structure.
	 * 
	 * @return the assigned peak to structure
	 */
	public HashMap<Double, List<Candidate>> getAssignedPeakToStructure() {
		//prepare datastructure because it has to contain all peaks
		prepareAssignedPeaksToStructure();
		return assignedPeakToStructure;
	}


	/**
	 * Sets the mass bank accession.
	 * 
	 * @param massBankAccession the new mass bank accession
	 */
	public void setMassBankAccession(String massBankAccession) {
		MassBankAccession = massBankAccession;
	}


	/**
	 * Gets the mass bank accession.
	 * 
	 * @return the mass bank accession
	 */
	public String getMassBankAccession() {
		return MassBankAccession;
	}
	
}
