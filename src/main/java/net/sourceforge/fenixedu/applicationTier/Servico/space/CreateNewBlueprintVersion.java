package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager.SpaceManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class CreateNewBlueprintVersion extends BlueprintVersionManagmentService {

    protected Blueprint run(CreateBlueprintSubmissionBean blueprintSubmissionBean) throws IOException, FenixServiceException {

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getPerson();

        final Blueprint blueprint = new Blueprint(space, person);

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

    // Service Invokers migrated from Berserk

    private static final CreateNewBlueprintVersion serviceInstance = new CreateNewBlueprintVersion();

    @Service
    public static Blueprint runCreateNewBlueprintVersion(CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws IOException, FenixServiceException, NotAuthorizedException {
        SpaceManagerAuthorizationFilter.instance.execute();
        return serviceInstance.run(blueprintSubmissionBean);
    }

}