/*
 * Created on 26/Mar/2003
 *
 * 
 */
package Dominio;

/**
 * @author João Mota
 *
 *
 */
public class Professorship extends DomainObject implements IProfessorship {
	protected ITeacher teacher;
	protected IDisciplinaExecucao executionCourse;
	
	private Integer keyTeacher;
	private Integer keyExecutionCourse;

	/**
	 * 
	 */
	public Professorship() {}

	public Professorship(ITeacher teacher,IDisciplinaExecucao executionCourse) {
	setTeacher(teacher);
	setExecutionCourse(executionCourse);
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
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyTeacher() {
		return keyTeacher;
	}

	/**
	 * @return ITeacher
	 */
	public ITeacher getTeacher() {
		return teacher;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * Sets the keyExecutionCourse.
	 * @param keyExecutionCourse The keyExecutionCourse to set
	 */
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	/**
	 * Sets the keyTeacher.
	 * @param keyTeacher The keyTeacher to set
	 */
	public void setKeyTeacher(Integer keyTeacher) {
		this.keyTeacher = keyTeacher;
	}

	/**
	 * Sets the teacher.
	 * @param teacher The teacher to set
	 */
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}
	public String toString() {
			String result = "Professorship :\n";
			result += "\n  - ExecutionCourse : "+ getExecutionCourse();
			result += "\n  - Teacher : " + getTeacher();
			
			
			return result;
		}
}
