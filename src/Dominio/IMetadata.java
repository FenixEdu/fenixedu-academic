package Dominio;

/**
 * 
 * @author Susana Fernandes
 * 
 */

public interface IMetadata extends IDomainObject {
	public abstract IDisciplinaExecucao getExecutionCourse();
	public abstract Integer getKeyExecutionCourse();
	public abstract String getMetadataFile();
	public abstract void setExecutionCourse(IDisciplinaExecucao execucao);
	public abstract void setKeyExecutionCourse(Integer integer);
	public abstract void setMetadataFile(String string);
}