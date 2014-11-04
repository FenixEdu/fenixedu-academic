/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Apr 26, 2004
 *
 */
package net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class SibsPaymentFileConstants {

    // Type of record field
    public static final int FIELD_TYPE_OF_RECORD_BEGIN_INDEX = 0;

    public static final int FIELD_TYPE_OF_RECORD_END_INDEX = 1;

    // Date field and components
    public static final int FIELD_TRANSACTION_DATE_BEGIN_INDEX = 15;

    public static final int FIELD_TRANSACTION_DATE_END_INDEX = 27;

    // Payed value field
    public static final int FIELD_PAYED_VALUE_BEGIN_INDEX = 27;

    public static final int FIELD_PAYED_VALUE_END_INDEX = 37;

    // Reference field
    public static final int FIELD_REFERENCE_BEGIN_INDEX = 74;

    public static final int FIELD_REFERENCE_END_INDEX = 83;

    // Year inside reference field
    public static final int FIELD_REFERENCE_YEAR_BEGIN_INDEX = 0;

    public static final int FIELD_REFERENCE_YEAR_END_INDEX = 2;

    public static final int FIELD_REFERENCE_YEAR_MAX_SIZE = 2;

    // Registration number inside reference field
    public static final int FIELD_REFERENCE_STUDENT_NUMBER_BEGIN_INDEX = 2;

    public static final int FIELD_REFERENCE_STUDENT_NUMBER_END_INDEX = 7;

    // Gratuity type inside reference field
    public static final int FIELD_REFERENCE_PAYMENT_CODE_BEGIN_INDEX = 7;

    public static final int FIELD_REFERENCE_PAYMENT_CODE_END_INDEX = 9;

    // Codes for each entry in file
    public static final int HEADER_RECORD_CODE = 0;

    // NOTE: This is the code actually used by SIBS.
    // The number provided by SIBS user manual its not up-to-date
    public static final int DETAIL_RECORD_CODE = 2;

    public static final int FOOTER_RECORD_CODE = 9;

}