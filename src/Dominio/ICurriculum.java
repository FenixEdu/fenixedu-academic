/*
 * ICurriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:31
 */

package Dominio;



/**
 *
 * @author  EP 15 - fjgc
 * @author jmota
 */
public interface ICurriculum {
    String getGeneralObjectives();
    String getOperacionalObjectives();
    String getProgram();
    
    IDisciplinaExecucao getExecutionCourse();
    
    void setGeneralObjectives(String generalObjectives);
    void setOperacionalObjectives(String operacionalObjectives);
    void setProgram(String program);
	
	void setExecutionCourse(IDisciplinaExecucao executionCourse);
    
}
