/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 4/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;

/**
 * @author jpvl
 */
public final class SessionUtils {

    public static CurricularYearAndSemesterAndInfoExecutionDegree getContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return (CurricularYearAndSemesterAndInfoExecutionDegree) session
                .getAttribute(SessionConstants.CONTEXT_KEY);
    }

    public static IUserView getUserView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (IUserView) session.getAttribute(SessionConstants.U_VIEW) : null;
    }

    public static List getExecutionCourses(HttpServletRequest request) throws Exception {

        //HttpSession session = request.getSession(false);
        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        IUserView userView = SessionUtils.getUserView(request);
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        Object[] args = { infoExecutionDegree, infoExecutionPeriod, infoCurricularYear.getYear() };

        infoCourseList = (ArrayList) ServiceUtils.executeService(userView,
                "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", args);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);

        return infoCourseList;

    }

    public static List getExecutionCoursesForAssociateToExam(HttpServletRequest request, ActionForm form)
            throws Exception {

        //HttpSession session = request.getSession(false);
        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        IUserView userView = SessionUtils.getUserView(request);
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionDegree, infoExecutionPeriod, infoCurricularYear.getYear() };

        infoCourseList = (ArrayList) ServiceUtils.executeService(userView,
                "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", args);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;
        String[] executionCourseIDArray = (String[]) chooseCourseForm.get("executionCourses");

        List newInfoCourseList = new ArrayList();
        Iterator iter = infoCourseList.iterator();
        boolean ignore = false;
        while (iter.hasNext()) {
            InfoExecutionCourse element = (InfoExecutionCourse) iter.next();
            for (int i = 0; i < executionCourseIDArray.length; i++) {
                Integer idExecutionCourse = new Integer(executionCourseIDArray[i]);
                if (idExecutionCourse.equals(element.getIdInternal())) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore)
                newInfoCourseList.add(element);
            ignore = false;
        }

        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, newInfoCourseList);

        return infoCourseList;

    }

    /**
     * Removes all attributes that start with prefix parameter. It uses
     * case-sensitive search.
     */
    public static void removeAttributtes(HttpSession session, String prefix) {
        if (session != null) {
            Enumeration attNames = session.getAttributeNames();
            Vector toRemoveAtts = new Vector();
            while (attNames.hasMoreElements()) {
                String attName = (String) attNames.nextElement();
                if (attName.startsWith(prefix)) {
                    toRemoveAtts.add(attName);
                }
            }
            for (int i = 0; i < toRemoveAtts.size(); i++) {
                session.removeAttribute((String) toRemoveAtts.elementAt(i));
            }
        }
    }

    /**
     * @deprecated
     * @param request
     * @param mapping
     * @throws FenixActionException
     */
    public static void validSessionVerification(HttpServletRequest request, ActionMapping mapping)
            throws FenixActionException {

        HttpSession session = request.getSession(false);

        if (session == null
                || request.getSession(false).getAttribute(SessionConstants.SESSION_IS_VALID) == null) {

            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("error.invalid.session");
            errors.add("error.invalid.session", error);
            request.setAttribute(Globals.ERROR_KEY, errors);
            throw new InvalidSessionActionException();
        }

    }

}