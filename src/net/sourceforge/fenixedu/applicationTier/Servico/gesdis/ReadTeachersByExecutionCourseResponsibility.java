package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadTeachersByExecutionCourseResponsibility extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        return run(infoExecutionCourse.getIdInternal());
    }

    public List run(Integer executionCourseID) throws ExcepcaoPersistencia {
        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = suportePersistente.getIPersistentExecutionCourse();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        for (final Professorship professorship : executionCourse.responsibleFors()) {
            result.add(InfoTeacher.newInfoFromDomain(professorship.getTeacher()));
        }
        return result;
    }

}