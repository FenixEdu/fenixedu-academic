package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 */
public class ViewClassesWithShift extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        try {
            //DynaValidatorForm shiftForm = (DynaValidatorForm) form;
            //String name = (String) shiftForm.get("name");
            //String name = request.getParameter("name");
            InfoShift infoShift = getInfoShift(request);

            Object[] args = { infoShift };
            List infoClasses = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadClassesWithShiftService", args);

            if (infoClasses == null || infoClasses.isEmpty()) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("message.shift.no.classes", new ActionError("message.shift.no.classes",
                        infoShift.getNome()));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            Collections.sort(infoClasses, new BeanComparator("nome"));
            request.setAttribute("classesWithShift", infoClasses);

            return mapping.findForward("sucess");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method getInfoShift.
     * 
     * @param name
     * @param request
     * @return InfoShift
     */
    private InfoShift getInfoShift(HttpServletRequest request) throws Exception {
        Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        Object args[] = { shiftOID };
        InfoShift infoShift = (InfoShift) ServiceManagerServiceFactory.executeService(userView,
                "ReadShiftByOID", args);

        if (infoShift == null) {
            throw new IllegalStateException("O turno pretendido não existe");
        }

        return infoShift;
    }
}