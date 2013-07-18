package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditExternalUnit {

    @Service
    public static void run(final EditExternalUnitBean externalUnitBean) {
        final Unit externalUnit = externalUnitBean.getExternalUnit();
        externalUnit.edit(new MultiLanguageString(Language.getDefaultLanguage(), externalUnitBean.getUnitName()),
                externalUnitBean.getUnitCode());
    }
}