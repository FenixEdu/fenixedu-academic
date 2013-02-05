package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/mergeJournalIssues", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "show-research-activity-merge-list",
                path = "/scientificCouncil/researchActivity/showJournalIssuesToMergeList.jsp", tileProperties = @Tile(
                        title = "private.administrator.scientificactivities.mergejournalissues")),
        @Forward(name = "show-research-activity-merge-page",
                path = "/scientificCouncil/researchActivity/showJournalIssuesToMerge.jsp", tileProperties = @Tile(
                        title = "private.administrator.scientificactivities.mergejournalissues")) })
public class MergeJournalIssuesDAForScientificCouncil extends
        net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity.MergeJournalIssuesDA {
}