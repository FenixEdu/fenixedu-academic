/*
 * Created on 7/Mai/2003
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
public class InfoSiteSection implements ISiteComponent {

private InfoSection section;
private List items;
/**
 * @return
 */
public List getItems() {
	return items;
}

/**
 * @return
 */
public InfoSection getSection() {
	return section;
}

/**
 * @param list
 */
public void setItems(List list) {
	items = list;
}

/**
 * @param section
 */
public void setSection(InfoSection section) {
	this.section = section;
}

}
