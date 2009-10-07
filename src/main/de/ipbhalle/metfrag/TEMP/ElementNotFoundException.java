package de.ipbhalle.metfrag.TEMP;

public class ElementNotFoundException extends Exception {

	ElementNotFoundException(String elementName){
		super("Element \""+elementName+"\" not found in element table!");
	}
}
