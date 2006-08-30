package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile.ResultDocumentFilePermissionType;

public class ResultDocumentFileSubmissionBean implements Serializable {
    private DomainReference<Result> result;
    
    private transient InputStream inputStream;

    private String fileName;
    
    private String displayName;
    
    private ResultDocumentFilePermissionType permission;

    public ResultDocumentFileSubmissionBean(Result result) {
	setResult(new DomainReference<Result>(result));
	setFileName(null);
	setDisplayName(null);
	setInputStream(null);
	setPermission(ResultDocumentFilePermissionType.getDefaultType());
    }

    public Result getResult() {
	return result.getObject();
    }

    public void setResult(DomainReference<Result> result) {
	this.result = result;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ResultDocumentFilePermissionType getPermission() {
        return permission;
    }

    public void setPermission(ResultDocumentFilePermissionType permission) {
        this.permission = permission;
    }
}
