package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/manageFinalDegreeWorks", input = "/manageFinalDegreeWorks.do?method=prepare&page=0",
        attribute = "manageFinalDegreeWorksForm", formBean = "manageFinalDegreeWorksForm", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "show-final-degree-works-managment-page",
        path = "/coordinator/finalDegreeWork/manageFinalDegreeWorks.jsp") })
public class ManageFinalDegreeWorksDAForCoordinator extends
        net.sourceforge.fenixedu.presentationTier.Action.coordinator.ManageFinalDegreeWorksDA {
}