package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CreateNewBlueprintVersion extends BlueprintVersionManagmentService {

    public Blueprint run(CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws IOException, FenixServiceException {

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getPerson();

        final Blueprint blueprint = new Blueprint(space, person);

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

}
