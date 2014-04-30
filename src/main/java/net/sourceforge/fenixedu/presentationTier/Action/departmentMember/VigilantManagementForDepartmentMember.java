package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberDepartmentApp;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantManagement;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "vigilancies",
        titleKey = "label.navheader.person.vigilant", bundle = "VigilancyResources")
@Mapping(module = "departmentMember", path = "/vigilancy/vigilantManagement")
@Forwards({ @Forward(name = "displayConvokeMap", path = "/departmentMember/vigilancy/manageVigilant.jsp"),
        @Forward(name = "showReport", path = "/departmentMember/vigilancy/showWrittenEvaluationReport.jsp") })
public class VigilantManagementForDepartmentMember extends VigilantManagement {
}