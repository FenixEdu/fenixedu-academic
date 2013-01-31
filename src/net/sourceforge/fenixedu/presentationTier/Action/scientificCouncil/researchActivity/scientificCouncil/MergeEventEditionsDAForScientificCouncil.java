package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/mergeEventEditions", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(
				name = "show-research-activity-merge-list",
				path = "/scientificCouncil/researchActivity/showEventEditionsToMergeList.jsp",
				tileProperties = @Tile(title = "private.administrator.scientificactivities.mergeeventissues")),
		@Forward(
				name = "show-research-activity-merge-page",
				path = "/scientificCouncil/researchActivity/showEventEditionsToMerge.jsp",
				tileProperties = @Tile(bundle = "SCIENTIFIC_COUNCIL_RESOURCES", title = "title.event.edition.chooseToMerge")) })
public class MergeEventEditionsDAForScientificCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.MergeEventEditionsDA {
}