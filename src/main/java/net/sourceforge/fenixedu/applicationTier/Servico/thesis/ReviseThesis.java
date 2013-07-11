package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixframework.Atomic;

public class ReviseThesis {

    @Atomic
    public static void run(Thesis thesis) {
        thesis.allowRevision();
    }

}