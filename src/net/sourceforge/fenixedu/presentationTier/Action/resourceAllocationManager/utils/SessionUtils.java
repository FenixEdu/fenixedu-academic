/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop.utils
 * 
 * Created on 4/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;

import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public final class SessionUtils {

    public static List getExecutionCourses(HttpServletRequest request) throws Exception {

        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        IUserView userView = UserView.getUser();
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        Object[] args = { infoExecutionDegree, infoExecutionPeriod, infoCurricularYear.getYear() };

        infoCourseList = (ArrayList) ServiceUtils.executeService(
                "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", args);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);

        return infoCourseList;

    }

    public static List getExecutionCoursesForAssociateToExam(HttpServletRequest request, ActionForm form)
            throws Exception {

        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        IUserView userView = UserView.getUser();
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionDegree, infoExecutionPeriod, infoCurricularYear.getYear() };

        infoCourseList = (ArrayList) ServiceUtils.executeService(
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

}