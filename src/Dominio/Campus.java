package Dominio;

/**
 * @author Tânia Pousão
 * Create on 10/Nov/2003
 */
public class Campus extends DomainObject implements ICampus {
	private Integer degreeKey;
	private ICurso degree;
	
	private String name;
	
	/**
	 * @return Returns the degree.
	 */
	public ICurso getDegree() {
		return degree;
	}

	/**
	 * @param degree The degree to set.
	 */
	public void setDegree(ICurso degree) {
		this.degree = degree;
	}

	/**
	 * @return Returns the degreeKey.
	 */
	public Integer getDegreeKey() {
		return degreeKey;
	}

	/**
	 * @param degreeKey The degreeKey to set.
	 */
	public void setDegreeKey(Integer degreeKey) {
		this.degreeKey = degreeKey;
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

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICampus) {
			ICampus campus = (ICampus) obj;
			result =
				getDegree().equals(campus.getDegree())
				&& getName().equals(campus.getName());
		}
		return result;
	}
	
	public String toString() {
		String result = "[INFODEGREE_INFO:";
		result += " codigo interno= " + getIdInternal();
		result += " degree= " + getDegree();
		result += " name= " + getName();
		result += "]";
		return result;
	}
}
