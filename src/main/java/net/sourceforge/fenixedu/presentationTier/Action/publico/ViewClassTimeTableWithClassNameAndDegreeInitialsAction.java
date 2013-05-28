/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 24/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author tfc130
 * 
 */
@Mapping(module = "publico", path = "/viewClassTimeTableWithClassNameAndDegreeInitialsAction",
        attribute = "classTimeTableWithClassNameAndDegreeInitialsForm",
        formBean = "classTimeTableWithClassNameAndDegreeInitialsForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "/viewClassTimeTable.do") })
public class ViewClassTimeTableWithClassNameAndDegreeInitialsAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.execute(mapping, form, request, response);

        String degreeInitials = request.getParameter("degreeInitials");
        String classIdString = request.getParameter("classId");
        if (degreeInitials == null && classIdString == null) {
            return mapping.getInputForward();
        }

        final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(Integer.valueOf(classIdString));
        final InfoExecutionDegree infoExecutionDegree =
                ReadExecutionDegreesByExecutionYearAndDegreeInitials.getInfoExecutionDegree(schoolClass.getExecutionDegree());
        request.setAttribute("exeDegree", infoExecutionDegree);
        return mapping.findForward("Sucess");
    }
}