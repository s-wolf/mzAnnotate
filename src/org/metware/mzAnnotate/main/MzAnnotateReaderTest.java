package org.metware.mzAnnotate.main;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.metware.mzAnnotate.MzAnnotateReader;
import org.openscience.cdk.exception.CDKException;

public class MzAnnotateReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sep = System.getProperty("file.separator");
		MzAnnotateReader test = new MzAnnotateReader();
		try {
			test.readMzAnnotate("examples" + sep + "naringenin" + sep + "MassBankAssigned.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
