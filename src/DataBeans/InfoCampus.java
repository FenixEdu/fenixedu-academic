package DataBeans;

/**
 * @author Tânia Pousão
 * Create on 10/Nov/2003
 */
public class InfoCampus extends InfoObject {
	private InfoDegree infoDegree;
	
	private String name;

	public InfoCampus() {	
	}
	
	public InfoCampus(InfoDegree degree, String name) {
		this.infoDegree = degree;
		this.name = name;
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
	 * @return Returns the infoDegree.
	 */
	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	/**
	 * @param infoDegree The infoDegree to set.
	 */
	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}
	
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCampus) {
			InfoCampus infoCampus = (InfoCampus) obj;
			result =
				getInfoDegree().equals(infoCampus.getInfoDegree())
				&& getName().equals(infoCampus.getName());
		}
		return result;
	}
	
	public String toString() {
		String result = "[INFODEGREE_INFO:";
		result += " codigo interno= " + getIdInternal();
		result += " degree= " + getInfoDegree();
		result += " name= " + getName();
		result += "]";
		return result;
	}
}
