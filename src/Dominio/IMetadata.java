package Dominio;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author Susana Fernandes
 * 
 */

public interface IMetadata extends IDomainObject
{
	public abstract IExecutionCourse getExecutionCourse();
	public abstract Integer getKeyExecutionCourse();
	public abstract String getMetadataFile();
	public abstract String getAuthor();
	public abstract String getDescription();
	public abstract String getDifficulty();
	public abstract Calendar getLearningTime();
	public abstract String getLevel();
	public abstract String getMainSubject();
	public abstract Integer getNumberOfMembers();
	public abstract String getSecondarySubject();
	public abstract Boolean getVisibility();
	public abstract List getVisibleQuestions();
	public abstract void setExecutionCourse(IExecutionCourse execucao);
	public abstract void setKeyExecutionCourse(Integer integer);
	public abstract void setMetadataFile(String string);
	public abstract void setVisibility(Boolean boolean1);
	public abstract void setAuthor(String string);
	public abstract void setDescription(String string);
	public abstract void setDifficulty(String string);
	public abstract void setLearningTime(Calendar calendar);
	public abstract void setLevel(String string);
	public abstract void setMainSubject(String string);
	public abstract void setNumberOfMembers(Integer integer);
	public abstract void setSecondarySubject(String string);
	public abstract void setVisibleQuestions(List list);
}