package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultTeacher;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.StringNormalizer;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateResultDocumentFile extends Service {
    public void run(ResultDocumentFileSubmissionBean bean) {
        final ResearchResult result = bean.getResult();
        final String displayName = bean.getDisplayName();
        
        final String fileName = result.getIdInternal() + String.valueOf(System.currentTimeMillis());
        
        
        final Group permittedGroup = ResearchResultDocumentFile.getPermittedGroup(bean.getPermission());
        
        ResearchResult researchResult = bean.getResult();
        
        Collection<FileSetMetaData> metadata = createMetaData(researchResult);
        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
         getVirtualPath(result), bean.getFileName(), (permittedGroup != null) ? true : false, metadata, bean.getInputStream());
        
        result.addDocumentFile(fileDescriptor.getFilename(), displayName, bean.getPermission(), fileDescriptor.getMimeType(), fileDescriptor
				        .getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                fileDescriptor.getSize(), fileDescriptor.getUniqueId(), permittedGroup);
        /*result.addDocumentFile(bean.getFileName(), displayName, bean.getPermission(), "mime", "checkSum", "alg",
        	12345, "uniqueId", permittedGroup);*/
    }

    private Collection<FileSetMetaData> createMetaData(ResearchResult researchResult) {
		ArrayList<FileSetMetaData> metadata = new ArrayList<FileSetMetaData>();
		
		Set<Person> authors = new HashSet<Person>();
		
		for(ResultParticipation participation: researchResult.getOrderedResultParticipations()){
					authors.add(participation.getPerson());	
		}
		for(ResultTeacher participation: researchResult.getResultTeachers()){
			authors.add(participation.getTeacher().getPerson());			
		}
	
		for(Person person : authors) {
			metadata.add(FileSetMetaData.createAuthorMeta(StringNormalizer.normalize(person.getName())));	
		}
		
		metadata.add(FileSetMetaData.createTitleMeta(researchResult.getTitle()));
		metadata.add(new FileSetMetaData("creator",null,null,StringNormalizer.normalize(researchResult.getCreator().getName())));
		
		if(researchResult instanceof ResearchResultPublication) {
			ResearchResultPublication publication = (ResearchResultPublication) researchResult;
			Unit publisher = publication.getPublisher();
			if(publisher!=null) {
				metadata.add(new FileSetMetaData("publisher",null,null,StringNormalizer.normalize(publisher.getName())));
			}
			MultiLanguageString notes = publication.getNote();
			if(notes!=null) {
				for(String description : notes.getAllContents()) {
					metadata.add(new FileSetMetaData("description",null ,null, (description!=null) ? description : ""));
				}
			}
			Integer year = publication.getYear();
			if(year!=null) {
				Month month = publication.getMonth();
				metadata.add(new FileSetMetaData("date", "issued", null, publication.getYear() + ((month!=null) ? "-" + month.ordinal()+1 : "")));
			}
			String type[] = publication.getOjbConcreteClass().split("\\.");
			metadata.add(new FileSetMetaData("type",null, null, type[type.length-1]));
		}
		
		return metadata;
	}

	/**
     * @param result
     * @return filePath
     *
     * Path examples:   /Research/Results/Publications/pub{idInternal}/
     *                  /Research/Results/Patents/pat{idInternal}/
     */
    private VirtualPath getVirtualPath(ResearchResult result) {
        final VirtualPath filePath = new VirtualPath();
        
        filePath.addNode(new VirtualPathNode("Research", "Research"));
        filePath.addNode(new VirtualPathNode("Results", "Results"));
        
        if (result instanceof ResearchResultPublication) {
            filePath.addNode(new VirtualPathNode("Publications", "Publications"));
            filePath.addNode(new VirtualPathNode("pub" + result.getIdInternal(), "pub" + result.getIdInternal().toString()));
        }
        if (result instanceof ResearchResultPatent){
            filePath.addNode(new VirtualPathNode("Patents", "Patents"));
            filePath.addNode(new VirtualPathNode("pat" + result.getIdInternal(), "pat" + result.getIdInternal().toString()));
        }
        
        return filePath;
    }
}
