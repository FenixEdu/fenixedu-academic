package Dominio;

/**
 * 
 * @author Susana Fernandes
 * 
 */

public interface IMetadata extends IDomainObject {
	public abstract IExecutionCourse getExecutionCourse();
	public abstract Integer getKeyExecutionCourse();
	public abstract String getMetadataFile();
	public abstract Boolean getVisibility();
	public abstract void setExecutionCourse(IExecutionCourse execucao);
	public abstract void setKeyExecutionCourse(Integer integer);
	public abstract void setMetadataFile(String string);
	public abstract void setVisibility(Boolean boolean1);
}