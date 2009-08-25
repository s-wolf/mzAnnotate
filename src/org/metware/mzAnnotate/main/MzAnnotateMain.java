package org.metware.mzAnnotate.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.metware.mzAnnotate.Candidate;
import org.metware.mzAnnotate.MzAnnotate;
import org.metware.mzAnnotate.MzAnnotateWriter;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.element.CMLCml;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class MzAnnotateMain {

	/**
	 * @param args
	 */
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
		//peak 147_1: add to molecular formula another proton because of positive mode
		candidateStructuresP1.add(new Candidate("p1", exactMass, molecularFormulaP1, mols.get(1)));
		//peak 147_2 (has the same molecular formula)
		candidateStructuresP1.add(new Candidate("p2", exactMass, molecularFormulaP1, mols.get(2)));

		
		List<Candidate> candidateStructuresP2 = new ArrayList<Candidate>();
		IMolecularFormula molecularFormulaP2 = new MolecularFormula();
		molecularFormulaP2 = MolecularFormulaManipulator.getMolecularFormula(mols.get(3), molecularFormulaP2);
		exactMass = MolecularFormulaManipulator.getNaturalExactMass(molecularFormulaP2);
		//peak 153, add to molecular formula another proton because of positive mode
		candidateStructuresP2.add(new Candidate("p3", exactMass, molecularFormulaP2, mols.get(3)));
		
		
		MzAnnotate mzAnnot = new MzAnnotate("examples/naringenin/PB000122.txt");
		Vector<Peak> peaks = mzAnnot.getPeakList();
		//peak: 147
		mzAnnot.assignPeak(peaks.get(0), candidateStructuresP1);
		//peak: 153
		mzAnnot.assignPeak(peaks.get(1), candidateStructuresP2);
		
		
		MzAnnotateWriter writerTest = new MzAnnotateWriter();
		CMLCml cml = writerTest.GetMzAnnotate(mzAnnot, mols.get(0));
		System.out.println(cml.toXML());
	}
}


