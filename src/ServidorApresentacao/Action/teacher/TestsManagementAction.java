/*
 * Created on 14/Ago/2003
 */

package ServidorApresentacao.Action.teacher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
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
import org.apache.util.Base64;

import DataBeans.InfoSiteDistributedTest;
import DataBeans.InfoSiteStudentTestFeedback;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoSiteTest;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentTestLog;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.tests.CardinalityType;
import Util.tests.CorrectionAvailability;
import Util.tests.CorrectionFormula;
import Util.tests.QuestionType;
import Util.tests.Response;
import Util.tests.ResponseLID;
import Util.tests.ResponseNUM;
import Util.tests.ResponseSTR;
import Util.tests.TestQuestionChangesType;
import Util.tests.TestQuestionStudentsChangesType;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class TestsManagementAction extends FenixDispatchAction {

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        return mapping.findForward("testsFirstPage");
    }

    public ActionForward prepareCreateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer availableMetadatas = null;
        try {
            Object[] args = { executionCourseId };
            availableMetadatas = (Integer) ServiceUtils.executeService(userView,
                    "CountMetadatasByExecutionCourse", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("availableMetadatas", availableMetadatas);
        request.setAttribute("objectCode", executionCourseId);
        return mapping.findForward("createTest");
    }

    public ActionForward createTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String title = request.getParameter("title");
        String information = request.getParameter("information");
        Object[] args = { executionCourseId, title, information };
        Integer testCode = null;
        try {
            testCode = (Integer) ServiceUtils.executeService(userView, "InsertTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward editAsNewTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer testCode = getCodeFromRequest(request, "testCode");
        Object[] args = { executionCourseId, testCode };
        Integer newTestCode = null;
        try {
            newTestCode = (Integer) ServiceUtils.executeService(userView, "InsertTestAsNewTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", newTestCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward showAvailableQuestions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        SiteView siteView = null;
        Integer testCode = getCodeFromRequest(request, "testCode");
        String order = request.getParameter("order");
        String asc = request.getParameter("asc");
        try {
            Object[] args = { executionCourseId, testCode, order, asc };
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadMetadatasByTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("testCode", testCode);
        request.setAttribute("order", order);
        request.setAttribute("asc", asc);
        return mapping.findForward("showAvailableQuestions");
    }

    public ActionForward prepareInsertTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        Integer metadataCode = getCodeFromRequest(request, "metadataCode");
        String exerciseIdString = request.getParameter("exerciseCode");
        Integer exerciseCode = null;
        if (exerciseIdString == null)
            exerciseCode = new Integer(-1);
        else
            exerciseCode = new Integer(exerciseIdString);
        SiteView siteView = null;
        String path = getServlet().getServletContext().getRealPath("/");
        try {
            Object[] args = { executionCourseId, metadataCode, exerciseCode, path };
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadQuestion", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        SiteView siteViewAux = null;
        try {
            Object[] args = { executionCourseId, testCode, path };
            siteViewAux = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        List testQuestionList = new ArrayList();
        testQuestionList = ((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
        Collections.sort(testQuestionList);
        List testQuestionNames = new ArrayList();
        List testQuestionValues = new ArrayList();
        for (int i = 0; i < testQuestionList.size(); i++) {
            testQuestionNames.add(new String("Pergunta " + (i + 1)));
            testQuestionValues.add(new Integer(i));
        }
        List formulas = CorrectionFormula.getFormulas();
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("formulas", formulas);
        request.setAttribute("testCode", testCode);
        request.setAttribute("exerciseCode", exerciseCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("insertTestQuestion");
    }

    public ActionForward insertTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        //Integer metadataCode = getCodeFromRequest(request, "metadataCode");
        String[] metadataCodes = request.getParameterValues("metadataCode");
        Integer questionOrder = getCodeFromRequest(request, "questionOrder");
        Integer testCode = getCodeFromRequest(request, "testCode");
        Double questionValue = null;
        if (request.getParameter("questionValue") != null) {
            questionValue = new Double(request.getParameter("questionValue"));
        }
        Integer formula = getCodeFromRequest(request, "formula");
        String path = getServlet().getServletContext().getRealPath("/");

        CorrectionFormula correctionFormulas = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormulas = new CorrectionFormula(formula);
        }
        Object[] arguments = { executionCourseId, testCode, metadataCodes, questionOrder, questionValue,
                correctionFormulas, path };
        try {
            ServiceUtils.executeService(userView, "InsertTestQuestion", arguments);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testCode", testCode);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward prepareEditTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer questionCode = getCodeFromRequest(request, "questionCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        String path = getServlet().getServletContext().getRealPath("/");
        SiteView siteView = null;
        try {
            Object[] args = { executionCourseId, testCode, questionCode, path };
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTestQuestion", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        SiteView siteViewAux = null;
        try {
            Object[] args = { executionCourseId, testCode, path };
            siteViewAux = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        List testQuestionList = new ArrayList();
        testQuestionList = ((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
        Collections.sort(testQuestionList);
        List testQuestionNames = new ArrayList();
        List testQuestionValues = new ArrayList();
        int questionOrder = ((InfoSiteTestQuestion) siteView.getComponent()).getInfoTestQuestion()
                .getTestQuestionOrder().intValue();
        for (int i = 0; i < testQuestionList.size(); i++) {
            if ((i + 1) != questionOrder && (i + 1) != questionOrder + 1) {
                testQuestionNames.add(new String("Pergunta " + (i + 1)));
                testQuestionValues.add(new Integer(i));
            }
        }
        List formulas = CorrectionFormula.getFormulas();
        ((DynaActionForm) form).set("formula", ((InfoSiteTestQuestion) siteView.getComponent())
                .getInfoTestQuestion().getCorrectionFormula().getFormula().toString());
        request.setAttribute("formulas", formulas);
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("testCode", testCode);
        request.setAttribute("questionCode", questionCode);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("editTestQuestion");
    }

    public ActionForward editTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        Integer testQuestionCode = getCodeFromRequest(request, "testQuestionCode");
        Integer questionOrder = getCodeFromRequest(request, "testQuestionOrder");
        Double questionValue = new Double(request.getParameter("testQuestionValue"));
        Integer formula = getCodeFromRequest(request, "formula");
        CorrectionFormula correctionFormula = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormula = new CorrectionFormula(formula);
        }
        Object[] arguments = { executionCourseCode, testCode, testQuestionCode, questionOrder,
                questionValue, correctionFormula };
        try {
            ServiceUtils.executeService(userView, "EditTestQuestion", arguments);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward deleteTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        Integer questionCode = getCodeFromRequest(request, "questionCode");
        Object[] args = { executionCourseCode, testCode, questionCode };
        try {
            ServiceUtils.executeService(userView, "DeleteTestQuestion", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward showTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        SiteView siteView = null;
        try {
            Object[] args = { executionCourseId };
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTests", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTests");
    }

    public ActionForward prepareDeleteTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);
        String path = getServlet().getServletContext().getRealPath("/");
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    objectCode, testCode, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("title", ((InfoSiteTest) siteView.getComponent()).getInfoTest().getTitle());
        return mapping.findForward("prepareDeleteTest");
    }

    public ActionForward deleteTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        Object[] args = { executionCourseCode, testCode };
        try {
            ServiceUtils.executeService(userView, "DeleteTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseCode);
        return showTests(mapping, form, request, response);
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer exerciseCode = getCodeFromRequest(request, "exerciseCode");
        Integer imgCode = getCodeFromRequest(request, "imgCode");
        String imgTypeString = request.getParameter("imgType");
        String studentCode = request.getParameter("studentCode");
        String optionShuffle = request.getParameter("optionShuffle");
        Integer testCode = getCodeFromRequest(request, "testCode");

        String feedbackCode = request.getParameter("feedbackCode");
        String path = getServlet().getServletContext().getRealPath("/");
        String img = null;
        if (studentCode != null && testCode != null) {
            Object[] args = { studentCode, testCode, exerciseCode, imgCode, feedbackCode, path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadStudentTestQuestionImage",
                        args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else if (optionShuffle != null) {
            Object[] args = { testCode, exerciseCode, optionShuffle, imgCode, path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadQuestionImage", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            Object[] args = { exerciseCode, imgCode, path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadQuestionImage", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        byte[] imageData = Base64.decode(img.getBytes());
        try {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            String imageName = new String("image"
                    + exerciseCode
                    + imgCode
                    + "."
                    + imgTypeString
                            .substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
            dataOut.write(imageData);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward editTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { executionCourseCode, testCode, path };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(((InfoSiteTest) siteView.getComponent()).getInfoTestQuestions());
        request.setAttribute("testCode", testCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("editTest");
    }

    public ActionForward prepareEditTestHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { executionCourseCode, testCode, path };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("editTestHeader");
    }

    public ActionForward editTestHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String title = request.getParameter("title");
        String information = request.getParameter("information");
        Object[] args = { executionCourseCode, testCode, title, information };
        try {
            ServiceUtils.executeService(userView, "EditTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward prepareDistributeTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");

        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { objectCode, testCode, path };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (((InfoSiteTest) siteView.getComponent()).getInfoTest().getNumberOfQuestions().intValue() >= 1) {

            List testTypeList = (new TestType()).getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();

            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals(""))
                ((DynaActionForm) form).set("testType", "1");
            if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
                ((DynaActionForm) form).set("availableCorrection", "3");
            if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
                ((DynaActionForm) form).set("imsFeedback", "true");
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation",
                        createDefaultDistributedTestInfo(siteView));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form)
                        .get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals(""))
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            request.setAttribute("testCode", testCode);
            request.setAttribute("objectCode", objectCode);
            return mapping.findForward("distributeTest");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        request.setAttribute("objectCode", objectCode);
        return showTests(mapping, form, request, response);

    }

    public ActionForward chooseDistributionFor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        if (request.getParameter("shifts") != null) {
            if (!compareDates(request)) {
                return prepareDistributeTest(mapping, form, request, response);
            }
            return chooseShifts(mapping, form, request, response);
        } else if (request.getParameter("students") != null) {
            if (!compareDates(request)) {
                return prepareDistributeTest(mapping, form, request, response);
            }
            return chooseStudents(mapping, form, request, response);
        } else if (request.getParameter("addShifts") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return chooseAddShifts(mapping, form, request, response);
        } else if (request.getParameter("addStudents") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return chooseAddStudents(mapping, form, request, response);
        } else if (request.getParameter("save") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return editDistributedTest(mapping, form, request, response);
        } else
            return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward chooseShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        List shifts = null;
        try {
            Object[] args = { objectCode, null };
            shifts = (List) ServiceUtils.executeService(userView, "ReadShiftsByDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(shifts, new InfoShiftComparatorByLessonType());
        request.setAttribute("shifts", shifts);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("distributeTestByShifts");
    }

    public ActionForward chooseStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        TeacherAdministrationSiteView siteView = null;
        Object[] args = { objectCode, null };
        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "ReadStudentsByCurricularCourse", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
        Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
        request.setAttribute("siteView", siteView);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("distributeTestByStudents");
    }

    public ActionForward distributeTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        String testInformation = request.getParameter("testInformation");
        String testBeginDay = request.getParameter("beginDayFormatted");
        String testBeginMonth = request.getParameter("beginMonthFormatted");
        String testBeginYear = request.getParameter("beginYearFormatted");
        String testBeginHour = request.getParameter("beginHourFormatted");
        String testBeginMinute = request.getParameter("beginMinuteFormatted");
        String testEndDay = request.getParameter("endDayFormatted");
        String testEndMonth = request.getParameter("endMonthFormatted");
        String testEndYear = request.getParameter("endYearFormatted");
        String testEndHour = request.getParameter("endHourFormatted");
        String testEndMinute = request.getParameter("endMinuteFormatted");
        String testType = request.getParameter("testType");
        String availableCorrection = request.getParameter("availableCorrection");
        String imsFeedback = request.getParameter("imsFeedback");
        Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        Calendar endHour = string2Hour(testEndHour, testEndMinute);
        String[] selected = request.getParameterValues("selected");
        String insertByShifts = request.getParameter("insertByShifts");

        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }

        List infoStudentList = null;
        Integer advisoryId = null;
        try {

            infoStudentList = (List) ServiceUtils.executeService(userView, "ReadStudentsByIdArray",
                    new Object[] { objectCode, selected, new Boolean(insertByShifts) });
            Object[] args = { objectCode, testCode, testInformation, beginDate, beginHour, endDate,
                    endHour, testTypeArg, correctionAvailabilityArg, imsFeedbackArg, infoStudentList,
                    request.getContextPath() };
            advisoryId = (Integer) ServiceUtils.executeService(userView, "InsertDistributedTest", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (advisoryId == null) {
            request.setAttribute("successfulDistribution", new Boolean(false));
            return showDistributedTests(mapping, form, request, response);
        }

        for (int times = 0; times < 3; times++) {
            List studentWithoutAdvisory = new ArrayList();
            for (int i = 0; i < infoStudentList.size(); i++) {
                InfoStudent infoStudent = (InfoStudent) infoStudentList.get(i);
                try {
                    Object[] args = { objectCode, advisoryId, infoStudent.getIdInternal() };
                    ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory", args);
                } catch (FenixServiceException e) {
                    studentWithoutAdvisory.add(infoStudent);
                }
            }
            infoStudentList = studentWithoutAdvisory;
            if (infoStudentList.size() == 0)
                break;
        }
        request.setAttribute("infoStudentList", infoStudentList);
        request.setAttribute("successfulDistribution", new Boolean(true));
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward showDistributedTests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Object[] args = { objectCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadDistributedTests", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showDistributedTests");
    }

    public ActionForward prepareEditDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Object[] args = { objectCode, distributedTestCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        List testTypeList = (new TestType()).getAllTypes();
        request.setAttribute("testTypeList", testTypeList);
        List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
        request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);

        if ((((DynaActionForm) form).get("testInformation")).equals(""))
            ((DynaActionForm) form).set("testInformation", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getTestInformation());

        if ((((DynaActionForm) form).get("beginDayFormatted")).equals(""))
            ((DynaActionForm) form).set("beginDayFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getBeginDayFormatted());
        if ((((DynaActionForm) form).get("beginMonthFormatted")).equals(""))
            ((DynaActionForm) form).set("beginMonthFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getBeginMonthFormatted());
        if ((((DynaActionForm) form).get("beginYearFormatted")).equals(""))
            ((DynaActionForm) form).set("beginYearFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getBeginYearFormatted());
        if ((((DynaActionForm) form).get("beginHourFormatted")).equals(""))
            ((DynaActionForm) form).set("beginHourFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getBeginHourFormatted());
        if ((((DynaActionForm) form).get("beginMinuteFormatted")).equals(""))
            ((DynaActionForm) form).set("beginMinuteFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getBeginMinuteFormatted());

        if ((((DynaActionForm) form).get("endDayFormatted")).equals(""))
            ((DynaActionForm) form).set("endDayFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getEndDayFormatted());
        if ((((DynaActionForm) form).get("endMonthFormatted")).equals(""))
            ((DynaActionForm) form).set("endMonthFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getEndMonthFormatted());
        if ((((DynaActionForm) form).get("endYearFormatted")).equals(""))
            ((DynaActionForm) form).set("endYearFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getEndYearFormatted());

        if ((((DynaActionForm) form).get("endHourFormatted")).equals(""))
            ((DynaActionForm) form).set("endHourFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getEndHourFormatted());
        if ((((DynaActionForm) form).get("endMinuteFormatted")).equals(""))
            ((DynaActionForm) form).set("endMinuteFormatted", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getEndMinuteFormatted());
        if ((((DynaActionForm) form).get("testType")).equals(""))
            ((DynaActionForm) form).set("testType", ((InfoSiteDistributedTest) siteView.getComponent())
                    .getInfoDistributedTest().getTestType().getType().toString());
        if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
            ((DynaActionForm) form).set("availableCorrection", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getCorrectionAvailability()
                    .getAvailability().toString());
        if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
            ((DynaActionForm) form).set("imsFeedback", ((InfoSiteDistributedTest) siteView
                    .getComponent()).getInfoDistributedTest().getImsFeedback().toString());
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("editDistributedTest");
    }

    public ActionForward chooseAddShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        List shifts = null;
        try {
            Object[] args = { objectCode, distributedTestCode };
            shifts = (List) ServiceUtils.executeService(userView, "ReadShiftsByDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(shifts, new InfoShiftComparatorByLessonType());
        request.setAttribute("shifts", shifts);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("addShiftsToDistributedTest");
    }

    public ActionForward chooseAddStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        List students = null;
        try {
            Object[] args = { objectCode, distributedTestCode };
            students = (List) ServiceUtils.executeService(userView,
                    "ReadStudentsWithoutDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(students, new BeanComparator("number"));
        request.setAttribute("students", students);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("addStudentsToDistributedTest");
    }

    public ActionForward editDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        String testInformation = request.getParameter("testInformation");
        String testBeginDay = request.getParameter("beginDayFormatted");
        String testBeginMonth = request.getParameter("beginMonthFormatted");
        String testBeginYear = request.getParameter("beginYearFormatted");
        String testBeginHour = request.getParameter("beginHourFormatted");
        String testBeginMinute = request.getParameter("beginMinuteFormatted");
        String testEndDay = request.getParameter("endDayFormatted");
        String testEndMonth = request.getParameter("endMonthFormatted");
        String testEndYear = request.getParameter("endYearFormatted");
        String testEndHour = request.getParameter("endHourFormatted");
        String testEndMinute = request.getParameter("endMinuteFormatted");
        String testType = request.getParameter("testType");
        String availableCorrection = request.getParameter("availableCorrection");
        String imsFeedback = request.getParameter("imsFeedback");
        Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        Calendar endHour = string2Hour(testEndHour, testEndMinute);
        String[] selected = request.getParameterValues("selected");
        String insertByShifts = request.getParameter("insertByShifts");
        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;

        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }
        request.setAttribute("objectCode", objectCode);

        List infoStudentList = new ArrayList();
        Integer dataChangesAdvisory = null;

        try {
            Object[] args = { objectCode, distributedTestCode, testInformation, beginDate, beginHour,
                    endDate, endHour, testTypeArg, correctionAvailabilityArg, imsFeedbackArg,
                    request.getContextPath() };
            dataChangesAdvisory = (Integer) ServiceUtils.executeService(userView, "EditDistributedTest",
                    args);
        } catch (FenixServiceException e) {
            request.setAttribute("successfulEdition", new Boolean(false));
            return showDistributedTests(mapping, form, request, response);
        }

        request.setAttribute("successfulEdition", new Boolean(true));

        List infoOldStudentList = new ArrayList();
        if (dataChangesAdvisory != null) {
            try {
                infoOldStudentList = (List) ServiceUtils.executeService(userView,
                        "ReadStudentsWithDistributedTest", new Object[] { objectCode,
                                distributedTestCode });
            } catch (FenixServiceException e) {
                request.setAttribute("insuccessfulAdvisoryDistribution", new Boolean(true));
                return showDistributedTests(mapping, form, request, response);
            }
            for (int times = 0; times < 3; times++) {
                List studentWithoutAdvisory = new ArrayList();
                for (int i = 0; i < infoOldStudentList.size(); i++) {
                    InfoStudent infoStudent = (InfoStudent) infoOldStudentList.get(i);
                    try {
                        ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory",
                                new Object[] { objectCode, dataChangesAdvisory,
                                        infoStudent.getIdInternal() });
                    } catch (FenixServiceException e) {
                        studentWithoutAdvisory.add(infoStudent);
                    }
                }
                infoOldStudentList = studentWithoutAdvisory;
                if (infoOldStudentList.size() == 0)
                    break;
            }
            request.setAttribute("infoStudentList", infoOldStudentList);
        } else
            infoOldStudentList = new ArrayList();
        Integer addStudentsAdvisory = null;
        if (selected != null) {
            try {
                infoStudentList = (List) ServiceUtils.executeService(userView, "ReadStudentsByIdArray",
                        new Object[] { objectCode, selected, new Boolean(insertByShifts) });
                addStudentsAdvisory = (Integer) ServiceUtils.executeService(userView,
                        "AddStudentsToDistributedTest", new Object[] { objectCode, distributedTestCode,
                                infoStudentList, request.getContextPath() });
            } catch (FenixServiceException e) {
                request.setAttribute("successfulStudentAddition", new Boolean(false));
                return showDistributedTests(mapping, form, request, response);
            }

            if (addStudentsAdvisory != null) {

                for (int times = 0; times < 3; times++) {
                    List studentWithoutAdvisory = new ArrayList();
                    for (int i = 0; i < infoStudentList.size(); i++) {
                        InfoStudent infoStudent = (InfoStudent) infoStudentList.get(i);
                        try {
                            Object[] args = { objectCode, addStudentsAdvisory,
                                    infoStudent.getIdInternal() };
                            ServiceUtils.executeService(userView,
                                    "InsertStudentDistributedTestAdvisory", args);
                        } catch (FenixServiceException e) {
                            studentWithoutAdvisory.add(infoStudent);
                        }
                    }
                    infoStudentList = studentWithoutAdvisory;
                    if (infoStudentList.size() == 0)
                        break;
                }
            }
            infoOldStudentList.addAll(infoStudentList);
            request.setAttribute("infoStudentList", infoOldStudentList);
        }
        request.setAttribute("objectCode", objectCode);
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward prepareDeleteDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Object[] args = { objectCode, distributedTestCode };
        Boolean canDelete = null;
        try {
            canDelete = (Boolean) ServiceUtils.executeService(userView,
                    "VerifyIfCanDeleteDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("canDelete", canDelete);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return mapping.findForward("prepareDeleteDistributedTest");
    }

    public ActionForward deleteDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Object[] args = { objectCode, distributedTestCode };
        Boolean successfulTestDeletion = null;
        try {
            successfulTestDeletion = (Boolean) ServiceUtils.executeService(userView,
                    "DeleteDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("successfulTestDeletion", successfulTestDeletion);
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward showStudentTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Integer studentCode = getCodeFromRequest(request, "studentCode");
        String path = getServlet().getServletContext().getRealPath("/");
        List infoStudentTestQuestionList = null;
        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentDistributedTest", new Object[] { objectCode, distributedTestCode,
                            studentCode, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                .getDistributedTest().getNumberOfQuestions().intValue();

        double classification = 0;

        String[] userResponse = new String[numQuestions];

        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList
                    .get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                    && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                    && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                        .getType().intValue() == CardinalityType.SINGLE)
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse()[0]);
                else
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse());
            classification += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
        }

        request.setAttribute("studentTestForm", form);

        if (classification < 0)
            classification = 0;
        request.setAttribute("classification", (new DecimalFormat("#0.##").format(classification)));

        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("showStudentTest");
    }

    public ActionForward showStudentTestLog(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Integer studentCode = getCodeFromRequest(request, "studentCode");
        List infoStudentTestLogList = null;
        try {
            infoStudentTestLogList = (List) ServiceUtils.executeService(userView, "ReadStudentTestLog",
                    new Object[] { objectCode, distributedTestCode, studentCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Iterator it = infoStudentTestLogList.iterator();
        int maxQuestionNumber = 0;
        while (it.hasNext()) {
            InfoStudentTestLog infoStudentTestLog = (InfoStudentTestLog) it.next();
            if (maxQuestionNumber < infoStudentTestLog.getEventList().size())
                maxQuestionNumber = infoStudentTestLog.getEventList().size();
        }
        request.setAttribute("questionNumber", new Integer(maxQuestionNumber));
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("infoStudentTestLogList", infoStudentTestLogList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return mapping.findForward("showStudentTestLog");
    }

    public ActionForward showTestMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        Object[] args = { objectCode, distributedTestCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils
                    .executeService(userView, "ReadDistributedTestMarks", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTestMarks");
    }

    public ActionForward showTestMarksStatistics(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        //        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { objectCode, distributedTestCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadDistributedTestMarksStatistics", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTestMarksStatistics");
    }

    public ActionForward showTestStatistics(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { objectCode, distributedTestCode, path };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadInquiryStatistics", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTestStatistics");
    }

    public ActionForward downloadTestMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        String result = null;
        try {
            if (distributedTestCode != null) {
                Object[] args = { objectCode, distributedTestCode };
                result = (String) ServiceUtils.executeService(userView,
                        "ReadDistributedTestMarksToString", args);
            } else {
                Object[] args = { objectCode, request.getParameterValues("distributedTestCodes") };
                result = (String) ServiceUtils.executeService(userView,
                        "ReadDistributedTestMarksToString", args);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=pauta.xls");
            writer.print(result);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixActionException();
        }
        return null;
    }

    public ActionForward prepareChangeStudentTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getCodeFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        List changesTypeList = (new TestQuestionChangesType()).getAllTypes();
        request.setAttribute("changesTypeList", changesTypeList);
        List studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("changesType", "1");
        ((DynaActionForm) form).set("deleteVariation", "true");
        ((DynaActionForm) form).set("studentsType", "4");
        return mapping.findForward("changeStudentTestQuestion");
    }

    public ActionForward chooseAnotherExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");
        String path = getServlet().getServletContext().getRealPath("/");
        SiteView siteView = null;
        try {
            Object[] args = { executionCourseId, distributedTestId, path };
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadMetadatasByDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", distributedTestId);
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        request.setAttribute("successfulChanged", request.getAttribute("successfulChanged"));
        request.setAttribute("studentsType", request.getAttribute("studentsType"));
        request.setAttribute("changesType", request.getAttribute("changesType"));
        request.setAttribute("deleteVariation", request.getAttribute("deleteVariation"));
        request.setAttribute("siteView", siteView);
        return mapping.findForward("chooseAnotherExercise");
    }

    public ActionForward changeStudentTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer questionId = getCodeFromRequest(request, "questionCode");
        Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");
        Integer studentId = getCodeFromRequest(request, "studentCode");
        Integer metadataId = getCodeFromRequest(request, "metadataCode");
        String changesType = request.getParameter("changesType");
        String delete = request.getParameter("deleteVariation");
        String studentsType = request.getParameter("studentsType");
        request.setAttribute("studentCode", studentId);
        request.setAttribute("distributedTestCode", distributedTestId);
        request.setAttribute("objectCode", executionCourseId);
        if (((new TestQuestionChangesType(new Integer(changesType))).getType().intValue() == 2)
                && (metadataId == null)) {
            request.setAttribute("deleteVariation", delete);
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        List result;
        try {
            Object[] args = { executionCourseId, distributedTestId, questionId, metadataId, studentId,
                    new TestQuestionChangesType(new Integer(changesType)), new Boolean(delete),
                    new TestQuestionStudentsChangesType(new Integer(studentsType)) };
            result = (List) ServiceUtils.executeService(userView, "ChangeStudentTestQuestion", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (result == null || result.size() == 0) {
            request.setAttribute("successfulChanged", new Boolean(false));
            request.setAttribute("deleteVariation", delete);
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        Collections.sort(result, new BeanComparator("label"));
        request.setAttribute("successfulChanged", result);

        Integer advisoryId = null;
        List infoStudentList = new ArrayList();
        try {
            infoStudentList = (List) ServiceUtils.executeService(userView, "ReadStudentsByIdArray",
                    new Object[] { executionCourseId, result });

            Object[] args = { executionCourseId, distributedTestId, request.getContextPath(),
                    metadataId, studentId, new TestQuestionChangesType(new Integer(changesType)),
                    new Boolean(delete), new TestQuestionStudentsChangesType(new Integer(studentsType)) };
            advisoryId = (Integer) ServiceUtils.executeService(userView, "CreateStudentTestAdvisory",
                    new Object[] { executionCourseId, distributedTestId, request.getContextPath() });
            for (int times = 0; times < 3; times++) {
                List studentWithoutAdvisory = new ArrayList();
                for (int i = 0; i < infoStudentList.size(); i++) {
                    InfoStudent student = (InfoStudent) infoStudentList.get(i);
                    try {
                        ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory",
                                new Object[] { executionCourseId, advisoryId, student.getIdInternal() });
                    } catch (FenixServiceException e) {
                        studentWithoutAdvisory.add(student);
                    }

                }
                infoStudentList = studentWithoutAdvisory;
                if (infoStudentList.size() == 0)
                    break;
            }
            request.setAttribute("infoStudentList", infoStudentList);
        } catch (FenixServiceException e) {
            if (infoStudentList == null || infoStudentList.size() == 0)
                request.setAttribute("insuccessfulAdvisoryDistribution", new Boolean(true));
            else
                request.setAttribute("infoStudentList", infoStudentList);
        }
        return mapping.findForward("showStudentTest");
    }

    public ActionForward chooseTestSimulationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = getSession(request);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getCodeFromRequest(request, "objectCode");
        Integer testCode = getCodeFromRequest(request, "testCode");
        request.setAttribute("objectCode", objectCode);
        String path = getServlet().getServletContext().getRealPath("/");
        Object[] args = { objectCode, testCode, path };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (((InfoSiteTest) siteView.getComponent()).getInfoTest().getNumberOfQuestions().intValue() >= 1) {

            List testTypeList = (new TestType()).getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();

            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals(""))
                ((DynaActionForm) form).set("testType", "1");
            if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
                ((DynaActionForm) form).set("availableCorrection", "3");
            if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
                ((DynaActionForm) form).set("imsFeedback", "true");
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation",
                        createDefaultDistributedTestInfo(siteView));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form)
                        .get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals(""))
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            request.setAttribute("testCode", testCode);
            return mapping.findForward("chooseTestSimulationOptions");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        return showTests(mapping, form, request, response);
    }

    public ActionForward prepareSimulateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer testId = getCodeFromRequest(request, "testCode");
        if (testId == null)
            testId = getCodeFromRequest(request, "distributedTestCode");
        String testInformation = request.getParameter("testInformation");
        String testType = request.getParameter("testType");
        String availableCorrection = request.getParameter("availableCorrection");
        String imsFeedback = request.getParameter("imsFeedback");
        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }

        List infoStudentTestQuestionList = new ArrayList();
        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "GenetareStudentTestForSimulation", new Object[] { executionCourseId, testId,
                            getServlet().getServletContext().getRealPath("/"), testTypeArg,
                            correctionAvailabilityArg, imsFeedbackArg, testInformation });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                .getDistributedTest().getNumberOfQuestions().intValue();

        String[] userResponse = new String[numQuestions];

        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList
                    .get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                    && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                    && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                        .getType().intValue() == CardinalityType.SINGLE)
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse()[0]);
                else
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse());
        }

        request.setAttribute("studentTestForm", form);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("simulate", new Boolean(true));
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        return mapping.findForward("doTestSimulation");
    }

    public ActionForward simulateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");

        String testInformation = request.getParameter("testInformation");
        String testType = request.getParameter("testType");
        String availableCorrection = request.getParameter("availableCorrection");
        String imsFeedback = request.getParameter("imsFeedback");
        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }

        String path = getServlet().getServletContext().getRealPath("/");
        SiteView siteView = null;
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("distributedTestCode", distributedTestId);
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseId, distributedTestId, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        int numberOfQuestions = ((InfoSiteTest) siteView.getComponent()).getInfoTest()
                .getNumberOfQuestions().intValue();

        String[] questionCodes = new String[numberOfQuestions];
        String[] optionsShuffle = new String[numberOfQuestions];
        String[] questionTypes = new String[numberOfQuestions];
        for (int i = 0; i < numberOfQuestions; i++) {
            questionCodes[i] = request.getParameter("questionCode" + i);
            optionsShuffle[i] = request.getParameter("optionShuffle" + i);
            questionTypes[i] = request.getParameter("questionType" + i);
        }

        Response[] userResponse = new Response[numberOfQuestions];
        for (int i = 0; i < numberOfQuestions; i++) {
            if (new Integer(questionTypes[i]).intValue() == QuestionType.STR)
                userResponse[i] = new ResponseSTR(request.getParameter(new String("question" + i)));
            else if (new Integer(questionTypes[i]).intValue() == QuestionType.NUM)
                userResponse[i] = new ResponseNUM(request.getParameter(new String("question" + i)));
            else if (new Integer(questionTypes[i]).intValue() == QuestionType.LID)
                userResponse[i] = new ResponseLID(request.getParameterValues(new String("question" + i)));
        }
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = null;
        try {
            infoSiteStudentTestFeedback = (InfoSiteStudentTestFeedback) ServiceUtils.executeService(
                    userView, "SimulateTest", new Object[] { executionCourseId, distributedTestId,
                            userResponse, questionCodes, optionsShuffle, testTypeArg,
                            correctionAvailabilityArg, imsFeedbackArg, testInformation, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoSiteStudentTestFeedback != null) {
            List infoStudentTestQuestionList = infoSiteStudentTestFeedback
                    .getInfoStudentTestQuestionList();
            int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                    .getDistributedTest().getNumberOfQuestions().intValue();

            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList
                        .get(i);

                if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                        && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion
                            .getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                        && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion
                            .getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                        && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                            .getType().intValue() == CardinalityType.SINGLE)
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                                .getResponse()).getResponse()[0]);
                    else
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                                .getResponse()).getResponse());
                if (((InfoStudentTestQuestion) infoSiteStudentTestFeedback
                        .getInfoStudentTestQuestionList().get(0)).getDistributedTest()
                        .getCorrectionAvailability().getAvailability().intValue() == CorrectionAvailability.ALWAYS) {
                    request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                            .getInfoStudentTestQuestionList());
                }
            }
            if (request.getAttribute("showCorrection") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                        .getInfoStudentTestQuestionList());
                return mapping.findForward("showSimulationCorrection");
            }
            if (request.getAttribute("doTestSimulation") != null
                    || request.getParameter("doTestSimulation") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                        .getInfoStudentTestQuestionList());
                return mapping.findForward("doTestSimulation");
            }

        }
        request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        return mapping.findForward("showSimulationFeedback");
    }

    public ActionForward showSimulationCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        request.setAttribute("showCorrection", new Boolean(true));
        return simulateTest(mapping, form, request, response);
    }

    public ActionForward doTestSimulation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        request.setAttribute("doTestSimulation", new Boolean(true));
        return simulateTest(mapping, form, request, response);
    }

    private void error(HttpServletRequest request, String errorProperty, String error) {
        ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(errorProperty, new ActionError(error));
        saveErrors(request, actionErrors);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);
        if (objectCode != null) {
            if (objectCode instanceof String)
                code = new Integer((String) objectCode);
            else if (objectCode instanceof Integer)
                code = (Integer) objectCode;
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null)
                code = new Integer(thisCodeString);
        }
        return code;
    }

    private Calendar string2Date(String day, String month, String year) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.DAY_OF_MONTH, (new Integer(day)).intValue());
        result.set(Calendar.MONTH, (new Integer(month)).intValue() - 1);
        result.set(Calendar.YEAR, (new Integer(year)).intValue());
        return result;
    }

    private Calendar string2Hour(String hour, String minute) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.HOUR_OF_DAY, (new Integer(hour)).intValue());
        result.set(Calendar.MINUTE, (new Integer(minute)).intValue());
        result.set(Calendar.SECOND, new Integer(0).intValue());
        return result;
    }

    private boolean compareDates(HttpServletRequest request) {
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        String testBeginDay = request.getParameter("beginDayFormatted");
        String testBeginMonth = request.getParameter("beginMonthFormatted");
        String testBeginYear = request.getParameter("beginYearFormatted");
        String testBeginHour = request.getParameter("beginHourFormatted");
        String testBeginMinute = request.getParameter("beginMinuteFormatted");
        String testEndDay = request.getParameter("endDayFormatted");
        String testEndMonth = request.getParameter("endMonthFormatted");
        String testEndYear = request.getParameter("endYearFormatted");
        String testEndHour = request.getParameter("endHourFormatted");
        String testEndMinute = request.getParameter("endMinuteFormatted");
        Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        Calendar endHour = string2Hour(testEndHour, testEndMinute);
        if (dateComparator.compare(beginDate, endDate) > 0) {
            error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
            return false;
        }
        if (dateComparator.compare(beginDate, endDate) == 0) {
            if (hourComparator.compare(beginHour, endHour) >= 0) {
                error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
                return false;
            }
        }
        return true;
    }

    private String createDefaultDistributedTestInfo(SiteView siteView) {

        Integer numberOfQuestions = ((InfoSiteTest) siteView.getComponent()).getInfoTest()
                .getNumberOfQuestions();
        String title = ((InfoSiteTest) siteView.getComponent()).getInfoTest().getTitle();
        String result = new String("A '" + title + "' é constituida por " + numberOfQuestions
                + " pergunta");
        if (numberOfQuestions.intValue() > 1)
            result = result.concat("s");
        result = result.concat(". Uma pergunta certa vale a cotação indicada.");
        return result;
    }

    private String createDefaultDistributedInquiryInfo() {
        return new String(
                "Este Questionário tem um papel meramente estatístico e visa contribuir para a melhoria substancial da qualidade de ensino. A última resposta dada será a definitiva.");
    }
}