package net.sourceforge.fenixedu.presentationTier.Action.equivalence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author David Santos in Apr 28, 2004
 */

public class PrepareAplicationContextForEnrollmentEquivalenceAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        String referer = request.getHeader("Referer");
        String forward = null;
        String backLink = null;

        // These 2 request attributes do not exist the first time this action is
        // executed in the use case
        // but are set for the second time the action is executed in this same
        // use case.
        String degreeType = (String) request.getAttribute("degreeType");
        String studentNumber = (String) request.getAttribute("studentNumber");

        if (StringUtils.contains(referer, "degreeAdministrativeOffice")) {
            backLink = "backToDegreeAdministrativeOfficeHome";
            forward = findForward("enrollmentEquivalence", backLink);

            // In case ther was no degreeType attribute in request.
            if (degreeType == null) {
                degreeType = DegreeType.DEGREE.toString();
            }

        } else if (StringUtils.contains(referer, "posGraduacao")) {
            backLink = "backToMasterDegreeAdministrativeOfficeHome";
            forward = findForward("enrollmentEquivalence", backLink);

            // In case ther was no degreeType attribute in request.
            if (degreeType == null) {
                degreeType = DegreeType.MASTER_DEGREE.toString();
            }

        } else if (StringUtils.contains(referer, "coordinator")) {
            backLink = "backToCoordinatorHome";
            forward = findForward("enrollmentEquivalence", backLink);

            // In case ther was no degreeType attribute in request.
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session
                    .getAttribute(SessionConstants.MASTER_DEGREE);
            degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                    .getTipoCurso().toString();
        }

        if ((forward == null) && (backLink == null)) {
            backLink = (String) request.getAttribute("backLink");
            if (backLink == null) {
                backLink = request.getParameter("backLink");
            }

            if (backLink != null) {
                forward = findForward("enrollmentEquivalence", backLink);

                if (forward.equals("enrollmentEquivalenceForDegreeAdministratorOffice")) {
                    // In case ther was no degreeType attribute in request.
                    if (degreeType == null) {
                        degreeType = DegreeType.DEGREE.toString();
                    }
                } else if (forward.equals("enrollmentEquivalenceForMasterDegreeAdministratorOffice")) {
                    // In case ther was no degreeType attribute in request.
                    if (degreeType == null) {
                        degreeType = DegreeType.MASTER_DEGREE.toString();
                    }
                } else if (forward.equals("enrollmentEquivalenceForCoordinator")) {
                    // In case ther was no degreeType attribute in request.
                    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session
                            .getAttribute(SessionConstants.MASTER_DEGREE);
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                            .getTipoCurso().toString();
                }
            }
        }

        if (forward == null) {
            forward = "";
        }

        if (backLink == null) {
            backLink = "";
        }

        request.setAttribute("degreeType", degreeType);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("backLink", backLink);

        return mapping.findForward(forward);
    }

    public static String findForward(String forwardPrefix, String backLink) {
        String forward = null;

        if (backLink.equals("backToDegreeAdministrativeOfficeHome")) {
            forward = forwardPrefix + "ForDegreeAdministratorOffice";
        } else if (backLink.equals("backToMasterDegreeAdministrativeOfficeHome")) {
            forward = forwardPrefix + "ForMasterDegreeAdministratorOffice";
        } else if (backLink.equals("backToCoordinatorHome")) {
            forward = forwardPrefix + "ForCoordinator";
        }

        return forward;
    }

}