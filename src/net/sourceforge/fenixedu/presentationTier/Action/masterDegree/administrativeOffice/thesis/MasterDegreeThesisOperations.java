package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */

public class MasterDegreeThesisOperations extends DispatchAction {

    public boolean getStudentByNumberAndDegreeType(ActionForm form, HttpServletRequest request,
            ActionErrors actionErrors) throws FenixActionException, FenixFilterException {

        boolean result = false;

        DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeType = Integer.valueOf(this.getFromRequest("degreeType", request));
        Integer studentNumber = Integer.valueOf(this.getFromRequest("studentNumber", request));

        if ((degreeType == null) || (studentNumber == null)) {

            try {
                degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
                studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm
                        .get("studentNumber"));
            } catch (NumberFormatException e) {
                degreeType = (Integer) request.getAttribute("degreeType");
                studentNumber = (Integer) request.getAttribute("studentNumber");
                getStudentByNumberAndDegreeTypeForm.set("degreeType", degreeType.toString());
                getStudentByNumberAndDegreeTypeForm.set("studentNumber", studentNumber.toString());
            }
        }

        InfoStudent infoStudent = null;

        Object args[] = { studentNumber, new TipoCurso(degreeType) };
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

    public List getTeachersByNumbers(ActionForm form, HttpServletRequest request,
            String teachersNumbersListField, String sessionConstant, ActionErrors actionErrors)
            throws FenixActionException, FenixFilterException {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer[] teachersNumbersList = (Integer[]) masterDegreeThesisForm.get(teachersNumbersListField);

        List infoTeachersList = new ArrayList();

        InfoTeacher infoTeacher = null;
        Object args[] = new Object[1];
        Integer teacherNumber = null;

        for (int i = 0; i < teachersNumbersList.length; i++) {

            teacherNumber = teachersNumbersList[i];

            if (teacherNumber.intValue() == 0)
                continue;

            args[0] = teacherNumber;

            try {
                infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
                        args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoTeacher == null)
                actionErrors.add("error.no.teacher.in.database", new ActionError(
                        "error.no.teacher.in.database", teacherNumber.toString()));
            else if (infoTeachersList.contains(infoTeacher) == false) {
                infoTeachersList.add(infoTeacher);
            }

        }

        if (infoTeachersList.isEmpty() == false)
            request.setAttribute(sessionConstant, infoTeachersList);

        return infoTeachersList;

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

    public List getExternalPersonsByIDs(ActionForm form, HttpServletRequest request,
            String externalPersonsIDsListField, String sessionConstant, ActionErrors actionErrors)
            throws FenixActionException, FenixFilterException {

        DynaActionForm masterDegreeThesisForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer[] externalPersonsIDsList = (Integer[]) masterDegreeThesisForm
                .get(externalPersonsIDsListField);

        List infoExternalPersonsList = new ArrayList();

        InfoExternalPerson infoExternalPerson = null;
        Object args[] = new Object[1];
        Integer externalPersonID = null;

        for (int i = 0; i < externalPersonsIDsList.length; i++) {

            externalPersonID = externalPersonsIDsList[i];
            args[0] = externalPersonID;

            try {
                infoExternalPerson = (InfoExternalPerson) ServiceUtils.executeService(userView,
                        "ReadExternalPersonByID", args);
            } catch (NonExistingServiceException e) {
                actionErrors.add(e.getMessage(), new ActionError(e.getMessage(), externalPersonID
                        .toString()));
                continue;
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoExternalPersonsList.contains(infoExternalPerson) == false) {
                infoExternalPersonsList.add(infoExternalPerson);
            }

        }

        if (infoExternalPersonsList.isEmpty() == false)
            request.setAttribute(sessionConstant, infoExternalPersonsList);

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