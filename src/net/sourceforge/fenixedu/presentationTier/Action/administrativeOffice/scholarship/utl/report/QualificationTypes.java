package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.util.List;

import net.sourceforge.fenixedu.domain.QualificationType;

public class QualificationTypes {

	public static final List<QualificationType> MASTER_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
			QualificationType.INTEGRATED_MASTER_DEGREE, QualificationType.MASTER,
			QualificationType.MASTER_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.MASTER_DEGREE_WITH_RECOGNITION });

	public static boolean isMasterQualificationType(QualificationType type) {
		return MASTER_QUALIFICATION_TYPES.contains(type);
	}

	public static final List<QualificationType> PHD_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
			QualificationType.DOCTORATE_DEGREE, QualificationType.DOCTORATE_DEGREE_BOLOGNA,
			QualificationType.DOCTORATE_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DOCTORATE_DEGREE_WITH_RECOGNITION,
			QualificationType.DOCTORATE_DEGREE_WITH_REGISTER });

	public static boolean isPhdQualificationType(QualificationType type) {
		return PHD_QUALIFICATION_TYPES.contains(type);
	}

	public static final List<QualificationType> DEGREE_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
			QualificationType.BACHELOR_AND_DEGREE, QualificationType.BACHELOR_DEGREE,
			QualificationType.BACHELOR_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DEGREE,
			QualificationType.DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.INTEGRATED_MASTER_DEGREE, });

	public static boolean isDegreeQualificationType(QualificationType type) {
		return DEGREE_QUALIFICATION_TYPES.contains(type);
	}

	public static boolean isCETQualificationType(QualificationType type) {
		return false;
	}

}
