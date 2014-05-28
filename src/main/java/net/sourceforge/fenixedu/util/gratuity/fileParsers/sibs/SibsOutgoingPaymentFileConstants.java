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
package net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class SibsOutgoingPaymentFileConstants {

    // Header Register Type (usually 0)
    public static final String HEADER_REGISTER_TYPE = "0";

    // Footer Register Type (usually 9)
    public static final String FOOTER_REGISTER_TYPE = "9";

    // Line Register Type (usually 1)
    public static final String LINE_REGISTER_TYPE = "1";

    // File Type
    public static final String FILE_TYPE = "AEPS";

    // ID. Source Institution
    public static final String ID_SOURCE_INSTITUTION = "90000820";

    // ID. Destination Institution
    public static final String ID_DESTINATION_INSTITUTION = "50000000";

    // Entity
    public static final String ENTITY = "20821";

    // Currency Code
    public static final String CURRENCY_CODE = "978";

    // White Spaces in Header
    public static final int WHITE_SPACES_IN_HEADER = 3;

    // White Spaces in LINE
    public static final int WHITE_SPACES_IN_LINE = 2;

    // White Spaces in Footer
    public static final int WHITE_SPACES_IN_FOOTER = 41;

    // Omission sequence number
    public static final String OMISSION_SEQUENCE_NUMBER = "1";

    // Space char
    public static final char SPACE_CHAR = ' ';

    // Zero char
    public static final char ZERO_CHAR = '0';

    // Number of lines record descriptor length
    public static final int NUMBER_OF_LINES_DESCRIPTOR_LENGTH = 8;

    // Line Processing code (usually 80 but it can be 82)
    public static final String LINE_PROCESSING_CODE = "80";

    // Max student number length in reference part
    public static final int MAX_STUDENT_NUMBER_LENGTH = 5;

    // Integer part length in value
    public static final int INTEGER_PART_LENGTH = 8;

    // Decimal part length in value
    public static final int DECIMAL_PART_LENGTH = 2;

}