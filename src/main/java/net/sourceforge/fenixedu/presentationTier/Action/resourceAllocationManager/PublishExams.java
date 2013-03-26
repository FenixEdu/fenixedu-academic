package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.SwitchPublishedExamsFlag;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Luis Cruz
 */
@Mapping(module = "resourceAllocationManager", path = "/publishExams", input = "/mainExamsNew.do?method=prepare&page=0",
        attribute = "publishExamsForm", formBean = "publishExamsForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "switch", path = "/mainExamsNew.do?method=prepare&page=0") })
public class PublishExams extends FenixContextDispatchAction {

    public ActionForward switchPublishedState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
        SwitchPublishedExamsFlag.run(academicInterval);

        return mapping.findForward("switch");
    }

}