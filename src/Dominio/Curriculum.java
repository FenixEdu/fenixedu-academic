/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package Dominio;

/**
 *
 * @author  EP 15 - fjgc
 * @author jmota
 */
public class Curriculum implements ICurriculum {
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected IDisciplinaExecucao executionCourse;
	private Integer internalCode;
	private Integer keyExecutionCourse;
	
	/** Creates a new instance of Curriculum */
	public Curriculum() {
	}
	
	public Curriculum(
		IDisciplinaExecucao executionCourse,
		String generalObjectives,
		String operacionalObjectives,
		String program) {
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setProgram(program);
		setExecutionCourse(executionCourse);
	}
	
	public Integer getInternalCode() {
		return internalCode;
	}
	
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}
	
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}
	
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}
	public String getGeneralObjectives() {
		return generalObjectives;
	}
	
	public String getOperacionalObjectives() {
		return operacionalObjectives;
	}
	
	public String getProgram() {
		return program;
	}
	
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}
	
	public void setGeneralObjectives(String generalObjectives) {
		this.generalObjectives = generalObjectives;
	}
	
	public void setOperacionalObjectives(String operacionalObjectives) {
		this.operacionalObjectives = operacionalObjectives;
	}
	
	public void setProgram(String program) {
		this.program = program;
	}
	
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}
	
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICurriculum) {
			ICurriculum curriculum = (ICurriculum) obj;
			result =
				getGeneralObjectives().equals(
					curriculum.getGeneralObjectives())
					&& getOperacionalObjectives().equals(
						curriculum.getOperacionalObjectives())
					&& getProgram().equals(curriculum.getProgram());
		}
		return result;
	}
	
	
	public String toString() {
		String result = "[CURRICULUM";
		result += "]";
		return result;
	}
}
