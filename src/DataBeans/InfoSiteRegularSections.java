package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda Quitério
 *
 * 
 */
public class InfoSiteRegularSections extends DataTranferObject implements ISiteComponent {

	private List regularSections;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSITEREGULARSECTIONS";
		result += ", regularSections=" + getRegularSections();
		result += "]";
		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSiteRegularSections) {
			result = true;
		}

		if (((InfoSiteRegularSections) obj).getRegularSections() == null && this.getRegularSections() == null) {
			System.out.println("-------> result true" + result);

			return true;
		}
		if (((InfoSiteRegularSections) obj).getRegularSections() == null
			|| this.getRegularSections() == null
			|| ((InfoSiteRegularSections) obj).getRegularSections().size() != this.getRegularSections().size()) {
				System.out.println("-------> result false " + result);
			return false;
		}

		ListIterator iter1 = ((InfoSiteRegularSections) obj).getRegularSections().listIterator();
		ListIterator iter2 = this.getRegularSections().listIterator();
		while (result && iter1.hasNext()) {
			InfoSection infoSection1 = (InfoSection) iter1.next();
			InfoSection infoSection2 = (InfoSection) iter2.next();
			if (!infoSection1.equals(infoSection2)) {
				result = false;
			}
		}
	System.out.println("-------> result " + result);
		return result;
	}

	/**
	 * @return
	 */
	public List getRegularSections() {
		return regularSections;
	}

	/**
	 * @param list
	 */
	public void setRegularSections(List list) {
		regularSections = list;
	}

}
