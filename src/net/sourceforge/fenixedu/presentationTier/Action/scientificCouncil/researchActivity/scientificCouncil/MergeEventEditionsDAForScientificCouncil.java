package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.scientificCouncil;

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

@Mapping(module = "scientificCouncil", path = "/mergeEventEditions", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-research-activity-merge-list", path = "/scientificCouncil/researchActivity/showEventEditionsToMergeList.jsp"),
		@Forward(name = "show-research-activity-merge-page", path = "/scientificCouncil/researchActivity/showEventEditionsToMerge.jsp") })
public class MergeEventEditionsDAForScientificCouncil extends net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.MergeEventEditionsDA {
}