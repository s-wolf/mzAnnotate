package org.metware.mzAnnotate;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openscience.cdk.libio.cml.Convertor;
import org.xmlcml.cml.base.CMLAttribute;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLMoleculeList;
import org.xmlcml.cml.element.CMLProduct;
import org.xmlcml.cml.element.CMLProductList;
import org.xmlcml.cml.element.CMLReactant;
import org.xmlcml.cml.element.CMLReactantList;
import org.xmlcml.cml.element.CMLReaction;
import org.xmlcml.cml.element.CMLReactionList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;


import de.ipbhalle.metfrag.massbankParser.Peak;

public class MzAnnotateWriter {
	
	
	/**
	 * Gets the mzAnnotate. The mzAnnotate object is analyzed and then the
	 * necessary parts are returned. See the use cases for the different
	 * outputs.
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return the mz annotate
	 */
	public CMLCml getMzAnnotate(MzAnnotate mzAnno)
	{
		//get header
		CMLCml rootCML = getHeader();
		rootCML.addNamespaceDeclaration("", "http://www.xml-cml.org/schema");
		
		CMLSpect spect = new CMLSpect();

		// the spectrum is mandatory
		rootCML.appendChild(spect.getCmlSpect(mzAnno.getSpecData(), mzAnno.getAssignedFragments()));
		
		//reaction list
		if(mzAnno.isReactionGiven())
		{
			rootCML = getReactionList(mzAnno, rootCML);
		}
		
		CMLMoleculeList allMolecules = new CMLMoleculeList();
		CMLMolecule cmlMeasuredCompound = new CMLMolecule();
		//now check if there is the measured structure given
		if(Tools.isStructureGiven(mzAnno))
		{
			Fragment originalStructure = Tools.getMeasuredCompound(mzAnno.getFragMap().getFragList());
			if(originalStructure.isStructureKnown())
			{
				cmlMeasuredCompound = getMolecule(mzAnno);
			}
			else
			{
				//TODO! no structure given but only the molecular formula!
			}	
		}
		//no structure given...measured compound unknown...
		else
		{
			//TODO
		}
		
		//molecule list
		if(Tools.isMoleculeListGiven(mzAnno))
		{
			allMolecules = getMoleculeList(mzAnno);
		}
		
		//also add the measured compound
		allMolecules.addMolecule(cmlMeasuredCompound);
		
		//surround with list dict...
		CMLList cmlList = new CMLList();
		CMLAttribute attribute = new CMLAttribute("dictRef");
	    attribute.setValue("cdk:moleculeSet");
	    cmlList.addAttribute(attribute);
	    cmlList.appendChild(allMolecules);
		//now add the molecule list to the previously generated cml
	    rootCML.appendChild(cmlList);
		
		return rootCML;		
	}
	
	
	/**
	 * Gets the mzannotate string formatted.
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return the mzannotate string formatted
	 */
	public String getMzannotateStringFormatted(MzAnnotate mzAnno)
	{
		CMLCml mzAnnoObject = getMzAnnotate(mzAnno);
		return prettyFormat(mzAnnoObject.toXML(), 2);
	}
	
	
	/**
	 * Pretty format.
	 * 
	 * @param input the input
	 * @param indent the indent
	 * 
	 * @return the string
	 */
	private static String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // simple exception handling, please review it
	    }
	}


	
	
	/**
	 * Gets the rectionList. For now only the reactant (measured compound)
	 * and the products are listed.
	 * TODO: SpectralTrees have to be implemented....
	 * 
	 * @param data the spectrum
	 * @param originalMolecule the original molecule
	 * 
	 * @return the cML cml
	 */
	private CMLCml getReactionList(MzAnnotate mzAnno, CMLCml cml)
	{
		HashMap<String, Fragment> fragList = mzAnno.getFragMap().getFragList();
		HashMap<Peak, List<String>> assignedFrags = mzAnno.getAssignedFragments();
		
		CMLReactionList reactionList = new CMLReactionList();
		CMLReaction reaction = new CMLReaction();
		reactionList.addReaction(reaction);
		
		//set the reactant
		for (String fragID : fragList.keySet()) {	
			//skip the product
			if(fragList.get(fragID).isMeasuredCompound())
			{
				CMLReactantList reactantList = new CMLReactantList();
				reaction.addReactantList(reactantList);
				//add reactant
				CMLMolecule reactantRef = new CMLMolecule();
				CMLAttribute refReactant = new CMLAttribute("ref");
				refReactant.setValue(fragID);
				reactantRef.addAttribute(refReactant);
				CMLReactant reactant = new CMLReactant();;
				reactant.addMolecule(reactantRef);
				reactantList.addReactant(reactant);
				break;
			}
		}
		

		CMLProductList productList = new CMLProductList();
		reaction.addProductList(productList);
		
		//now set the assigned structures in the reaction list
		for (String fragID : fragList.keySet()) {	
			//skip the product
			if(!fragList.get(fragID).isMeasuredCompound())
			{
				CMLMolecule productRef = new CMLMolecule();
				
				CMLAttribute ref = new CMLAttribute("ref");
				ref.setValue(fragID);
				productRef.addAttribute(ref);
				
				CMLProduct product = new CMLProduct();
				product.addMolecule(productRef);
				productList.addProduct(product);
			}
		}

		cml.appendChild(reactionList);
		
		return cml;

	}
	
	
	/**
	 * Gets the molecule list as mzAnnotate.
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return the molecule list
	 */
	private CMLMoleculeList getMoleculeList(MzAnnotate mzAnno)
	{
		HashMap<String, Fragment> fragList = mzAnno.getFragMap().getFragList();
		CMLMoleculeList moleculeList = new CMLMoleculeList();
		Convertor convertor = new Convertor(true, "cml");
		
		for (String fragID : fragList.keySet()) {	
			Fragment frag = fragList.get(fragID);
			if(!frag.isMeasuredCompound())
			{
				if(frag.isStructureKnown())
				{
					frag.getMolecule().setID(fragID);
					CMLMolecule molecule = convertor.cdkAtomContainerToCMLMolecule(frag.getMolecule());
					moleculeList.addMolecule(molecule);
				}
				else
				{
					//TODO! only molecular formula given
				}
			}
		}
	   
		return moleculeList;
	}
	
	
	/**
	 * Gets the molecule. This is used when the original structure is given!
	 * 
	 * @param mzAnno the mz anno
	 * 
	 * @return the molecule
	 */
	private CMLMolecule getMolecule(MzAnnotate mzAnno)
	{
		
		CMLMolecule cmlMol = new CMLMolecule();
		
		Fragment originalMolecule = Tools.getMeasuredCompound(mzAnno.getFragMap().getFragList());
		if(originalMolecule.isStructureKnown())
		{

			Convertor convertor = new Convertor(true, "cml");
			cmlMol = convertor.cdkAtomContainerToCMLMolecule(originalMolecule.getMolecule());
		}
		else
		{
			//TODO! only molecular formula given!			
		}
		
		return cmlMol;
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
