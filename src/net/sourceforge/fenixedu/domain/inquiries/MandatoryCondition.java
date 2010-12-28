package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.List;

public class MandatoryCondition extends MandatoryCondition_Base {

    public MandatoryCondition() {
	super();
    }

    public List<String> getConditionValuesAsList() {
	List<String> result = new ArrayList<String>();
	String[] values = getConditionValues().split("_#_");
	for (int iter = 0; iter < values.length; iter++) {
	    result.add(values[iter]);
	}
	return result;
    }
}
