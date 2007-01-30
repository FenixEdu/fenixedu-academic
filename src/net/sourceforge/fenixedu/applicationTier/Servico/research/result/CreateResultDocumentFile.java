package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateResultDocumentFile extends Service {
	public void run(ResultDocumentFileSubmissionBean bean) {
		final ResearchResult result = bean.getResult();
		final String displayName = bean.getDisplayName();

		final Group permittedGroup = ResearchResultDocumentFile.getPermittedGroup(bean.getPermission());
		String itemHandle = result.getUniqueStorageId();
		
		final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getContentFileManager().addFileToItem(
				getVirtualPath(result), bean.getFileName(), itemHandle,
				(permittedGroup != null) ? true : false, bean.getInputStream());

		result.addDocumentFile(fileDescriptor.getFilename(), displayName, bean.getPermission(),
				fileDescriptor.getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
						.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
				permittedGroup);
	}

	/**
	 * @param result
	 * @return filePath
	 * 
	 * Path examples: /Research/Results/Publications/pub{idInternal}/
	 * /Research/Results/Patents/pat{idInternal}/
	 */
	private VirtualPath getVirtualPath(ResearchResult result) {
		final VirtualPath filePath = new VirtualPath();

		filePath.addNode(new VirtualPathNode("Research", "Research"));
		filePath.addNode(new VirtualPathNode("Results", "Results"));

		if (result instanceof ResearchResultPublication) {
			filePath.addNode(new VirtualPathNode("Publications", "Publications"));
			filePath.addNode(new VirtualPathNode("pub" + result.getIdInternal(), "pub"
					+ result.getIdInternal().toString()));
		}
		if (result instanceof ResearchResultPatent) {
			filePath.addNode(new VirtualPathNode("Patents", "Patents"));
			filePath.addNode(new VirtualPathNode("pat" + result.getIdInternal(), "pat"
					+ result.getIdInternal().toString()));
		}

		return filePath;
	}
}
