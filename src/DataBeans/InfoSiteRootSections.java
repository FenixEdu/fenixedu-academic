package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda Quitério
 * 
 *  
 */
public class InfoSiteRootSections extends DataTranferObject implements ISiteComponent {

    private List rootSections;

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOSITEROOTSECTIONS";
        result += ", rootSections=" + getRootSections();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteRootSections) {
            result = true;
        }

        if (((InfoSiteRootSections) obj).getRootSections() == null && this.getRootSections() == null) {
            return true;
        }
        if (((InfoSiteRootSections) obj).getRootSections() == null
                || this.getRootSections() == null
                || ((InfoSiteRootSections) obj).getRootSections().size() != this.getRootSections()
                        .size()) {
            return false;
        }

        ListIterator iter1 = ((InfoSiteRootSections) obj).getRootSections().listIterator();
        ListIterator iter2 = this.getRootSections().listIterator();
        while (result && iter1.hasNext()) {
            InfoSection infoSection1 = (InfoSection) iter1.next();
            InfoSection infoSection2 = (InfoSection) iter2.next();
            if (!infoSection1.equals(infoSection2)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getRootSections() {
        return rootSections;
    }

    /**
     * @param list
     */
    public void setRootSections(List list) {
        rootSections = list;
    }

}