package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.result.Result;

public class ResultDocumentFileSubmissionBean implements Serializable {
    private DomainReference<Result> result;
    private String filename;
    private transient InputStream inputStream;
    
    public ResultDocumentFileSubmissionBean(Result resultReference) {
        super();
        this.result = new DomainReference<Result>(resultReference); 
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    public Result getResult() {
        return (this.result == null) ? null : this.result.getObject();
    }
    
    public void setResult(Result result) {
        this.result = (result != null) ? new DomainReference<Result>(result) : null;
    }
}
