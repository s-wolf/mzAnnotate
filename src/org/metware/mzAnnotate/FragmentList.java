package org.metware.mzAnnotate;

import java.util.HashMap;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class FragmentList {
	
	private HashMap<String, Fragment> fragMap;
	private int idCount = 0;
	private IAtomContainer measuredCompound;
	
	/**
	 * Instantiates a new fragment list.
	 */
	public FragmentList()
	{
		this.fragMap = new HashMap<String, Fragment>();
	}
	
	
	/**
	 * Adds the fragment and returns the id from it. With this id it is possible 
	 * to connect a peak and this fragment later on.
	 * 
	 * @param mol the mol
	 * 
	 * @return the string
	 */
	public String addFragment(IAtomContainer mol)
	{
		IMolecularFormula molFormula = MolecularFormulaManipulator.getMolecularFormula(mol);
		String id = "f" + idCount;
		Fragment frag = new Fragment(molFormula, mol, false);
		fragMap.put(id, frag);
		idCount++;
		return id;
	}
	
	/**
	 * Adds the fragment and returns the id from it. With this id it is possible 
	 * to connect a peak and this fragment later on.
	 * 
	 * @param mol the mol
	 * 
	 * @return the string
	 */
	public String addFragment(IMolecularFormula molFormula)
	{
		String id = "f" + idCount;
		Fragment frag = new Fragment(molFormula, false);
		fragMap.put(id, frag);
		idCount++;
		return id;
	}
	
	
	/**
	 * Adds the measured structure.
	 * 
	 * @param mol the mol
	 * 
	 * @return the string
	 */
	public String addStructure(IAtomContainer mol)
	{
		IMolecularFormula molFormula = MolecularFormulaManipulator.getMolecularFormula(mol);
		String id = "m" + idCount;
		Fragment frag = new Fragment(molFormula, mol, true);
		fragMap.put(id, frag);
		idCount++;
		return id;
	}
	
	/**
	 * Adds the measured structure but only the molecular formula. The structure is not known.
	 * 
	 * @param mol the mol
	 * 
	 * @return the string
	 */
	public String addStructure(IMolecularFormula formula)
	{
		String id = "m" + idCount;
		Fragment frag = new Fragment(formula, true);
		fragMap.put(id, frag);
		idCount++;
		return id;
	}
	

	/**
	 * Sets the frag map
	 * 
	 * @param fragList the new frag map
	 */
	public void setFragList(HashMap<String, Fragment> fragList) {
		this.fragMap = fragList;
	}

	/**
	 * Gets the frag map
	 * 
	 * @return the frag map
	 */
	public HashMap<String, Fragment> getFragList() {
		return fragMap;
	}

}
