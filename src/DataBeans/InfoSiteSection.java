/*
 * Created on 7/Mai/2003
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
