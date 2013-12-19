package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalRegime extends ProfessionalRegime_Base {

    public ProfessionalRegime(final String giafId, final MultiLanguageString name, final Integer weighting,
            final BigDecimal fullTimeEquivalent, final CategoryType categoryType) {
        super();
        String[] args1 = {};
        if (giafId == null || giafId.isEmpty()) {
            throw new DomainException("", args1);
        }
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setRootDomainObject(Bennu.getInstance());
        setGiafId(giafId);
        setName(name);
        setWeighting(weighting);
        setFullTimeEquivalent(fullTimeEquivalent);
        setCategoryType(categoryType);
    }

    @Atomic
    public void edit(final MultiLanguageString name, final Integer weighting, final BigDecimal fullTimeEquivalent,
            final CategoryType categoryType) {
        String[] args = {};
        if (name == null) {
            throw new DomainException("", args);
        }
        setName(name);
        setWeighting(weighting);
        setFullTimeEquivalent(fullTimeEquivalent);
        setCategoryType(categoryType);
    }
}
