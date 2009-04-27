package org.metware.mzAnnotate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLProduct;
import org.xmlcml.cml.element.CMLProductList;
import org.xmlcml.cml.element.CMLReactant;
import org.xmlcml.cml.element.CMLReactantList;
import org.xmlcml.cml.element.CMLReaction;
import org.xmlcml.cml.element.CMLReactionList;

import de.ipbhalle.metfrag.tools.WrapperSpectrum;


public class MzAnnotateWriter {
	    
   
    public static void main(String[] args) throws FileNotFoundException, CDKException {
		  WrapperSpectrum spectrum = new WrapperSpectrum("examples/naringenin/PB000122.txt");
		  CMLSpect test = new CMLSpect();
		  
		  //write spectrum first
		  CMLList cml = test.getCmlSpect(spectrum);
		  

	      //now write the corresponding molecule and its fragments
      	  String file = "examples/naringenin/naringenin.mol";			
		  MDLReader reader;
		  List<IAtomContainer> containersList;
			
	      reader = new MDLReader(new FileReader(new File(file)));
	      ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	      containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        
	      IAtomContainer mol = containersList.get(0);
	          
	      Convertor convertor = new Convertor(true, "cml");
	      mol.setID("original");
	      org.xmlcml.cml.element.CMLMolecule originalMol = convertor.cdkAtomContainerToCMLMolecule(mol);
	      int atomCount = mol.getAtomCount();
	      int bondCount = mol.getBondCount();
	      
	      //now preprocess the atom and bond id's
	      mol.setID("p1");
	      for (IBond bond : mol.bonds()) {
	    	bondCount++;
			bond.setID("b" + bondCount);
	      }
	      for (IAtom atom : mol.atoms()) {
				atomCount++;
				atom.setID("a" + atomCount);
		  }
	      
	      
	      org.xmlcml.cml.element.CMLMolecule productMol = convertor.cdkAtomContainerToCMLMolecule(mol);
	      

	      CMLReactionList reactionList = new CMLReactionList();
	      CMLReaction reaction = new CMLReaction();
	      reactionList.addReaction(reaction);
	      
	      CMLReactantList reactantList = new CMLReactantList();
	      reaction.addReactantList(reactantList);
	      
	      CMLReactant reactant = new CMLReactant();
	      reactant.addMolecule(originalMol);
	      reactantList.addReactant(reactant);
	      
	      
	      
	      CMLProductList productList = new CMLProductList();	      
	      reaction.addProductList(productList);
	      
	      //TODO: add real candidate structures
	      CMLProduct product = new CMLProduct();
	      product.addMolecule(productMol);
	      productList.addProduct(product);
	       
	      cml.appendChild(reactionList);
	      

	      System.out.println(cml.toXML());
    }
    
}
