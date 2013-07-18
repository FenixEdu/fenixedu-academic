package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixWebFramework.services.Service;

public class EditExercise {

    protected Boolean run(Integer executionCourseId, Integer metadataId, String author, String description, String difficulty,
            Calendar learningTime, String level, String mainSubject, String secondarySubject) throws FenixServiceException {
        Metadata metadata = RootDomainObject.getInstance().readMetadataByOID(metadataId);
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

    // Service Invokers migrated from Berserk

    private static final EditExercise serviceInstance = new EditExercise();

    @Service
    public static Boolean runEditExercise(Integer executionCourseId, Integer metadataId, String author, String description,
            String difficulty, Calendar learningTime, String level, String mainSubject, String secondarySubject)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId, author, description, difficulty, learningTime, level,
                mainSubject, secondarySubject);
    }

}