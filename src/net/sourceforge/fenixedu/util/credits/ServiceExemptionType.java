/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.util.credits;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author jpvl
 */
public class ServiceExemptionType extends ValuedEnum {
    public static final int TEACHING_EXEMPTION_TYPE = 1;

    public static final int GRANT_OWNER_EQUIVALENCE_TYPE = 2;

    public static final int SABBATICAL_TYPE = 3;

    public static final ServiceExemptionType TEACHING_EXEMPTION = new ServiceExemptionType(
            "service-exemption-type.teacher-exemption", TEACHING_EXEMPTION_TYPE);

    public static final ServiceExemptionType GRANT_OWNER_EQUIVALENCE = new ServiceExemptionType(
            "service-exemption-type.grant-owner-equivalence", GRANT_OWNER_EQUIVALENCE_TYPE);

    public static final ServiceExemptionType SABBATICAL = new ServiceExemptionType(
            "service-exemption-type.sabbatical", SABBATICAL_TYPE);

    protected ServiceExemptionType(String name, int type) {
        super(name, type);
    }

    public static ServiceExemptionType getEnum(String type) {
        return (ServiceExemptionType) getEnum(ServiceExemptionType.class, type);
    }

    public static ServiceExemptionType getEnum(int type) {
        return (ServiceExemptionType) getEnum(ServiceExemptionType.class, type);
    }

    public static Map getEnumMap() {
        return getEnumMap(ServiceExemptionType.class);
    }

    public static List getEnumList() {
        return getEnumList(ServiceExemptionType.class);
    }

    public static Iterator iterator() {
        return iterator(ServiceExemptionType.class);
    }

}