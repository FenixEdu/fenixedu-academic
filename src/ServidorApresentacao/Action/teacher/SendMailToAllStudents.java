/*
 * Created on 17/Set/2003, 15:19:24
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.teacher;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoPerson;
import DataBeans.InfoSite;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyDetails;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 17/Set/2003, 15:19:24
 * 
 */
public class SendMailToAllStudents extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = this.getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        TeacherAdministrationSiteView siteView = null;
        Integer objectCode = null;
        // Integer shiftID = null;
        // try
        // {
        // shiftID = new Integer(request.getParameter("shiftID"));
        // } catch (NumberFormatException ex)
        // {
        // //ok, we don't want to view a shift's student list
        // }
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        objectCode = new Integer(objectCodeString);
        Object argsReadSiteView[] = { objectCode, null };
        Object argsReadExecutionCourse[] = { objectCode };
        InfoExecutionCourse infoExecutionCourse = null;
        InfoSite infoSite = null;

        siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentsByCurricularCourse", argsReadSiteView);
        infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
                userView, "ReadExecutionCourseByOID", argsReadExecutionCourse);
        Object argsReadSite[] = { infoExecutionCourse };
        infoSite = (InfoSite) ServiceManagerServiceFactory.executeService(userView, "ReadSite",
                argsReadSite);

        DynaActionForm sendMailForm = (DynaActionForm) form;
        sendMailForm.set("from", infoSite.getMail());
        sendMailForm.set("fromName", infoSite.getInfoExecutionCourse().getNome());
        sendMailForm.set("text", "");
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showEmailForm");
    }

    public ActionForward prepareCandidaciesSend(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixServiceException, FenixFilterException {
        HttpSession session = this.getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        TeacherAdministrationSiteView siteView = null;
        Object argsReadPerson[] = { userView.getUtilizador() };
        InfoPerson infoPerson = null;

        infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                "ReadPersonByUsername", argsReadPerson);

        DynaActionForm sendMailForm = (DynaActionForm) form;
        sendMailForm.set("from", infoPerson.getEmail());
        sendMailForm.set("fromName", infoPerson.getNome());
        // String subject = request.getParameter("subject");
        sendMailForm.set("text", "");
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showEmailForm");
    }

    Object[] getReadCandidaciesArgs(HttpServletRequest request) {
        Integer modalityID;
        Integer themeID;
        Integer case1Id;
        Integer case2Id;
        Integer case3Id;
        Integer case4Id;
        Integer case5Id;
        Integer curricularCourseID;
        Integer degreeID;
        Integer seminaryID;
        Boolean approved = null;
        //
        //
        String stringApproved = request.getParameter("approved");
        if (stringApproved != null && (stringApproved.equals("true") || stringApproved.equals("false")))
            approved = new Boolean(stringApproved);
        //
        //        
        try {
            themeID = new Integer(request.getParameter("themeID"));
        } catch (NumberFormatException ex) {
            themeID = new Integer(-1);
        }
        try {
            modalityID = new Integer(request.getParameter("modalityID"));
        } catch (NumberFormatException ex) {
            modalityID = new Integer(-1);
        }
        try {
            seminaryID = new Integer(request.getParameter("seminaryID"));
        } catch (NumberFormatException ex) {
            seminaryID = new Integer(-1);
        }
        try {
            case1Id = new Integer(request.getParameter("case1ID"));
        } catch (NumberFormatException ex) {
            case1Id = new Integer(-1);
        }
        try {
            case2Id = new Integer(request.getParameter("case2ID"));
        } catch (NumberFormatException ex) {
            case2Id = new Integer(-1);
        }
        try {
            case3Id = new Integer(request.getParameter("case3ID"));
        } catch (NumberFormatException ex) {
            case3Id = new Integer(-1);
        }
        try {
            case4Id = new Integer(request.getParameter("case4ID"));
        } catch (NumberFormatException ex) {
            case4Id = new Integer(-1);
        }
        try {
            case5Id = new Integer(request.getParameter("case5ID"));
        } catch (NumberFormatException ex) {
            case5Id = new Integer(-1);
        }
        try {
            curricularCourseID = new Integer(request.getParameter("courseID"));
        } catch (NumberFormatException ex) {
            curricularCourseID = new Integer(-1);
        }
        try {
            degreeID = new Integer(request.getParameter("degreeID"));
        } catch (NumberFormatException ex) {
            degreeID = new Integer(-1);
        }
        Object[] arguments = { modalityID, seminaryID, themeID, case1Id, case2Id, case3Id, case4Id,
                case5Id, curricularCourseID, degreeID, approved };
        return arguments;
    }

    public ActionForward sendMailCandidacies(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        //
        String from = request.getParameter("from");
        String fromName = request.getParameter("fromName");
        String text = request.getParameter("text");
        String subject = request.getParameter("subject");
        List candidacies = new LinkedList();
        List candidaciesExtendedInfo = new LinkedList();
        List failedEmails = null;
        try {
            Object[] argsReadCandidacies = getReadCandidaciesArgs(request);
            candidacies = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.ReadCandidacies", argsReadCandidacies);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
                InfoStudent student = null;
                InfoCurricularCourse curricularCourse = null;
                InfoTheme theme = null;
                InfoModality modality = null;
                // String motivation = null;
                InfoSeminary seminary = null;
                List casesChoices = null;
                List cases = new LinkedList();
                InfoCandidacy candidacy = (InfoCandidacy) iterator.next();

                student = candidacy.getInfoStudent();
                curricularCourse = candidacy.getCurricularCourse();
                theme = candidacy.getTheme();
                modality = candidacy.getInfoModality();
                seminary = candidacy.getInfoSeminary();
                // motivation = candidacy.getMotivation();
                casesChoices = candidacy.getCaseStudyChoices();
                //
                for (Iterator casesIterator = casesChoices.iterator(); casesIterator.hasNext();) {
                    InfoCaseStudyChoice choice = (InfoCaseStudyChoice) casesIterator.next();
                    InfoCaseStudy infoCaseStudy = choice.getCaseStudy();
                    cases.add(infoCaseStudy);
                }
                //   
                InfoCandidacyDetails infoCandidacyDetails = new InfoCandidacyDetails();
                infoCandidacyDetails.setCases(cases);
                infoCandidacyDetails.setIdInternal(candidacy.getIdInternal());
                infoCandidacyDetails.setModality(modality);
                infoCandidacyDetails.setSeminary(seminary);
                infoCandidacyDetails.setStudent(student);
                infoCandidacyDetails.setTheme(theme);
                infoCandidacyDetails.setMotivation(candidacy.getMotivation());
                infoCandidacyDetails.setCurricularCourse(curricularCourse);
                candidaciesExtendedInfo.add(infoCandidacyDetails);
                //
                //
            }
            // and finnaly, let us send the emaaaaaaaails !
            List toList = new LinkedList();
            List bccList = new LinkedList();
            toList.add(from);
            List ccList = new LinkedList();
            for (Iterator iter = candidaciesExtendedInfo.iterator(); iter.hasNext();) {
                InfoStudent infoStudent = ((InfoCandidacyDetails) iter.next()).getStudent();
                bccList.add(infoStudent.getInfoPerson().getEmail());
            }
            Object[] argsSendMails = { toList, ccList, bccList, fromName, from, subject, text };
            failedEmails = (List) ServiceManagerServiceFactory.executeService(userView,
                    "commons.SendMail", argsSendMails);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        getFailedMails(request, candidaciesExtendedInfo, failedEmails);
        return mapping.findForward("mailCandidaciesSent");
    }

    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException,
            FenixFilterException {
        HttpSession session = this.getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = null;
        Integer shiftID = null;
        Integer groupCode = null;
        String from = request.getParameter("from");
        String fromName = request.getParameter("fromName");
        String text = request.getParameter("text");
        String subject = request.getParameter("subject");
        if (request.getParameter("candidaciesSend") != null
                && request.getParameter("candidaciesSend").equals("true")) {
            return this.sendMailCandidacies(mapping, form, request, response);
        }

        try {
            groupCode = new Integer(request.getParameter("studentGroupCode"));
        } catch (NumberFormatException ex) {
            // ok, we don't want to view a group's student list
        }
        try {
            shiftID = new Integer(request.getParameter("shiftCode"));
        } catch (NumberFormatException ex) {
            // ok, we don't want to view a shift's student list
        }
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        objectCode = new Integer(objectCodeString);
        Object args[] = { objectCode, null };
        TeacherAdministrationSiteView siteView = null;
        InfoSiteStudents infoSiteStudents = null;
        List groupStudents = null;
        List shiftStudents = null;
        List failedEmails = null;

        siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentsByCurricularCourse", args);
        infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
        if (shiftID != null) {
            // the objectCode is needed by the filter...doing this is
            // awfull !!!
            // please read
            // http://www.dcc.unicamp.br/~oliva/fun/prog/resign-patterns
            Object[] argsReadShiftStudents = { objectCode, shiftID };
            shiftStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                    "teacher.ReadStudentsByShiftID", argsReadShiftStudents);
            infoSiteStudents.setStudents(shiftStudents);
        }
        if (groupCode != null) {
            Object[] argsReadGroupStudents = { objectCode, groupCode };
            groupStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                    "teacher.ReadStudentsByStudentGroupID", argsReadGroupStudents);
            infoSiteStudents.setStudents(groupStudents);
        }
        Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
        //
        // and finnaly, let us send the emaaaaaaaails !
        //
        List bccList = new LinkedList();
        List toList = new LinkedList();
        toList.add(from);
        List ccList = new LinkedList();
        for (Iterator iter = infoSiteStudents.getStudents().iterator(); iter.hasNext();) {
            InfoStudent infoStudent = (InfoStudent) iter.next();
            bccList.add(infoStudent.getInfoPerson().getEmail());
        }
        Object[] argsSendMails = { toList, ccList, bccList, fromName, from, subject, text };
        failedEmails = (List) ServiceManagerServiceFactory.executeService(userView, "commons.SendMail",
                argsSendMails);

        getFailedMails(request, infoSiteStudents.getStudents(), failedEmails);
        return mapping.findForward("mailSent");

    }

    /**
     * @param request
     * @param infoSiteStudents
     * @param failedEmails
     */
    private void getFailedMails(HttpServletRequest request, List mailsList, List failedEmails) {
        ActionErrors actionErrors = new ActionErrors();
        for (Iterator iter = failedEmails.iterator(); iter.hasNext();) {
            String to = "";
            Object object = iter.next();
            if (object instanceof String) {
                to = (String) object;
            }
            ActionError actionError = new ActionError("error.email.notSend", to);
            actionErrors.add("error.seminaries.candidaciesLimitReached", actionError);
        }
        if (failedEmails != null && failedEmails.size() > 0
                && failedEmails.size() != mailsList.size() + 1) {
            actionErrors.add("error.remaining.success", new ActionError("error.email.remained.success"));
        }
        saveErrors(request, actionErrors);
    }
}