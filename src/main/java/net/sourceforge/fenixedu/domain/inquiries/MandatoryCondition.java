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
        for (String value : values) {
            result.add(value);
        }
        return result;
    }
    @Deprecated
    public boolean hasMinimumNumberOfChars() {
        return getMinimumNumberOfChars() != null;
    }

    @Deprecated
    public boolean hasConditionValues() {
        return getConditionValues() != null;
    }

    @Deprecated
    public boolean hasShowIfConditionIsFalse() {
        return getShowIfConditionIsFalse() != null;
    }

    @Deprecated
    public boolean hasDependsResult() {
        return getDependsResult() != null;
    }

}
