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
import org.xmlcml.cml.base.CMLAttribute;
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

public class MzAnnotateWriter {
	
	/**
	 * Gets the mzAnnotate.
	 * 
	 * @param spectrum the spectrum
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	public CMLCml GetMzAnnotate(MzAnnotate spectrum, IAtomContainer originalMolecule)
	{
		//get header
		CMLCml rootCML = getHeader();
		
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
	
	/**
	 * Gets the mzAnnotate for an MassBank entry. If IAtomContainer is null 
	 * no molecule is supllied and also not written!
	 * 
	 * @param spectrum the spectrum
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	public CMLCml GetMzAnnotateMassBank(MzAnnotate spectrum, IAtomContainer originalMolecule)
	{
		//cml root element
		CMLCml rootCML = getHeader();
		
		CMLSpect spect = new CMLSpect();
		// write spectrum first
		CMLList cml = spect.getCmlSpect(spectrum);
		
		if(originalMolecule != null)
		{
			Convertor convertor = new Convertor(true, "cml");
			CMLMolecule originalMol = convertor.cdkAtomContainerToCMLMolecule(originalMolecule);
			cml.appendChild(originalMol);
		}

		// add to root element
		rootCML.appendChild(cml);
		
		return rootCML;
	}
	
	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	private CMLCml getHeader()
	{
		//cml root element
		CMLCml rootCML = new CMLCml();
		//add convention mzAnnot
		CMLAttribute convent = new CMLAttribute("convention", "mzAnnot");
		rootCML.addAttribute(convent);
		
		return rootCML;
	}

}
