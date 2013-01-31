package net.sourceforge.fenixedu.dataTransferObject.documents;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentSearchBean implements Serializable {
	private static final long serialVersionUID = 1360357466011500899L;

	private PersonBean addressee = new PersonBean();

	private PersonBean operator = new PersonBean();

	private GeneratedDocumentType type;

	private LocalDate uploadTime = new LocalDate();

	public PersonBean getAddressee() {
		return addressee;
	}

	public void setAddressee(PersonBean addressee) {
		this.addressee = addressee;
	}

	public boolean hasAddressee() {
		return StringUtils.isNotEmpty(getAddressee().getUsername()) || StringUtils.isNotEmpty(getAddressee().getName())
				|| StringUtils.isNotEmpty(getAddressee().getDocumentIdNumber());
	}

	public PersonBean getOperator() {
		return operator;
	}

	public void setOperator(PersonBean operator) {
		this.operator = operator;
	}

	public boolean hasOperator() {
		return StringUtils.isNotEmpty(getOperator().getUsername()) || StringUtils.isNotEmpty(getOperator().getName())
				|| StringUtils.isNotEmpty(getOperator().getDocumentIdNumber());
	}

	public GeneratedDocumentType getType() {
		return type;
	}

	public void setType(GeneratedDocumentType type) {
		this.type = type;
	}

	public LocalDate getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(LocalDate uploadTime) {
		this.uploadTime = uploadTime;
	}
}
