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
public interface IResponsibleFor {
	public ITeacher getTeacher();
	public IDisciplinaExecucao getExecutionCourse();

	public void setTeacher(ITeacher teacher);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
}
