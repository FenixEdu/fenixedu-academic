package Dominio;

/**
 * @author Tânia Pousão
 *
 */
public interface IEvalutionExecutionCourse  extends IDomainObject{
	public IEvaluation getEvaluation();
	public IExecutionCourse getExecutionCourse();

	public void setEvaluation(IEvaluation evaluation);
	public void setExecutionCourse(IExecutionCourse executionCourse);
}
