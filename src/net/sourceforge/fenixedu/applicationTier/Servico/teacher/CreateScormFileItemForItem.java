package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
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

		public CreateScormFileItemForItemArgs(Item item, InputStream inputStream, String originalFilename,
				String displayName, Group permittedGroup, ScormMetaDataHash scormParameters) {
			this.item = item;
			this.inputStream = inputStream;
			this.originalFilename = originalFilename;
			this.displayName = displayName;
			this.permittedGroup = permittedGroup;
			this.scormParameters = scormParameters;
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
	}

	private ThreadLocal<ScormMetaDataHash> extraScormParam = null;

	public void run(CreateScormFileItemForItemArgs args) throws FenixServiceException, ExcepcaoPersistencia {
		extraScormParam = new ThreadLocal<ScormMetaDataHash>();
		extraScormParam.set(args.getScormParameters());

		super.run(args.getItem(), args.getInputStream(), args.getOriginalFilename(), args.getDisplayName(),
				args.getPermittedGroup());
	}

	@Override
	protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission,
			String name, String displayName, InputStream inputStream) {
		final IFileManager fileManager = FileManagerFactory.getFileManager();
		return fileManager.saveScormFile(filePath, originalFilename, permission, name, displayName, inputStream, extraScormParam
				.get());

	}

}
