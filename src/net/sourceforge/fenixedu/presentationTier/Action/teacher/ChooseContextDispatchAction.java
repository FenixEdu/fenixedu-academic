/**
 * 
 * Package presentationTier.Action.teacher
 * 
 * Created on 17/set/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author joaosa & rmalo
 */
public class ChooseContextDispatchAction extends FenixDateAndTimeDispatchAction {

    protected static final String INFO_DEGREE_INITIALS_PARAMETER = "degreeInitials";

    protected static final String SEMESTER_PARAMETER = "semester";

    protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {

            String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);
            String nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
            if (inputPage != null)
                request.setAttribute(SessionConstants.INPUT_PAGE, inputPage);
            if (nextPage != null)
                request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);

            IUserView userView = SessionUtils.getUserView(request);

            InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

            // TODO: this semester and curricular year list needs to be
            // refactored in order to incorporate masters
            /* Criar o bean de semestres */
            ArrayList semestres = new ArrayList();
            semestres.add(new LabelValueBean("escolher", ""));
            semestres.add(new LabelValueBean("1 º", "1"));
            semestres.add(new LabelValueBean("2 º", "2"));
            request.setAttribute("semestres", semestres);

            /* Criar o bean de anos curricutares */
            ArrayList anosCurriculares = new ArrayList();
            anosCurriculares.add(new LabelValueBean("escolher", ""));
            anosCurriculares.add(new LabelValueBean("1 º", "1"));
            anosCurriculares.add(new LabelValueBean("2 º", "2"));
            anosCurriculares.add(new LabelValueBean("3 º", "3"));
            anosCurriculares.add(new LabelValueBean("4 º", "4"));
            anosCurriculares.add(new LabelValueBean("5 º", "5"));
            request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, anosCurriculares);

            /* Cria o form bean com as licenciaturas em execucao. */
            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

            List executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

            ArrayList licenciaturas = new ArrayList();

            licenciaturas.add(new LabelValueBean("escolher", ""));

            Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

            Iterator iterator = executionDegreeList.iterator();

            int index = 0;
            while (iterator.hasNext()) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
                String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                        .getNome();

                name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                        .toString()
                        + " em " + name;

                name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

                licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
            }

            request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY, executionDegreeList);

            request.setAttribute(SessionConstants.DEGREES, licenciaturas);

            if (inputPage != null) {
                return mapping.findForward(inputPage);
            }

            // TODO : throw a proper exception
            throw new Exception("SomeOne is messing around with the links");
        }

        throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao

    }

    public ActionForward preparePublic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        ISiteComponent shiftsAndGroupsView = new InfoSiteShiftsAndGroups();
        readSiteView(request, shiftsAndGroupsView, null, groupPropertiesCode, null);

        // TODO: this semester and curricular year list needs to be refactored
        // in order to incorporate masters
        /* Criar o bean de semestres */
        ArrayList semestres = new ArrayList();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 º", "1"));
        semestres.add(new LabelValueBean("2 º", "2"));
        request.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        ArrayList anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        ArrayList licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
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

    public ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        // SessionUtils.removeAttributtes(session,
        // SessionConstants.CONTEXT_PREFIX);

        String nextPage = (String) request.getAttribute(SessionConstants.NEXT_PAGE);
        if (nextPage == null) {
            nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
        }

        if (session != null) {
            Integer semestre = ((InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD)).getSemester();
            Integer anoCurricular = (Integer) escolherContextoForm.get("curricularYear");

            int index = Integer.parseInt((String) escolherContextoForm.get("index"));

            request.setAttribute("anoCurricular", anoCurricular);
            request.setAttribute("semestre", semestre);

            // List infoExecutionDegreeList = (List)
            // session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
            Object argsLerLicenciaturas[] = { ((InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD)).getInfoExecutionYear() };
            List infoExecutionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
            ArrayList licenciaturas = new ArrayList();
            licenciaturas.add(new LabelValueBean("escolher", ""));
            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());
            // ////

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList
                    .get(index);

            if (infoExecutionDegree != null) {
                CurricularYearAndSemesterAndInfoExecutionDegree cYSiED = new CurricularYearAndSemesterAndInfoExecutionDegree(
                        anoCurricular, semestre, infoExecutionDegree);
                request.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);

                request.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, anoCurricular);
                request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, anoCurricular.toString());
                request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree
                        .getIdInternal().toString());
            } else {
                return mapping.findForward("Licenciatura execucao inexistente");
            }

            // String nextPage = (String)
            // request.getAttribute(SessionConstants.NEXT_PAGE);
            if (nextPage != null) {
                return mapping.findForward(nextPage);
            }

            // TODO : throw a proper exception
            throw new Exception("SomeOne is messing around with the links");
        }

        throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao

    }

    public ActionForward nextPagePublic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(true);
        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Integer semestre = infoExecutionPeriod.getSemester();
        Integer anoCurricular = (Integer) escolherContextoForm.get("curYear");

        // Integer degreeCurricularPlanId =
        // getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", "");

        // Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", "");

        Integer index = new Integer((String) escolherContextoForm.get("index"));

        request.setAttribute("curYear", anoCurricular);
        request.setAttribute("semester", semestre);

        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List infoExecutionDegreeList;
        try {
            infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList
                .get(index.intValue());
        /*
         * InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
         * Iterator iterator = infoExecutionDegreeList.iterator(); while
         * (iterator.hasNext()) { infoExecutionDegree= (InfoExecutionDegree)
         * iterator.next(); if
         * (infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId)) {
         * request.setAttribute(SessionConstants.EXECUTION_DEGREE,
         * infoExecutionDegree);
         * request.setAttribute("executionDegreeID",infoExecutionDegree.getIdInternal().toString());
         * request.setAttribute("infoDegreeCurricularPlan",
         * infoExecutionDegree); break; } }
         */
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
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    // TODO When session is removed from SOP, use method with same name from
    // RequestUtils
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

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Object obj1, Object obj2) throws FenixActionException,
            FenixFilterException {

        HttpSession session = getSession(request);

        IUserView userView = getUserView(request);

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            objectCode = getObjectCode(request);
            infoExecutionCourseCode = objectCode;
        }

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { infoExecutionCourseCode, commonComponent, firstPageComponent, objectCode,
                obj1, obj2 };

        try {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                    .executeService(userView, "TeacherAdministrationSiteComponentService", args);
            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                        .getSection());
            }

            return siteView;

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

}