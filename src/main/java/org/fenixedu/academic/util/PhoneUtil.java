/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util;

import org.fenixedu.academic.domain.contacts.PartyContact;

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneUtil {

    public interface PhoneNumberHandler {

        public PhoneNumber parsePhoneNumber(String number);

        public boolean shouldReceiveValidationCall(String number);

        public boolean shouldReceiveValidationSMS(String number);

        public boolean isMobileNumber(String number);

        public boolean isFixedNumber(String number);

        public String getInternationalFormatNumber(String numberText);

        public String resolve(PartyContact contact);

    }

    private static PhoneNumberHandler PHONE_NUMBER_HANDLER = new AcademicPhoneNumberHandler();

    public static PhoneNumber getPhoneNumber(String numberText) {
        return PHONE_NUMBER_HANDLER.parsePhoneNumber(numberText);
    }

    public static String getInternacionalFormatNumber(String numberText) {
        return PHONE_NUMBER_HANDLER.getInternationalFormatNumber(numberText);
    }

    public static boolean isValidNumber(String numberText) {
        final PhoneNumber phoneNumber = getPhoneNumber(numberText);
        return phoneNumber != null;
    }

    public static boolean isMobileNumber(String numberText) {
        return PHONE_NUMBER_HANDLER.isMobileNumber(numberText);
    }

    public static boolean isFixedNumber(String numberText) {
        return PHONE_NUMBER_HANDLER.isFixedNumber(numberText);
    }

    /**
     * This function is never used
     */
    @Deprecated
    public static int getCountry(String numberText) {
        final PhoneNumber phoneNumber = getPhoneNumber(numberText);
        if (phoneNumber != null) {
            return phoneNumber.getCountryCode();
        }
        return -1;
    }

    public static boolean shouldReceiveValidationCall(String numberText) {
        return PHONE_NUMBER_HANDLER.shouldReceiveValidationCall(numberText);
    }

    public static boolean shouldReceiveValidationSMS(String numberText) {
        return PHONE_NUMBER_HANDLER.shouldReceiveValidationSMS(numberText);
    }

    public static void setPhoneNumberHandler(PhoneNumberHandler handler) {
        PHONE_NUMBER_HANDLER = handler;
    }

    public static String resolve(PartyContact contact) {
        return PHONE_NUMBER_HANDLER.resolve(contact);
    }
}
