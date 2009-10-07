package org.metware.mzAnnotate.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.metware.mzAnnotate.FragmentList;
import org.metware.mzAnnotate.MzAnnotate;
import org.metware.mzAnnotate.SpectrumData;
import org.metware.mzAnnotate.MzAnnotateWriter;
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


public class MzAnnotateMainMB {
	
	public static void main(String[] args) {
		
		//this only creates a a single spectrum without a molecule
		try {
			MDLReader reader = new MDLReader(new FileReader(new File("examples/naringenin/naringenin.mol")));
			ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
			List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
			IAtomContainer mol = containersList.get(0);
			
			//only the spectrum
			MzAnnotateWriter test = new MzAnnotateWriter();
			SpectrumData specData = new SpectrumData("examples/naringenin/PB000122.txt");
			MzAnnotate mzAnnotate = new MzAnnotate(specData);
			CMLCml cml = test.getMzAnnotate(mzAnnotate);
			System.out.println(cml.toXML());
			
			//spectrum and mol combined
			FragmentList fragList = new FragmentList();
			fragList.addStructure(mol);
			MzAnnotate mzAnnotate1 = new MzAnnotate(specData, fragList);
			MzAnnotateWriter test1 = new MzAnnotateWriter();
			CMLCml cml1 = test1.getMzAnnotate(mzAnnotate1);
			System.out.println(cml1.toXML());
			
			System.out.println(test1.getMzannotateStringFormatted(mzAnnotate1));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
