package org.metware.mzAnnotate;

import java.util.HashMap;

import org.openscience.cdk.libio.cml.Convertor;
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
	 * @param data the spectrum
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	public CMLCml GetMzAnnotate(MzAnnotate mzAnno)
	{
		//get header
		CMLCml rootCML = getHeader();
		
		CMLSpect test = new CMLSpect();
		// write spectrum first
		CMLList cml = test.getCmlSpect(mzAnno.getSpecData(), mzAnno.getAssignedFragments());
		
		
		Convertor convertor = new Convertor(true, "cml");
		Fragment fragOrig = Tools.getMeasuredCompound(mzAnno.getFragMap().getFragList());
		CMLMolecule cmlMol = null;
		if(fragOrig.isStructureKnown())
			cmlMol = convertor.cdkAtomContainerToCMLMolecule(fragOrig.getMolecule());
		else
		{
			//TODO!
		}	
			
		CMLReactionList reactionList = new CMLReactionList();
		CMLReaction reaction = new CMLReaction();
		reactionList.addReaction(reaction);

		CMLReactantList reactantList = new CMLReactantList();
		reaction.addReactantList(reactantList);
		CMLReactant reactant = new CMLReactant();
		reactant.addMolecule(cmlMol);
		reactantList.addReactant(reactant);
		
		
		HashMap<String, Fragment> fragList = mzAnno.getFragMap().getFragList();
		
		CMLProductList productList = new CMLProductList();
		reaction.addProductList(productList);
		
		for (String fragID : fragList.keySet()) {	
			Fragment frag = fragList.get(fragID);
			if(!frag.isMeasuredCompound())
			{
				if(frag.isStructureKnown())
				{
					frag.getMolecule().setID(fragID);
					CMLMolecule productMol = convertor.cdkAtomContainerToCMLMolecule(frag.getMolecule());
					CMLProduct product = new CMLProduct();
					product.addMolecule(productMol);
					productList.addProduct(product);
				}
				else
				{
					//TODO!
				}
			
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
	 * @param data the data
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	public CMLCml GetMzAnnotateMassBank(MzAnnotate mzAnno)
	{
		//cml root element
		CMLCml rootCML = getHeader();
		
		CMLSpect spect = new CMLSpect();
		// write spectrum first
		CMLList cml = spect.getCmlSpect(mzAnno.getSpecData(), null);
		Fragment originalMolecule = Tools.getMeasuredCompound(mzAnno.getFragMap().getFragList());
		if(originalMolecule != null && originalMolecule.isStructureKnown())
		{
			Convertor convertor = new Convertor(true, "cml");
			CMLMolecule originalMol = convertor.cdkAtomContainerToCMLMolecule(originalMolecule.getMolecule());
			cml.appendChild(originalMol);
		}
		//no molecule given...maybe only a molecular formula
		else if(originalMolecule != null)
		{
			//TODO!
//			Convertor convertor = new Convertor(true, "cml");
//			CMLMolecule originalMol = convertor.cdkAtomContainerToCMLMolecule();
//			cml.appendChild(originalMol);
			
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
