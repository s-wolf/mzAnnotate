package org.metware.mzAnnotate;

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

	/**
	 * Sets the exact mass.
	 * 
	 * @param exactMass the new exact mass
	 */
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

	/**
	 * Sets the molecular formula.
	 * 
	 * @param molecularFormula the new molecular formula
	 */
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

	/**
	 * Sets the molecule.
	 * 
	 * @param molecule the new molecule
	 */
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


	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
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
