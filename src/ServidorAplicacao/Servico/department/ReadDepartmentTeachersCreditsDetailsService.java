/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.teacher.credits.TeacherCreditsDetailsDTO;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.Department;
import Dominio.ExecutionPeriod;
import Dominio.ICredits;
import Dominio.IDepartment;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.util.TransformationUtils;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentCredits;
/**
 * @author jpvl
 */
public class ReadDepartmentTeachersCreditsDetailsService implements IService
{
    public ReadDepartmentTeachersCreditsDetailsService()
    {
    }
    /**
     * @param searchParameters
     *            idInternal (java.lang.String); executionPeriodId
     *            (java.lang.String)
     * @return @throws
     *         FenixServiceException
     */
    public List run(HashMap searchParameters) throws FenixServiceException
    {
        ISuportePersistente sp;
        List teachers;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            teachers = doSearch(searchParameters, sp);

            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            final IExecutionPeriod executionPeriod =
                readExecutionPeriod(searchParameters, executionPeriodDAO);

            List teachersIds = TransformationUtils.transformToIds(teachers);

            final List creditsList =
                creditsDAO.readByTeachersAndExecutionPeriod(teachersIds, executionPeriod);

            List teachersCreditsDetailsList = (List) CollectionUtils.collect(teachers, new Transformer()
            {
                public Object transform(Object input)
                {
                    ITeacher teacher = (ITeacher) input;
                    InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                    TeacherCreditsDetailsDTO teacherCreditsDetailsDTO =
                        new TeacherCreditsDetailsDTO(infoTeacher);
                    ICredits credits = getCredits(teacher);
                    if (credits != null)
                    {
                        InfoCredits infoCredits = Cloner.copyICredits2InfoCredits(credits);
                        teacherCreditsDetailsDTO.setInfoCredits(infoCredits);
                    }
                    return teacherCreditsDetailsDTO;
                }

                private ICredits getCredits(ITeacher teacher)
                {
                    ICredits credits = new Credits();
                    credits.setTeacher(teacher);
                    credits.setExecutionPeriod(executionPeriod);
                    int indeOf = creditsList.indexOf(credits);
                    return (ICredits) (indeOf == -1 ? null : creditsList.get(indeOf));
                }

            });

            return teachersCreditsDetailsList;
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems with database!", e);
        }
    } /*
       * (non-Javadoc)
       * 
       * @see ServidorAplicacao.Servico.framework.SearchService#doSearch(java.util.HashMap,
       *      ServidorPersistente.ISuportePersistente)
       */
    /**
     * @param searchParameters
     * @param executionPeriodDAO
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private IExecutionPeriod readExecutionPeriod(
        HashMap searchParameters,
        IPersistentExecutionPeriod executionPeriodDAO)
        throws ExcepcaoPersistencia
    {
        final IExecutionPeriod executionPeriod;
        Integer executionPeriodId = null;
        try
        {
            executionPeriodId = Integer.valueOf((String) searchParameters.get("executionPeriodId"));
        } catch (NumberFormatException e)
        {
        }
        
        if (executionPeriodId == null)
        {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else
        {
            executionPeriod =
                (IExecutionPeriod) executionPeriodDAO.readByOId(
                    new ExecutionPeriod(executionPeriodId),
                    false);
        }
        return executionPeriod;
    }
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        Integer departmentId = Integer.valueOf((String) searchParameters.get("idInternal"));
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
        IDepartment department =
            (IDepartment) departmentDAO.readByOId(new Department(departmentId), false);
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        List teachers = teacherDAO.readByDepartment(department);
        return teachers;
    }
}