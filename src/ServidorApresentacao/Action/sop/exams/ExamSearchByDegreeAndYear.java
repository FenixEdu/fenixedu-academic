/*
 * Created on 5/Fev/2004
 */
package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Ana e Ricardo
 */
public class ExamSearchByDegreeAndYear extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

        List curricularYearsList = new ArrayList();
        curricularYearsList.add("1");
        curricularYearsList.add("2");
        curricularYearsList.add("3");
        curricularYearsList.add("4");
        curricularYearsList.add("5");
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
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " de " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        //  request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
        // executionDegreeList);

        request.setAttribute(SessionConstants.DEGREES, licenciaturas);

        return mapping.findForward("show");

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        //HttpSession session = request.getSession(false);
        DynaActionForm examSearchByDegreeAndYearForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        //SessionUtils.removeAttributtes(session,
        // SessionConstants.CONTEXT_PREFIX);

        String[] selectedCurricularYears = (String[]) examSearchByDegreeAndYearForm
                .get("selectedCurricularYears");

        Boolean selectAllCurricularYears = (Boolean) examSearchByDegreeAndYearForm
                .get("selectAllCurricularYears");

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
        System.out.println("CURRICULAR_YEARS_LIST= " + curricularYears);

        int index = -1;
        try {
            index = Integer.parseInt((String) examSearchByDegreeAndYearForm.get("index"));
        } catch (NumberFormatException ex) {
            index = -1;
        }

        InfoExecutionDegree infoExecutionDegree = null;
        List infoExamsMap = new ArrayList();

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        Object argsReadDegrees[] = { infoExecutionPeriod.getInfoExecutionYear() };
        List executionDegreeList = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYear", argsReadDegrees);
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        if (index != -1) {

            infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(index);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree
                    .getIdInternal().toString());
            infoExamsMap.add(getExamsMap(request, curricularYears, infoExecutionDegree));
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        } else {
            // all degrees
            infoExamsMap = getExamsMap(request, curricularYears, executionDegreeList);
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        }

        return mapping.findForward("showExamsMap");

    }

    /**
     * @param request
     * @param curricularYears
     * @param infoExecutionDegree
     * @return
     */
    private InfoExamsMap getExamsMap(HttpServletRequest request, List curricularYears,
            InfoExecutionDegree infoExecutionDegree) throws FenixActionException {
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        InfoExamsMap infoRoomExamsMaps = null;

        //        for (Iterator iter = curricularYears.iterator(); iter.hasNext();)
        //        {
        //            Integer year = (Integer) iter.next();
        Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        try {
            infoRoomExamsMaps = (InfoExamsMap) ServiceUtils.executeService(userView,
                    "ReadFilteredExamsMap", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        //        }

        return infoRoomExamsMaps;
    }

    /**
     * @param request
     * @param curricularYears
     * @return
     */
    private List getExamsMap(HttpServletRequest request, List curricularYears, List executionDegreeList)
            throws FenixActionException {
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        List infoExamsMaps = new ArrayList();

        for (int i = 0; i < executionDegreeList.size(); i++) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) executionDegreeList.get(i);
            InfoExamsMap infoRoomExamsMap = null;

            Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
            try {
                infoRoomExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView,
                        "ReadFilteredExamsMap", args);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

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

    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            IUserView userView = SessionUtils.getUserView(request);
            infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                    "ReadCurrentExecutionPeriod", new Object[0]);

            request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

}