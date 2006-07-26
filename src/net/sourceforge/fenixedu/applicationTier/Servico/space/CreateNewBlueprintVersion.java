package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

public class CreateNewBlueprintVersion extends Service {

    public Blueprint run(CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws FenixServiceException {

        String filename = blueprintSubmissionBean.getSpaceInformation().getIdInternal()
                + String.valueOf(System.currentTimeMillis());
        String displayName = blueprintSubmissionBean.getFilename();

        SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
        Space space = spaceInformation.getSpace();

        if (space == null) {
            throw new FenixServiceException("error.blueprint.submission.no.space");
        }

        Person person = AccessControl.getUserView().getPerson();

        Blueprint blueprint = new Blueprint(space, person);

        final FileMetadata fileMetadata = new FileMetadata(filename, person.getName());

        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                getFilePath(space.getMostRecentSpaceInformation()), filename, true, fileMetadata,
                blueprintSubmissionBean.getInputStream());

        new BlueprintFile(blueprint, filename, displayName, fileDescriptor.getMimeType(), fileDescriptor
                .getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
                fileDescriptor.getUniqueId(), new RoleGroup(Role
                        .getRoleByRoleType(RoleType.SPACE_MANAGER)));

        return blueprint;
    }

    private FilePath getFilePath(SpaceInformation spaceInformation) {
        final FilePath filePath = new FilePath();
        filePath.addNode(new Node("Spaces", "Spaces"));
        filePath.addNode(new Node("Spaces" + spaceInformation.getSpace().getIdInternal(),
                spaceInformation.getPresentationName()));
        filePath.addNode(new Node("Blueprints", "Blueprints"));
        return filePath;
    }
}
