/*
 * Site.java
 * Mar 10, 2003
 */
package Dominio;

/**
 * @author Ivo Brandão
 */
public class Site implements ISite {

	private Integer internalCode;
	private IDisciplinaExecucao executionCourse;
	private Integer keyExecutionCourse;

	/** 
	 * Construtor
	 */
	public Site() {}

	/** 
	 * Construtor
	 */
	public Site(Integer internalCode, IDisciplinaExecucao executionCourse, 
		Integer keyExecutionCourse) {
			
		this.internalCode = internalCode;
		this.executionCourse = executionCourse;
		this.keyExecutionCourse = keyExecutionCourse;
	}

	/**
	 * @return IDisciplinaExecucao
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * Sets the keyExecutionCourse.
	 * @param keyExecutionCourse The keyExecutionCourse to set
	 */
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof ISite) {
			result = (getExecutionCourse().equals(((ISite) arg0).getExecutionCourse()));
		} 
		return result;
	}

}
