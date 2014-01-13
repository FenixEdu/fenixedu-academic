package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.domain.UnitFile;
import pt.ist.fenixframework.Atomic;

public class DeleteUnitFile {

    @Atomic
    public static void run(final UnitFile file) {
        file.delete();
    }
}