package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class MasterDegreeThesisOperations extends FenixDispatchAction {

    public boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request,
            ActionErrors actionErrors) throws FenixActionException, FenixFilterException {

        boolean result = false;

        DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        String degreeType = this.getFromRequest("degreeType", request);
        Integer studentNumber = Integer.valueOf(this.getFromRequest("studentNumber", request));

        if ((degreeType == null) || (studentNumber == null)) {

            try {
                degreeType = (String) getStudentByNumberAndDegreeTypeForm.get("degreeType");
                studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm
                        .get("studentNumber"));
            } catch (NumberFormatException e) {
                degreeType = (String) request.getAttribute("degreeType");
                studentNumber = (Integer) request.getAttribute("studentNumber");
                getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType);
                getStudentByNumberAndDegreeTypeForm.set("studentNumber", studentNumber.toString());
            }
        }

        InfoStudent infoStudent = null;

        Object args[] = { studentNumber, DegreeType.valueOf(degreeType) };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView,
                    "ReadStudentByNumberAndDegreeType", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            actionErrors.add("error.no.student.in.database", new ActionError(
                    "error.no.student.in.database", studentNumber.toString()));
            result = false;
        } else {
            request.setAttribute(SessionConstants.STUDENT, infoStudent);
            result = true;
        }

        return result;
    }

    public List<InfoTeacher> getTeachersByNumbers(ActionForm form, HttpServletRequest request,
            String teachersNumbersListField, String sessionConstant, ActionErrors actionErrors)
            throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        List<Integer> teachersNumbersList = getTeachersNumbers(form, teachersNumbersListField);

        List<InfoTeacher> infoTeachers = null;
        Object args[] = { teachersNumbersList };
        try {
            infoTeachers = (List<InfoTeacher>) ServiceUtils.executeService(userView,
                    "ReadTeachersByNumbers", args);
        } catch (FenixServiceException e) {
            // actionErrors.add("error.no.teacher.in.database", new ActionError(
            // "error.no.teacher.in.database", teacherNumber.toString()));
            throw new FenixActionException(e);
        }

        if (!infoTeachers.isEmpty()) {
            request.setAttribute(sessionConstant, infoTeachers);
        }

        return infoTeachers;

    }

    public List<Integer> getTeachersNumbers(ActionForm form, String teachersNumbersListField) {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

        Integer[] teachersNumbersArray = (Integer[]) masterDegreeThesisForm
                .get(teachersNumbersListField);
        List<Integer> teachersNumbersList = CollectionUtils.toList(teachersNumbersArray);
        teachersNumbersList.remove(new Integer(0));
        return teachersNumbersList;

    }

    public void getExternalPersonsByName(ActionForm form, HttpServletRequest request,
            String externalPersonNameField, String sessionConstant, ActionErrors actionErrors)
            throws FenixActionException, FenixFilterException {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        String externalAssistentGuiderName = (String) masterDegreeThesisForm
                .get(externalPersonNameField);

        List infoExternalPersonsList = null;
        Object args[] = { externalAssistentGuiderName };

        try {
            if (!externalAssistentGuiderName.equals(""))
                infoExternalPersonsList = (ArrayList) ServiceUtils.executeService(userView,
                        "SearchExternalPersonsByName", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExternalPersonsList != null)
            if (infoExternalPersonsList.isEmpty() == false)
                request.setAttribute(sessionConstant, infoExternalPersonsList);

        if ((infoExternalPersonsList == null) || (infoExternalPersonsList.isEmpty()))
            actionErrors.add("label.masterDegree.administrativeOffice.searchResultsEmpty",
                    new ActionError("label.masterDegree.administrativeOffice.searchResultsEmpty"));

    }

    public List<Integer> getExternalPersonsIDs(ActionForm form, String externalPersonNameField) {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;

        Integer[] externalPersonsIDsArray = (Integer[]) masterDegreeThesisForm
                .get(externalPersonNameField);
        List<Integer> externalPersonsIDsList = CollectionUtils.toList(externalPersonsIDsArray);
        externalPersonsIDsList.remove(new Integer(0));
        return externalPersonsIDsList;

    }

    public List<InfoExternalPerson> getExternalPersonsByIDs(ActionForm form, HttpServletRequest request,
            String externalPersonsIDsListField, String sessionConstant, ActionErrors actionErrors)
            throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        List<Integer> externalPersonsIDsList = getExternalPersonsIDs(form, externalPersonsIDsListField);

        Object args[] = { externalPersonsIDsList };
        List<InfoExternalPerson> infoExternalPersonsList = null;

        try {
            infoExternalPersonsList = (List<InfoExternalPerson>) ServiceUtils.executeService(userView,
                    "ReadExternalPersonsByIDs", args);
        } catch (FenixServiceException e) {
            // actionErrors.add(e.getMessage(), new ActionError(e.getMessage(),
            // externalPersonID
            // .toString()));
            throw new FenixActionException(e);
        }

        if (!infoExternalPersonsList.isEmpty()) {
            request.setAttribute(sessionConstant, infoExternalPersonsList);
        }

        return infoExternalPersonsList;

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}