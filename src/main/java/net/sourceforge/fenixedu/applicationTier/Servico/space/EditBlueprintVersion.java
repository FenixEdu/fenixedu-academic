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

public class EditBlueprintVersion extends BlueprintVersionManagmentService {

    protected Blueprint run(Blueprint blueprint, CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws FenixServiceException, IOException {

        blueprint.setBlueprintFile(null);

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getPerson();

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

    // Service Invokers migrated from Berserk

    private static final EditBlueprintVersion serviceInstance = new EditBlueprintVersion();

    @Service
    public static Blueprint runEditBlueprintVersion(Blueprint blueprint, CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws FenixServiceException, IOException, NotAuthorizedException {
        SpaceManagerAuthorizationFilter.instance.execute();
        return serviceInstance.run(blueprint, blueprintSubmissionBean);
    }

}