/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IProfessorship {
	public ITeacher getTeacher();
	public IDisciplinaExecucao getExecutionCourse();  

	public void setTeacher(ITeacher teacher);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);  
}
