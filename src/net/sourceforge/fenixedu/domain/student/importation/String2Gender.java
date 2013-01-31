/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.person.Gender;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class String2Gender {

	public static Gender convert(String genderString) {

		if (genderString.equalsIgnoreCase("M") || genderString.equalsIgnoreCase("MASCULINO")
				|| genderString.equalsIgnoreCase("male")) {
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}

	}

}
