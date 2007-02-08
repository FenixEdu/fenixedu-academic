package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.DateTime;

public abstract class ResearchResult extends ResearchResult_Base {

	public ResearchResult() {

		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setOnCreateAtributes();
	}

	public abstract String getResume();

	protected String getParticipationsAndTitleString() {
		String resume = "";
		int i = 0;
		for (ResultParticipation participation : getOrderedResultParticipations()) {
			resume = resume + participation.getPerson().getName();
			i++;
			if (i < getResultParticipationsCount())
				resume = resume + ", ";
			else
				resume = resume + " - ";
		}
		resume = resume + getTitle() + " - ";
		return resume;
	}

	protected String finishResume(String resume) {
		if ((resume.charAt(resume.length() - 1) == ','))
			resume = resume.substring(0, resume.length() - 1);
		else if ((resume.charAt(resume.length() - 2) == ','))
			resume = resume.substring(0, resume.length() - 2);
		else if ((resume.charAt(resume.length() - 2) == '-'))
			resume = resume.substring(0, resume.length() - 2);
		return resume;
	}

	@Checked("ResultPredicates.createPredicate")
	private void setOnCreateAtributes() {
		super.setOjbConcreteClass(getClass().getName());
		if (AccessControl.getUserView() != null) {
			super.setModifiedBy(AccessControl.getPerson().getName());
			setCreator(AccessControl.getPerson());
		}
		super.setLastModificationDate(new DateTime());
	}

	@Checked("ResultPredicates.createPredicate")
	public ResultParticipation setCreatorParticipation(Person participator, ResultParticipationRole role) {
		return new ResultParticipation(this, participator, role, this.getResultParticipationsCount());
	}

	@Checked("ResultPredicates.writePredicate")
	public ResultParticipation addParticipation(Person participator, ResultParticipationRole role) {
		final ResultParticipation participation = new ResultParticipation(this, participator, role, this
				.getResultParticipationsCount());
		updateModifiedByAndDate();
		return participation;
	}

	@Checked("ResultPredicates.writePredicate")
	public void removeParticipation(ResultParticipation participation) {

		Person person = participation.getPerson();
		if (person.equals(this.getCreator())) {
			throw new DomainException("error.researcher.Result.cannotRemoveCreatorParticipation");
		} else {
			participation.deleteIfNotLast();
			this.reOrderParticipations(this.getOrderedResultParticipations());
			updateModifiedByAndDate();
		}
	}

	@Checked("ResultPredicates.writePredicate")
	public ResultUnitAssociation addUnitAssociation(Unit unit, ResultUnitAssociationRole role) {
		final ResultUnitAssociation association = new ResultUnitAssociation(this, unit, role);
		updateModifiedByAndDate();
		return association;
	}

	@Checked("ResultPredicates.writePredicate")
	public void removeUnitAssociation(ResultUnitAssociation association) {
		association.delete();
		updateModifiedByAndDate();
	}

	@Checked("ResultPredicates.writePredicate")
	public ResultEventAssociation addEventAssociation(Event event, ResultEventAssociationRole role) {
		final ResultEventAssociation association = new ResultEventAssociation(this, event, role);
		updateModifiedByAndDate();
		return association;
	}

	@Checked("ResultPredicates.writePredicate")
	public void removeEventAssociation(ResultEventAssociation association) {
		association.delete();
		updateModifiedByAndDate();
	}

	@Checked("ResultPredicates.writePredicate")
	public ResearchResultDocumentFile addDocumentFile(String filename, String displayName,
			FileResultPermittedGroupType permittedGroupType, String mimeType, String checksum,
			String checksumAlgorithm, Integer size, String externalStorageId, Group permittedGroup) {
		return addDocumentFile(filename, displayName, permittedGroupType, mimeType, checksum,
				checksumAlgorithm, size, externalStorageId, permittedGroup, Boolean.TRUE);
	}

	@Checked("ResultPredicates.writePredicate")
	public ResearchResultDocumentFile addDocumentFile(String filename, String displayName,
			FileResultPermittedGroupType permittedGroupType, String mimeType, String checksum,
			String checksumAlgorithm, Integer size, String externalStorageId, Group permittedGroup,
			Boolean isVisible) {
		final ResearchResultDocumentFile documentFile = new ResearchResultDocumentFile(this, filename,
				displayName, permittedGroupType, mimeType, checksum, checksumAlgorithm, size,
				externalStorageId, permittedGroup);
		documentFile.setVisible(isVisible);
		updateModifiedByAndDate();
		return documentFile;
	}

	@Checked("ResultPredicates.writePredicate")
	public void removeDocumentFile(ResearchResultDocumentFile documentFile) {
		documentFile.delete();
		updateModifiedByAndDate();
	}

	@Checked("ResultPredicates.writePredicate")
	public void setParticipationsOrder(List<ResultParticipation> newParticipationsOrder) {
		reOrderParticipations(newParticipationsOrder);
		updateModifiedByAndDate();
	}
	
	@Checked("ResultPredicates.writePredicate")
	public void setModifiedByAndDate() {
		updateModifiedByAndDate();
	}

	@Checked("ResultPredicates.writePredicate")
	public void delete() {
		Person requestingPerson = AccessControl.getPerson();
		if (!hasPersonParticipation(requestingPerson)) {
			throw new DomainException("error.researcher.Result.onlyParticipantsCanDelete");
		}
		removeAssociations();
		removeRootDomainObject();
		deleteDomainObject();
	}

	
	public final static ResearchResult readByOid(Integer oid) {
		final ResearchResult result = RootDomainObject.getInstance().readResearchResultByOID(oid);

		if (result == null) {
			throw new DomainException("error.researcher.Result.null");
		}
		return result;
	}

	/**
	 * Returns participations list ordered.
	 */
	public List<ResultParticipation> getOrderedResultParticipations() {
		return Collections.unmodifiableList(sort(super.getResultParticipations()));
	}

	/**
	 * Returns true if already exists a result participation with the given
	 * person and role.
	 */
	public boolean hasPersonParticipationWithRole(Person person, ResultParticipationRole role) {
		if (this.hasAnyResultParticipations()) {
			for (ResultParticipation participation : this.getResultParticipations()) {
				if (participation.getPerson() != null && participation.getPerson().equals(person)
						&& participation.getRole().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if already exists a result participation with the given
	 * person. (Role not relevant. Used for access control verification).
	 */
	public boolean hasPersonParticipation(Person person) {
		if (this.hasAnyResultParticipations()) {
			for (ResultParticipation participation : this.getResultParticipations()) {
				if (participation.getPerson() != null && participation.getPerson().equals(person)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if exists an association between result and the given event
	 * and role.
	 */
	public boolean hasAssociationWithEventRole(Event event, ResultEventAssociationRole role) {
		if (event != null && role != null && this.hasAnyResultEventAssociations()) {
			final List<ResultEventAssociation> list = this.getResultEventAssociations();

			for (ResultEventAssociation association : list) {
				if (association.getEvent() != null && association.getEvent().equals(event)
						&& association.getRole().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if exists an association between result and the given unit
	 * and role.
	 */
	public boolean hasAssociationWithUnitRole(Unit unit, ResultUnitAssociationRole role) {
		if (unit != null && role != null && this.hasAnyResultUnitAssociations()) {
			final List<ResultUnitAssociation> list = this.getResultUnitAssociations();

			for (ResultUnitAssociation association : list) {
				if (association.getUnit() != null && association.getUnit().equals(unit)
						&& association.getRole().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public final Boolean getIsPossibleSelectPersonRole() {
		if ((this instanceof Book) || (this instanceof BookPart) || (this instanceof Inproceedings))
			return true;
		return false;
	}

	public final boolean acceptsParticipationRole(ResultParticipationRole role) {
		if (role == null)
			return false;
		if (this instanceof Proceedings) {
			if (role.equals(ResultParticipationRole.Editor)) {
				return true;
			}
		}
		if (this instanceof Book || this instanceof BookPart || this instanceof Inproceedings) {
			if (role.equals(ResultParticipationRole.Editor) || role.equals(ResultParticipationRole.Author)) {
				return true;
			}
		}
		if (role.equals(ResultParticipationRole.Author)) {
			return true;
		}
		return false;
	}

	private void updateModifiedByAndDate() {
		if (AccessControl.getUserView() != null) {
			super.setModifiedBy(AccessControl.getPerson().getName());
		}
		super.setLastModificationDate(new DateTime());
	}

	/**
	 * Order result participations by person order.
	 */
	private static <T extends ResultParticipation> List<T> sort(Collection<T> resultParticipations) {
		List<T> sorted = new ArrayList<T>(resultParticipations);
		Collections.sort(sorted, new ResultParticipation.OrderComparator());

		return sorted;
	}

	/**
	 * Method responsible for updates on result participations order.
	 */
	private void reOrderParticipations(List<ResultParticipation> newParticipationsOrder) {
		int order = 0;
		for (ResultParticipation participation : newParticipationsOrder) {
			int index = this.getResultParticipations().indexOf(participation);
			ResultParticipation aux = this.getResultParticipations().get(index);
			aux.setPersonOrder(order++);
		}
	}

	/**
	 * Removes all references to result.
	 */
	private void removeAssociations() {
		super.setCountry(null);

		for (ResearchResultDocumentFile documentFile : getAllResultDocumentFiles()) {
			documentFile.delete();
		}

		for (ResultEventAssociation association : getResultEventAssociations()) {
			association.delete();
		}

		for (ResultUnitAssociation association : getResultUnitAssociations()) {
			association.delete();
		}

		// These should be the last to remove, because of access control
		// verifications.
		for (ResultParticipation participation : getResultParticipations()) {
			participation.delete();
		}
	}

	/**
	 * Block individual setters
	 */
	@Override
	public void setOjbConcreteClass(String ojbConcreteClass) {
		throw new DomainException("error.researcher.Result.call", "setOjbConcreteClass");
	}

	// @Override
	// public void setModifiedBy(String modifiedBy) {
	// throw new DomainException("error.researcher.Result.call",
	// "setModifiedBy");
	// }

	@Override
	public void setLastModificationDate(DateTime lastModificationDate) {
		throw new DomainException("error.researcher.Result.call", "setLastModificationDate");
	}

	@Override
	public void removeCountry() {
		throw new DomainException("error.researcher.Result.call", "removeCountry");
	}

	/**
	 * Block operations on relation lists.
	 */
	@Override
	public void addResultEventAssociations(ResultEventAssociation resultEventAssociations) {
		throw new DomainException("error.researcher.Result.call", "addResultEventAssociations");
	}

	@Override
	public void removeResultEventAssociations(ResultEventAssociation resultEventAssociations) {
		throw new DomainException("error.researcher.Result.call", "removeResultEventAssociations");
	}

	@Override
	public List<ResultEventAssociation> getResultEventAssociations() {
		return Collections.unmodifiableList(super.getResultEventAssociations());
	}

	@Override
	public Iterator<ResultEventAssociation> getResultEventAssociationsIterator() {
		return getResultEventAssociationsSet().iterator();
	}

	@Override
	public Set<ResultEventAssociation> getResultEventAssociationsSet() {
		return Collections.unmodifiableSet(super.getResultEventAssociationsSet());
	}

	@Override
	public void addResultParticipations(ResultParticipation resultParticipations) {
		throw new DomainException("error.researcher.Result.call", "addResultParticipations");
	}

	@Override
	public void removeResultParticipations(ResultParticipation resultParticipations) {
		throw new DomainException("error.researcher.Result.call", "removeResultParticipations");
	}

	@Override
	public List<ResultParticipation> getResultParticipations() {
		return Collections.unmodifiableList(super.getResultParticipations());
	}

	@Override
	public Iterator<ResultParticipation> getResultParticipationsIterator() {
		return getResultParticipationsSet().iterator();
	}

	@Override
	public Set<ResultParticipation> getResultParticipationsSet() {
		return Collections.unmodifiableSet(super.getResultParticipationsSet());
	}

	@Override
	public void addResultUnitAssociations(ResultUnitAssociation resultUnitAssociations) {
		throw new DomainException("error.researcher.Result.call", "addResultUnitAssociations");
	}

	@Override
	public void removeResultUnitAssociations(ResultUnitAssociation resultUnitAssociations) {
		throw new DomainException("error.researcher.Result.call", "removeResultUnitAssociations");
	}

	@Override
	public List<ResultUnitAssociation> getResultUnitAssociations() {
		return Collections.unmodifiableList(super.getResultUnitAssociations());
	}

	@Override
	public Iterator<ResultUnitAssociation> getResultUnitAssociationsIterator() {
		return getResultUnitAssociationsSet().iterator();
	}

	@Override
	public Set<ResultUnitAssociation> getResultUnitAssociationsSet() {
		return Collections.unmodifiableSet(super.getResultUnitAssociationsSet());
	}

		
	@Override
	public List<ResearchResultDocumentFile> getResultDocumentFiles() {
		return Collections.unmodifiableList(getVisibleResultDocumentFiles());
	}

	@Override
	public Iterator<ResearchResultDocumentFile> getResultDocumentFilesIterator() {
		return getResultDocumentFilesSet().iterator();
	}

	@Override
	public Set<ResearchResultDocumentFile> getResultDocumentFilesSet() {
		return Collections.unmodifiableSet(getVisibleResultDocumentFilesSet());
	}

	public List<ResearchResultDocumentFile> getAllResultDocumentFiles() {
		return Collections.unmodifiableList(super.getResultDocumentFiles());
	}
	
	private List<ResearchResultDocumentFile> getVisibleResultDocumentFiles() {
		
		List<ResearchResultDocumentFile> visibleFiles = new ArrayList<ResearchResultDocumentFile> ();
		Collection visibleDocuments = getVisibleFiles();
		
		if(visibleDocuments.size()>0) {
			visibleFiles.addAll(visibleDocuments);
		}
		return visibleFiles;
	}
	
	private Set<ResearchResultDocumentFile> getVisibleResultDocumentFilesSet() {
		Collection visibleDocuments = getVisibleFiles();
		Set<ResearchResultDocumentFile> visibleFiles = new HashSet<ResearchResultDocumentFile> ();
		
		if(visibleDocuments.size()>0) {
			visibleFiles.addAll(visibleDocuments);
		}
		return visibleFiles;
	}

	private List<ResearchResultDocumentFile> getVisibleFiles() {
		List<ResearchResultDocumentFile> files = super.getResultDocumentFiles();
		List<ResearchResultDocumentFile> visibleDocuments = new ArrayList<ResearchResultDocumentFile> ();
		for(ResearchResultDocumentFile file : files) {
			if(file.getVisible()) {
				visibleDocuments.add(file);
			}
		}
		return visibleDocuments;
	}
	
	public boolean isEditableByCurrentUser() {
		Person person = AccessControl.getPerson();
		for (ResultParticipation participation : this.getResultParticipations()) {
			if (participation.getPerson().equals(person)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDeletableByCurrentUser() {
		return hasPersonParticipation(AccessControl.getPerson());
	}
	
	public abstract String getSchema();

}
