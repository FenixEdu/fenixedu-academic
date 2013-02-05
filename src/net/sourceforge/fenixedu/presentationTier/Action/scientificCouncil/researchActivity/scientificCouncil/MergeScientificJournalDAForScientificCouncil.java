package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/mergeScientificJournal", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "show-research-activity-merge-list",
                path = "/scientificCouncil/researchActivity/showScientificJournalsToMergeList.jsp", tileProperties = @Tile(
                        title = "private.administrator.scientificactivities.mergejournals")),
        @Forward(name = "show-research-activity-merge-page",
                path = "/scientificCouncil/researchActivity/showScientificJournalsToMerge.jsp", tileProperties = @Tile(
                        title = "private.administrator.scientificactivities.mergejournals")) })
public class MergeScientificJournalDAForScientificCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.MergeScientificJournalDA {
}