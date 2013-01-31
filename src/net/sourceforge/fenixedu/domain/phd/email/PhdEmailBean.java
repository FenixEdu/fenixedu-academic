package net.sourceforge.fenixedu.domain.phd.email;

import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

public abstract class PhdEmailBean implements java.io.Serializable {

	private static int MAX_EMAILS_PER_LINE = 5;
	private static final long serialVersionUID = 1L;

	protected String bccs;
	protected String subject;
	protected String message;
	protected DateTime creationDate;
	protected Person creator;

	public String getBccs() {
		return bccs;
	}

	public void setBccs(String bccs) {
		this.bccs = bccs;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public String getBccsView() {
		StringTokenizer tokenizer = new StringTokenizer(getBccs(), ",");
		StringBuilder result = new StringBuilder();
		int emailsCurrentLine = 0;
		while (tokenizer.hasMoreTokens()) {
			if (emailsCurrentLine == MAX_EMAILS_PER_LINE) {
				result.append('\n');
				emailsCurrentLine = 0;
			}
			result.append(tokenizer.nextToken());
			result.append(',');
			emailsCurrentLine++;
		}
		if (!StringUtils.isEmpty(result.toString())) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}
}
