/* 
 * ################################################################
 * 
 * FenixEdu: The Java(TM) Object-Oriented Framework for University 
 *	    Academic Applications
 * 
 * Copyright (C) 2002-2006 IST/Technical University of Lisbon
 * Contact: suporte@dot.ist.utl.pt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *  
 *  Initial developer(s):               The Fenix Team
 *                              http://fenix-ashes.ist.utl.pt/
 *  Contributor(s): 
 * 
 * ################################################################
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum Gender implements IPresentableEnum {

    MALE, FEMALE;

    /**
     * This method has been deprecated, as it is only a proxy to @see
     * GenderHelper#getSexLabelValues(Locale) It is strange that an enum
     * belonging to the domain model objects should be dependent on struts as it
     * is...
     * 
     * @deprecated
     * 
     * @param locale
     *            The locale for the localized labels
     * @return an array of LabelValueBean containing the name of the Gender and
     *         its localized string version
     */
    @Deprecated
    public static LabelValueBean[] getSexLabelValues(Locale locale) {
        return GenderHelper.getSexLabelValues(locale);
    }

    /**
     * Returns a localized String of Genders
     * 
     * @see GenderHelper#toLocalizedString(Gender, Locale)
     * 
     * @param locale
     *            The Locale for which to get the localized version
     * @return A string representation based on chosen locale
     */
    public String toLocalizedString(Locale locale) {
        return GenderHelper.toLocalizedString(this, locale);
    }

    /**
     * Returns a localized String of Genders based on the default locale
     * 
     * @see GenderHelper#toLocalizedString(Gender)
     * 
     * @return A string representation based on chosen locale
     */
    public String toLocalizedString() {
        return GenderHelper.toLocalizedString(this);
    }

    /**
     * Returns a Gender based on the name parameter... This name should be equal
     * to the enum's member name
     * 
     * @see GenderHelper#parseGender(String)
     * 
     * @param name
     *            The enum's member name
     * @return A Gender parsed from the name or null if none is found
     */
    public static Gender parseGender(String name) {
        return GenderHelper.parseGender(name);
    }

    /**
     * Returns a Gender based on the Localized name of the gender
     * 
     * @see GenderHelper#parseGender(String)
     * 
     * @param localizedName
     *            The localized name of the gender
     * @return A Gender parsed from the localized name or null if none is found
     */
    public static Gender parse(String localizedName) {
        for (Gender gender : values()) {
            if (StringUtils.equals(gender.getLocalizedName(), localizedName)) {
                return gender;
            }
        }
        return null;
    }

    public static List<String> getLocalizedNames() {
        ArrayList<String> localizedNames = new ArrayList<>();
        for (Gender gender : Gender.values()) {
            localizedNames.add(gender.getLocalizedName());
        }
        return localizedNames;
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
        return bundle.getString(this.getClass().getName() + "." + name());
    }
}
