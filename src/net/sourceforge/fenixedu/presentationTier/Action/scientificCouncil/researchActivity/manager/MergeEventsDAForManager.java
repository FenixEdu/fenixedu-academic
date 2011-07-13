package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.manager;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/mergeEvents", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-research-activity-merge-list", path = "/manager/researchActivity/showEventsToMergeList.jsp"),
		@Forward(name = "show-research-activity-merge-page", path = "/manager/researchActivity/showEventsToMerge.jsp") })
public class MergeEventsDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.MergeEventsDA {
}