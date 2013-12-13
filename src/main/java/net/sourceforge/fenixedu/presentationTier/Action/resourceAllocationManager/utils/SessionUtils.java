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

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.bennu.core.security.Authenticate;

/**
 * @author jpvl
 */
public final class SessionUtils {

    public static List getExecutionCourses(HttpServletRequest request) throws Exception {

        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        User userView = Authenticate.getUser();
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));

        infoCourseList =
                LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular.run(infoExecutionDegree, academicInterval,
                        infoCurricularYear.getYear());

        request.setAttribute(PresentationConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);

        return infoCourseList;

    }

    public static List getExecutionCoursesForAssociateToExam(HttpServletRequest request, ActionForm form) throws Exception {

        List infoCourseList = new ArrayList();

        // Nao verifica se ja existem em sessao porque podem
        // ser de um periodo execucao diferente

        User userView = Authenticate.getUser();
        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        infoCourseList =
                LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular.run(infoExecutionDegree, infoExecutionPeriod,
                        infoCurricularYear.getYear());

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;
        String[] executionCourseIDArray = (String[]) chooseCourseForm.get("executionCourses");

        List newInfoCourseList = new ArrayList();
        Iterator iter = infoCourseList.iterator();
        boolean ignore = false;
        while (iter.hasNext()) {
            InfoExecutionCourse element = (InfoExecutionCourse) iter.next();
            for (String element2 : executionCourseIDArray) {
                Integer idExecutionCourse = new Integer(element2);
                if (idExecutionCourse.equals(element.getExternalId())) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore) {
                newInfoCourseList.add(element);
            }
            ignore = false;
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE_LIST_KEY, newInfoCourseList);

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