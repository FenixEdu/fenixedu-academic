/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.credits.InfoCredits;
import DataBeans.teacher.InfoCategory;
import DataBeans.teacher.credits.TeacherCreditsDetailsDTO;
import Dominio.Department;
import Dominio.ExecutionPeriod;
import Dominio.IDepartment;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.Profiler;

/**
 * @author jpvl
 */
public class ReadDepartmentTeachersCreditsDetailsService implements IService {
    public ReadDepartmentTeachersCreditsDetailsService() {
    }

    /**
     * @param searchParameters
     *            idInternal (java.lang.String); executionPeriodId
     *            (java.lang.String)
     * @return @throws
     *         FenixServiceException
     */
    public List run(HashMap searchParameters) throws FenixServiceException {
        ISuportePersistente sp;
        List teachers;
        try {
            Profiler.getInstance();
            Profiler.resetInstance();
            
            sp = SuportePersistenteOJB.getInstance();
            
            teachers = doSearch(searchParameters, sp);
            
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            final IExecutionPeriod executionPeriod = readExecutionPeriod(searchParameters,
                    executionPeriodDAO);
            
            InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = (InfoExecutionPeriodWithInfoExecutionYear) InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionPeriod);
            
            List list = new ArrayList();
            for (int i = 0; i < teachers.size(); i++) {
                ITeacher teacher = (ITeacher) teachers.get(i);
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
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems with database!", e);
        }
    }

    /**
     * @param searchParameters
     * @param executionPeriodDAO
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private IExecutionPeriod readExecutionPeriod(HashMap searchParameters,
            IPersistentExecutionPeriod executionPeriodDAO) throws ExcepcaoPersistencia {
        final IExecutionPeriod executionPeriod;
        Integer executionPeriodId = null;
        try {
            executionPeriodId = Integer.valueOf((String) searchParameters.get("executionPeriodId"));
        } catch (NumberFormatException e) {
        }

        if (executionPeriodId == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }

    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        Integer departmentId = Integer.valueOf((String) searchParameters.get("idInternal"));
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
        IDepartment department = (IDepartment) departmentDAO.readByOID(Department.class, departmentId);
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        List teachers = teacherDAO.readByDepartment(department);
        return teachers;
    }
}