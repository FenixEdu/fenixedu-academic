/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

import java.util.List;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoSiteTimetable implements ISiteComponent {

private List lessons;
/**
 * @return
 */
public List getLessons() {
	return lessons;
}

/**
 * @param list
 */
public void setLessons(List list) {
	lessons = list;
}

}
