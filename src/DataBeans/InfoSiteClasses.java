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
public class InfoSiteClasses implements ISiteComponent {

private List classes;
	/**
	 * 
	 */
	public InfoSiteClasses() {
	}

/**
 * @return
 */
public List getClasses() {
	return classes;
}

/**
 * @param list
 */
public void setClasses(List list) {
	classes = list;
}

}
