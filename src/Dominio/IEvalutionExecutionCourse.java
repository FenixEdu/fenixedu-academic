package Dominio;

/**
 * @author Tânia Pousão
 *
 */
public interface IEvalutionExecutionCourse {
	public IEvaluation getEvaluation();
	public IExecutionCourse getExecutionCourse();

	public void setEvaluation(IEvaluation evaluation);
	public void setExecutionCourse(IExecutionCourse executionCourse);
}
