package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class DeleteExternalUnit {

    @Atomic
    public static void run(final Unit externalUnit) throws FenixServiceException {
        if (externalUnit.isOfficialExternal()) {
            externalUnit.delete();
        } else {
            throw new FenixServiceException("error.DeleteExternalUnit.invalid.unit");
        }
    }
}