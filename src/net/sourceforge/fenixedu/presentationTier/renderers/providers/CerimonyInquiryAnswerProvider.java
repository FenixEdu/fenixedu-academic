package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryAnswer;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CerimonyInquiryAnswerProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final List<CerimonyInquiryAnswer> result = new ArrayList<CerimonyInquiryAnswer>();

		final CerimonyInquiryPerson cerimonyInquiryPerson = (CerimonyInquiryPerson) source;
		final CerimonyInquiry cerimonyInquiry = cerimonyInquiryPerson.getCerimonyInquiry();
		result.addAll(cerimonyInquiry.getOrderedCerimonyInquiryAnswer());

		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
