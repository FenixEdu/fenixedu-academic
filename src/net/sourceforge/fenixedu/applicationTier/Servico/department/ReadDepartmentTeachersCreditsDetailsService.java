package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.TeacherCreditsDetailsDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.tools.Profiler;

public class ReadDepartmentTeachersCreditsDetailsService extends Service {

    public List run(HashMap searchParameters) throws FenixServiceException, ExcepcaoPersistencia {
        Profiler.getInstance();
        Profiler.resetInstance();

        final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport
                .getIPersistentExecutionPeriod();
        final ExecutionPeriod executionPeriod = readExecutionPeriod(searchParameters, executionPeriodDAO);

        final InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = (InfoExecutionPeriodWithInfoExecutionYear) InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod);

        List<Teacher> teachers = doSearch(searchParameters);
        List<TeacherCreditsDetailsDTO> list = new ArrayList<TeacherCreditsDetailsDTO>(teachers.size());
        for (Teacher teacher : teachers) {
            TeacherCreditsDetailsDTO details = new TeacherCreditsDetailsDTO();
            InfoCredits infoCredits = teacher.getExecutionPeriodCredits(executionPeriod);
            if (teacher.getCategory() != null) {
                details.setCategory(InfoCategory.newInfoFromDomain(teacher.getCategory()));
            }
            details.setTeacherId(teacher.getIdInternal());
            details.setTeacherName(teacher.getPerson().getNome());
            details.setTeacherNumber(teacher.getTeacherNumber());
            details.setInfoCredits(infoCredits);
            details.setInfoExecution(infoExecutionPeriod);
            list.add(details);
        }
        return list;
    }

    private ExecutionPeriod readExecutionPeriod(HashMap searchParameters,
            IPersistentExecutionPeriod executionPeriodDAO) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod;
        Integer executionPeriodId = null;
        try {
            executionPeriodId = Integer.valueOf((String) searchParameters.get("executionPeriodId"));
        } catch (NumberFormatException e) {
        }

        if (executionPeriodId == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }

    protected List<Teacher> doSearch(HashMap searchParameters) throws ExcepcaoPersistencia {
        Integer departmentId = Integer.valueOf((String) searchParameters.get("idInternal"));

        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

        final Department department = (Department) persistentObject.readByOID(Department.class,
                departmentId);

        List<Teacher> teachers = department.getCurrentTeachers();
        return teachers;
    }

}
