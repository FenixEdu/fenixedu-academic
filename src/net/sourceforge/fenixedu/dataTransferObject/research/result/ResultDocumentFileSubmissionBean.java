package net.sourceforge.fenixedu.dataTransferObject.research.result;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;

public class ResultDocumentFileSubmissionBean extends OpenFileBean {
    
    private DomainReference<ResearchResult> result;
    private String displayName;
    private FileResultPermittedGroupType permission;

    public ResultDocumentFileSubmissionBean(ResearchResult result) {
	setResult(new DomainReference<ResearchResult>(result));
	setFileName(null);
	setDisplayName(null);
	setInputStream(null);
	setPermission(FileResultPermittedGroupType.getDefaultType());
    }

    public ResearchResult getResult() {
	return result.getObject();
    }

    public void setResult(DomainReference<ResearchResult> result) {
	this.result = result;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public FileResultPermittedGroupType getPermission() {
        return permission;
    }

    public void setPermission(FileResultPermittedGroupType permission) {
        this.permission = permission;
    }
}
