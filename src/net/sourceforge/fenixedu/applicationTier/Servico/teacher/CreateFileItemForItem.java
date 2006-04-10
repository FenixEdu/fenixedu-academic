package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.File;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClient;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClientException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.dspace.external.interfaces.remoteManager.objects.FileUpload;
import org.dspace.external.interfaces.remoteManager.objects.ItemMetadata;
import org.dspace.external.interfaces.remoteManager.objects.Path;
import org.dspace.external.interfaces.remoteManager.objects.PathComponent;
import org.dspace.external.interfaces.remoteManager.objects.UploadedFileDescriptor;

/**
 * 
 * @author naat
 * 
 */
public class CreateFileItemForItem extends Service {

    public void run(Integer itemID, File temporaryFile,
            String originalFilename, String displayName,
            FileItemPermittedGroupType fileItemPermittedGroupType) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        final Item item = rootDomainObject.readItemByOID(itemID);
        final Path destination = getDspaceDestination(item);
        final ItemMetadata itemMetadata = new ItemMetadata(displayName, item.getSection().getSite()
                .getExecutionCourse().getNome());
        final Group permittedGroup = createPermittedGroup(fileItemPermittedGroupType, item);

        final FileUpload fileUpload = new FileUpload(destination, originalFilename,
                (permittedGroup != null) ? true : false, itemMetadata);

        UploadedFileDescriptor uploadedFileDescriptor;
        try {
            uploadedFileDescriptor = DspaceClient.uploadFile(fileUpload, temporaryFile);
        } catch (DspaceClientException e) {
            throw new FenixServiceException(e.getMessage());
        }

        final FileItem fileItem = DomainFactory.makeFileItem();
        fileItem.setFilename(uploadedFileDescriptor.getFilename());
        fileItem.setDisplayName(displayName);
        fileItem.setMimeType(uploadedFileDescriptor.getMimeType());
        fileItem.setChecksum(uploadedFileDescriptor.getChecksum());
        fileItem.setChecksumAlgorithm(uploadedFileDescriptor.getChecksumAlgorithm());
        fileItem.setSize(uploadedFileDescriptor.getSize());
        fileItem.setDspaceBitstreamIdentification(uploadedFileDescriptor.getBitstreamIdentification());
        fileItem.setPermittedGroupType(fileItemPermittedGroupType);
        fileItem.setPermittedGroup(permittedGroup);

        item.addFileItems(fileItem);

    }

    private Path getDspaceDestination(Item item) {
        final Path dspaceDestination = new Path();
        dspaceDestination
                .addPathComponent(new PathComponent("I" + item.getIdInternal(), item.getName()));

        final Section section = item.getSection();
        dspaceDestination.add(0, new PathComponent("S" + section.getIdInternal(), section.getName()));

        if (section.getSuperiorSection() != null) {
            Section superiorSection = section.getSuperiorSection();

            while (superiorSection != null) {
                dspaceDestination.add(0, new PathComponent("S" + superiorSection.getIdInternal(),
                        superiorSection.getName()));

                superiorSection = superiorSection.getSuperiorSection();
            }
        }

        final ExecutionCourse executionCourse = section.getSite().getExecutionCourse();
        dspaceDestination.add(0, new PathComponent("EC" + executionCourse.getIdInternal(),
                executionCourse.getNome()));

        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        dspaceDestination.add(0, new PathComponent("EP" + executionPeriod.getIdInternal(),
                executionPeriod.getName()));

        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        dspaceDestination.add(0, new PathComponent("EY" + executionYear.getIdInternal(), executionYear
                .getYear()));

        return dspaceDestination;
    }

    private Group createPermittedGroup(FileItemPermittedGroupType fileItemPermittedGroupType, Item item)
            throws FenixServiceException {

        if (fileItemPermittedGroupType == FileItemPermittedGroupType.PUBLIC) {
            return null;
        } else if (fileItemPermittedGroupType == FileItemPermittedGroupType.INSTITUTION_PERSONS) {
            final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
            return new RoleGroup(personRole);
        } else if (fileItemPermittedGroupType == FileItemPermittedGroupType.EXECUTION_COURSE_TEACHERS_AND_STUDENTS) {
            final ExecutionCourse executionCourse = item.getSection().getSite().getExecutionCourse();
            return new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse),
                    new ExecutionCourseStudentsGroup(executionCourse));
        } else {
            throw new FenixServiceException("error.exception");
        }

    }

}