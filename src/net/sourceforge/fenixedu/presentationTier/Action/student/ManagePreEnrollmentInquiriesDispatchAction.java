/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDislocatedStudent;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManagePreEnrollmentInquiriesDispatchAction extends FenixDispatchAction {

    public ActionForward prepareDislocatedStudentInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        DynaActionForm studentDataInquiryForm = (DynaActionForm) form;
        studentDataInquiryForm.set("studentID", infoStudent.getIdInternal());

        InfoDislocatedStudent infoDislocatedStudent = (InfoDislocatedStudent) ServiceManagerServiceFactory
                .executeService(userView, "ReadDislocatedStudentByStudentID", new Object[] { infoStudent
                        .getIdInternal() });

        if (infoDislocatedStudent != null) {
            return mapping.findForward("proceedToPersonalDataInquiry");
        }

        final List infoCountries = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAllCountries", null);

        List uniqueInfoCountries = new ArrayList();
        Integer defaultCountryID = null;
        for (Iterator iter = infoCountries.iterator(); iter.hasNext();) {
            InfoCountry infoCountry = (InfoCountry) iter.next();
            if (!containsCountry(uniqueInfoCountries, infoCountry)) {
                uniqueInfoCountries.add(infoCountry);
                if (infoCountry.getName().equalsIgnoreCase("PORTUGAL")) {
                    defaultCountryID = infoCountry.getIdInternal();
                }
            }
        }
        Collections.sort(uniqueInfoCountries, new BeanComparator("name"));
        studentDataInquiryForm.set("countryID", defaultCountryID);
        request.setAttribute("infoCountries", uniqueInfoCountries);

        List infoDistricts = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAllDistricts", null);
        request.setAttribute("infoDistricts", infoDistricts);

        if (studentDataInquiryForm.get("dislocatedCountryID") != null) {
            Integer dislocatedCountryID = (Integer) studentDataInquiryForm.get("dislocatedCountryID");
            if (dislocatedCountryID.equals(defaultCountryID)) {
                request.setAttribute("portugal", "portugal");
            }
        }

        if (studentDataInquiryForm.get("dislocatedAnswer") != null) {
            String dislocatedAnswer = (String) studentDataInquiryForm.get("dislocatedAnswer");
            if (dislocatedAnswer.equalsIgnoreCase("true")) {
                request.setAttribute("dislocated", "dislocated");
            }
        }

        return mapping.findForward("showDislocatedStudentInquiry");
    }

    public ActionForward registerDislocatedStudentInquiryAnswers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        DynaActionForm studentDataInquiryForm = (DynaActionForm) form;
        Integer countryID = (Integer) studentDataInquiryForm.get("countryID");
        String dislocatedStudent = (String) studentDataInquiryForm.get("dislocatedAnswer");
        Integer districtID = null;
        Integer dislocatedCountryID = null;
        if (dislocatedStudent != null && dislocatedStudent.equalsIgnoreCase("true")) {
            dislocatedCountryID = (Integer) studentDataInquiryForm.get("dislocatedCountryID");
            districtID = (Integer) studentDataInquiryForm.get("districtID");
        }

        Object[] args = { infoStudent.getIdInternal(), countryID, dislocatedCountryID, districtID };
        ServiceManagerServiceFactory.executeService(userView, "WriteDislocatedStudentAnswer", args);

        return mapping.findForward("proceedToPersonalDataInquiry");
    }

    public ActionForward preparePersonalDataInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        Object[] args = { infoStudent.getIdInternal() };
        StudentPersonalDataAuthorizationChoice spdaChoice = (StudentPersonalDataAuthorizationChoice) ServiceManagerServiceFactory
                .executeService(userView, "ReadActualPersonalDataAuthorizationAnswer", args);

        if (spdaChoice != null) {
            return mapping.findForward("proceedToEnrollment");
        }

        return mapping.findForward("showAuthorizationInquiry");
    }

    public ActionForward registerPersonalDataInquiryAnswer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        DynaActionForm inquiryForm = (DynaActionForm) form;
        String answer = (String) inquiryForm.get("authorizationAnswer");
        if (answer == null || answer.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.enrollment.personalInquiry.mandatory"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        Object[] args = { infoStudent.getIdInternal(), answer };
        ServiceManagerServiceFactory.executeService(userView,
                "WriteStudentPersonalDataAuthorizationAnswer", args);

        return mapping.findForward("proceedToEnrollment");
    }

    private boolean containsCountry(List infoCountries, final InfoCountry country) {
        return CollectionUtils.exists(infoCountries, new Predicate() {

            public boolean evaluate(Object arg0) {
                InfoCountry infoCountry = (InfoCountry) arg0;
                return infoCountry.getName().equalsIgnoreCase(country.getName());
            }
        });
    }
}
