/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteCommon implements ISiteComponent {

private String title;
private String mail;
private InfoExecutionCourse executionCourse;
private List sections;
private List associatedDegrees;
// in reality the associatedDegrees list is a list of curricular courses

/**
 * @return
 */
public List getAssociatedDegrees() {
	return associatedDegrees;
}

/**
 * @return
 */
public String getMail() {
	return mail;
}

/**
 * @return
 */
public List getSections() {
	return sections;
}

/**
 * @return
 */
public String getTitle() {
	return title;
}

/**
 * @param list
 */
public void setAssociatedDegrees(List list) {
	associatedDegrees = list;
}

/**
 * @param string
 */
public void setMail(String string) {
	mail = string;
}

/**
 * @param list
 */
public void setSections(List list) {
	sections = list;
}

/**
 * @param string
 */
public void setTitle(String string) {
	title = string;
}

/**
 * @return
 */
public InfoExecutionCourse getExecutionCourse() {
	return executionCourse;
}

/**
 * @param course
 */
public void setExecutionCourse(InfoExecutionCourse course) {
	executionCourse = course;
}

}
