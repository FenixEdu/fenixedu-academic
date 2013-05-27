package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.Space;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteSpace {

    @Service
    public static void run(final Space space) {
        if (space != null) {
            space.delete();
        }
    }
}