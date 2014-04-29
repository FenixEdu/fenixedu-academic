/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum AccountabilityTypeEnum implements IPresentableEnum {

    MANAGEMENT_FUNCTION,

    ORGANIZATIONAL_STRUCTURE, ACADEMIC_STRUCTURE, ADMINISTRATIVE_STRUCTURE,

    GEOGRAPHIC,

    // Contracts
    WORKING_CONTRACT, MAILING_CONTRACT, INVITATION,

    RESEARCH_CONTRACT,

    ASSIDUOUSNESS_STRUCTURE;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AccountabilityTypeEnum.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AccountabilityTypeEnum.class.getName() + "." + name();
    }

    @Override
    public String getLocalizedName() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }
}