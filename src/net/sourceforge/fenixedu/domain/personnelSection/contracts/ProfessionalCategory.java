package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalCategory extends ProfessionalCategory_Base {

    public ProfessionalCategory(final String giafId, final MultiLanguageString name, final CategoryType categoryType) {
	super();
	check(giafId, "");
	check(name, "");
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafId(giafId);
	setName(name);
	setCategoryType(categoryType);
    }

    @Service
    public void edit(final MultiLanguageString name, final CategoryType categoryType) {
	check(name, "");
	setName(name);
	setCategoryType(categoryType);
    }

}
