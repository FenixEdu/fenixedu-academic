package Dominio;

import java.util.List;

/**
 * @author Susana Fernandes
 */

public class Metadata extends DomainObject implements IMetadata
{
	private String MetadataFile;
	private IExecutionCourse executionCourse;
	private Integer keyExecutionCourse;

	private String description;
	private String difficulty;
	private String learningTime;
	private String level;
	private String mainSubject;
	private String secondarySubject;
	private String author;
	private Integer numberOfMembers;
	private Boolean visibility;
	private List visibleQuestions;

	public Metadata()
	{
	}

	public Metadata(Integer metadataId)
	{
		setIdInternal(metadataId);
	}

	public IExecutionCourse getExecutionCourse()
	{
		return executionCourse;
	}

	public Integer getKeyExecutionCourse()
	{
		return keyExecutionCourse;
	}

	public String getMetadataFile()
	{
		return MetadataFile;
	}

	public String getAuthor()
	{
		return author;
	}

	public String getDescription()
	{
		return description;
	}

	public String getDifficulty()
	{
		return difficulty;
	}

	public String getLearningTime()
	{
		return learningTime;
	}

	public String getLevel()
	{
		return level;
	}

	public String getMainSubject()
	{
		return mainSubject;
	}

	public Integer getNumberOfMembers()
	{
		return numberOfMembers;
	}

	public String getSecondarySubject()
	{
		return secondarySubject;
	}

	public List getVisibleQuestions()
	{
		return visibleQuestions;
	}

	public void setExecutionCourse(IExecutionCourse execucao)
	{
		executionCourse = execucao;
	}

	public void setKeyExecutionCourse(Integer integer)
	{
		keyExecutionCourse = integer;
	}

	public void setMetadataFile(String string)
	{
		MetadataFile = string;
	}

	public Boolean getVisibility()
	{
		return visibility;
	}

	public void setAuthor(String string)
	{
		author = string;
	}

	public void setDescription(String string)
	{
		description = string;
	}

	public void setDifficulty(String string)
	{
		difficulty = string;
	}

	public void setLearningTime(String string)
	{
		learningTime = string;
	}

	public void setLevel(String string)
	{
		level = string;
	}

	public void setMainSubject(String string)
	{
		mainSubject = string;
	}

	public void setNumberOfMembers(Integer integer)
	{
		numberOfMembers = integer;
	}

	public void setSecondarySubject(String string)
	{
		secondarySubject = string;
	}

	public void setVisibility(Boolean boolean1)
	{
		visibility = boolean1;
	}

	public void setVisibleQuestions(List list)
	{
		visibleQuestions = list;
	}

}
