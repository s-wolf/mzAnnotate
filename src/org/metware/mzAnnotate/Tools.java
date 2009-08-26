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
	
	Integer atomCount = 0;
	Integer bondCount = 0;
	
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
	 * Gets the numbered mol.
	 * 
	 * @param mol the mol
	 * @param start the start
	 * 
	 * @return the numbered mol
	 */
	public IAtomContainer getNumberedMol(IAtomContainer mol, int atomStart, int bondStart)
	{		
		bondCount = bondStart;
		atomCount = atomStart;
		
		for (IBond bond : mol.bonds()) {
			bondCount++;
			bond.setID("b" + bondCount.toString());
    		for (IAtom atom : bond.atoms()) {
    			atomCount++;
    			atom.setID("a" + atomCount.toString());
			}
		}
		return mol;
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
	 * Gets the last bond count.
	 * 
	 * @return the last bond count
	 */
	public int getLastBondCount()
	{
		return this.bondCount;
	}
	
	/**
	 * Gets the last atom count.
	 * 
	 * @return the last atom count
	 */
	public int getLastAtomCount()
	{
		return this.atomCount;
	}
}
