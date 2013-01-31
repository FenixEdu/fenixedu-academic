package net.sourceforge.fenixedu.domain.inquiries;

public class ECTSVisibleCondition extends ECTSVisibleCondition_Base {

	final static public String UC_ECTS_MARKER = "_!##!_";
	final static public String CALCULATED_ECTS_MARKER = "_$%%$_";
	final static public int UC_ECTS_MARKER_INDEX = 0;
	final static public int CALCULATED_ECTS_MARKER_INDEX = 1;

	public ECTSVisibleCondition() {
		super();
	}

	public boolean isVisible(StudentInquiryRegistry studentInquiryRegistry) {
		if (studentInquiryRegistry.getEstimatedECTS() == null) {
			return false;
		}
		if (getWorkLoadExcessive()) {
			Double difference =
					studentInquiryRegistry.getEstimatedECTS()
							- studentInquiryRegistry.getCurricularCourse().getEctsCredits(
									studentInquiryRegistry.getExecutionPeriod());
			return difference > getEctsDifference();
		}
		Double difference =
				studentInquiryRegistry.getCurricularCourse().getEctsCredits(studentInquiryRegistry.getExecutionPeriod())
						- studentInquiryRegistry.getEstimatedECTS();
		return difference > getEctsDifference();
	}

	/**
	 * Returns an Array of strings, with 2 positions,
	 * - the first one is the curricular course ECTS for the given execution period
	 * - the second one is the calculated ECTS from the student answers
	 * 
	 * @param studentInquiryRegistry
	 * @return
	 */
	public String[] getConditionValues(StudentInquiryRegistry studentInquiryRegistry) {
		String[] conditionValues = new String[2];
		conditionValues[UC_ECTS_MARKER_INDEX] =
				studentInquiryRegistry.getCurricularCourse().getEctsCredits(studentInquiryRegistry.getExecutionPeriod())
						.toString();
		conditionValues[CALCULATED_ECTS_MARKER_INDEX] =
				studentInquiryRegistry.getEstimatedECTS() != null ? studentInquiryRegistry.getEstimatedECTS().toString() : null;
		return conditionValues;
	}

}
