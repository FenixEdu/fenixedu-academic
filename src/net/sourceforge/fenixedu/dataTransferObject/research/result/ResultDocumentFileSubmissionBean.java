package net.sourceforge.fenixedu.dataTransferObject.research.result;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile.FileResultPermittedGroupType;

public class ResultDocumentFileSubmissionBean extends OpenFileBean {
    
    private DomainReference<Result> result;
    private String displayName;
    private FileResultPermittedGroupType permission;

    public ResultDocumentFileSubmissionBean(Result result) {
	setResult(new DomainReference<Result>(result));
	setFileName(null);
	setDisplayName(null);
	setInputStream(null);
	setPermission(FileResultPermittedGroupType.getDefaultType());
    }

    public Result getResult() {
	return result.getObject();
    }

    public void setResult(DomainReference<Result> result) {
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
