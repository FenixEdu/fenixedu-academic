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
public class InfoSiteBibliography implements ISiteComponent {

private List bibliographicReferences;

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
