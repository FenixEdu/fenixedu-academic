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
