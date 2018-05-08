package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class EmptyConversionTable extends EmptyConversionTable_Base {
    
    private EmptyConversionTable() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static EmptyConversionTable getInstance() {
        if (Bennu.getInstance().getEmptyConversionTable() == null) {
            FenixFramework.atomic(() -> {
                if (Bennu.getInstance().getEmptyConversionTable() == null) {
                    new EmptyConversionTable();
                }
            });
        }
        return Bennu.getInstance().getEmptyConversionTable();
    }

    @Override
    public CurricularYear getCurricularYear() {
        return null;
    }

    @Override
    public CycleType getCycle() {
        return null;
    }

    @Override
    public DomainObject getTargetEntity() {
        return null;
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return null;
    }

    @Override
    public LocalizedString getPresentationName() {
        return BundleUtil.getLocalizedString(Bundle.ENUMERATION, getClass().getSimpleName() + ".presentation.name");
    }
}
