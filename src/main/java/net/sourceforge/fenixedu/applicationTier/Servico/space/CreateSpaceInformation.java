package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import pt.ist.fenixWebFramework.services.Service;

public class CreateSpaceInformation {

    @Service
    public static void run(final Integer spaceInformationID) {
        final SpaceInformation spaceInformation = RootDomainObject.getInstance().readSpaceInformationByOID(spaceInformationID);
        // spaceInformation.createNewSpaceInformation();
    }

}