package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ImportBibtexBean implements Serializable {

	private List<BibtexPublicationBean> bibtexPublications = new ArrayList<BibtexPublicationBean>();

	private int totalPublicationsReaded = 0;

	private int currentPublicationPosition = 0;

	private int currentAuthorPosition = 0;

	private int currentEditorPosition = 0;

	private List<ParsingErrorIdentifier> parsingErrors = new ArrayList<ParsingErrorIdentifier>();
	
	public List<ParsingErrorIdentifier> getParsingErrors() {
		return parsingErrors;
	}
	
	public void addParsingError(String publicationId, String message) {
		parsingErrors.add(new ParsingErrorIdentifier(publicationId,message));
	}
	
	public List<BibtexPublicationBean> getBibtexPublications() {
		return bibtexPublications;
	}

	public void setBibtexPublications(List<BibtexPublicationBean> bibtexPublications) {
		this.bibtexPublications = bibtexPublications;
	}

	public ResultPublicationBean getCurrentPublicationBean() {
		BibtexPublicationBean bean = getCurrentBibtexPublication();
		return (bean != null) ? bean.getPublicationBean() : null;

	}

	public BibtexPublicationBean getCurrentBibtexPublication() {
		BibtexPublicationBean bean = bibtexPublications.get(currentPublicationPosition);
		return (bean != null) ? bean : null;
	}

	public String getCurrentBibtex() {
		BibtexPublicationBean bean = getCurrentBibtexPublication();
		return (bean != null) ? bean.getBibtex() : null;
	}

	public List<BibtexParticipatorBean> getCurrentAuthors() {
		BibtexPublicationBean bean = getCurrentBibtexPublication();
		return (bean != null) ? bean.getAuthors() : new ArrayList<BibtexParticipatorBean>();

	}

	public List<BibtexParticipatorBean> getCurrentEditors() {
		BibtexPublicationBean bean = getCurrentBibtexPublication();
		return (bean != null) ? bean.getEditors() : new ArrayList<BibtexParticipatorBean>();
	}

	public List<BibtexParticipatorBean> getCurrentParticipators() {
		BibtexPublicationBean bean = getCurrentBibtexPublication();
		return (bean != null) ? bean.getParticipators() : new ArrayList<BibtexParticipatorBean>();
	}

	public boolean isCurrentProcessedParticipators() {
		return getCurrentBibtexPublication().isProcessedParticipators();
	}

	public void setCurrentProcessedParticipators(boolean value) throws FenixActionException {
		Person person = AccessControl.getPerson();
		if (this.personBelongsToParticipators(person)) {
			getCurrentBibtexPublication().setProcessedParticipators(value);
		} else {
			throw new FenixActionException("error.importBibtex.personImportingNotInParticipants");
		}
	}

	public boolean hasMorePublications() {
		return currentPublicationPosition < bibtexPublications.size();
	}

	public int getCurrentPublicationPosition() {
		return currentPublicationPosition + 1;
	}

	public void setCurrentPublicationPosition(int currentPublicationPosition) {
		this.currentPublicationPosition = currentPublicationPosition;
	}

	public int getTotalPublicationsReaded() {
		return totalPublicationsReaded;
	}

	public void setTotalPublicationsReaded(int totalPublicationsReaded) {
		this.totalPublicationsReaded = totalPublicationsReaded;
	}

	public boolean moveToNextPublicaton() {
		currentPublicationPosition++;
		resetBeanCounters();
		return (hasMorePublications()) ? true : false;
	}

	public int getNumberOfAuthors() {
		List authors = getCurrentAuthors();
		return (authors!=null) ? authors.size() : 0;
	}

	public int getCurrentAuthorIndex() {
		return currentAuthorPosition;
	}

	public int getCurrentAuthorPosition() {
		return currentAuthorPosition + 1;
	}

	public boolean hasMoreAuthors() {
		List<BibtexParticipatorBean> authors = getCurrentAuthors().subList(currentAuthorPosition, getNumberOfAuthors());
		for(BibtexParticipatorBean author : authors) {
			if(!author.isParticipatorProcessed()) {
				return true;
			}
			moveToNextAuthor();
		}
		
		return false;
	}

	public BibtexParticipatorBean getNextAuthor() {
		return (hasMoreAuthors()) ? getCurrentAuthors().get(currentAuthorPosition) : null;
	}

	public void moveToNextAuthor() {
		currentAuthorPosition++;
	}

	public int getNumberOfEditors() {
		List editors =getCurrentEditors(); 
		return (editors!=null) ? editors.size() : 0;
	}

	public int getCurrentEditorIndex() {
		return currentEditorPosition;
	}

	public int getCurrentEditorPosition() {
		return currentEditorPosition + 1;
	}

	public boolean hasMoreEditors() {
		List<BibtexParticipatorBean> editors = getCurrentEditors().subList(currentEditorPosition, getNumberOfEditors());
		for(BibtexParticipatorBean editor : editors) {
			if(!editor.isParticipatorProcessed()) {
				return true;
			}
			moveToNextEditor();
		}
		return false;
	}

	public BibtexParticipatorBean getNextEditor() {
		return (hasMoreEditors()) ? getCurrentEditors().get(currentEditorPosition) : null;
	}

	public void moveToNextEditor() {
		currentEditorPosition++;
	}

	public boolean hasMoreParticipations() {
		return this.hasMoreAuthors() || this.hasMoreEditors();
	}

	public boolean personBelongsToParticipators(Person person) {
		for (BibtexParticipatorBean participation : this.getCurrentParticipators()) {
			if (person.equals(participation.getPerson())) {
				return true;
			}
		}
		return false;
	}

	public void moveToNextParticipation(String participationType) {
		if (participationType.equalsIgnoreCase("author")) {
			this.moveToNextAuthor();
		} else {
			this.moveToNextEditor();
		}
	}

	public BibtexParticipatorBean getNextParticipation(String participationType) {
		return (participationType.equalsIgnoreCase("author")) ? this.getNextAuthor() : this.getNextEditor();
	}
	
	public void resetBeanCounters() {
		currentAuthorPosition=0;
		currentEditorPosition=0;
		List<BibtexParticipatorBean> beans = this.getCurrentParticipators();
		for(BibtexParticipatorBean bean : beans) {
			bean.reset();
		}
	}
	
	public List<BibtexParticipatorBean> getProcessedBeans() {
		List<BibtexParticipatorBean> beans = this.getCurrentParticipators();
		return beans.subList(0, currentAuthorPosition + currentEditorPosition);
	}

	public void resetParticipationBean(Integer index) {
		getCurrentParticipators().get(index).reset();
		if(currentAuthorPosition>index) {
			currentAuthorPosition = index;
		}
		else {
			currentEditorPosition = index;
		}
	}
	
	public class ParsingErrorIdentifier implements Serializable{
		String publicationId;
		String message;
		
		public ParsingErrorIdentifier(String publicationId, String message) {
			super();
			this.publicationId = publicationId;
			this.message = message;
		}
		
		public String getMessage() {
			return RenderUtils.getResourceString("RESEARCHER_RESOURCES", "error.importBibtex.expandingReferencesForEntry", new Object[] {message,publicationId});
		}
	}
	
}
