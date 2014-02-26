package net.sourceforge.fenixedu.presentationTier.Action.vigilancy.examCoordination;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.UnavailablePeriodManagement;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ExamCoordinationApplication.class, path = "unavailability",
        titleKey = "label.vigilancy.unavailablePeriodsShortLabel")
@Mapping(module = "examCoordination", path = "/vigilancy/unavailablePeriodManagement")
@Forwards({
        @Forward(name = "prepareAddPeriodToVigilant", path = "/examCoordinator/vigilancy/addUnavailablePeriodToVigilant.jsp"),
        @Forward(name = "manageUnavailablePeriodsOfVigilants",
                path = "/examCoordinator/vigilancy/manageGroupsUnavailablePeriods.jsp"),
        @Forward(name = "editPeriodOfVigilant", path = "/examCoordinator/vigilancy/editUnavailablePeriodOfVigilant.jsp") })
public class UnavailablePeriodManagementForExamCoordination extends UnavailablePeriodManagement {
}