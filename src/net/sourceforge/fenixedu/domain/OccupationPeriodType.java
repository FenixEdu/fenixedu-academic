package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public enum OccupationPeriodType implements IPresentableEnum {

	LESSONS, EXAMS, GRADE_SUBMISSION, EXAMS_SPECIAL_SEASON, GRADE_SUBMISSION_SPECIAL_SEASON;

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources.ResourceAllocationManagerResources",
				"label.occupation.period.type." + name());
	}

}
