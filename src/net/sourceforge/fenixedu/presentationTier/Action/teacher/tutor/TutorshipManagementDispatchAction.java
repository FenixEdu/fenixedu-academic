package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "teacher", path = "/tutorSection", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "tutorMenu", path = "/teacher/tutor/help.jsp", tileProperties = @Tile(
        navLocal = "/teacher/commons/navigationBarIndex.jsp", title = "private.teacher.managementmentoring")) })
public class TutorshipManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Person person = getLoggedPerson(request);

        request.setAttribute("tutor", person);
        return mapping.findForward("tutorMenu");
    }
}
