package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.InputStream;
import java.io.Serializable;

import org.joda.time.DateTime;

public class ResultsFileBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient InputStream inputStream;
	private DateTime resultsDate;
	private Boolean newResults;

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setResultsDate(DateTime resultsDate) {
		this.resultsDate = resultsDate;
	}

	public DateTime getResultsDate() {
		return resultsDate;
	}

	public void setNewResults(Boolean newResults) {
		this.newResults = newResults;
	}

	public Boolean getNewResults() {
		return newResults;
	}
}
