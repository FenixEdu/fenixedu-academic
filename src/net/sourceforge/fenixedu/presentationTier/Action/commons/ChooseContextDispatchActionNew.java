package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author jpvl
 */
public class ChooseContextDispatchActionNew extends FenixDateAndTimeDispatchAction {

    protected static final String INFO_DEGREE_INITIALS_PARAMETER = "degreeInitials";

    protected static final String SEMESTER_PARAMETER = "semester";

    protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession(false);

        if (session != null) {
            String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);
            if (inputPage != null) {
                request.setAttribute(SessionConstants.INPUT_PAGE, inputPage);
            }
            
            String nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
            if (nextPage != null) {
                request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);
            }

            setExecutionContext(request);

            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            Integer degreeId = getFromRequest("degreeID", request);
            request.setAttribute("degreeID", degreeId);

            // lista
            List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
            if (executionPeriodsLabelValueList.size() > 1) {
                request.setAttribute("lista", executionPeriodsLabelValueList);
            } else {
                request.removeAttribute("lista");
            }

            return mapping.findForward("prepare");

        } else {
            throw new Exception();    
        }
    }

    // TODO When session is removed from SOP, use method with same name from RequestUtils
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            IUserView userView = SessionUtils.getUserView(request);
            infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadCurrentExecutionPeriod", new Object[0]);
            request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

    public ActionForward preparePublic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        // TODO: this semester and curricular year list needs to be refactored
        // in order to incorporate masters
        /* Criar o bean de semestres */
        List<LabelValueBean> semestres = new ArrayList<LabelValueBean>();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 �", "1"));
        semestres.add(new LabelValueBean("2 �", "2"));
        request.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        List<LabelValueBean> anosCurriculares = new ArrayList<LabelValueBean>();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 �", "1"));
        anosCurriculares.add(new LabelValueBean("2 �", "2"));
        anosCurriculares.add(new LabelValueBean("3 �", "3"));
        anosCurriculares.add(new LabelValueBean("4 �", "4"));
        anosCurriculares.add(new LabelValueBean("5 �", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        List<LabelValueBean> licenciaturas = new ArrayList<LabelValueBean>();

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

    public ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

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

            Object argsLerLicenciaturas[] = { ((InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD)).getInfoExecutionYear() };
            List infoExecutionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
            List<LabelValueBean> licenciaturas = new ArrayList<LabelValueBean>();
            licenciaturas.add(new LabelValueBean("escolher", ""));
            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

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

            if (nextPage != null) {
                return mapping.findForward(nextPage);
            }

            // TODO : throw a proper exception
            throw new Exception("SomeOne is messing around with the links");
        }

        throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao
    }

    public ActionForward nextPagePublic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession(false);
        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);
        
        final ActionErrors errors = new ActionErrors();
        final DynaActionForm escolherContextoForm = (DynaActionForm) form;

        // degreeID
        final Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        // degreeCurricularPlanID
        final Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        // lista
        final List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute("lista", executionPeriodsLabelValueList);
        } else {
            request.removeAttribute("lista");
        }
        
        // curYear
        final Integer anoCurricular = (Integer) escolherContextoForm.get("curYear");
        request.setAttribute("curYear", anoCurricular);
        
        // infoDegreeCurricularPlan
        if (degreeCurricularPlanId != null) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
            
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan);
            infoDegreeCurricularPlan.prepareEnglishPresentation(getLocale(request));
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        }
        
        // indice, SessionConstants.EXECUTION_PERIOD, SessionConstants.EXECUTION_PERIOD_OID
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        Integer executionPeriodID = (Integer) escolherContextoForm.get("indice");
        if (executionPeriodID == null) {
            executionPeriodID = getFromRequest("indice", request);
        }
        if (executionPeriodID != null) {
            try {
                final Object args[] = { executionPeriodID };
                infoExecutionPeriod = (InfoExecutionPeriod) ServiceManagerServiceFactory.executeService(null, "ReadExecutionPeriodByOID", args);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
        }
        request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
        escolherContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
        RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
        request.setAttribute("semester", infoExecutionPeriod.getSemester());
        
        // SessionConstants.EXECUTION_DEGREE, executionDegreeID, infoExecutionDegree
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionPeriod.getExecutionYear());
        if (executionDegree == null) {
            executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
            
            if (executionDegree != null) {
                infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear().readExecutionPeriodForSemester(1));
                request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
                escolherContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
                RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
                request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
                request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
                request.setAttribute("semester", infoExecutionPeriod.getSemester());
            }
        }
        
        if (executionDegree != null) {
            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear.newInfoFromDomain(executionDegree);
            request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal().toString());
            RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
            
            infoExecutionDegree.getInfoDegreeCurricularPlan().prepareEnglishPresentation(getLocale(request));
            request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree.getInfoDegreeCurricularPlan());
            request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree.getInfoDegreeCurricularPlan());
        }

        String nextPage = request.getParameter("nextPage");
        if (nextPage != null) {
            return mapping.findForward(nextPage);
        } else {
            throw new FenixActionException();
        }
        
    }

}
