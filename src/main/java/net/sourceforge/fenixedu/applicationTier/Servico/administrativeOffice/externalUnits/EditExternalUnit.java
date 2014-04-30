package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;
import java.util.Locale;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditExternalUnit {

    @Atomic
    public static void run(final EditExternalUnitBean externalUnitBean) {
        final Unit externalUnit = externalUnitBean.getExternalUnit();
        externalUnit.edit(new MultiLanguageString(Locale.getDefault(), externalUnitBean.getUnitName()),
                externalUnitBean.getUnitCode());
    }
}