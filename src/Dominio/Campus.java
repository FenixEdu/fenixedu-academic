package Dominio;

import java.util.List;

/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class Campus extends DomainObject implements ICampus {
	private String name;

	private List executionDegreeList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getExecutionDegreeList() {
		return executionDegreeList;
	}

	public void setExecutionDegreeList(List executionDegreeList) {
		this.executionDegreeList = executionDegreeList;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICampus) {
			ICampus campus = (ICampus) obj;
			result = getName().equals(campus.getName());
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
