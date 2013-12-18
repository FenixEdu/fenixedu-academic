/*
 * Created on 5/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadFilteredExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Ana e Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/ExamSearchByDegreeAndYear",
        input = "/ExamSearchByDegreeAndYear.do?method=prepare&page=0", attribute = "examSearchByDegreeAndYearForm",
        formBean = "examSearchByDegreeAndYearForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show", path = "df.page.selectDegreeAndYear"),
        @Forward(name = "showExamsMap", path = "df.page.degreeYearExamsMap") })
@Exceptions(
        value = { @ExceptionHandling(
                type = net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadFilteredExamsMap.ExamsPeriodUndefined.class,
                key = "error.exams.period.undefined",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ExamSearchByDegreeAndYear extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        List curricularYearsList = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            curricularYearsList.add(String.valueOf(i));
        }

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYearsList);

        /* Cria o form bean com as licenciaturas em execucao. */

        List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        List licenciaturas = new ArrayList();
        licenciaturas.add(new LabelValueBean("< Todos >", ""));
        Iterator iterator = executionDegreeList.iterator();
        int index = 0;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                    resourceBundle.getString(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType()
                            .name())
                            + " de " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute(PresentationConstants.DEGREES, licenciaturas);
        return mapping.findForward("show");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm examSearchByDegreeAndYearForm = (DynaActionForm) form;
        User userView = getUserView(request);

        String[] selectedCurricularYears = (String[]) examSearchByDegreeAndYearForm.get("selectedCurricularYears");
        Boolean selectAllCurricularYears = (Boolean) examSearchByDegreeAndYearForm.get("selectAllCurricularYears");

        if ((selectAllCurricularYears != null) && selectAllCurricularYears.booleanValue()) {
            String[] allCurricularYears = { "1", "2", "3", "4", "5" };
            selectedCurricularYears = allCurricularYears;
        }

        List curricularYears = new ArrayList(selectedCurricularYears.length);
        for (String selectedCurricularYear : selectedCurricularYears) {
            curricularYears.add(new Integer(selectedCurricularYear));
            if (selectedCurricularYear.equals("1")) {
                request.setAttribute(PresentationConstants.CURRICULAR_YEARS_1, "1");
            }
            if (selectedCurricularYear.equals("2")) {
                request.setAttribute(PresentationConstants.CURRICULAR_YEARS_2, "2");
            }
            if (selectedCurricularYear.equals("3")) {
                request.setAttribute(PresentationConstants.CURRICULAR_YEARS_3, "3");
            }
            if (selectedCurricularYear.equals("4")) {
                request.setAttribute(PresentationConstants.CURRICULAR_YEARS_4, "4");
            }
            if (selectedCurricularYear.equals("5")) {
                request.setAttribute(PresentationConstants.CURRICULAR_YEARS_5, "5");
            }
        }

        request.setAttribute(PresentationConstants.CURRICULAR_YEARS_LIST, curricularYears);

        int index = -1;
        try {
            index = Integer.parseInt((String) examSearchByDegreeAndYearForm.get("index"));
        } catch (NumberFormatException ex) {
            index = -1;
        }

        InfoExecutionDegree infoExecutionDegree = null;
        List infoExamsMap = new ArrayList();

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        if (index != -1) {
            infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(index);
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getExternalId().toString());
            infoExamsMap.add(getExamsMap(request, curricularYears, infoExecutionDegree, infoExecutionPeriod));
            request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);
        } else {
            // all degrees
            infoExamsMap = getExamsMap(request, curricularYears, executionDegreeList, infoExecutionPeriod);
            request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);
        }

        return mapping.findForward("showExamsMap");

    }

    private InfoExamsMap getExamsMap(HttpServletRequest request, List curricularYears, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

        User userView = getUserView(request);
        InfoExamsMap infoRoomExamsMaps = null;

        infoRoomExamsMaps =
                ReadFilteredExamsMap.runReadFilteredExamsMap(infoExecutionDegree, curricularYears, infoExecutionPeriod);

        return infoRoomExamsMaps;
    }

    private List getExamsMap(HttpServletRequest request, List curricularYears, List executionDegreeList,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

        User userView = getUserView(request);
        List infoExamsMaps = new ArrayList();

        for (int i = 0; i < executionDegreeList.size(); i++) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(i);
            InfoExamsMap infoRoomExamsMap = null;

            infoRoomExamsMap =
                    ReadFilteredExamsMap.runReadFilteredExamsMap(infoExecutionDegree, curricularYears, infoExecutionPeriod);
            infoExamsMaps.add(infoRoomExamsMap);
        }
        return infoExamsMaps;
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }

        }
        return false;
    }
}