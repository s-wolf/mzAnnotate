package org.metware.mzAnnotate;

import java.util.HashMap;
import java.util.Vector;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class Tools {
	
	/**
	 * Gets the exact mass.
	 * 
	 * @param molecularFormula the molecular formula
	 * 
	 * @return the exact mass
	 */
	public static double getExactMass(IMolecularFormula molecularFormula)
	{
		return MolecularFormulaManipulator.getNaturalExactMass(molecularFormula);
	}
	
	/**
	 * Gets the measured molecule. This is the compound which was measured!
	 * 
	 * @param fragList the frag list
	 * 
	 * @return the measured molecule
	 */
	public static Fragment getMeasuredCompound(HashMap<String, Fragment> fragList)
	{
		for (Fragment frag : fragList.values()) {
			if(frag.isMeasuredCompound())
				return frag;
		}
		
		return null;
	}
	
	
	/**
	 * Gets the peak.
	 * 
	 * @param specData the spec data
	 * @param mz the mz
	 * 
	 * @return the peak
	 * 
	 * @throws Exception the exception
	 */
	public static Peak getPeak(SpectrumData specData, double mz) throws Exception
	{
		Vector<Peak> peakList = new Vector<Peak>();
		peakList = specData.getPeakList();
		for (Peak peak : peakList) {
			if(peak.getMass() == mz)
				return peak;
		}
		
		throw new Exception("No corresponding Peak Object found!");
	}
	
	
	/**
	 * Checks if the compound structure from the measured molecule is given
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return true, if is structure given
	 */
	public static boolean isStructureGiven(MzAnnotate mzAnno)
	{
		boolean isGiven = false;
		if(mzAnno.getFragMap() == null)
			return false;
		else if(mzAnno.getFragMap() != null && mzAnno.getFragMap().isStructureGiven())
			return true;
		
		return isGiven;
	}
	
	/**
	 * Checks if a molecule list (fragments) is available.
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return true, if is molecule list given
	 */
	public static boolean isMoleculeListGiven(MzAnnotate mzAnno)
	{
		boolean isGiven = false;
		if(mzAnno.getFragMap() == null)
			return false;
		else if(mzAnno.getFragMap() != null && mzAnno.getFragMap().isFragGiven())
			return true;
		
		return isGiven;
	}
	
}
