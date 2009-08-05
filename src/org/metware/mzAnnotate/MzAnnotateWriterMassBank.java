package org.metware.mzAnnotate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;

import de.ipbhalle.metfrag.tools.WrapperSpectrum;

public class MzAnnotateWriterMassBank {
	
	/**
	 * Gets the mzAnnotate for an MassBank entry. If IAtomContainer is null 
	 * no molecule is supllied and also not written!
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
	
	public static void main(String[] args) {

		try {
			MDLReader reader = new MDLReader(new FileReader(new File("examples/naringenin/naringenin.mol")));
			ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
			List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
			IAtomContainer mol = containersList.get(0);
			MzAnnotateWriterMassBank test = new MzAnnotateWriterMassBank();
			WrapperSpectrum spectrum = new WrapperSpectrum("examples/naringenin/PB000122.txt");
			CMLCml cml = test.GetMzAnnotate(spectrum, mol);
			System.out.println(cml.toXML());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
