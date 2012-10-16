package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalRegime extends ProfessionalRegime_Base {

    public ProfessionalRegime(final String giafId, final MultiLanguageString name, final Integer weighting,
	    final BigDecimal fullTimeEquivalent, final CategoryType categoryType) {
	super();
	check(giafId, "");
	check(name, "");
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafId(giafId);
	setName(name);
	setWeighting(weighting);
	setFullTimeEquivalent(fullTimeEquivalent);
	setCategoryType(categoryType);
    }

    @Service
    public void edit(final MultiLanguageString name, final Integer weighting, final BigDecimal fullTimeEquivalent,
	    final CategoryType categoryType) {
	check(name, "");
	setName(name);
	setWeighting(weighting);
	setFullTimeEquivalent(fullTimeEquivalent);
	setCategoryType(categoryType);
    }
}
