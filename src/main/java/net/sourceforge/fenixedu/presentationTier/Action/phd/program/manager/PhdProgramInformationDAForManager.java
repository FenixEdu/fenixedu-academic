package net.sourceforge.fenixedu.presentationTier.Action.phd.program.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.phd.program.PhdProgramInformationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramInformation", module = "manager")
@Forwards({ @Forward(name = "listPhdPrograms", path = "/phd/manager/program/information/listPhdPrograms.jsp"),
        @Forward(name = "listPhdProgramInformations", path = "/phd/manager/program/information/listPhdProgramInformations.jsp") })
public class PhdProgramInformationDAForManager extends PhdProgramInformationDA {

    @Override
    public ActionForward listPhdPrograms(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("phdPrograms", Bennu.getInstance().getPhdProgramsSet());
        return mapping.findForward("listPhdPrograms");
    }

}
