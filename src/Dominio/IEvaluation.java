/*
 * Created on 23/Abr/2003
 *
 * 
 */
package Dominio;

/**
 * @author João Mota
 *
 * 
 */
public interface IEvaluation {

	public String getEvaluationElements();
	public String getEvaluationElementsEn();
	public void setEvaluationElements(String string);
	public void setEvaluationElementsEn(String string);
	public Integer getKeyExecutionCourse();
	public void setKeyExecutionCourse(Integer integer);
	public IDisciplinaExecucao getExecutionCourse();
	public void setExecutionCourse(IDisciplinaExecucao execucao);

}
