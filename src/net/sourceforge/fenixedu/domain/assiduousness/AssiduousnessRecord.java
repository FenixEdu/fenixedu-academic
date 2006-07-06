package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class AssiduousnessRecord extends AssiduousnessRecord_Base {

    public static final Comparator<AssiduousnessRecord> COMPARATORY_BY_DATE = new BeanComparator("date");

	public AssiduousnessRecord() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
