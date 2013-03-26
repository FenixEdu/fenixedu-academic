/*
 * Created on Nov 18, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.mapping.framework;

import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class SearchActionMapping extends ActionMapping {
    private String serviceName;

    private String objectAttribute;

    private String listAttribute;

    private String notFoundMessageKey;

    private String defaultSortBy;

    /**
     * @return Returns the listAttribute.
     */
    public String getListAttribute() {
        return this.listAttribute;
    }

    /**
     * @param listAttribute
     *            The listAttribute to set.
     */
    public void setListAttribute(String listAttribute) {
        this.listAttribute = listAttribute;
    }

    /**
     * @return Returns the objectAttribute.
     */
    public String getObjectAttribute() {
        return this.objectAttribute;
    }

    /**
     * @param objectAttribute
     *            The objectAttribute to set.
     */
    public void setObjectAttribute(String objectAttribute) {
        this.objectAttribute = objectAttribute;
    }

    /**
     * @return Returns the serviceName.
     */
    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * @param serviceName
     *            The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return Returns the notFoundMessageKey.
     */
    public String getNotFoundMessageKey() {
        return this.notFoundMessageKey;
    }

    /**
     * @param notFoundMessageKey
     *            The notFoundMessageKey to set.
     */
    public void setNotFoundMessageKey(String notFoundMessageKey) {
        this.notFoundMessageKey = notFoundMessageKey;
    }

    /**
     * @return Returns the defaultSortBy.
     */
    public String getDefaultSortBy() {
        return this.defaultSortBy;
    }

    /**
     * @param defaultSortBy
     *            The defaultSortBy to set.
     */
    public void setDefaultSortBy(String defaultSortBy) {
        this.defaultSortBy = defaultSortBy;
    }
}