package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

    public Transaction() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public Double getValue() {
	return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
	setValueBigDecimal(BigDecimal.valueOf(value));
    }

}
