/*
 * Created on 23/Jul/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */

public class InfoMetadata extends InfoObject {
	private String metadataFile;
	private InfoExecutionCourse infoExecutionCourse;
	private Boolean visibility;
	private List visibleQuestions;

	private String difficulty;
	private String level;
	private String mainSubject;
	private List secondarySubject;
	private List author;
	private List members;

	public InfoMetadata() {
	}

	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	public String getMetadataFile() {
		return metadataFile;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(Boolean boolean1) {
		visibility = boolean1;
	}

	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	public void setMetadataFile(String string) {
		metadataFile = string;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getLevel() {
		return level;
	}

	public String getMainSubject() {
		return mainSubject;
	}

	public List getMembers() {
		return members;
	}

	public List getSecondarySubject() {
		return secondarySubject;
	}

	public void setDifficulty(String string) {
		difficulty = string;
	}

	public void setLevel(String string) {
		level = string;
	}

	public void setMainSubject(String string) {
		mainSubject = string;
	}

	public void setMembers(List list) {
		members = list;
	}

	public void setSecondarySubject(List list) {
		secondarySubject = list;
	}

	public List getAuthor() {
		return author;
	}

	public void setAuthor(List list) {
		author = list;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoMetadata) {
			InfoMetadata infoMetadata = (InfoMetadata) obj;
			result = getIdInternal().equals(infoMetadata.getIdInternal());
			result =
				result
					|| (getInfoExecutionCourse()
						.equals(infoMetadata.getInfoExecutionCourse())
						&& (getMetadataFile()
							.equals(infoMetadata.getMetadataFile()))
						&& (getDifficulty().equals(infoMetadata.getDifficulty()))
						&& (getLevel().equals(infoMetadata.getLevel()))
						&& (getMainSubject()
							.equals(infoMetadata.getMainSubject()))
						&& (getSecondarySubject()
							.containsAll(infoMetadata.getSecondarySubject()))
						&& (infoMetadata
							.getSecondarySubject()
							.containsAll(getSecondarySubject()))
						&& (getAuthor().containsAll(infoMetadata.getAuthor()))
						&& (infoMetadata.getAuthor().containsAll(getAuthor()))
						&& (getMembers().containsAll(infoMetadata.getMembers()))
						&& (infoMetadata.getMembers().containsAll(getMembers()))
						&& (getVisibility().equals(infoMetadata.getVisibility())));
		}
		return result;
	}

	public List getVisibleQuestions()
	{
		return visibleQuestions;
	}

	public void setVisibleQuestions(List list)
	{
		visibleQuestions = list;
	}

}
