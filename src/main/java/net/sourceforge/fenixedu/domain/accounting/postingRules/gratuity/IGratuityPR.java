package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.Money;

public interface IGratuityPR {

    public Money getDefaultGratuityAmount(ExecutionYear executionYear);

}
