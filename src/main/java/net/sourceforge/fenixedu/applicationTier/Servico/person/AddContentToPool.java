package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import pt.ist.fenixWebFramework.services.Service;

public class AddContentToPool {

    @Service
    public static void run(MetaDomainObjectPortal portal, Content content) {
        portal.addPool(content);
    }
}