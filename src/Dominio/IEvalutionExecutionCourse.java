package Dominio;

/**
 * @author Tânia Pousão
 *
 */
public interface IEvalutionExecutionCourse {
	public IEvaluation getEvaluation();
	public IDisciplinaExecucao getExecutionCourse();

	public void setEvaluation(IEvaluation evaluation);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
}
