package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.UnavailablePeriodManagement;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/vigilancy/unavailablePeriodManagement",
        functionality = VigilantManagementForDepartmentMember.class)
@Forwards({ @Forward(name = "editUnavailablePeriod", path = "/departmentMember/vigilancy/editUnavailablePeriod.jsp"),
        @Forward(name = "addedUnavailablePeriod", path = "/departmentMember/vigilancy/manageVigilant.jsp"),
        @Forward(name = "addUnavailablePeriod", path = "/departmentMember/vigilancy/createUnavailable.jsp"),
        @Forward(name = "deleteUnavailablePeriod", path = "/departmentMember/vigilancy/manageVigilant.jsp") })
public class UnavailablePeriodManagementForDepartmentMember extends UnavailablePeriodManagement {
}