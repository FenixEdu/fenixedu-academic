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
public interface IEvaluationMethod {

	public String getEvaluationElements();
	public String getEvaluationElementsEn();
	public void setEvaluationElements(String string);
	public void setEvaluationElementsEn(String string);
	public Integer getKeyCurricularCourse();
	public void setKeyCurricularCourse(Integer integer);
	public ICurricularCourse getCurricularCourse();
	public void setCurricularCourse(ICurricularCourse execucao);

}
