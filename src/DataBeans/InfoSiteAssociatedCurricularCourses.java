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
public class InfoSiteAssociatedCurricularCourses implements ISiteComponent {

private List associatedCurricularCourses;

/**
 * @return
 */
public List getAssociatedCurricularCourses() {
	return associatedCurricularCourses;
}

/**
 * @param list
 */
public void setAssociatedCurricularCourses(List list) {
	associatedCurricularCourses = list;
}

}
