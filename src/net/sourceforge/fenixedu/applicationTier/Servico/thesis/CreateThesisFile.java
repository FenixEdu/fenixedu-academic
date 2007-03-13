package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public abstract class CreateThesisFile extends Service {

    public ThesisFile run(Thesis thesis, InputStream stream, String fileName) {

        if (! thesis.isWaitingConfirmation()) {
            throw new DomainException("thesis.files.submit.unavailable");
        }
        
        removePreviousFile(thesis);
        
        if (stream == null || fileName == null) {
            return null;
        }
        
        VirtualPath filePath = getVirtualPath(thesis);
        Collection<FileSetMetaData> metaData = createMetaData(thesis, fileName);
        
        FileDescriptor descriptor = saveFile(filePath, fileName, true, metaData, stream);
        
        ThesisFile file = new ThesisFile(descriptor.getUniqueId(), fileName);
        file.setSize(descriptor.getSize());
        file.setMimeType(descriptor.getMimeType());
        file.setChecksum(descriptor.getChecksum());
        file.setChecksumAlgorithm(descriptor.getChecksumAlgorithm());

        CurrentDegreeCoordinatorsGroup coordinators = new CurrentDegreeCoordinatorsGroup(thesis.getDegree());
        PersonGroup student = thesis.getStudent().getPerson().getPersonGroup();
        file.setPermittedGroup(new GroupUnion(coordinators, student));
        
        updateThesis(thesis, file);
        
        return file;
    }

    private VirtualPath getVirtualPath(Thesis thesis) {
        // TODO: thesis, review path
        
        VirtualPathNode[] nodes = { 
                new VirtualPathNode("Thesis", "Thesis"),
                new VirtualPathNode("Student" + thesis.getStudent().getIdInternal(), "Student")
        };
        
        VirtualPath path = new VirtualPath();
        for (VirtualPathNode node : nodes) {
            path.addNode(node);
        }
        
        return path;
    }

    private Collection<FileSetMetaData> createMetaData(Thesis thesis, String fileName) {
        List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
        
        metaData.add(FileSetMetaData.createAuthorMeta(thesis.getStudent().getPerson().getName()));
        metaData.add(FileSetMetaData.createTitleMeta(thesis.getTitle().getContent()));
        
        return metaData;
    }

    private FileDescriptor saveFile(VirtualPath filePath, String fileName, boolean isPrivate, Collection<FileSetMetaData> metaData, InputStream stream) {
        IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
        return fileManager.saveFile(filePath, fileName, isPrivate, metaData, stream);
    }

    protected abstract void removePreviousFile(Thesis thesis);
    protected abstract void updateThesis(Thesis thesis, ThesisFile file);

}
