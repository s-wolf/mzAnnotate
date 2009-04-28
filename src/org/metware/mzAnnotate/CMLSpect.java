package org.metware.mzAnnotate;

import java.util.HashMap;
import java.util.List;

import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.base.CMLAttribute;
import org.xmlcml.cml.element.CMLConditionList;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMetadata;
import org.xmlcml.cml.element.CMLMetadataList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLPeakList;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.element.CMLSpectrum;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.Candidate;
import de.ipbhalle.metfrag.tools.WrapperSpectrum;

public class CMLSpect {
	
	/**
	   *  Gets this spectrum as cmlSpect.
	   *  FROM NMRShiftDB!
	   *
	   * @return                   The cmlSpect value.
	   * @exception  Exception     Database problems
	   * @exception  CMLException  xml problems.
	   */
	  public CMLList getCmlSpect(WrapperSpectrum spectrumMassbank) {
	    CMLList cml = new CMLList();
	    cml.addNamespaceDeclaration("", "http://www.xml-cml.org/schema");
	    CMLAttribute attribute = new CMLAttribute("dictRef");
	    attribute.setValue("cdk:model");
	    cml.addAttribute(attribute);
	    
	    CMLSpectrum spectrum = new CMLSpectrum();
	    
	    
//	    <list dictRef="cdk:model" xmlns="http://www.xml-cml.org/schema">
//	    <spectrum id="massbank:PB000123" moleculeRef="CHEBI:50202" type="MS2" 
//	    xmlns:macie="http://www.xml-cml.org/dict/macie" 
//	    xmlns:siUnits="http://www.xml-cml.org/units/siUnits" 
//	    xmlns:ms="http://www.massbank.jp/dict" 
//	    xmlns:subst="http://www.xml-cml.org/dict/substDict" 
//	    xmlns:cmlDict="http://www.xml-cml.org/dict/cmlDict" 
//	    xmlns:cml="http://www.xml-cml.org/dict/cml" 
//	    xmlns:units="http://www.xml-cml.org/units/units">
	    
	    spectrum.addNamespaceDeclaration("macie","http://www.xml-cml.org/dict/macie");
	    spectrum.addNamespaceDeclaration("siUnits","http://www.xml-cml.org/units/siUnits");
	    spectrum.addNamespaceDeclaration("ms","http://www.massbank.jp/dict");
	    spectrum.addNamespaceDeclaration("subst","http://www.xml-cml.org/dict/substDict");
	    spectrum.addNamespaceDeclaration("cmlDict","http://www.xml-cml.org/dict/cmlDict");
	    spectrum.addNamespaceDeclaration("cml","http://www.xml-cml.org/dict/cml");
	    spectrum.addNamespaceDeclaration("units","http://www.xml-cml.org/units/units");
	    CMLPeakList peaklist = new CMLPeakList();
	    CMLConditionList cl=new CMLConditionList();
	    spectrum.addConditionList(cl);
	    CMLMetadataList ml=new CMLMetadataList();
	    spectrum.addMetadataList(ml);
	    //CMLSubstanceList sl=new CMLSubstanceList();
	    //spectrum.appendChild(sl);
	    
	    //TODO: properly get all spectrum conditions
	    //get collision energy
	    CMLScalar scalar=new CMLScalar(spectrumMassbank.getCollisionEnergy());
	    scalar.setDictRef("cml:collisionenergy");
	    scalar.setUnits("siUnits:eV");
	    cl.addScalar(scalar);
	    
	    String modeString = "positive";
	    int mode = spectrumMassbank.getMode();
	    if(mode < 0)
	    	modeString = "negative";
	    scalar = new CMLScalar(modeString);
	    scalar.setDictRef("cml:polarity");
	    cl.addScalar(scalar);
	    
	    //add metadata
	    CMLMetadata metadata=new CMLMetadata();
		metadata.setName("ms:assignmentMethod");
		metadata.setContent("MetFrag");
		ml.addMetadata(metadata);
		
		metadata=new CMLMetadata();
		metadata.setName("ms:name");
		metadata.setContent(spectrumMassbank.getTrivialName());
		ml.addMetadata(metadata);
		
		metadata=new CMLMetadata();
		metadata.setName("ms:exactMass");
		Double exactMass = Math.round(spectrumMassbank.getExactMass() * 1000.0) / 1000.0;
		metadata.setContent(exactMass.toString());
		ml.addMetadata(metadata);
	    
	    
	    spectrum.addPeakList(peaklist);
	    

	    spectrum.setId("massbank:" + spectrumMassbank.getFilename());
	    spectrum.setMoleculeRef("PubChem:" + spectrumMassbank.getCID());
	    spectrum.setType("MS2");
	    List<Peak> peaks = spectrumMassbank.getPeakList();
	    
	    Integer count = 0;
	    for (Peak peak : peaks) {
	    	CMLPeak cmlPeak = new CMLPeak();
	    	cmlPeak.setXValue(peak.getMass());
	    	cmlPeak.setXUnits("units:mz");
	    	cmlPeak.setYValue(peak.getRelIntensity());
	    	cmlPeak.setYUnits("units:cps");
	    	cmlPeak.setId("peak" + count.toString());
	    	
	    	//TODO: add molecule refs
	    	List<Candidate> structures = spectrumMassbank.getAssignedFrags(peak.getMass());
	    	if(structures != null)
	    	{
		    	for (Candidate candidate : structures) {
		    		CMLMolecule assignedMols = new CMLMolecule();
		    		//set ref
		    		assignedMols.setRef(candidate.getId());
		    		//set molecular formula
		    		assignedMols.setFormula(MolecularFormulaManipulator.getString(candidate.getMolecularFormula()));
		    		cmlPeak.addMolecule(assignedMols);
				}
	    	}
	    	
	    	peaklist.addPeak(cmlPeak);
	    	count++;
		}
	    
	    cml.appendChild(spectrum);
	    return cml;
	  }
}