/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteBibliography extends DataTranferObject implements ISiteComponent {

private List bibliographicReferences;


public boolean equals(Object objectToCompare) {
	boolean result = false;
	if (objectToCompare instanceof InfoSiteBibliography) {
		result = true;
	}

	if (((InfoSiteBibliography) objectToCompare).getBibliographicReferences() == null
		&& this.getBibliographicReferences() == null) {
		return true;
	}
	if (((InfoSiteBibliography) objectToCompare).getBibliographicReferences() == null
		|| this.getBibliographicReferences() == null
		|| ((InfoSiteBibliography) objectToCompare).getBibliographicReferences().size() != this.getBibliographicReferences().size()) {
		return false;
	}
	ListIterator iter1 = ((InfoSiteBibliography) objectToCompare).getBibliographicReferences().listIterator();
	ListIterator iter2 = this.getBibliographicReferences().listIterator();
	while (result && iter1.hasNext()) {
		InfoBibliographicReference infoBibliographicReference1 = (InfoBibliographicReference) iter1.next();
		InfoBibliographicReference infoBibliographicReference2 = (InfoBibliographicReference) iter2.next();
		if (!infoBibliographicReference1.equals(infoBibliographicReference2)) {
			result = false;
		}
	}

	return result;
}

/**
 * @return
 */
public List getBibliographicReferences() {
	return bibliographicReferences;
}

/**
 * @param list
 */
public void setBibliographicReferences(List list) {
	bibliographicReferences = list;
}

}
