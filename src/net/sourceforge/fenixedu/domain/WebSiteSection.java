package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteSection extends WebSiteSection_Base {

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[WEBSITESECTION";
        result += ", codInt=" + getIdInternal();
        result += ", name=" + getName();
        result += ", ftpName=" + getFtpName();
        result += ", size=" + getSize();
        result += ", sortingOrder=" + getSortingOrder();
        result += ", whatToSort=" + getWhatToSort();
        result += ", excerptSize=" + getExcerptSize();
        result += ", webSite=" + getWebSite();
        result += "]";

        return result;
    }

}
