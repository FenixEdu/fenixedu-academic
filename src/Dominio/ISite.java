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
	List getSections();
	ISection getInitialSection();
	List getAnnouncements();
	public void setInitialSection(ISection section);
}
