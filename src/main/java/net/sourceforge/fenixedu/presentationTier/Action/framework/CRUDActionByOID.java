/*
 * Created on Nov 15, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.framework;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;

public abstract class CRUDActionByOID extends FenixDispatchAction {

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        deleteIt(getOIDProperty(form));
        return mapping.findForward("successfull-delete");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        InfoObject infoObject = populateInfoObjectFromForm(form);
        editIt(getOIDProperty(form), infoObject);
        return mapping.findForward("successfull-edit");
    }

    protected InfoObject populateInfoObjectFromForm(ActionForm form) throws FenixActionException {
        try {
            InfoObject infoObject = (InfoObject) Class.forName(getInfoObjectClassName()).newInstance();

            Map formPropertiesHashMap = PropertyUtils.describe(form);
            Iterator iterator = formPropertiesHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String formProperty = (String) entry.getKey();
                StringTokenizer propertyTokenizer = new StringTokenizer(formProperty, "#");

                if (propertyTokenizer.countTokens() == 1) {
                    if (PropertyUtils.isWriteable(infoObject, formProperty)) {
                        BeanUtils.copyProperty(infoObject, formProperty, entry.getValue());
                    }
                } else {
                    Object value = null;
                    while (propertyTokenizer.hasMoreElements()) {
                        String property = propertyTokenizer.nextToken();

                        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(infoObject, property);
                        if (value == null) {
                            value = propertyDescriptor.getPropertyType().newInstance();
                            if (PropertyUtils.isWriteable(infoObject, property)) {
                                PropertyUtils.setProperty(infoObject, property, value);
                            } else {
                                break;
                            }
                        } else {
                            if (PropertyUtils.isWriteable(value, property)) {
                                PropertyUtils.setProperty(value, property, entry.getValue());
                            }
                        }
                    }
                }
            }
            return infoObject;
        } catch (Exception e) {
            throw new FenixActionException(e);
        }
    }

    private String getOIDProperty(ActionForm form) {
        try {
            return BeanUtils.getProperty(form, "externalId");
        } catch (NumberFormatException e) {
        } catch (Exception e) {
        }
        return null;
    }

    private boolean hasErrors(HttpServletRequest request) {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject, ActionForm form,
            HttpServletRequest request) throws FenixActionException {
        try {

            Map formPropertiesHashMap = BeanUtils.describe(form);

            Iterator iterator = formPropertiesHashMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String formProperty = (String) entry.getKey();
                String beanProperty = formProperty.replace('#', '.');
                if (PropertyUtils.isReadable(infoObject, beanProperty)) {
                    Object value = PropertyUtils.getProperty(infoObject, beanProperty);
                    BeanUtils.copyProperty(form, formProperty, value);
                }
            }
        } catch (Exception e1) {
            throw new FenixActionException(e1);
        }
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoObject infoObject = readInfoObject(form, request);
        if (!hasErrors(request) && infoObject != null) {
            populateFormFromInfoObject(mapping, infoObject, form, request);
        }
        prepareFormConstants(mapping, form, request);
        setInfoObjectToRequest(request, infoObject);
        return mapping.findForward("show-form");
    }

    protected void prepareFormConstants(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
    }

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        InfoObject infoObject = readInfoObject(form, request);
        setInfoObjectToRequest(request, infoObject);
        return mapping.findForward("sucessfull-read");
    }

    private InfoObject readInfoObject(ActionForm form, HttpServletRequest request) throws FenixServiceException {
        User userView = Authenticate.getUser();
        String oid = getOIDProperty(form);
        InfoObject infoObject = null;
        if (oid != null) {
            infoObject = readIt(getOIDProperty(form));
        }
        return infoObject;
    }

    private void setInfoObjectToRequest(HttpServletRequest request, InfoObject infoObject) {
        if (infoObject != null) {
            request.setAttribute(getRequestAttribute(), infoObject);
        }
    }

    protected abstract InfoObject readIt(String externalId) throws NotAuthorizedException;

    protected abstract String getRequestAttribute();

    protected abstract void deleteIt(String externalId) throws NotAuthorizedException;

    protected abstract void editIt(String externalId, InfoObject bean) throws NotAuthorizedException, FenixServiceException;

    protected abstract String getInfoObjectClassName();
}