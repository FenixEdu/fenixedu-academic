package net.sourceforge.fenixedu.presentationTier.Action.coordinator.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/manageFinalDegreeWorks",
        input = "/manageFinalDegreeWorks.do?method=prepare&page=0", attribute = "manageFinalDegreeWorksForm",
        formBean = "manageFinalDegreeWorksForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-final-degree-works-managment-page",
        path = "/departmentAdmOffice/finalDegreeWork/manageFinalDegreeWorks.jsp") })
public class ManageFinalDegreeWorksDAForDepartmentAdmOffice extends
        net.sourceforge.fenixedu.presentationTier.Action.coordinator.ManageFinalDegreeWorksDA {
}