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
