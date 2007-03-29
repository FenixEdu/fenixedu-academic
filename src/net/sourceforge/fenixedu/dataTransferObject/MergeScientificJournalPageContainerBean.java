package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;


import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class MergeScientificJournalPageContainerBean extends MergeResearchActivityPageContainerBean implements Serializable{
    
    private String name;
    private String issn;
    private ScopeType researchActivityLocationType;
    private String url;
    private ResearchActivityStage stage;

    
    public String getIssn() {
        return this.issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScopeType getResearchActivityLocationType() {
        return researchActivityLocationType;
    }

    public void setResearchActivityLocationType(ScopeType researchActivityLocationType) {
        this.researchActivityLocationType = researchActivityLocationType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResearchActivityStage getStage() {
        return stage;
    }

    public void setStage(ResearchActivityStage stage) {
        this.stage = stage;
    }


}
