/*
 * ISite.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.List;

/**
 * @author Ivo Brandão
 */
public interface ISite {

	public IDisciplinaExecucao getExecutionCourse();
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
	ISection getInitialSection();
	public void setInitialSection(ISection section);
	List getSections();
	public void setSections(List sections);
	List getAnnouncements();
	public void setAnnouncements(List announcements);

}
