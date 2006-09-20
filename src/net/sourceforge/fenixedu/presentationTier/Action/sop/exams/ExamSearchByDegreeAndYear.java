/*
 * Created on 5/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Ana e Ricardo
 */
public class ExamSearchByDegreeAndYear extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
              
        IUserView userView = SessionUtils.getUserView(request);        
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        List curricularYearsList = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            curricularYearsList.add(String.valueOf(i));
        }
        
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYearsList);

        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        List licenciaturas = new ArrayList();
        licenciaturas.add(new LabelValueBean("< Todos >", ""));
        Iterator iterator = executionDegreeList.iterator();
        int index = 0;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
        
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = resourceBundle.getString(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().name())                    
                    + " de " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute(SessionConstants.DEGREES, licenciaturas);             
        return mapping.findForward("show");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm examSearchByDegreeAndYearForm = (DynaActionForm) form;
        IUserView userView = getUserView(request);

        String[] selectedCurricularYears = (String[]) examSearchByDegreeAndYearForm.get("selectedCurricularYears");
        Boolean selectAllCurricularYears = (Boolean) examSearchByDegreeAndYearForm.get("selectAllCurricularYears");

        if ((selectAllCurricularYears != null) && selectAllCurricularYears.booleanValue()) {
            String[] allCurricularYears = { "1", "2", "3", "4", "5" };
            selectedCurricularYears = allCurricularYears;
        }

        List curricularYears = new ArrayList(selectedCurricularYears.length);
        for (int i = 0; i < selectedCurricularYears.length; i++) {
            curricularYears.add(new Integer(selectedCurricularYears[i]));
            if (selectedCurricularYears[i].equals("1")) {
                request.setAttribute(SessionConstants.CURRICULAR_YEARS_1, "1");
            }
            if (selectedCurricularYears[i].equals("2")) {
                request.setAttribute(SessionConstants.CURRICULAR_YEARS_2, "2");
            }
            if (selectedCurricularYears[i].equals("3")) {
                request.setAttribute(SessionConstants.CURRICULAR_YEARS_3, "3");
            }
            if (selectedCurricularYears[i].equals("4")) {
                request.setAttribute(SessionConstants.CURRICULAR_YEARS_4, "4");
            }
            if (selectedCurricularYears[i].equals("5")) {
                request.setAttribute(SessionConstants.CURRICULAR_YEARS_5, "5");
            }
        }

        request.setAttribute(SessionConstants.CURRICULAR_YEARS_LIST, curricularYears);

        int index = -1;
        try {
            index = Integer.parseInt((String) examSearchByDegreeAndYearForm.get("index"));
        } catch (NumberFormatException ex) {
            index = -1;
        }

        InfoExecutionDegree infoExecutionDegree = null;
        List infoExamsMap = new ArrayList();

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        
        Object argsReadDegrees[] = { infoExecutionPeriod.getInfoExecutionYear() };
        List executionDegreeList = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYear", argsReadDegrees);
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        if (index != -1) {
            infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(index);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree
                    .getIdInternal().toString());
            infoExamsMap.add(getExamsMap(request, curricularYears, infoExecutionDegree, infoExecutionPeriod));
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        } else {
            // all degrees
            infoExamsMap = getExamsMap(request, curricularYears, executionDegreeList, infoExecutionPeriod);
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        }

        return mapping.findForward("showExamsMap");

    }

    private InfoExamsMap getExamsMap(HttpServletRequest request, List curricularYears,
            InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException, FenixFilterException {
        
        IUserView userView = getUserView(request);
        InfoExamsMap infoRoomExamsMaps = null;

        Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        infoRoomExamsMaps = (InfoExamsMap) ServiceUtils.executeService(userView, "ReadFilteredExamsMap", args);

        return infoRoomExamsMaps;
    }

    private List getExamsMap(HttpServletRequest request, List curricularYears, List executionDegreeList, InfoExecutionPeriod infoExecutionPeriod)
            throws FenixServiceException, FenixFilterException {
        
        IUserView userView = getUserView(request);
        List infoExamsMaps = new ArrayList();

        for (int i = 0; i < executionDegreeList.size(); i++) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(i);
            InfoExamsMap infoRoomExamsMap = null;

            Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };

            infoRoomExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView, "ReadFilteredExamsMap", args);
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
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }
}