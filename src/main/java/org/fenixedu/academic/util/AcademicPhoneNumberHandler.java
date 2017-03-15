package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.util.PhoneUtil.PhoneNumberHandler;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class AcademicPhoneNumberHandler implements PhoneNumberHandler {

    protected static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();

    private static final Map<Class<? extends PartyContact>, ContactResolver> DEFAULT_RESOLVERS = new HashMap<>();

    static {
        DEFAULT_RESOLVERS.put(EmailAddress.class, (pc) -> ((EmailAddress) pc).getValue());
        DEFAULT_RESOLVERS.put(MobilePhone.class, (pc) -> ((MobilePhone) pc).getNumber());
        DEFAULT_RESOLVERS.put(Phone.class, (pc) -> ((Phone) pc).getNumber());
        DEFAULT_RESOLVERS.put(PhysicalAddress.class, (pc) -> ((PhysicalAddress) pc).getAddress());
        DEFAULT_RESOLVERS.put(WebAddress.class, (pc) -> ((WebAddress) pc).getUrl());
    }

    private static final Collection<PhoneNumberType> MOBILE_NUMBERS = Collections.singletonList(PhoneNumberType.MOBILE);
    private static final Collection<PhoneNumberType> FIXED_NUMBERS = new ArrayList<PhoneNumberType>();

    static {
        FIXED_NUMBERS.add(PhoneNumberType.VOIP);
        FIXED_NUMBERS.add(PhoneNumberType.FIXED_LINE);
    }

    private static ContactResolver getResolver(Class<? extends PartyContact> class1) {
        synchronized (DEFAULT_RESOLVERS) {
            return DEFAULT_RESOLVERS.get(class1);
        }
    }

    private PhoneNumberType getPhoneNumberType(PhoneNumber phoneNumber) {
        return phoneNumber != null ? PHONE_UTIL.getNumberType(phoneNumber) : null;
    }

    @Override
    public boolean isMobileNumber(String numberText) {
        return isType(getPhoneNumberType(parsePhoneNumber(numberText)), MOBILE_NUMBERS);
    }

    @Override
    public boolean isFixedNumber(String numberText) {
        return isType(getPhoneNumberType(parsePhoneNumber(numberText)), FIXED_NUMBERS);
    }

    private boolean isType(PhoneNumberType type, Collection<PhoneNumberType> types) {
        return types.contains(type);
    }

    @Override
    public boolean shouldReceiveValidationSMS(String number) {
        return isMobileNumber(number);
    }

    @Override
    public boolean shouldReceiveValidationCall(String number) {
        return isFixedNumber(number);
    }

    @Override
    public PhoneNumber parsePhoneNumber(String numberText) {
        if (!StringUtils.isEmpty(numberText)) {

            if (numberText.startsWith("00")) {
                numberText = numberText.replaceFirst("00", "+");
            }

            try {
                final PhoneNumber phoneNumber = PHONE_UTIL.parse(numberText, null);
                if (PHONE_UTIL.isValidNumber(phoneNumber)) {
                    return phoneNumber;
                }
            } catch (NumberParseException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getInternationalFormatNumber(String numberText) {
        final PhoneNumber phoneNumber = parsePhoneNumber(numberText);
        return phoneNumber != null ? PHONE_UTIL.format(phoneNumber, PhoneNumberFormat.E164) : null;
    }

    @Override
    public String resolve(PartyContact contact) {
        ContactResolver contactResolver = getResolver(contact.getClass());
        return contactResolver != null ? contactResolver.getPresentationValue(contact) : contact.getPresentationValue();
    }

}
