package Util.sms;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Util.FenixValuedEnum;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public final class SmsCommandAuthenticationType extends FenixValuedEnum {

    public static final int NONE_TYPE = 1;

    public static final int USER_PASS_TYPE = 2;

    public static final SmsCommandAuthenticationType NONE = new SmsCommandAuthenticationType("none",
            SmsCommandAuthenticationType.NONE_TYPE);

    public static final SmsCommandAuthenticationType USER_PASS = new SmsCommandAuthenticationType(
            "userpass", SmsCommandAuthenticationType.USER_PASS_TYPE);

    /**
     * @param name
     * @param value
     */
    private SmsCommandAuthenticationType(String name, int value) {
        super(name, value);
    }

    public static SmsCommandAuthenticationType getEnum(String name) {
        return (SmsCommandAuthenticationType) getEnum(SmsCommandAuthenticationType.class, name);
    }

    public static SmsCommandAuthenticationType getEnum(int value) {
        return (SmsCommandAuthenticationType) getEnum(SmsCommandAuthenticationType.class, value);
    }

    public static Map getEnumMap() {
        return getEnumMap(SmsCommandAuthenticationType.class);
    }

    public static List getEnumList() {
        return getEnumList(SmsCommandAuthenticationType.class);
    }

    public static Iterator iterator() {
        return iterator(SmsCommandAuthenticationType.class);
    }

    public String toString() {
        String result = "Sms Command Authentication Type :\n";
        result += "\n  - Sms Command Authentication Type : " + this.getName();

        return result;
    }

}