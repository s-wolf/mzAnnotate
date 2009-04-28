package org.metware.mzAnnotate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLProduct;
import org.xmlcml.cml.element.CMLProductList;
import org.xmlcml.cml.element.CMLReactant;
import org.xmlcml.cml.element.CMLReactantList;
import org.xmlcml.cml.element.CMLReaction;
import org.xmlcml.cml.element.CMLReactionList;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.Candidate;
import de.ipbhalle.metfrag.tools.WrapperSpectrum;

public class MzAnnotateWriter {
	
	/**
	 * Gets the mzAnnotate.
	 * 
	 * @param spectrum the spectrum
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	public CMLCml GetMzAnnotate(WrapperSpectrum spectrum, IAtomContainer originalMolecule)
	{
		//cml root element
		CMLCml rootCML = new CMLCml();
		
		CMLSpect test = new CMLSpect();
		// write spectrum first
		CMLList cml = test.getCmlSpect(spectrum);
		
		Convertor convertor = new Convertor(true, "cml");
		CMLMolecule originalMol = convertor.cdkAtomContainerToCMLMolecule(originalMolecule);
		CMLReactionList reactionList = new CMLReactionList();
		CMLReaction reaction = new CMLReaction();
		reactionList.addReaction(reaction);

		CMLReactantList reactantList = new CMLReactantList();
		reaction.addReactantList(reactantList);
		CMLReactant reactant = new CMLReactant();
		reactant.addMolecule(originalMol);
		reactantList.addReactant(reactant);
		
		
		
		HashMap<Double, List<Candidate>> assignedStructures = spectrum.getAssignedPeakToStructure();
		
		CMLProductList productList = new CMLProductList();
		reaction.addProductList(productList);
		
		for (List<Candidate> candidates : assignedStructures.values()) {
			
			if(candidates == null)
				continue;
			
			for (Candidate candidate : candidates) {
				CMLMolecule productMol = convertor.cdkAtomContainerToCMLMolecule(candidate.getMolecule());
				CMLProduct product = new CMLProduct();
				product.addMolecule(productMol);
				productList.addProduct(product);
			}
		}

		cml.appendChild(reactionList);

		// add to root element
		rootCML.appendChild(cml);
		
		return rootCML;

	}
	

	public static void main(String[] args) throws FileNotFoundException, CDKException {

		//get example data and create the data structure
		List<String> exampleStructures = new ArrayList<String>();
		exampleStructures.add("examples/naringenin/naringenin.mol");
		exampleStructures.add("examples/naringenin/147_1.mol");
		exampleStructures.add("examples/naringenin/147_2.mol");
		exampleStructures.add("examples/naringenin/153.mol");
		
		int atomCount = 0;
		int bondCount = 0;
		int molCount = 0;
		
		List<IAtomContainer> mols = new ArrayList<IAtomContainer>();
		for (String file : exampleStructures) {
			MDLReader reader;
			List<IAtomContainer> containersList;

			reader = new MDLReader(new FileReader(new File(file)));
			ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
			containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
			IAtomContainer mol = containersList.get(0);
			
			String id = "original";
			if(atomCount > 0)
				id = "p" + molCount;
			
			mol.setID(id);
			
			// now preprocess the atom and bond id's
			for (IBond bond : mol.bonds()) {
				bondCount++;
				bond.setID("b" + bondCount);
			}
			for (IAtom atom : mol.atoms()) {
				atomCount++;
				atom.setID("a" + atomCount);
			}
			mols.add(mol);
			molCount++;
		}
		
		//now assign each fragment a peak
		List<Candidate> candidateStructuresP1 = new ArrayList<Candidate>();
		IMolecularFormula molecularFormulaP1 = new MolecularFormula();
		molecularFormulaP1 = MolecularFormulaManipulator.getMolecularFormula(mols.get(1), molecularFormulaP1);
		double exactMass = MolecularFormulaManipulator.getNaturalExactMass(molecularFormulaP1);
		//peak 147_1: add to molecular formula another proton because of positive mode TODO!
		candidateStructuresP1.add(new Candidate("p1", exactMass, molecularFormulaP1, mols.get(1)));
		//peak 147_2 (has the same molecular formula)
		candidateStructuresP1.add(new Candidate("p2", exactMass, molecularFormulaP1, mols.get(2)));

		
		List<Candidate> candidateStructuresP2 = new ArrayList<Candidate>();
		IMolecularFormula molecularFormulaP2 = new MolecularFormula();
		molecularFormulaP2 = MolecularFormulaManipulator.getMolecularFormula(mols.get(3), molecularFormulaP2);
		exactMass = MolecularFormulaManipulator.getNaturalExactMass(molecularFormulaP2);
		//peak 153, add to molecular formula another proton because of positive mode TODO!
		candidateStructuresP2.add(new Candidate("p3", exactMass, molecularFormulaP2, mols.get(3)));
		
		
		List<Candidate> candidateStructuresOrig = new ArrayList<Candidate>();
		IMolecularFormula molecularFormulaOrig = new MolecularFormula();
		molecularFormulaOrig = MolecularFormulaManipulator.getMolecularFormula(mols.get(0), molecularFormulaOrig);
		exactMass = MolecularFormulaManipulator.getNaturalExactMass(molecularFormulaOrig);
		//original naringenin
		candidateStructuresOrig.add(new Candidate("p4", exactMass, molecularFormulaOrig, mols.get(0)));
		
		
		WrapperSpectrum spectrum = new WrapperSpectrum("examples/naringenin/PB000122.txt");
		Vector<Peak> peaks = spectrum.getPeakList();
		//peak: 147
		spectrum.assignPeak(peaks.get(0), candidateStructuresP1);
		//peak: 153
		spectrum.assignPeak(peaks.get(1), candidateStructuresP2);
		
		
		MzAnnotateWriter writerTest = new MzAnnotateWriter();
		CMLCml cml = writerTest.GetMzAnnotate(spectrum, mols.get(0));
		System.out.println(cml.toXML());
	}

}
