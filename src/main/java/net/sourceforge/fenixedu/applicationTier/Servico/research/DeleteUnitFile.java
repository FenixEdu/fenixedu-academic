package net.sourceforge.fenixedu.applicationTier.Servico.research;


import net.sourceforge.fenixedu.domain.UnitFile;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnitFile {

    @Service
    public static void run(final UnitFile file) {
        file.delete();
    }
}