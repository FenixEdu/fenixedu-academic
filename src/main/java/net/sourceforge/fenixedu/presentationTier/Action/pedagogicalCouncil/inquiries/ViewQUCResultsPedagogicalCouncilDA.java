/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/viewQucResults", module = "pedagogicalCouncil")
@Forwards({
        @Forward(name = "chooseDepartment", path = "/pedagogicalCouncil/inquiries/chooseDepartment.jsp", tileProperties = @Tile(
                title = "private.pedagogiccouncil.control.qucresults2")),
        @Forward(name = "viewResumeResults", path = "/departmentMember/quc/viewResumeResults.jsp"),
        @Forward(name = "viewCompetenceResults", path = "/departmentMember/quc/viewCompetenceResults.jsp"),
        @Forward(name = "viewTeachersResults", path = "/departmentMember/quc/viewTeachersResults.jsp"),
        @Forward(name = "departmentUCView", path = "/departmentMember/quc/departmentUCView.jsp"),
        @Forward(name = "departmentTeacherView", path = "/departmentMember/quc/departmentTeacherView.jsp") })
public class ViewQUCResultsPedagogicalCouncilDA extends ViewQUCResultsDA {

    public ActionForward chooseDepartment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<DepartmentUnit> departmentUnits = new ArrayList<DepartmentUnit>();
        for (DepartmentUnit departmentUnit : DepartmentUnit.readAllDepartmentUnits()) {
            if (departmentUnit.getDepartment() != null) {
                departmentUnits.add(departmentUnit);
            }
        }
        Collections.sort(departmentUnits, new BeanComparator("name"));
        request.setAttribute("departments", departmentUnits);
        return mapping.findForward("chooseDepartment");
    }

    @Override
    public ActionForward showTeacherResultsAndComments(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("showAllComments", "true");
        return super.showTeacherResultsAndComments(actionMapping, actionForm, request, response);
    }

    @Override
    protected DepartmentUnit getDepartmentUnit(HttpServletRequest request) {
        String departmentUnitOID = request.getParameter("departmentUnitOID");
        if (StringUtils.isEmpty(departmentUnitOID)) {
            DepartmentExecutionSemester departmentExecutionSemester = getRenderedObject("executionSemesterBean");
            departmentUnitOID = departmentExecutionSemester.getDepartmentUnitOID();
        }
        DepartmentUnit departmentUnit = AbstractDomainObject.fromExternalId(departmentUnitOID);
        request.setAttribute("fromPedagogicalCouncil", "true");
        request.setAttribute("departmentName", departmentUnit.getName());
        return departmentUnit;
    }

    @Override
    public boolean getShowAllComments() {
        return true;
    }
}
