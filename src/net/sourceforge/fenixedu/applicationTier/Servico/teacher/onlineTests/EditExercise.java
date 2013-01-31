package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

public class EditExercise extends FenixService {

	public Boolean run(Integer executionCourseId, Integer metadataId, String author, String description, String difficulty,
			Calendar learningTime, String level, String mainSubject, String secondarySubject) throws FenixServiceException {
		Metadata metadata = rootDomainObject.readMetadataByOID(metadataId);
		if (metadata == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (author != null) {
			metadata.setAuthor(author);
		}
		if (!difficulty.equals("-1")) {
			metadata.setDifficulty(difficulty);
		}
		metadata.setDescription(description);
		metadata.setLearningTime(learningTime);
		metadata.setLevel(level);
		metadata.setMainSubject(mainSubject);
		metadata.setSecondarySubject(secondarySubject);

		return true;
	}

}
