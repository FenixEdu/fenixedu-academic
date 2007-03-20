package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;

public class MergeScientificJournalPageContainerBean extends MergeResearchActivityPageContainerBean implements Serializable{
    
    private String name;
    private Integer issn;
    private ResearchActivityLocationType researchActivityLocationType;
    private String url;

    
    public MergeScientificJournalPageContainerBean(Comparator<DomainObject> comparator) {
	super(comparator);
    }
    
    public Integer getIssn() {
        return this.issn;
    }

    public void setIssn(Integer issn) {
        this.issn = issn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResearchActivityLocationType getResearchActivityLocationType() {
        return researchActivityLocationType;
    }

    public void setResearchActivityLocationType(ResearchActivityLocationType researchActivityLocationType) {
        this.researchActivityLocationType = researchActivityLocationType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
