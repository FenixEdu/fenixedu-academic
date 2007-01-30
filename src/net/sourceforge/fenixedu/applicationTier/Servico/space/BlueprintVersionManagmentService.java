package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import net.sourceforge.fenixedu.util.ByteArray;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class BlueprintVersionManagmentService extends Service {

    protected Space getSpace(CreateBlueprintSubmissionBean blueprintSubmissionBean) throws FenixServiceException {
        final SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
        final Space space = spaceInformation.getSpace();
        if (space == null) {
            throw new FenixServiceException("error.blueprint.submission.no.space");
        }
        return space;
    }

    protected void editBlueprintVersion(CreateBlueprintSubmissionBean blueprintSubmissionBean,
            final Space space, final Person person, final Blueprint blueprint) throws IOException {
        final String filename = blueprintSubmissionBean.getSpaceInformation().getIdInternal()
                + String.valueOf(System.currentTimeMillis());
        

        final byte[] contents = readInputStream(blueprintSubmissionBean.getInputStream());
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contents);

        final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager().saveFile(
        		getVirtualPath(space.getMostRecentSpaceInformation()), filename, true, person.getName(),filename,
                byteArrayInputStream);

        final String displayName = blueprintSubmissionBean.getFilename();
        final BlueprintFile blueprintFile = new BlueprintFile(blueprint, filename, displayName, fileDescriptor.getMimeType(), fileDescriptor
                .getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
                fileDescriptor.getUniqueId(), new RoleGroup(Role
                        .getRoleByRoleType(RoleType.SPACE_MANAGER)));
        blueprintFile.setContent(new ByteArray(contents));
    }

    private byte[] readInputStream(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileUtils.copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected VirtualPath getVirtualPath(SpaceInformation spaceInformation) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("Spaces", "Spaces"));
        filePath.addNode(new VirtualPathNode("Spaces" + spaceInformation.getSpace().getIdInternal(),
                spaceInformation.getPresentationName()));
        filePath.addNode(new VirtualPathNode("Blueprints", "Blueprints"));
        return filePath;
    }

}
