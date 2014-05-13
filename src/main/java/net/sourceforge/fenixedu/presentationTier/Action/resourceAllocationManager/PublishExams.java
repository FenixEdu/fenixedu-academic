package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.SwitchPublishedExamsFlag;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams.MainExamsDA;
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
@Mapping(module = "resourceAllocationManager", path = "/publishExams", functionality = MainExamsDA.class)
@Forwards({ @Forward(name = "switch", path = "/resourceAllocationManager/mainExams.do?method=prepare") })
public class PublishExams extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString(request
                        .getParameter(PresentationConstants.ACADEMIC_INTERVAL));
        SwitchPublishedExamsFlag.run(academicInterval);

        request.setAttribute("academicInterval", academicInterval);

        return mapping.findForward("switch");
    }

}