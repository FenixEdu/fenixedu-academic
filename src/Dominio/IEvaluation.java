/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IEvaluation {

	public String getEvaluationElements();
	public void setEvaluationElements(String string);
	public Integer getKeyExecutionCourse();
	public void setKeyExecutionCourse(Integer integer);
	public IDisciplinaExecucao getExecutionCourse();
	public void setExecutionCourse(IDisciplinaExecucao execucao);

}
