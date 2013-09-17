package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.Space;
import pt.ist.fenixframework.Atomic;

public class DeleteSpace {

    @Atomic
    public static void run(final Space space) {
        if (space != null) {
            space.delete();
        }
    }
}