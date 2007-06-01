package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class UnitFileUploadBean implements Serializable {

		private DomainReference<Unit> unit;
		
	  	private String fileName;
	    private Long fileSize;
	    
	    private String displayName;
	    private List<IGroup> permittedGroups;
	    private Group permittedGroup;

	    transient private InputStream file;
	    private String authorsName;
	    private String description;
	    private String tags;
	    
	    public String getTags() {
			return tags;
		}

		public void setTags(String tags) {
			this.tags = tags;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public UnitFileUploadBean(Unit unit) {
	    	this.unit = new DomainReference<Unit>(unit);
	    	permittedGroups = new ArrayList<IGroup> ();
	    }
	    
	    public Unit getUnit() {
	    	return this.unit.getObject();
	    }
	    
		public String getAuthorsName() {
			return authorsName;
		}

		public void setAuthorsName(String authorsName) {
			this.authorsName = authorsName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
		public InputStream getFile() {
			return file;
		}
		
		public void setFile(InputStream file) {
			this.file = file;
		}
		
		public String getFileName() {
			return fileName;
		}
		
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		public Long getFileSize() {
			return fileSize;
		}
		
		public void setFileSize(Long fileSize) {
			this.fileSize = fileSize;
		}
		
		public Group getPermittedGroup() {
			return (permittedGroup != null) ? permittedGroup : getUnion();
		}
		
		public void setPermittedGroup(Group permittedGroup) {
			this.permittedGroup = permittedGroup;
		}

		public Group getUnion() {
			return new GroupUnion((Collection<IGroup>)getPermittedGroups());
		}
		
		public List<IGroup> getPermittedGroups() {
			return permittedGroups;
		}

		public void setPermittedGroups(List<IGroup> permittedGroups) {
			this.permittedGroups = permittedGroups;
		}
}
