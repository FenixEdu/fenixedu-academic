package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.services.Service;

public class CardGenerationProblem extends CardGenerationProblem_Base {

	public static class CardGenerationProblemDeleter implements FactoryExecutor {

		private final CardGenerationProblem cardGenerationProblem;

		public CardGenerationProblemDeleter(final CardGenerationProblem cardGenerationProblem) {
			this.cardGenerationProblem = cardGenerationProblem;
		}

		@Override
		public Object execute() {
			if (cardGenerationProblem != null) {
				cardGenerationProblem.delete();
			}
			return null;
		}
	}

	public CardGenerationProblem(final CardGenerationBatch cardGenerationBatch, final String descriptionKey, final String arg,
			final Person person) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setCardGenerationBatch(cardGenerationBatch);
		setDescriptionKey(descriptionKey);
		setArg(arg);
		setPerson(person);
	}

	public void delete() {
		removePerson();
		removeCardGenerationBatch();
		removeRootDomainObject();
		deleteDomainObject();
	}

	@Service
	public void setPersonForCardGenerationEntry(final CardGenerationEntry cardGenerationEntry, final Person person) {
		cardGenerationEntry.setPerson(person);
		final CardGenerationBatch cardGenerationBatchWithProblems = cardGenerationEntry.getCardGenerationBatch();
		final CardGenerationBatch cardGenerationBatch =
				findBatchUnderConstruction(cardGenerationBatchWithProblems.getExecutionYear());
		cardGenerationEntry.setCardGenerationBatch(cardGenerationBatch);
		crossReference(cardGenerationEntry);
		delete();
		if (!cardGenerationBatchWithProblems.hasAnyCardGenerationProblems()
				&& !cardGenerationBatchWithProblems.hasAnyCardGenerationEntries()) {
			cardGenerationBatchWithProblems.delete();
		}
	}

	private void crossReference(final CardGenerationEntry cardGenerationEntry) {
		final CardGenerationBatch cardGenerationBatch = cardGenerationEntry.getCardGenerationBatch();
		final CardGenerationBatch cardGenerationBatchWithProblems = getCardGenerationBatch();
		final Person person = cardGenerationEntry.getPerson();
		final String line = cardGenerationEntry.getLine();

		final Category category = cardGenerationEntry.getCategory();
		if (category == Category.CODE_73 || category == Category.CODE_82 || category == Category.CODE_96) {
			final Category categoryForLine = CardGenerationEntry.readCategory(line);
			final String studentLine = ImportIdentificationCardDataFromFile.createNewLine(person, cardGenerationBatch);
			if (studentLine == null) {
				if (category != Category.CODE_96) {
					cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithProblems);
					new CardGenerationProblem(cardGenerationBatchWithProblems, "multiple.user.information.type.not.crossed",
							cardGenerationEntry.getDocumentID(), person);
				}
			} else {
				final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
				final String newLine =
						ImportIdentificationCardDataFromFile.merge(line, categoryForLine, studentLine, categoryForStudentLine);
				cardGenerationEntry.setLine(newLine);
			}
		} else if (cardGenerationEntry.getCardGenerationBatch() == cardGenerationBatch && person != null
				&& person.hasRole(RoleType.STUDENT)) {
			final Category categoryForLine = CardGenerationEntry.readCategory(line);
			final String studentLine = ImportIdentificationCardDataFromFile.createNewLine(person, cardGenerationBatch);
			if (studentLine == null) {
//	    		cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchUnmatched);
//	    		new CardGenerationProblem(cardGenerationBatchUnmatched, "person.has.student.role.but.cannot.generate.line", identificationId, person);
			} else {
				final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
				final String newLine =
						ImportIdentificationCardDataFromFile.merge(line, categoryForLine, studentLine, categoryForStudentLine);
				cardGenerationEntry.setLine(newLine);
			}
		}
	}

	private CardGenerationBatch findBatchUnderConstruction(final ExecutionYear executionYear) {
		for (final CardGenerationBatch cardGenerationBatch : executionYear.getCardGenerationBatchesSet()) {
			if (cardGenerationBatch.getSent() == null && !cardGenerationBatch.hasAnyCardGenerationProblems()) {
				return cardGenerationBatch;
			}
		}
		return new CardGenerationBatch("New Batch", executionYear, true);
	}

}
