package de.ipbhalle.metfrag.tools;

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;;

public class Candidate {
	
	private double exactMass = 0.0;
	private IMolecularFormula molecularFormula = null;
	private IAtomContainer molecule = null;
	private String id = "";
	
	/**
	 * Instantiates a new candidate.
	 * 
	 * @param exactMass the exact mass
	 * @param molecularFormula the molecular formula
	 * @param molecule the molecule
	 * @param id the id
	 */
	public Candidate(String id, double exactMass, IMolecularFormula molecularFormula, IAtomContainer molecule){
		setExactMass(exactMass);
		setMolecularFormula(molecularFormula);
		setMolecule(molecule);
		setId(id);
	}

	private void setExactMass(double exactMass) {
		this.exactMass = exactMass;
	}

	/**
	 * Gets the exact mass.
	 * 
	 * @return the exact mass
	 */
	public double getExactMass() {
		return exactMass;
	}

	private void setMolecularFormula(IMolecularFormula molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	/**
	 * Gets the molecular formula.
	 * 
	 * @return the molecular formula
	 */
	public IMolecularFormula getMolecularFormula() {
		return molecularFormula;
	}

	private void setMolecule(IAtomContainer molecule) {
		this.molecule = molecule;
	}

	/**
	 * Gets the molecule.
	 * 
	 * @return the molecule
	 */
	public IAtomContainer getMolecule() {
		return molecule;
	}


	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
