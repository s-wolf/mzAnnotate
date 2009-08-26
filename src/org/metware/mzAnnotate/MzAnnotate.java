package org.metware.mzAnnotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class MzAnnotate {
	
	private SpectrumData specData;
	private FragmentList fragMap;
	private HashMap<Peak, List<String>> assignedFragments;
	

	/**
	 * Instantiates a new mz annotate. This is the minimal requirement.
	 * Only a peak list given with optional metadata.
	 * 
	 * @param specData the spec data
	 */
	public MzAnnotate(SpectrumData specData)
	{
		this.specData = specData;
	}
	
	/**
	 * Instantiates a new mzAnnotate object. A spectrum and possible fragments (sum formula or
	 * structure) is given. Those structures still have to be assigned to each other!
	 * 
	 * @param spectData the spect data
	 * @param fragList the frag list
	 */
	public MzAnnotate(SpectrumData spectData, FragmentList fragMap)
	{
		this.specData = spectData;
		this.fragMap = fragMap;
		this.assignedFragments = new HashMap<Peak, List<String>>();
	}
	
	/**
	 * Assign peak to fragment using the fragment id and the peak object.
	 * One peak can have several candidate structures.
	 * 
	 * @param peak the peak
	 * @param fragID the frag id
	 * 
	 * @throws Exception the exception
	 */
	public void assignPeakToFragment(Peak peak, String fragID) throws Exception
	{
		HashMap<String, Fragment> fragList = this.fragMap.getFragList();
		List<String> fragIDList = null;
		//id is known and can be assigned
		if(fragList.containsKey(fragID))
		{
			//multiple assignments are possible --> one peak might have two possible candidate structures
			fragIDList = this.assignedFragments.get(peak);
			if(fragIDList == null || fragIDList.size() == 0)
			{
				fragIDList = new ArrayList<String>();
				fragIDList.add(fragID);
			}
			else
				fragIDList.add(fragID);
			
			//override old possible older entry
			this.assignedFragments.put(peak, fragIDList);
		}
		else
			throw new Exception("This fragment id is not known!");
	}

	/**
	 * Sets the spec data.
	 * 
	 * @param specData the new spec data
	 */
	public void setSpecData(SpectrumData specData) {
		this.specData = specData;
	}

	/**
	 * Gets the spec data.
	 * 
	 * @return the spec data
	 */
	public SpectrumData getSpecData() {
		return specData;
	}

	/**
	 * Sets the frag map.
	 * 
	 * @param fragMap the new frag map
	 */
	public void setFragMap(FragmentList fragMap) {
		this.fragMap = fragMap;
	}

	/**
	 * Gets the frag map.
	 * 
	 * @return the frag map
	 */
	public FragmentList getFragMap() {
		return fragMap;
	}
	
	/**
	 * Gets the assigned fragments.
	 * 
	 * @return the assigned fragments
	 */
	public HashMap<Peak, List<String>> getAssignedFragments() {
		return assignedFragments;
	}

	/**
	 * Sets the assigned fragments.
	 * 
	 * @param assignedFragments the assigned fragments
	 */
	public void setAssignedFragments(HashMap<Peak, List<String>> assignedFragments) {
		this.assignedFragments = assignedFragments;
	}


}
