package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.erasmus;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class NationalIdCardAvoidanceProvider implements DataProvider {

    public Converter getConverter() {
	return new EnumConverter();
    }

    public Object provide(Object arg0, Object arg1) {
	return Arrays.asList(new NationalIdCardAvoidanceQuestion[] {
		NationalIdCardAvoidanceQuestion.COUNTRY_NOT_LISTED_IN_FENIX_AUTHENTICATION,
		NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_CODES_UNKNOWN,
		NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_SUBMISSION_AVAILABILITY_UNKNOWN,
		NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_SUBMISSION_TRUST_LACK,
		NationalIdCardAvoidanceQuestion.NOT_OWNER_ELECTRONIC_ID_CARD, NationalIdCardAvoidanceQuestion.OTHER_REASON });
    }

}
