package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda Quitério
 * 
 */
public class InfoSiteItems extends DataTranferObject implements ISiteComponent {

	private InfoItem item;
	private List items;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSITEITEMS";
		result += " item=" + getItem();
		result += ", items=" + getItems();
		result += "]";
		return result;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if (objectToCompare instanceof InfoSiteItems
			&& (((((InfoSiteItems) objectToCompare).getItem() != null
				&& this.getItem() != null
				&& ((InfoSiteItems) objectToCompare).getItem().equals(this.getItem()))
				|| ((InfoSiteItems) objectToCompare).getItem() == null
				&& this.getItem() == null))) {
			result = true;
		}

		if (((InfoSiteItems) objectToCompare).getItems() == null && this.getItems() == null && result == true) {
			return true;
		}
		if (((InfoSiteItems) objectToCompare).getItems() == null
			|| this.getItems() == null
			|| ((InfoSiteItems) objectToCompare).getItems().size() != this.getItems().size()) {

			return false;
		}
		ListIterator iter1 = ((InfoSiteItems) objectToCompare).getItems().listIterator();
		ListIterator iter2 = this.getItems().listIterator();
		while (result && iter1.hasNext()) {
			InfoItem infoItem1 = (InfoItem) iter1.next();
			InfoItem infoItem2 = (InfoItem) iter2.next();
			if (!infoItem1.equals(infoItem2)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	public InfoItem getItem() {
		return this.item;
	}

	/**
	 * @return
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @param section
	 */
	public void setItem(InfoItem item) {
		this.item = item;
	}

	/**
	 * @param list
	 */
	public void setItems(List list) {
		items = list;
	}
}