package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Fernanda Quitério 24/09/2003
 *  
 */
public class InfoWebSiteSection extends InfoObject {
    private String name;

    private String ftpName;

    private Integer size;

    private String sortingOrder;

    private String whatToSort;

    private Integer excerptSize;

    private InfoWebSite infoWebSite;

    private List infoItemsList;

    /**
     * @return
     */
    public String getWhatToSort() {
        return whatToSort;
    }

    /**
     * @param whatToSort
     */
    public void setWhatToSort(String whatToSort) {
        this.whatToSort = whatToSort;
    }

    /**
     * @return
     */
    public List getInfoItemsList() {
        return infoItemsList;
    }

    /**
     * @param itemsList
     */
    public void setInfoItemsList(List itemsList) {
        this.infoItemsList = itemsList;
    }

    /**
     * Construtor
     */

    public InfoWebSiteSection() {

    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOWEBSITESECTION";
        result += ", codInt=" + getIdInternal();
        result += ", name=" + getName();
        result += ", ftpName=" + getFtpName();
        result += ", size=" + getSize();
        result += ", sortingOrder=" + getSortingOrder();
        result += ", whatToSort=" + getWhatToSort();
        result += ", excerptSize=" + getExcerptSize();
        result += ", webSite=" + getInfoWebSite();
        result += "]";

        return result;
    }

    /**
     * @return
     */
    public Integer getExcerptSize() {
        return excerptSize;
    }

    /**
     * @param excerptSize
     */
    public void setExcerptSize(Integer excerptSize) {
        this.excerptSize = excerptSize;
    }

    /**
     * @return
     */
    public InfoWebSite getInfoWebSite() {
        return infoWebSite;
    }

    /**
     * @param infoWebSite
     */
    public void setInfoWebSite(InfoWebSite infoWebSite) {
        this.infoWebSite = infoWebSite;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getFtpName() {
        return ftpName;
    }

    /**
     * @param name
     */
    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    /**
     * @return
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return
     */
    public String getSortingOrder() {
        return sortingOrder;
    }

    /**
     * @param sortingOrder
     */
    public void setSortingOrder(String sortingOrder) {
        this.sortingOrder = sortingOrder;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoWebSiteSection) {
            InfoWebSiteSection webSiteSection = (InfoWebSiteSection) arg0;

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
                    && ((webSiteSection.getInfoWebSite() == null && this.getInfoWebSite() == null) || (webSiteSection
                            .getInfoWebSite() != null
                            && this.getInfoWebSite() != null && webSiteSection.getInfoWebSite().equals(
                            this.getInfoWebSite())))
                    && (CollectionUtils.isEqualCollection(webSiteSection.getInfoItemsList(), this
                            .getInfoItemsList()))
                    && ((webSiteSection.getSize() == null && this.getSize() == null) || (webSiteSection
                            .getSize() != null
                            && this.getSize() != null && webSiteSection.getSize().equals(this.getSize())))) {
                result = true;
            }
        }
        return result;
    }
}