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
public class InfoSiteTimetable extends DataTranferObject implements ISiteComponent {

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
