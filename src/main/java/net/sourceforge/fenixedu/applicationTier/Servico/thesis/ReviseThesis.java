package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.services.Service;

public class ReviseThesis {

    @Service
    public static void run(Thesis thesis) {
        thesis.allowRevision();
    }

}