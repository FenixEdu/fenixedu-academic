package net.sourceforge.fenixedu.domain.phd;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

abstract public class PhdProgramProcess extends PhdProgramProcess_Base {

    protected PhdProgramProcess() {
	super();
    }

    protected void addDocument(PhdProgramDocumentUploadBean each, Person responsible) {
	new PhdProgramProcessDocument(this, each.getType(), each.getRemarks(), each.getFileContent(), each.getFilename(),
		responsible);
    }

    public void removeDocumentsByType(PhdIndividualProgramDocumentType type) {
	for (final PhdProgramProcessDocument each : getDocuments()) {
	    if (each.getDocumentType() == type) {
		each.delete();
	    }
	}
    }

    public Set<PhdProgramProcessDocument> getDocumentsByType(PhdIndividualProgramDocumentType type) {
	final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();

	for (final PhdProgramProcessDocument document : getDocumentsSet()) {
	    if (document.getDocumentType() == type) {
		result.add(document);
	    }
	}

	return result;
    }

    protected Set<PhdProgramProcessDocument> filterLatestDocumentVersions(Collection<PhdProgramProcessDocument> documentsToFilter) {
	final Map<PhdIndividualProgramDocumentType, SortedSet<PhdProgramProcessDocument>> documentsByType = new java.util.HashMap<PhdIndividualProgramDocumentType, SortedSet<PhdProgramProcessDocument>>();

	for (final PhdProgramProcessDocument document : documentsToFilter) {
	    final SortedSet<PhdProgramProcessDocument> documents;
	    if (!documentsByType.containsKey(document.getDocumentType())) {
		documents = new TreeSet<PhdProgramProcessDocument>(PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME);
		documentsByType.put(document.getDocumentType(), documents);
	    } else {
		documents = documentsByType.get(document.getDocumentType());
	    }

	    documents.add(document);
	}

	final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();
	for (final Map.Entry<PhdIndividualProgramDocumentType, SortedSet<PhdProgramProcessDocument>> entry : documentsByType
		.entrySet()) {
	    if (entry.getKey().isVersioned()) {
		result.add(entry.getValue().last());
	    } else {
		result.addAll(entry.getValue());
	    }
	}

	return result;
    }
    
    protected PhdProgramProcessDocument getLastestDocumentVersionFor(PhdIndividualProgramDocumentType type) {
	final Collection<PhdProgramProcessDocument> documents = filterLatestDocumentVersions(getDocumentsByType(type));

	return documents.isEmpty() ? null : documents.iterator().next();
    }


    static protected boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView != null && userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    abstract protected PhdIndividualProgramProcess getIndividualProgramProcess();

    abstract protected Person getPerson();

}
