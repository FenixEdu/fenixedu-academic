/*
 * Created on Nov 15, 2003 by jpvl
 *
 */
package ServidorApresentacao.mapping.framework;

import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class CRUDMapping extends ActionMapping {
    /**
     * Represents the form property that identify the form property that we
     */
    private String oidProperty = "objectOID";

    private String requestAttribute;

    private String editService;

    private String readService;

    private String deleteService;

    private String infoObjectClassName;

    /**
     * @return Returns the oidProperty.
     */
    public String getOidProperty() {
        return this.oidProperty;
    }

    /**
     * @param oidProperty
     *            The oidProperty to set.
     */
    public void setOidProperty(String oidProperty) {
        this.oidProperty = oidProperty;
    }

    /**
     * @return Returns the requestAttribute.
     */
    public String getRequestAttribute() {
        return this.requestAttribute;
    }

    /**
     * @param requestAttribute
     *            The requestAttribute to set.
     */
    public void setRequestAttribute(String requestAttribute) {
        this.requestAttribute = requestAttribute;
    }

    /**
     * @return Returns the deleteService.
     */
    public String getDeleteService() {
        return this.deleteService;
    }

    /**
     * @param deleteService
     *            The deleteService to set.
     */
    public void setDeleteService(String deleteService) {
        this.deleteService = deleteService;
    }

    /**
     * @return Returns the editService.
     */
    public String getEditService() {
        return this.editService;
    }

    /**
     * @param editService
     *            The editService to set.
     */
    public void setEditService(String editService) {
        this.editService = editService;
    }

    /**
     * @return Returns the readService.
     */
    public String getReadService() {
        return this.readService;
    }

    /**
     * @param readService
     *            The readService to set.
     */
    public void setReadService(String readService) {
        this.readService = readService;
    }

    /**
     * @return Returns the infoObjectClassName.
     */
    public String getInfoObjectClassName() {
        return this.infoObjectClassName;
    }

    /**
     * @param infoObjectClassName
     *            The infoObjectClassName to set.
     */
    public void setInfoObjectClassName(String infoObjectClassName) {
        this.infoObjectClassName = infoObjectClassName;
    }

}