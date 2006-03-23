/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherOtherTypeCreditLineDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadOtherTypeCreditLineByTeacherAndExecutionPeriodService extends Service {

    public class TeacherNotFound extends FenixServiceException {
    }

    public class ExecutionPeriodNotFound extends FenixServiceException {
    }

    public TeacherOtherTypeCreditLineDTO run(Integer teacherId, Integer executionPeriodId)
            throws FenixServiceException, ExcepcaoPersistencia {
        TeacherOtherTypeCreditLineDTO teacherOtherTypeCreditLineDTO = new TeacherOtherTypeCreditLineDTO();
        IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = persistentSupport.getIPersistentOtherTypeCreditLine();

        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        if (teacher == null) {
            throw new TeacherNotFound();
        }

        final ExecutionPeriod executionPeriod;
        if (executionPeriodId == null || executionPeriodId.intValue() == 0) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        }

        if (executionPeriod == null) {
            throw new ExecutionPeriodNotFound();
        }

        List otherTypesList = otherTypeCreditLineDAO.readByTeacherAndExecutionPeriod(teacher
                .getIdInternal(), executionPeriod.getIdInternal());

        List infoOtherTypesList = (List) CollectionUtils.collect(otherTypesList, new Transformer() {

            public Object transform(Object input) {
                OtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine) input;
                InfoOtherTypeCreditLine infoOtherTypeCreditLine = InfoOtherTypeCreditLine
                        .newInfoFromDomain(otherTypeCreditLine);
                return infoOtherTypeCreditLine;
            }
        });

        teacherOtherTypeCreditLineDTO.setCreditLines(infoOtherTypesList);
        teacherOtherTypeCreditLineDTO.setInfoExecutionPeriod(InfoExecutionPeriod
                .newInfoFromDomain(executionPeriod));
        teacherOtherTypeCreditLineDTO.setInfoTeacher(InfoTeacherWithPersonAndCategory
                .newInfoFromDomain(teacher));
        return teacherOtherTypeCreditLineDTO;
    }

    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentOtherTypeCreditLine();
    }

    protected InfoObject clone2InfoObject(DomainObject domainObject) {
        return InfoOtherTypeCreditLine.newInfoFromDomain(((OtherTypeCreditLine) domainObject));
    }

}