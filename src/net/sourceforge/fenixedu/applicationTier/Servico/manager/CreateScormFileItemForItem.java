package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.InputStream;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class CreateScormFileItemForItem extends CreateFileItemForItem {

	public static class CreateScormFileItemForItemArgs {
		private Item item;

		private InputStream inputStream;

		private String originalFilename;

		private String displayName;

		private Group permittedGroup;

		private ScormMetaDataHash scormParameters;

		private Person person;
		
		private EducationalResourceType educationalResourceType;
		
		public CreateScormFileItemForItemArgs(Item item, InputStream inputStream, String originalFilename,
				String displayName, Group permittedGroup, ScormMetaDataHash scormParameters, Person person, EducationalResourceType educationalResourceType) {
			this.item = item;
			this.inputStream = inputStream;
			this.originalFilename = originalFilename;
			this.displayName = displayName;
			this.permittedGroup = permittedGroup;
			this.scormParameters = scormParameters;
			this.person = person;
			this.educationalResourceType = educationalResourceType;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		public String getOriginalFilename() {
			return originalFilename;
		}

		public void setOriginalFilename(String originalFilename) {
			this.originalFilename = originalFilename;
		}

		public Group getPermittedGroup() {
			return permittedGroup;
		}

		public void setPermittedGroup(Group permittedGroup) {
			this.permittedGroup = permittedGroup;
		}

		public ScormMetaDataHash getScormParameters() {
			return scormParameters;
		}

		public void setScormParameters(ScormMetaDataHash scormParameters) {
			this.scormParameters = scormParameters;
		}
		
		public Person getPerson() {
			return person;
		}
		
		public void setPerson(Person person) {
			this.person = person;
		}
		
		public EducationalResourceType getEducationalResourceType() {
			return educationalResourceType;
		}
		
		public void setEducationalResourceType(EducationalResourceType resourceType) {
			this.educationalResourceType = resourceType;
		}
        
        public Site getSite() {
            return getItem().getSection().getSite();
        }
	}

	private ThreadLocal<ScormMetaDataHash> extraScormParam = null;

	public void run(CreateScormFileItemForItemArgs args) throws FenixServiceException, ExcepcaoPersistencia {
		extraScormParam = new ThreadLocal<ScormMetaDataHash>();
		extraScormParam.set(args.getScormParameters());

		super.run(args.getSite(), args.getItem(), args.getInputStream(), args.getOriginalFilename(), args.getDisplayName(),
				args.getPermittedGroup(), args.getPerson(), args.getEducationalResourceType());
	}

	@Override
	protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission,
			Collection<FileSetMetaData> metaData, InputStream inputStream) {
		final IFileManager fileManager = FileManagerFactory.getFileManager();
		return fileManager.saveScormFile(filePath, originalFilename, permission, metaData, inputStream, extraScormParam
				.get());

	}

}
