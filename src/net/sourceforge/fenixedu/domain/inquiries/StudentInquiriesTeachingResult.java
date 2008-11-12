package net.sourceforge.fenixedu.domain.inquiries;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentInquiriesTeachingResult extends StudentInquiriesTeachingResult_Base {

    public StudentInquiriesTeachingResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isUnsatisfactory() {
	return getUnsatisfactoryResultsAssiduity() || getUnsatisfactoryResultsAuditable()
		|| getUnsatisfactoryResultsPedagogicalCapacity() || getUnsatisfactoryResultsPresencialLearning()
		|| getUnsatisfactoryResultsStudentInteraction();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAssiduity() {
	return super.getUnsatisfactoryResultsAssiduity() != null && super.getUnsatisfactoryResultsAssiduity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAuditable() {
	return super.getUnsatisfactoryResultsAuditable() != null && super.getUnsatisfactoryResultsAuditable();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPedagogicalCapacity() {
	return super.getUnsatisfactoryResultsPedagogicalCapacity() != null && super.getUnsatisfactoryResultsPedagogicalCapacity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPresencialLearning() {
	return super.getUnsatisfactoryResultsPresencialLearning() != null && super.getUnsatisfactoryResultsPresencialLearning();
    }

    @Override
    public Boolean getUnsatisfactoryResultsStudentInteraction() {
	return super.getUnsatisfactoryResultsStudentInteraction() != null && super.getUnsatisfactoryResultsStudentInteraction();
    }

    @Override
    public Boolean getInternalDegreeDisclosure() {
	return super.getInternalDegreeDisclosure() != null && super.getInternalDegreeDisclosure();
    }

    private Double getValueForPresentation(Double value) {
	// TODO: ugly hack, refactor
	if(value == null){
	    return new Double(0);
	}	
	BigDecimal round = new BigDecimal(value);
	round.setScale(2, RoundingMode.HALF_EVEN);
	return round.doubleValue();
    }

    public Double getAverage_P6_1ForPresentation() {
	return getValueForPresentation(super.getAverage_P6_1());
    }

    public Double getStandardDeviation_P6_1ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P6_1());
    }

    public Double getP6_1_greater_3ForPresentation() {
	return getValueForPresentation(super.getP6_1_greater_3());
    }

    public Double getPerc_P6_1_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_1());
    }

    public Double getPerc_P6_1_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_2());
    }

    public Double getPerc_P6_1_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_3());
    }

    public Double getPerc_P6_1_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_4());
    }

    public Double getPerc_P6_1_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_5());
    }

    public Double getPerc_P6_1_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_6());
    }

    public Double getPerc_P6_1_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_7());
    }

    public Double getP6_1_1_aForPresentation() {
	return getValueForPresentation(super.getP6_1_1_a());
    }

    public Double getP6_1_1_bForPresentation() {
	return getValueForPresentation(super.getP6_1_1_b());
    }

    public Double getP6_1_1_cForPresentation() {
	return getValueForPresentation(super.getP6_1_1_c());
    }

    public Double getP6_1_1_dForPresentation() {
	return getValueForPresentation(super.getP6_1_1_d());
    }

    public Double getP6_1_1_eForPresentation() {
	return getValueForPresentation(super.getP6_1_1_e());
    }

    public Double getPerc_P6_1_aForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_a());
    }

    public Double getPerc_P6_1_bForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_b());
    }

    public Double getPerc_P6_1_cForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_c());
    }

    public Double getPerc_P6_1_dForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_d());
    }

    public Double getPerc_P6_1_eForPresentation() {
	return getValueForPresentation(super.getPerc_P6_1_e());
    }

    public Double getAverage_P6_2ForPresentation() {
	return getValueForPresentation(super.getAverage_P6_2());
    }

    public Double getStandardDeviation_P6_2ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P6_2());
    }

    public Double getPerc_P6_2_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_1());
    }

    public Double getPerc_P6_2_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_2());
    }

    public Double getPerc_P6_2_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_3());
    }

    public Double getPerc_P6_2_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_4());
    }

    public Double getPerc_P6_2_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_5());
    }

    public Double getPerc_P6_2_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_6());
    }

    public Double getPerc_P6_2_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_7());
    }

    public Double getPerc_P6_2_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_8());
    }

    public Double getPerc_P6_2_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_2_9());
    }

    public Double getAverage_P6_3ForPresentation() {
	return getValueForPresentation(super.getAverage_P6_3());
    }

    public Double getStandardDeviation_P6_3ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P6_3());
    }

    public Double getPerc_P6_3_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_1());
    }

    public Double getPerc_P6_3_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_2());
    }

    public Double getPerc_P6_3_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_3());
    }

    public Double getPerc_P6_3_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_4());
    }

    public Double getPerc_P6_3_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_5());
    }

    public Double getPerc_P6_3_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_6());
    }

    public Double getPerc_P6_3_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_7());
    }

    public Double getPerc_P6_3_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_8());
    }

    public Double getPerc_P6_3_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P6_3_9());
    }

    public Double getAverage_P7_1ForPresentation() {
	return getValueForPresentation(super.getAverage_P7_1());
    }

    public Double getStandardDeviation_P7_1ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P7_1());
    }

    public Double getPerc_P7_1_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_1());
    }

    public Double getPerc_P7_1_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_2());
    }

    public Double getPerc_P7_1_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_3());
    }

    public Double getPerc_P7_1_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_4());
    }

    public Double getPerc_P7_1_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_5());
    }

    public Double getPerc_P7_1_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_6());
    }

    public Double getPerc_P7_1_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_7());
    }

    public Double getPerc_P7_1_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_8());
    }

    public Double getPerc_P7_1_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_1_9());
    }

    public Double getAverage_P7_2ForPresentation() {
	return getValueForPresentation(super.getAverage_P7_2());
    }

    public Double getStandardDeviation_P7_2ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P7_2());
    }

    public Double getPerc_P7_2_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_1());
    }

    public Double getPerc_P7_2_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_2());
    }

    public Double getPerc_P7_2_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_3());
    }

    public Double getPerc_P7_2_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_4());
    }

    public Double getPerc_P7_2_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_5());
    }

    public Double getPerc_P7_2_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_6());
    }

    public Double getPerc_P7_2_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_7());
    }

    public Double getPerc_P7_2_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_8());
    }

    public Double getPerc_P7_2_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_2_9());
    }

    public Double getAverage_P7_3ForPresentation() {
	return getValueForPresentation(super.getAverage_P7_3());
    }

    public Double getStandardDeviation_P7_3ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P7_3());
    }

    public Double getPerc_P7_3_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_1());
    }

    public Double getPerc_P7_3_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_2());
    }

    public Double getPerc_P7_3_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_3());
    }

    public Double getPerc_P7_3_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_4());
    }

    public Double getPerc_P7_3_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_5());
    }

    public Double getPerc_P7_3_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_6());
    }

    public Double getPerc_P7_3_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_7());
    }

    public Double getPerc_P7_3_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_8());
    }

    public Double getPerc_P7_3_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_3_9());
    }

    public Double getAverage_P7_4ForPresentation() {
	return getValueForPresentation(super.getAverage_P7_4());
    }

    public Double getStandardDeviation_P7_4ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P7_4());
    }

    public Double getPerc_P7_4_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_1());
    }

    public Double getPerc_P7_4_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_2());
    }

    public Double getPerc_P7_4_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_3());
    }

    public Double getPerc_P7_4_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_4());
    }

    public Double getPerc_P7_4_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_5());
    }

    public Double getPerc_P7_4_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_6());
    }

    public Double getPerc_P7_4_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_7());
    }

    public Double getPerc_P7_4_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_8());
    }

    public Double getPerc_P7_4_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P7_4_9());
    }

    public Double getAverage_P8_1ForPresentation() {
	return getValueForPresentation(super.getAverage_P8_1());
    }

    public Double getStandardDeviation_P8_1ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P8_1());
    }

    public Double getPerc_P8_1_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_1());
    }

    public Double getPerc_P8_1_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_2());
    }

    public Double getPerc_P8_1_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_3());
    }

    public Double getPerc_P8_1_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_4());
    }

    public Double getPerc_P8_1_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_5());
    }

    public Double getPerc_P8_1_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_6());
    }

    public Double getPerc_P8_1_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_7());
    }

    public Double getPerc_P8_1_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_8());
    }

    public Double getPerc_P8_1_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_1_9());
    }

    public Double getAverage_P8_2ForPresentation() {
	return getValueForPresentation(super.getAverage_P8_2());
    }

    public Double getStandardDeviation_P8_2ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P8_2());
    }

    public Double getPerc_P8_2_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_1());
    }

    public Double getPerc_P8_2_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_2());
    }

    public Double getPerc_P8_2_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_3());
    }

    public Double getPerc_P8_2_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_4());
    }

    public Double getPerc_P8_2_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_5());
    }

    public Double getPerc_P8_2_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_6());
    }

    public Double getPerc_P8_2_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_7());
    }

    public Double getPerc_P8_2_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_8());
    }

    public Double getPerc_P8_2_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P8_2_9());
    }

    public Double getAverage_P9ForPresentation() {
	return getValueForPresentation(super.getAverage_P9());
    }

    public Double getStandardDeviation_P9ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P9());
    }

    public Double getPerc_P9_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P9_1());
    }

    public Double getPerc_P9_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P9_2());
    }

    public Double getPerc_P9_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P9_3());
    }

}
