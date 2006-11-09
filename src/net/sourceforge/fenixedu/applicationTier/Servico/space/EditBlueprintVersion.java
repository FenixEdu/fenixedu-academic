package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class EditBlueprintVersion extends BlueprintVersionManagmentService {

    public Blueprint run(Blueprint blueprint, CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws FenixServiceException, IOException {

        blueprint.removeBlueprintFile();

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getUserView().getPerson();

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

}
