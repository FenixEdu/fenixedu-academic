package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.File;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

    public void run(Integer itemId, File temporaryFile, String originalFilename,
            String displayName, boolean isPrivateFile) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        final Item item = rootDomainObject.readItemByOID(itemId);
        final Path destination = getDspaceDestination(item);
        final ItemMetadata itemMetadata = new ItemMetadata(displayName, item.getSection().getSite()
                .getExecutionCourse().getNome());

        FileUpload fileUpload = new FileUpload(destination, originalFilename, isPrivateFile,
                itemMetadata);

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

        item.addFileItems(fileItem);

    }

    private Path getDspaceDestination(Item item) {
        Path dspaceDestination = new Path();
        dspaceDestination
                .addPathComponent(new PathComponent("I" + item.getIdInternal(), item.getName()));

        Section section = item.getSection();
        dspaceDestination.add(0, new PathComponent("S" + section.getIdInternal(), section.getName()));

        if (section.getSuperiorSection() != null) {
            Section superiorSection = section.getSuperiorSection();

            while (superiorSection != null) {
                dspaceDestination.add(0, new PathComponent("S" + superiorSection.getIdInternal(),
                        superiorSection.getName()));

                superiorSection = superiorSection.getSuperiorSection();
            }
        }

        ExecutionCourse executionCourse = section.getSite().getExecutionCourse();
        dspaceDestination.add(0, new PathComponent("EC" + executionCourse.getIdInternal(),
                executionCourse.getNome()));

        ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        dspaceDestination.add(0, new PathComponent("EP" + executionPeriod.getIdInternal(),
                executionPeriod.getName()));

        ExecutionYear executionYear = executionPeriod.getExecutionYear();
        dspaceDestination.add(0, new PathComponent("EY" + executionYear.getIdInternal(), executionYear
                .getYear()));

        return dspaceDestination;
    }

}