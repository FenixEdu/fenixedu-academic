package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteSection extends WebSiteSection_Base {

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IWebSiteSection) {
            IWebSiteSection webSiteSection = (IWebSiteSection) arg0;

            if (((webSiteSection.getName() == null && this.getName() == null) || (webSiteSection
                    .getName() != null
                    && this.getName() != null && webSiteSection.getName().equals(this.getName())))
                    && ((webSiteSection.getExcerptSize() == null && this.getExcerptSize() == null) || (webSiteSection
                            .getExcerptSize() != null
                            && this.getExcerptSize() != null && webSiteSection.getExcerptSize().equals(
                            this.getExcerptSize())))
                    && ((webSiteSection.getSortingOrder() == null && this.getSortingOrder() == null) || (webSiteSection
                            .getSortingOrder() != null
                            && this.getSortingOrder() != null && webSiteSection.getSortingOrder()
                            .equals(this.getSortingOrder())))
                    && ((webSiteSection.getWhatToSort() == null && this.getWhatToSort() == null) || (webSiteSection
                            .getWhatToSort() != null
                            && this.getWhatToSort() != null && webSiteSection.getWhatToSort().equals(
                            this.getWhatToSort())))
                    && ((webSiteSection.getWebSite() == null && this.getWebSite() == null) || (webSiteSection
                            .getWebSite() != null
                            && this.getWebSite() != null && webSiteSection.getWebSite().equals(
                            this.getWebSite())))
                    && ((webSiteSection.getSize() == null && this.getSize() == null) || (webSiteSection
                            .getSize() != null
                            && this.getSize() != null && webSiteSection.getSize().equals(this.getSize())))) {
                result = true;
            }
        }
        return result;
    }

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