/*
 * Created on 23/Jul/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */

public class InfoMetadata extends InfoObject
{
	private String metadataFile;
	private InfoExecutionCourse infoExecutionCourse;

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

	public InfoMetadata()
	{
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

	public InfoExecutionCourse getInfoExecutionCourse()
	{
		return infoExecutionCourse;
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

	public String getMetadataFile()
	{
		return metadataFile;
	}

	public Integer getNumberOfMembers()
	{
		return numberOfMembers;
	}

	public String getSecondarySubject()
	{
		return secondarySubject;
	}

	public Boolean getVisibility()
	{
		return visibility;
	}

	public List getVisibleQuestions()
	{
		return visibleQuestions;
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

	public void setInfoExecutionCourse(InfoExecutionCourse course)
	{
		infoExecutionCourse = course;
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

	public void setMetadataFile(String string)
	{
		metadataFile = string;
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

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof InfoMetadata)
		{
			InfoMetadata infoMetadata = (InfoMetadata) obj;
			result = getIdInternal().equals(infoMetadata.getIdInternal());
			result =
				result
					|| (getInfoExecutionCourse().equals(infoMetadata.getInfoExecutionCourse())
						&& (getMetadataFile().equals(infoMetadata.getMetadataFile()))
						&& (getDescription().equals(infoMetadata.getDescription()))
						&& (getDifficulty().equals(infoMetadata.getDifficulty()))
						&& (getLearningTime().equals(infoMetadata.getLearningTime()))
						&& (getLevel().equals(infoMetadata.getLevel()))
						&& (getMainSubject().equals(infoMetadata.getMainSubject()))
						&& (getSecondarySubject().equals(infoMetadata.getSecondarySubject()))
						&& (getAuthor().equals(infoMetadata.getAuthor()))
						&& (getNumberOfMembers().equals(infoMetadata.getNumberOfMembers()))
						&& (getVisibility().equals(infoMetadata.getVisibility()))
						&& (getVisibleQuestions().containsAll(infoMetadata.getVisibleQuestions()))
						&& (infoMetadata.getVisibleQuestions().containsAll(getVisibleQuestions())));
		}
		return result;
	}

}
