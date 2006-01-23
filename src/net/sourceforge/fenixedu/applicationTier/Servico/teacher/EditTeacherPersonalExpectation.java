package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class EditTeacherPersonalExpectation extends Service {

    public void run(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation) throws ExcepcaoPersistencia,
            FenixServiceException {
        TeacherPersonalExpectation teacherPersonalExpectation = (TeacherPersonalExpectation) persistentObject
                .readByOID(TeacherPersonalExpectation.class, infoTeacherPersonalExpectation.getIdInternal());
        teacherPersonalExpectation.edit(infoTeacherPersonalExpectation);

    }
}