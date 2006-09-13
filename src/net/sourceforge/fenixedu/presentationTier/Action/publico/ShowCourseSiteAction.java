package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author T�nia Pous�o Create on 20/Nov/2003
 */
public class ShowCourseSiteAction extends FenixContextDispatchAction {
    
    public ActionForward showCurricularCourseSite(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        //Integer executionPeriodOId = getFromRequest("executionPeriodOID",
        // request);

        DynaActionForm indexForm = (DynaActionForm) actionForm;
        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer index = getFromRequest("index", request);
        request.setAttribute("index", index);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer curricularCourseId = getFromRequest("curricularCourseID", request);
        request.setAttribute("curricularCourseID", curricularCourseId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        Integer curricularYear = (Integer) indexForm.get("curYear");
        
        indexForm.set("indice", indexForm.get("indice"));
        indexForm.set("curYear", curricularYear);
        Object[] args = { curricularCourseId };

        InfoCurriculum infoCurriculum = null;
        try {
            infoCurriculum = (InfoCurriculum) ServiceManagerServiceFactory.executeService(null,
                    "ReadCurriculumByCurricularCourseCode", args);
        } catch (NonExistingServiceException e) {
            errors.add("chosenCurricularCourse", new ActionError(
                    "error.coordinator.chosenCurricularCourse"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculum"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        //order list of execution courses by name
        if (infoCurriculum.getInfoCurricularCourse() != null
                && infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator(
                    "infoExecutionPeriod.infoExecutionYear.beginDate"), true);
            comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.beginDate"), true);

            Collections.sort(infoCurriculum.getInfoCurricularCourse()
                    .getInfoAssociatedExecutionCourses(), comparatorChain);
        }

        //order list by year, semester
        if (infoCurriculum.getInfoCurricularCourse() != null
                && infoCurriculum.getInfoCurricularCourse().getCurricularCourseExecutionScope() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator(
                    "infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoScopes(), comparatorChain);
        }
        infoCurriculum.prepareEnglishPresentation(getLocale(request));
        request.setAttribute("infoCurriculum", infoCurriculum);

        
        return mapping.findForward("showCurricularCourseSite");
       

    }

    public ActionForward showExecutionCourseSite(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionCourseId = getFromRequest("executionCourseID", request);
        request.setAttribute("executionCourseID", executionCourseId);

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args = { commonComponent, firstPageComponent, null, executionCourseId, null, null };
        ExecutionCourseSiteView siteView = null;

        try {
            siteView = (ExecutionCourseSiteView) ServiceManagerServiceFactory.executeService(null,
                    "ExecutionCourseSiteComponentService", args);

            request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent())
                    .getSiteIdInternal());
            request.setAttribute("siteView", siteView);
            request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
            request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getInfoExecutionPeriod().getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                        .getSection());
            }
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showExecutionCourseSite");
    }

}
