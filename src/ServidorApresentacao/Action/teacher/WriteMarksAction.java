package ServidorApresentacao.Action.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Pousão
 *  
 */
public class WriteMarksAction extends DispatchAction {
    public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        ActionErrors actionErrors = new ActionErrors();
        HashMap hashMarks = new HashMap();

        String lineReader = null;

        Integer objectCode = getObjectCode(request);
        Integer evaluationCode = getEvaluationCode(request);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("evaluationCode", evaluationCode);

        //	Read uploaded file
        DynaActionForm marksForm = (DynaActionForm) form;

        FormFile formFile = (FormFile) marksForm.get("theFile");
        if (!(formFile.getContentType().equals("text/plain"))) {
            prepareInputForward(request, session, objectCode, evaluationCode);
            actionErrors.add("FileNotExist", new ActionError("error.ficheiro.impossivelLer"));

            saveErrors(request, actionErrors);
            return mapping.findForward("viewMarksOptions");
        }

        InputStreamReader input = new InputStreamReader(formFile.getInputStream());
        BufferedReader reader = new BufferedReader(input);

        //parsing uploaded file
        int n = 0;
        String studentNumber = null;
        String mark = null;
        StringTokenizer stringTokenizer = null;

        try {
            for (lineReader = reader.readLine(); lineReader != null; lineReader = reader.readLine(), n++) {
                if ((lineReader != null) && (lineReader.length() != 0)) {
                    stringTokenizer = new StringTokenizer(lineReader);
                    try {
                        studentNumber = stringTokenizer.nextToken().trim();
                    } catch (NoSuchElementException e2) {
                        prepareInputForward(request, session, objectCode, evaluationCode);
                        actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));

                        saveErrors(request, actionErrors);
                        return mapping.findForward("viewMarksOptions");

                    }

                    try {
                        mark = stringTokenizer.nextToken().trim();

                    } catch (NoSuchElementException e1) {
                        prepareInputForward(request, session, objectCode, evaluationCode);
                        actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));

                        saveErrors(request, actionErrors);
                        return mapping.findForward("viewMarksOptions");

                    }
                    hashMarks.put(studentNumber, mark);
                }

            }
        } catch (IOException e) {
            prepareInputForward(request, session, objectCode, evaluationCode);
            actionErrors.add("FileNotExist", new ActionError("error.ficheiro.impossivelLer"));

            saveErrors(request, actionErrors);
            return mapping.findForward("viewMarksOptions");
        }

        if (n == 0) {
            prepareInputForward(request, session, objectCode, evaluationCode);
            actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));
            saveErrors(request, actionErrors);
            return mapping.findForward("viewMarksOptions");
        }
        reader.close();

        IUserView userView = SessionUtils.getUserView(request);

        Object[] args = { objectCode, evaluationCode, hashMarks };
        TeacherAdministrationSiteView siteView = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "InsertEvaluationMarks", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);

        // check for errors in service
        InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        if (infoSiteMarks.getStudentsListErrors() != null
                && infoSiteMarks.getStudentsListErrors().size() > 0) {
            ListIterator iterator = infoSiteMarks.getStudentsListErrors().listIterator();
            while (iterator.hasNext()) {

                String sNumber = (String) iterator.next();

                actionErrors.add("studentNonExistence", new ActionError("errors.student.nonExisting",
                        sNumber));
            }
            saveErrors(request, actionErrors);
        }

        if (infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0) {
            ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
            while (iterator.hasNext()) {
                InfoMark infoMark = (InfoMark) iterator.next();
                actionErrors.add("invalidMark", new ActionError("errors.invalidMark",
                        infoMark.getMark(), String.valueOf((infoMark.getInfoFrequenta().getAluno()
                                .getNumber()).intValue())));

            }
            saveErrors(request, actionErrors);

        }
        if (!actionErrors.isEmpty()) {
            return mapping.getInputForward();
        }

        return mapping.findForward("viewMarksOptions");
    }

    private void prepareInputForward(HttpServletRequest request, HttpSession session,
            Integer objectCode, Integer evaluationCode) throws FenixActionException, FenixFilterException {
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { objectCode, commonComponent, new InfoEvaluation(), null, evaluationCode, null };

        try {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                    .executeService(userView, "TeacherAdministrationSiteComponentService", args);

            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("evaluationCode", evaluationCode);
    }

    public ActionForward writeMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        ActionErrors actionErrors = new ActionErrors();

        DynaActionForm marksForm = (DynaActionForm) form;

        HashMap hashMarks = (HashMap) marksForm.get("hashMarks");

        Integer objectCode = getObjectCode(request);
        Integer evaluationCode = getEvaluationCode(request);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("evaluationCode", evaluationCode);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { objectCode, evaluationCode, hashMarks };
        TeacherAdministrationSiteView siteView = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "InsertEvaluationMarks", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);

        InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        //Check if ocurr errors in service
        if ((infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0)) {

            //list with errors Invalid Marks
            ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
            while (iterator.hasNext()) {
                InfoMark infoMark = (InfoMark) iterator.next();

                actionErrors.add("invalidMark", new ActionError("errors.invalidMark",
                        infoMark.getMark(), String.valueOf((infoMark.getInfoFrequenta().getAluno()
                                .getNumber()).intValue())));
            }
            saveErrors(request, actionErrors);
        }

        if (infoSiteMarks.getStudentsListErrors() != null
                && infoSiteMarks.getStudentsListErrors().size() > 0) {
            ListIterator iterator = infoSiteMarks.getStudentsListErrors().listIterator();
            while (iterator.hasNext()) {

                String studentNumber = (String) iterator.next();

                actionErrors.add("studentNonExistence", new ActionError("errors.student.nonExisting",
                        studentNumber));
            }
            saveErrors(request, actionErrors);
        }

        if (!actionErrors.isEmpty()) {
            return mapping.getInputForward();
        }

        return mapping.findForward("viewMarksOptions");
    }

    private Integer getEvaluationCode(HttpServletRequest request) {
        String evaluationCodeString = (String) request.getAttribute("evaluationCode");
        if (evaluationCodeString == null) {
            evaluationCodeString = request.getParameter("evaluationCode");
        }
        Integer evaluationCode = new Integer(evaluationCodeString);
        return evaluationCode;
    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = (String) request.getAttribute("objectCode");
        if (objectCodeString == null) {
            objectCodeString = request.getParameter("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }
}