package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author tfc130
 */

@Mapping(path = "/prepareViewRoomOcupation", module = "resourceAllocationManager")
@Forwards(value = { @Forward(name = "Sucess", path = "/chooseSemester.jsp") })
public class SelectSemesterAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // TODO: No futuro os periodos (semestres) devem ser lidos da base de
        // dados.
        // TODO: No futuro, os tipos de salas devem ser lidos da BD
        ArrayList types = new ArrayList();
        types.add(new LabelValueBean("Escolher", null));
        types.add(new LabelValueBean("1º semestre", "1"));
        types.add(new LabelValueBean("2º semestre", "2"));
        request.setAttribute("publico.semester", types);

        return mapping.findForward("Sucess");
    }
}