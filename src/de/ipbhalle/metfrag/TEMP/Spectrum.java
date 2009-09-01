package de.ipbhalle.metfrag.TEMP;

import java.util.Comparator;
import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;


public class Spectrum  implements java.io.Serializable, Comparable<Spectrum> {

	private int collisionEnergy;
	private Vector<PeakMzAnno> peaks;
	private double tic;
	private double exactMass;
	private int mode;
	private String InchI;
	private int CID;
	private String KEGG;
	private String nameTrivial;
	private String formula;
	
	public Spectrum(int collisionEnergy, double tic, Vector<PeakMzAnno> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String nameTrivial, String formula){
		this.collisionEnergy = collisionEnergy;
		this.tic = tic;
		this.peaks = peaks;
		this.exactMass = exactMass;
		this.mode = mode;
		this.InchI = InchI;
		this.CID = CID;
		this.KEGG = KEGG;
		this.nameTrivial = nameTrivial;
		this.setFormula(formula);
	}
	
	/**
	 * Instantiates a new spectrum with minimal information.
	 * 
	 * @param collisionEnergy the collision energy
	 * @param peaks the peaks
	 * @param exactMass the exact mass
	 * @param mode the mode
	 */
	public Spectrum(int collisionEnergy, Vector<PeakMzAnno> peaks, double exactMass, int mode){
		this.collisionEnergy = collisionEnergy;
		this.tic = 0.0;
		this.peaks = peaks;
		this.exactMass = exactMass;
		this.mode = mode;
		this.InchI = "";
		this.CID = 0;
		this.KEGG = "";
		this.nameTrivial = "";
		this.setFormula("");
	}
	
	public Spectrum(int collisionEnergy, Vector<PeakMzAnno> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String nameTrivial, String formula){
		this(collisionEnergy, 0.0, peaks, exactMass, mode, InchI, CID, KEGG, nameTrivial, formula);
	}	
	
	public int getCollisionEnergy(){
		return collisionEnergy;
	}
	
	public double getTic(){
		return tic;
	}
	
	public int getMode(){
		return mode;
	}
	
	public double getExactMass(){
		return exactMass;
	}

	public Vector<PeakMzAnno> getPeaks(){
	 return peaks;
	}
	
	public String getInchi(){
		return InchI;
	}
	
	public int getCID(){
		return CID;
	}
	
	public String getTrivialName(){
		return nameTrivial;
	}
	
	public String getKEGG(){
		//fix space after C-Number
		String temp = KEGG.split("\\ ")[0];
		return temp;
	}
	
	public void setCollisionEnergy(int collisionEnergy){
		this.collisionEnergy = collisionEnergy;
	}
	
	public void setTic(double tic){
		this.tic = tic;
	}
	
	public void setPeaks(Vector<PeakMzAnno> peaks){
		this.peaks = peaks;
	}
	
	public int compareTo(Spectrum another){
		return (collisionEnergy - another.collisionEnergy);
	}
	
	/* TODO: Fix method ... IntensityComparator??? */
	public void calculateSNR(){
		//Collections.sort(peaks, new IntensityComparator());
		Collections.sort(peaks);
		double number = peaks.size(), i = 1.0;
		double prob = 1.0;
		
		for (PeakMzAnno peak : peaks){
			//prob = lambda*Math.exp(-1*lambda*i/number);
			peak.setRelIntensity(i/number);
			++i;
		}
	}
	
	public void strictFilter(double threshold){
		for (Iterator<PeakMzAnno> pIter = peaks.iterator(); pIter.hasNext();){
			if (pIter.next().getRelIntensityDB() < threshold){
				pIter.remove();
			}
		}
	}
	
	public void calculateRaw(){
		if (tic != 0.0) {
			double sum = 0.0;
			for (PeakMzAnno peak : peaks){
				sum += peak.getIntensity();
			}		
			for (PeakMzAnno peak : peaks){
				peak.setRelIntensity(tic*peak.getIntensity()/sum);
			}
		}
	}
	
	public void calcRelIntensities(){
		double totalIntensity = 0.0;
		for (int i=0; i<peaks.size(); ++i){
			totalIntensity += peaks.get(i).getIntensity();
		}
		for (int i=0; i<peaks.size(); ++i){
			peaks.get(i).setRelIntensity(peaks.get(i).getIntensity()*100.0/totalIntensity);
		}
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormula() {
		return formula;
	}
}
