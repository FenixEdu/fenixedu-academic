package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum SearchBeanType implements IPresentableEnum {

	PERSON_SEARCH_BEAN, OPERATOR_SEARCH_BEAN, UNIT_SEARCH_BEAN;

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources.AccountingResources", "label." + name());
	}

}
