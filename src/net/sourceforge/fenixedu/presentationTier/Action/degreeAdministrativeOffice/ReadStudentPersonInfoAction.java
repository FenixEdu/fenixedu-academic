package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.ReadPersonInfoOfStudentsAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ReadStudentPersonInfoAction extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        //		Clear the Session
        session.removeAttribute(SessionConstants.NATIONALITY_LIST_KEY);
        session.removeAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY);
        session.removeAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY);
        session.removeAttribute(SessionConstants.SEX_LIST_KEY);
        session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
        session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
        session.removeAttribute(SessionConstants.YEARS_KEY);
        session.removeAttribute(SessionConstants.EXPIRATION_YEARS_KEY);
        session.removeAttribute(SessionConstants.CANDIDATE_SITUATION_LIST);

        if (session != null) {
            IUserView userView = getUserView(request);
            Integer studentNumber = 0;
            try {
                String numberString = getFromRequest("studentNumber", request);
                if (numberString == null || numberString.equals("")) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("nonExisting", new ActionError("error.degreeAdministration.emptyField"));
                    saveErrors(request, errors);
                    return mapping.findForward("Insuccess");
                }
                studentNumber = new Integer(numberString);
            } catch (NumberFormatException numberFormatException) {
                ActionErrors errors = new ActionErrors();
                errors.add("nonExisting", new ActionError("error.exception.noStudents"));
                saveErrors(request, errors);
                return mapping.findForward("Insuccess");
            }

            InfoStudent infoStudent = null;
            Object args[] = { studentNumber, DegreeType.DEGREE };
            try {
                infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                        "ReadStudentByNumber", args);
                if (infoStudent == null || infoStudent.getIdInternal() == null) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("nonExisting", new ActionError("error.degreeAdministration.inexistentStudent"));
                    saveErrors(request, errors);
                    return mapping.findForward("Insuccess");
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoStudent == null) {
                ActionErrors errors = new ActionErrors();
                errors.add("nonExisting", new ActionError("error.exception.noStudents"));
                saveErrors(request, errors);
                return mapping.findForward("NoStudent");
            }
            request.setAttribute("infoStudent", infoStudent);
            request.setAttribute("studentNumber", studentNumber);
            request.setAttribute("personalInfo", infoStudent.getInfoPerson());

            DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
            populateForm(changeApplicationInfoForm, infoStudent.getInfoPerson());

            //			Get List of available Countries
            Object result = null;
            result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
            List country = (ArrayList) result;

            //			Build List of Countries for the Form
            Iterator iterador = country.iterator();

            List nationalityList = new ArrayList();
            while (iterador.hasNext()) {
                InfoCountry countryTemp = (InfoCountry) iterador.next();
                nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp
                        .getNationality()));
            }

            session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
            session.setAttribute(SessionConstants.SEX_LIST_KEY, Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
            session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
            session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
            session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
            session.setAttribute(SessionConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());

            return mapping.findForward("Success");
        }
        throw new Exception();
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
    	return ReadPersonInfoOfStudentsAction.getFromRequest(parameter, request);
    }

    private void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson) {
    	ReadPersonInfoOfStudentsAction.populateForm(changeApplicationInfoForm, infoPerson);
    }

}