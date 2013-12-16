/**
 * Project sop 
 * 
 * Package presentationTier.Action.common
 * 
 * Created on 8/Jan/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixDateAndTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.bennu.core.security.Authenticate;

/**
 * @author jpvl
 */
public class ChooseContextDispatchAction extends FenixDateAndTimeDispatchAction {

    protected static final String INFO_DEGREE_INITIALS_PARAMETER = "degreeInitials";

    protected static final String SEMESTER_PARAMETER = "semester";

    protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts
     * .action.ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String inputPage = request.getParameter(PresentationConstants.INPUT_PAGE);
        String nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
        if (inputPage != null) {
            request.setAttribute(PresentationConstants.INPUT_PAGE, inputPage);
        }
        if (nextPage != null) {
            request.setAttribute(PresentationConstants.NEXT_PAGE, nextPage);
        }

        User userView = Authenticate.getUser();

        InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

        // TODO: this semester and curricular year list needs to be
        // refactored in order to incorporate masters
        /* Criar o bean de semestres */
        List semestres = new ArrayList();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 º", "1"));
        semestres.add(new LabelValueBean("2 º", "2"));
        request.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        List anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */

        List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

        List licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_LIST_KEY, executionDegreeList);

        request.setAttribute(PresentationConstants.DEGREES, licenciaturas);

        if (inputPage != null) {
            return mapping.findForward(inputPage);
        }

        // TODO : throw a proper exception
        throw new Exception("SomeOne is messing around with the links");
    }

    public ActionForward preparePublic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String inputPage = request.getParameter(PresentationConstants.INPUT_PAGE);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        // TODO: this semester and curricular year list needs to be refactored
        // in order to incorporate masters
        /* Criar o bean de semestres */
        List semestres = new ArrayList();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 º", "1"));
        semestres.add(new LabelValueBean("2 º", "2"));
        request.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        List anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */

        List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

        List licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute("degreeList", licenciaturas);

        if (inputPage != null) {
            return mapping.findForward(inputPage);
        }

        // TODO : throw a proper exception
        throw new Exception("SomeOne is messing around with the links");

    }

    public ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        String nextPage = (String) request.getAttribute(PresentationConstants.NEXT_PAGE);
        if (nextPage == null) {
            nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
        }

        Integer semestre = ((InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD)).getSemester();
        Integer anoCurricular = (Integer) escolherContextoForm.get("curricularYear");

        int index = Integer.parseInt((String) escolherContextoForm.get("index"));

        request.setAttribute("anoCurricular", anoCurricular);
        request.setAttribute("semestre", semestre);

        List infoExecutionDegreeList =
                ReadExecutionDegreesByExecutionYear.run(((InfoExecutionPeriod) request
                        .getAttribute(PresentationConstants.EXECUTION_PERIOD)).getInfoExecutionYear());
        List licenciaturas = new ArrayList();
        licenciaturas.add(new LabelValueBean("escolher", ""));
        Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        // ////

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index);

        if (infoExecutionDegree != null) {
            CurricularYearAndSemesterAndInfoExecutionDegree cYSiED =
                    new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, infoExecutionDegree);
            request.setAttribute(PresentationConstants.CONTEXT_KEY, cYSiED);

            request.setAttribute(PresentationConstants.CURRICULAR_YEAR_KEY, anoCurricular);
            request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, anoCurricular.toString());
            request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getExternalId().toString());
        } else {
            return mapping.findForward("Licenciatura execucao inexistente");
        }

        if (nextPage != null) {
            return mapping.findForward(nextPage);
        }

        // TODO : throw a proper exception
        throw new Exception("SomeOne is messing around with the links");
    }

    public ActionForward nextPagePublic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        Integer semestre = infoExecutionPeriod.getSemester();
        Integer anoCurricular = (Integer) escolherContextoForm.get("curYear");

        // Integer degreeCurricularPlanId =
        // getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", "");

        // Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", "");

        Integer index = Integer.valueOf((String) escolherContextoForm.get("index"));

        request.setAttribute("curYear", anoCurricular);
        request.setAttribute("semester", semestre);

        List infoExecutionDegreeList;
        infoExecutionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());
        Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index.intValue());
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);

        if (infoExecutionDegree == null) {
            return mapping.findForward("Licenciatura execucao inexistente");
        }

        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

        String nextPage = request.getParameter("nextPage");

        if (nextPage != null) {
            return mapping.findForward(nextPage);
        }

        // TODO : throw a proper exception
        throw new FenixActionException("SomeOne is messing around with the links");

    }

    /**
     * Method existencesOfInfoDegree.
     * 
     * @param executionDegreeList
     * @param infoExecutionDegree
     * @return int
     */
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

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            User userView = Authenticate.getUser();
            infoExecutionPeriod = ReadCurrentExecutionPeriod.run();

            request.setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

}