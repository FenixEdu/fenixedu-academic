package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.IOException;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Space;

public class CreateNewBlueprintVersion extends BlueprintVersionManagmentService {

    public Blueprint run(CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws IOException, FenixServiceException {

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getUserView().getPerson();

        final Blueprint blueprint = new Blueprint(space, person);

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

}
