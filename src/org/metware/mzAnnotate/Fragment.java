package org.metware.mzAnnotate;

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;;

public class Fragment {
	
	private double exactMass = 0.0;
	private IMolecularFormula molecularFormula = null;
	private IAtomContainer molecule = null;
	private boolean isMeasuredCompound = false;
	private boolean isStructureKnown = false;
	
	/**
	 * Instantiates a new candidate.
	 * 
	 * @param molecularFormula the molecular formula
	 * @param molecule the molecule
	 * @param isMeasuredCompound the is measured compound
	 */
	public Fragment(IMolecularFormula molecularFormula, IAtomContainer molecule, boolean isMeasuredCompound){
		setExactMass(Tools.getExactMass(molecularFormula));
		setMolecularFormula(molecularFormula);
		setMolecule(molecule);
		setMeasuredCompound(isMeasuredCompound);
		setStructureKnown(true);
	}
	
	/**
	 * Instantiates a new candidate only with sum formula(minimum requirement).
	 * 
	 * @param molecularFormula the molecular formula
	 * @param isMeasuredCompound the is measured compound
	 */
	public Fragment(IMolecularFormula molecularFormula, boolean isMeasuredCompound){
		setExactMass(Tools.getExactMass(molecularFormula));
		setMolecularFormula(molecularFormula);
		setMeasuredCompound(isMeasuredCompound);
		setStructureKnown(false);
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
	 * Sets the measured compound.
	 * 
	 * @param isMeasuredCompound the new measured compound
	 */
	public void setMeasuredCompound(boolean isMeasuredCompound) {
		this.isMeasuredCompound = isMeasuredCompound;
	}

	/**
	 * Checks if is measured compound.
	 * 
	 * @return true, if is measured compound
	 */
	public boolean isMeasuredCompound() {
		return isMeasuredCompound;
	}

	/**
	 * Sets the structure known.
	 * 
	 * @param isStructureKnown the new structure known
	 */
	public void setStructureKnown(boolean isStructureKnown) {
		this.isStructureKnown = isStructureKnown;
	}

	/**
	 * Checks if is structure known.
	 * 
	 * @return true, if is structure known
	 */
	public boolean isStructureKnown() {
		return isStructureKnown;
	}

}
