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
public class InfoSiteShifts implements ISiteComponent {

private List shifts;

/**
 * @return
 */
public List getShifts() {
	return shifts;
}

/**
 * @param list
 */
public void setShifts(List list) {
	shifts = list;
}

}
