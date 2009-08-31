package org.metware.mzAnnotate;

import java.util.HashMap;
import java.util.List;

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


public class CMLSpectMzAnnot {
	
	/**
	   *  Gets this spectrum as cmlSpect.
	   *  FROM NMRShiftDB!
	   *
	   * @return                   The cmlSpect value.
	   * @exception  Exception     Database problems
	   * @exception  CMLException  xml problems.
	   */
	  public CMLSpectrum getCmlSpect(SpectrumData spectrumMassbank, HashMap<Peak, List<String>> assignedFragments) {
	    
	    CMLSpectrum spectrum = new CMLSpectrum();
	    	    
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
	    
	    //TODO: do it properly
	    spectrum.setId("massbank:" + spectrumMassbank.getMassBankAccession());
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
	    	if(assignedFragments != null)
	    	{
		    	List<String> fragIDs = assignedFragments.get(peak);
		    	if(fragIDs != null && fragIDs.size() > 0)
		    	{
			    	for (String fragID : fragIDs) {
			    		CMLMolecule assignedMols = new CMLMolecule();
			    		//set ref
			    		assignedMols.setRef(fragID);
			    		//set molecular formula
			    		//assignedMols.setFormula(MolecularFormulaManipulator.getString(candidate.getMolecularFormula()));
			    		cmlPeak.addMolecule(assignedMols);
					}
		    	}
		    	peaklist.addPeak(cmlPeak);
	    	}
	    	else
	    	{
	    		peaklist.addPeak(cmlPeak);
	    	}
	    	
	    	count++;
		}
	    
	    return spectrum;
	  }
}