package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.ReadAllInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/insertExternalPerson",
        input = "/insertExternalPerson.do?page=0&method=prepare", attribute = "insertExternalPersonForm",
        formBean = "insertExternalPersonForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "error", path = "df.page.insertExternalPerson_Error", tileProperties = @Tile(title = "teste25")),
        @Forward(name = "start", path = "df.page.insertExternalPerson", tileProperties = @Tile(title = "teste26")),
        @Forward(name = "success", path = "df.page.insertExternalPerson_Success", tileProperties = @Tile(title = "teste27")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors actionErrors = new ActionErrors();
        List institutions = getInstitutions(request);

        if ((institutions == null) || (institutions.isEmpty())) {
            actionErrors.add("label.masterDegree.administrativeOffice.nonExistingInstitutions", new ActionError(
                    "label.masterDegree.administrativeOffice.nonExistingInstitutions"));

            saveErrors(request, actionErrors);
            return mapping.findForward("error");
        }

        request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
        return mapping.findForward("start");

    }

    private List getInstitutions(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        IUserView userView = UserView.getUser();
        List institutions = null;

        try {
            institutions = (ArrayList) ReadAllInstitutions.run();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (institutions != null) {
            if (institutions.isEmpty() == false) {
                Collections.sort(institutions, new BeanComparator("name"));
                List institutionsValueBeanList = new ArrayList();
                Iterator it = institutions.iterator();
                Unit infoInstitutions = null;

                institutionsValueBeanList.add(new LabelValueBean("", ""));
                while (it.hasNext()) {
                    infoInstitutions = (Unit) it.next();
                    institutionsValueBeanList.add(new LabelValueBean(infoInstitutions.getName(), infoInstitutions.getIdInternal()
                            .toString()));
                }

                request.setAttribute(PresentationConstants.WORK_LOCATIONS_LIST, institutionsValueBeanList);
            }
        }
        return institutions;
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IUserView userView = UserView.getUser();

        DynaActionForm insertExternalPersonForm = (DynaActionForm) form;

        String name = (String) insertExternalPersonForm.get("name");
        String sex = (String) insertExternalPersonForm.get("sex");
        Integer institutionID = (Integer) insertExternalPersonForm.get("institutionID");
        String address = (String) insertExternalPersonForm.get("address");
        String phone = (String) insertExternalPersonForm.get("phone");
        String mobile = (String) insertExternalPersonForm.get("mobile");
        String homepage = (String) insertExternalPersonForm.get("homepage");
        String email = (String) insertExternalPersonForm.get("email");

        if (institutionID == 0) {
            request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                    Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));

            getInstitutions(request);

            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("label.masterDegree.administrativeOffice.institutionRequired", new ActionError(
                    "label.masterDegree.administrativeOffice.institutionRequired"));

            saveErrors(request, actionErrors);
            return mapping.findForward("start");
        }

        try {
            InsertExternalPerson.run(name, sex, address, institutionID, phone, mobile, homepage, email);
        } catch (ExistingServiceException e) {
            request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                    Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
            getInstitutions(request);
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                    Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
            getInstitutions(request);
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

}