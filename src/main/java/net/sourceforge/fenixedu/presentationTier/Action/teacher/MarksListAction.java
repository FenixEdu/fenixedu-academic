package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PublishMarks;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadStudentsAndMarksByEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherAdministrationSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarksListAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(MarksListAction.class);

    public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = getUserView(request);

        String executionCourseCode = getFromRequest("objectCode", request);

        String evaluationCode = getFromRequest("evaluationCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();

        try {
            TeacherAdministrationSiteView siteView =
                    TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(executionCourseCode,
                            commonComponent, new InfoEvaluation(), evaluationCode, null);

            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                    .getExternalId());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("loadMarks");

    }

    public ActionForward loadMarksOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String executionCourseCode = getFromRequest("objectCode", request);

        String evaluationCode = getFromRequest("evaluationCode", request);

        TeacherAdministrationSiteView siteView = null;

        try {
            siteView =
                    (TeacherAdministrationSiteView) ReadStudentsAndMarksByEvaluation.runReadStudentsAndMarksByEvaluation(
                            executionCourseCode, evaluationCode);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        Collections.sort(infoSiteMarks.getInfoAttends(), new BeanComparator("aluno.number"));

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("marksList");
    }

    public ActionForward preparePublishMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String evaluationCode = getFromRequest("evaluationCode", request);

        String infoExecutionCourseCode = getFromRequest("objectCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        User userView = getUserView(request);
        TeacherAdministrationSiteView siteView = null;
        try {
            siteView =
                    TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(
                            infoExecutionCourseCode, commonComponent, new InfoEvaluation(), evaluationCode, null);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", infoExecutionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("preparePublishMarks");
    }

    public ActionForward publishMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String evaluationCode = getFromRequest("evaluationCode", request);

        String objectCode = getFromRequest("objectCode", request);

        DynaValidatorForm publishForm = (DynaValidatorForm) form;
        String publishmentMessage = (String) publishForm.get("publishmentMessage");
        Boolean sendSMS = (Boolean) publishForm.get("sendSMS");

        String announcementTitle = null;
        if (publishmentMessage != null && publishmentMessage.length() > 0) {
            MessageResources messages = getResources(request, "DEFAULT");
            announcementTitle = messages.getMessage("message.publishment");
        }

        User userView = getUserView(request);
        try {
            PublishMarks.runPublishMarks(objectCode, evaluationCode, publishmentMessage, sendSMS, announcementTitle);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        request.setAttribute("objectCode", objectCode);

        return mapping.findForward("viewMarksOptions");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = request.getAttribute(parameter).toString();
        }
        return parameterCodeString;
    }
}