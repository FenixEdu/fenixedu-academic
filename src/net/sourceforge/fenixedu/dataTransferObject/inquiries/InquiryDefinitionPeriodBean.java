package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InquiryDefinitionPeriodBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private InquiryTemplate inquiryTemplate;
	private DateTime begin;
	private DateTime end;
	private MultiLanguageString message;
	private Language selectedLanguage;
	private Language previousLanguage;
	private ExecutionSemester executionPeriod;
	private InquiryResponsePeriodType responsePeriodType;
	private boolean changedLanguage = false;

	public InquiryDefinitionPeriodBean() {
		setMessage(new MultiLanguageString());
		setSelectedLanguage(Language.pt);
		setResponsePeriodType(InquiryResponsePeriodType.STUDENT);
	}

	public String getDisplayMessage() {
		if (getMessage() == null) {
			return null;
		}
		return getMessage().getContent(getSelectedLanguage());
	}

	public void setDisplayMessage(String displayMessage) {
		if (getMessage() == null) {
			setMessage(new MultiLanguageString());
		}
		if (isChangedLanguage()) {
			getMessage().setContent(getPreviousLanguage(), displayMessage);
		} else {
			getMessage().setContent(getSelectedLanguage(), displayMessage);
		}
	}

	public DateTime getBegin() {
		return begin;
	}

	public void setBegin(DateTime begin) {
		this.begin = begin;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public void setMessage(MultiLanguageString message) {
		this.message = message;
	}

	public MultiLanguageString getMessage() {
		return message;
	}

	public ExecutionSemester getExecutionPeriod() {
		return executionPeriod;
	}

	public void setExecutionPeriod(ExecutionSemester executionPeriod) {
		this.executionPeriod = executionPeriod;
	}

	public InquiryResponsePeriodType getResponsePeriodType() {
		return responsePeriodType;
	}

	public void setResponsePeriodType(InquiryResponsePeriodType responsePeriodType) {
		this.responsePeriodType = responsePeriodType;
	}

	public void setSelectedLanguage(Language selectedLanguage) {
		setChangedLanguage(false);
		this.previousLanguage = this.selectedLanguage;
		if (this.selectedLanguage != null && this.selectedLanguage != selectedLanguage) {
			setChangedLanguage(true);
		}
		this.selectedLanguage = selectedLanguage;
	}

	public Language getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setChangedLanguage(boolean changedLanguage) {
		this.changedLanguage = changedLanguage;
	}

	public boolean isChangedLanguage() {
		return changedLanguage;
	}

	public Language getPreviousLanguage() {
		return previousLanguage;
	}

	public void setInquiryTemplate(InquiryTemplate inquiryTemplate) {
		this.inquiryTemplate = inquiryTemplate;
	}

	public InquiryTemplate getInquiryTemplate() {
		return inquiryTemplate;
	}

	public static class InquiryPeriodLanguageProvider implements DataProvider {

		@Override
		public Object provide(Object source, Object currentValue) {
			List<Language> languages = new ArrayList<Language>();
			languages.add(Language.pt);
			languages.add(Language.en);
			return languages;
		}

		@Override
		public Converter getConverter() {
			return new Converter() {
				@Override
				public Object convert(Class type, Object value) {
					return Language.valueOf((String) value);

				}
			};
		}
	}

	@Checked("RolePredicates.GEP_PREDICATE")
	@Service
	public void writePeriodAndMessage() {
		if (!getEnd().isAfter(getBegin())) {
			throw new DomainException("error.inquiry.endDateMustBeAfterBeginDate");
		}
		if (getInquiryTemplate() instanceof CurricularCourseInquiryTemplate) {
			for (StudentInquiryTemplate studentInquiryTemplate : StudentInquiryTemplate
					.getInquiryTemplatesByExecutionPeriod(getInquiryTemplate().getExecutionPeriod())) {
				studentInquiryTemplate.setResponsePeriodBegin(getBegin());
				studentInquiryTemplate.setResponsePeriodEnd(getEnd());
			}
		} else {
			getInquiryTemplate().setResponsePeriodBegin(getBegin());
			getInquiryTemplate().setResponsePeriodEnd(getEnd());
		}
		getInquiryTemplate().setInquiryMessage(getMessage());
	}
}
