/**
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : -Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 * modified by Fernanda Quit�rio
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator.ReadDegreeCandidates;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CoordinatedDegreeInfo extends FenixAction {

    public static void setCoordinatorContext(final HttpServletRequest request) {
        final Integer degreeCurricularPlanOID = findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            final DegreeCurricularPlan degreeCurricularPlan =
                    rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
            if (degreeCurricularPlan != null) {
                final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);

                final List<InfoMasterDegreeCandidate> infoMasterDegreeCandidates =
                        ReadDegreeCandidates.run(degreeCurricularPlanOID);
                request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT,
                        Integer.valueOf(infoMasterDegreeCandidates.size()));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException {
        setCoordinatorContext(request);
        CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate.getCurrentTemplate();
        if (coordinatorInquiryTemplate != null) {
            final InfoExecutionDegree infoExecutionDegree =
                    (InfoExecutionDegree) request.getAttribute(PresentationConstants.MASTER_DEGREE);
            if (infoExecutionDegree != null
                    && infoExecutionDegree.getExecutionDegree().getCoordinatorByTeacher(AccessControl.getPerson()) != null) {
                return new ActionForward("/viewInquiriesResults.do?method=prepare");
            }
        }
        return mapping.findForward("Success");
    }

    public static Integer findDegreeCurricularPlanID(HttpServletRequest request) {
        final Integer degreeCurricularPlanID;

        String paramValue = request.getParameter("degreeCurricularPlanID");
        if (!StringUtils.isEmpty(paramValue)) {
            degreeCurricularPlanID = Integer.valueOf(paramValue);
        } else {
            Object attribute = request.getAttribute("degreeCurricularPlanID");

            if (attribute != null && attribute instanceof Integer) {
                return (Integer) attribute;
            }
            paramValue = (String) attribute;
            degreeCurricularPlanID = StringUtils.isEmpty(paramValue) ? null : Integer.valueOf(paramValue);
        }

        return degreeCurricularPlanID;
    }

    /* uses external ids */
    public static void newSetCoordinatorContext(final HttpServletRequest request) {
        final String degreeCurricularPlanOID = newFindDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            final DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.fromExternalId(degreeCurricularPlanOID);
            if (degreeCurricularPlan != null) {
                final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);

                final List<InfoMasterDegreeCandidate> infoMasterDegreeCandidates =
                        ReadDegreeCandidates.run(degreeCurricularPlan.getIdInternal());
                request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT,
                        Integer.valueOf(infoMasterDegreeCandidates.size()));
            }
        }

        final String executionDegreePlanOID = newFindExecutionDegreeID(request);
        if (executionDegreePlanOID != null) {
            request.setAttribute("executionDegreeOID", executionDegreePlanOID);
            ExecutionDegree executionDegree = DomainObject.fromExternalId(executionDegreePlanOID);
            request.setAttribute("executionDegree", executionDegree);
        }
    }

    /* uses external ids */
    private static String newFindDegreeCurricularPlanID(HttpServletRequest request) {
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        if (degreeCurricularPlanID == null) {
            degreeCurricularPlanID = (String) request.getAttribute("degreeCurricularPlanID");
        }
        return degreeCurricularPlanID;
    }

    private static String newFindExecutionDegreeID(HttpServletRequest request) {
        String executionDegreePlanOID = request.getParameter("executionDegreeOID");
        if (executionDegreePlanOID == null) {
            executionDegreePlanOID = (String) request.getAttribute("executionDegreeOID");
        }
        return executionDegreePlanOID;
    }

}