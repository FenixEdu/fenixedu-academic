package DataBeans;

import java.util.List;

/**
 * @author Tânia Pousão
 * Create on 10/Nov/2003
 */
public class InfoCampus extends InfoObject {
	private String name;
	
	private List executionDegreeList;

	public InfoCampus() {	
	}
	
	public InfoCampus(String name, List executionDegreeList) {
		this.name = name;
		this.executionDegreeList = executionDegreeList;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the executionDegreeList.
	 */
	public List getExecutionDegreeList() {
		return executionDegreeList;
	}

	/**
	 * @param executionDegreeList The executionDegreeList to set.
	 */
	public void setExecutionDegreeList(List executionDegreeList) {
		this.executionDegreeList = executionDegreeList;
	}
	
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCampus) {
			InfoCampus infoCampus = (InfoCampus) obj;
			result = getName().equals(infoCampus.getName());
		}
		return result;
	}
	
	public String toString() {
		String result = "[INFODEGREE_INFO:";
		result += " codigo interno= " + getIdInternal();
		if (getExecutionDegreeList() != null) {
			result += "nr execution Degrees= " + getExecutionDegreeList().size();		
		}
		result += " name= " + getName();
		result += "]";
		return result;
	}
}
