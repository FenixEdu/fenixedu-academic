/*
 * Created on Nov 14, 2003
 *  
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class ExamStudentAuthorizationFilter extends AuthorizationByRoleFilter
{

    public ExamStudentAuthorizationFilter()
    {
    }

    protected RoleType getRoleType()
    {
        return RoleType.STUDENT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try
        {
            if ((id == null) || (id.getRoles() == null)
                            || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                            || !attendsExamExecutionCourse(id, arguments))
            {
                throw new NotAuthorizedException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedException();
        }
    }

    private boolean attendsExamExecutionCourse(IUserView id, Object[] argumentos)
    {
        ISuportePersistente sp;
        IExam exam = null;
        InfoExam infoExam = null;
        List intersection = null;

        if (argumentos == null)
        {
            return false;
        }
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IFrequentaPersistente persistentFrequenta = sp.getIFrequentaPersistente();

            if (argumentos[1] instanceof InfoExam)
            {
                infoExam = (InfoExam) argumentos[1];
                exam = Cloner.copyInfoExam2IExam(infoExam);
            }
            else
            {
                exam = (IExam) persistentExam.readByOId(new Exam((Integer) argumentos[1]), false);
            }

            List frequentaList = persistentFrequenta.readByUsername((String) argumentos[0]);
            List executionCourses = exam.getAssociatedExecutionCourses();
            List frequentaExecutionCourses = new ArrayList();
            Iterator iter = frequentaList.iterator();
            while (iter.hasNext())
            {
                IFrequenta frequenta = (IFrequenta) iter.next();
                IExecutionCourse disciplinaExecucao = frequenta.getDisciplinaExecucao();
                frequentaExecutionCourses.add(disciplinaExecucao);
            }
            intersection = (List) CollectionUtils.intersection(
                            frequentaExecutionCourses, executionCourses);

        }
        catch (Exception ex)
        {
            return false;
        }
        return intersection.size() != 0;

    }

}
