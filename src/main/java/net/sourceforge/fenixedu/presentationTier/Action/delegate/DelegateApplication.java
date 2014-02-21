package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

public class DelegateApplication {

    @StrutsApplication(bundle = "DelegateResources", path = "consult", titleKey = "label.delegates.consult",
            accessGroup = "role(DELEGATE)")
    public static class DelegateConsultApp {

    }

    @StrutsApplication(bundle = "DelegateResources", path = "communication", titleKey = "label.delegates.comunication",
            accessGroup = "role(DELEGATE)")
    public static class DelegateMessagingApp {

    }

    @StrutsApplication(bundle = "DelegateResources", path = "participate", titleKey = "label.participate",
            accessGroup = "role(DELEGATE)")
    public static class DelegateParticipateApp {

    }

    // TODO Fix the access group!
    @StrutsFunctionality(app = DelegateConsultApp.class, path = "evaluations", titleKey = "link.evaluations",
            accessGroup = "anyone")
    @Mapping(path = "/evaluationsForDelegates", module = "delegate")
    public static class EvaluationsForDelegatesAction extends Action {

        @Override
        public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            Student student = AccessControl.getPerson().getStudent();
            if (student != null) {
                PersonFunction function = student.getDelegateFunction();
                if (function != null) {
                    return new ActionForward("/evaluationsForDelegates.faces?degreeID="
                            + function.getUnit().getDegree().getExternalId());
                }
            }
            return null;
        }
    }

}
