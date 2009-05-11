/*
 * StudentCurricularState.java Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.studentCurricularPlan;

import net.sourceforge.fenixedu.util.BundleUtil;

/**
 * @author Nuno Nunes & Joana Mota
 */
public enum StudentCurricularPlanState {

    ACTIVE,

    CONCLUDED,

    INCOMPLETE,

    SCHOOLPARTCONCLUDED,

    INACTIVE,

    PAST;

    public String getName() {
	return name();
    }

    public String getLocalizedName() {
	return BundleUtil.getResourceString("resources.EnumerationResources", name());
    }
}