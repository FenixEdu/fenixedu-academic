/*
 * Created on 19/Mai/2003
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSummary;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 */
public class ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        ISummary summary = getSummary(arguments);
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacherLogged = persistentTeacher.readTeacherByUsername(id.getUtilizador());
        
        try {
            if(isTeacherResponsible(arguments, request)){
                List teachers = getExecutionCourseTeachers(arguments, request);
                if(isTeacherExecutionCourseMember(teachers, arguments, id)){
                    throw new NotAuthorizedFilterException();                  
                }                
            }
            else if(summary.getTeacher().equals(teacherLogged)){ 
                System.out.println("********************************");
            }    
            else if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !lecturesExecutionCourse(id, arguments)
                    || !SummaryBelongsExecutionCourse(id, arguments)
                    || !summaryBelongsToTeacher(id, arguments) || !validTeacher(id, arguments)) {
                throw new NotAuthorizedFilterException("error.summary.not.authorized");
            }
        } catch (RuntimeException ex) {
            throw new NotAuthorizedFilterException(ex.getMessage());
        }
    }

    /**
     * @param arguments
     * @param request
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getExecutionCourseTeachers(Object[] arguments, ServiceRequest request) throws ExcepcaoPersistencia {
        IUserView userView = getRemoteUser(request);
              
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, ((Integer)arguments[0]));
        
        List professorShips = persistentProfessorship.readByExecutionCourse(executionCourse);
        
        return professorShips;      
    }
    
    /**
     * @param teachers
     * @param arguments
     * @param userView
     * @throws ExcepcaoPersistencia
     */
    private boolean isTeacherExecutionCourseMember(List teachers, Object[] arguments, IUserView userView) throws ExcepcaoPersistencia {       
        ISummary summary = getSummary(arguments);
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();       
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        
        ITeacher teacherLogged = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class,  ((Integer)arguments[0]));
        IProfessorship professorshipLogged = persistentProfessorship.readByTeacherAndExecutionCourse(teacherLogged, executionCourse);       
        
        if(summary.getProfessorship() != null && !summary.getProfessorship().equals(professorshipLogged)){
            System.out.println("************************111111111111111111111");
            return true;
        }
        if(summary.getTeacher() != null){
            IProfessorship professorship = persistentProfessorship.readByTeacherAndExecutionCourse(summary.getTeacher(), executionCourse);
            if(teachers.contains(professorship) && !summary.getTeacher().equals(teacherLogged)){
                System.out.println("************************222222222222222222222");
                return true;
            }
        }
       return false;
    }

    
    /**
     * @param arguments
     * @throws NotAuthorizedFilterException
     * @throws ExcepcaoPersistencia
     */
    private boolean isTeacherResponsible(Object[] arguments, ServiceRequest request) throws NotAuthorizedFilterException, ExcepcaoPersistencia {
        IUserView userView = getRemoteUser(request);              
        List responsibleTeachers = getResponsibleTeachers((Integer) arguments[0]);  
        boolean loggedIsResponsible = false;  
        
        for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
            ITeacher teacher = (ITeacher) iter.next();
            if (teacher.getPerson().getUsername().equals(userView.getUtilizador()))
                loggedIsResponsible = true;
            break;
        }
        return loggedIsResponsible;
    }

    /**
     * @param executionCourse
     * @return
     * @throws NotAuthorizedFilterException
     */
    private List getResponsibleTeachers(Integer executionCourse) throws NotAuthorizedFilterException {
        try {
            List result = null;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            result = persistentResponsibleFor.readByExecutionCourseID(executionCourse);
            
            List infoResult = new ArrayList();
            if (result != null) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                    ITeacher teacher = responsibleFor.getTeacher();
                    infoResult.add(teacher);
                }
                return infoResult;
            }
            return result;
            
        } catch (ExcepcaoPersistencia e) {
            throw new NotAuthorizedFilterException(e);
        }
    }

    /**
     * @param id
     * @param arguments
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private boolean validTeacher(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        ITeacher teacher = getTeacher(id);
        final InfoSummary infoSummary = getInfoSummary(arguments);
        if (infoSummary == null) {
            final ISummary summary = getSummary(arguments);
            if (summary.getProfessorship() == null && summary.getTeacher() != null
                    && !summary.getTeacher().getTeacherNumber().equals(teacher.getTeacherNumber())) {

                List professorships = getProfessorships(arguments);
                IProfessorship professorship = (IProfessorship) CollectionUtils.find(professorships,
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                IProfessorship professorship = (IProfessorship) arg0;
                                return professorship.getTeacher().getTeacherNumber().equals(
                                        summary.getTeacher().getTeacherNumber());
                            }
                        });
                return professorship == null;
            }
        } else {

            if (infoSummary.getInfoProfessorship() == null
                    && infoSummary.getInfoTeacher() != null
                    && !infoSummary.getInfoTeacher().getTeacherNumber().equals(
                            teacher.getTeacherNumber())) {

                List professorships = getProfessorships(arguments);
                IProfessorship professorship = (IProfessorship) CollectionUtils.find(professorships,
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                IProfessorship professorship = (IProfessorship) arg0;
                                return professorship.getTeacher().getTeacherNumber().equals(
                                        infoSummary.getInfoTeacher().getTeacherNumber());
                            }
                        });
                return professorship == null;
            }
        }
        return true;
    }

    /**
     * @param arguments
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private List getProfessorships(Object[] arguments) throws ExcepcaoPersistencia {
        ISummary summary = getSummary(arguments);
        ISuportePersistente persistenteSupport = SuportePersistenteOJB.getInstance();
        IPersistentProfessorship persistentProfessorship = persistenteSupport
                .getIPersistentProfessorship();
        if (summary.getShift() == null) {
            return persistentProfessorship.readByExecutionCourse(summary.getExecutionCourse());
        }
        return persistentProfessorship.readByExecutionCourse(summary.getShift().getDisciplinaExecucao());
    }

    /**
     * @param arguments
     * @return
     */
    private InfoSummary getInfoSummary(Object[] arguments) {
        InfoSummary infoSummary = null;
        if (arguments[1] instanceof InfoSummary) {
            infoSummary = (InfoSummary) arguments[1];
        }
        return infoSummary;
    }

    /**
     * @param id
     * @param arguments
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private boolean summaryBelongsToTeacher(IUserView id, Object[] arguments)
            throws ExcepcaoPersistencia {
        ITeacher teacher = getTeacher(id);
        ISummary summary = getSummary(arguments);
        if (summary.getProfessorship() == null) {
            return isResponsible(teacher, summary);
        }
        return isOwner(teacher, summary);

    }

    /**
     * @param teacher
     * @param summary
     * @return
     */
    private boolean isOwner(ITeacher teacher, ISummary summary) {
        if (summary.getTeacher() != null) {
            return teacher.getIdInternal().equals(summary.getTeacher().getIdInternal());
        }
        return teacher.getIdInternal().equals(summary.getProfessorship().getTeacher().getIdInternal());
    }

    /**
     * @param teacher
     * @param summary
     * @return
     */
    private boolean isResponsible(final ITeacher teacher, ISummary summary) {
        if (summary.getShift() == null) {
            return CollectionUtils.find(summary.getExecutionCourse().getResponsibleTeachers(),
                    new Predicate() {

                        public boolean evaluate(Object arg0) {
                            IResponsibleFor responsibleFor = (IResponsibleFor) arg0;
                            return responsibleFor.getTeacher().getIdInternal().equals(
                                    teacher.getIdInternal());
                        }
                    }) != null;
        }
        return CollectionUtils.find(summary.getShift().getDisciplinaExecucao().getResponsibleTeachers(),
                new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IResponsibleFor responsibleFor = (IResponsibleFor) arg0;
                        return responsibleFor.getTeacher().getIdInternal().equals(
                                teacher.getIdInternal());
                    }
                }) != null;

    }

    /**
     * @param arguments
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private ISummary getSummary(Object[] arguments) throws ExcepcaoPersistencia {
        ISummary summary = null;
        InfoSummary infoSummary = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentSummary persistentSummary = sp.getIPersistentSummary();
        if (arguments[1] instanceof InfoSummary) {
            infoSummary = (InfoSummary) arguments[1];
            summary = (ISummary) persistentSummary.readByOID(Summary.class, infoSummary.getIdInternal());
        } else {
            summary = (ISummary) persistentSummary.readByOID(Summary.class, (Integer) arguments[1]);
        }
        return summary;
    }

    /**
     * @param id
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private ITeacher getTeacher(IUserView id) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
        return teacher;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean SummaryBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        ISummary summary = null;
        InfoSummary infoSummary = null;

        if (argumentos == null) {
            return false;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            } else {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            if (argumentos[1] instanceof InfoSummary) {
                infoSummary = (InfoSummary) argumentos[1];

                summary = (ISummary) persistentSummary.readByOID(Summary.class, infoSummary
                        .getIdInternal());
                if (summary == null) {
                    return false;
                }
            } else {
                summary = (ISummary) persistentSummary.readByOID(Summary.class, (Integer) argumentos[1]);
                if (summary == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        if (summary.getShift() != null && summary.getShift().getDisciplinaExecucao() != null) {
            return summary.getShift().getDisciplinaExecucao().equals(executionCourse);
        } else if (summary.getExecutionCourse() != null) {
            return summary.getExecutionCourse().equals(executionCourse);
        } else {
            return false;
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IProfessorship professorship = null;
        if (argumentos == null) {
            return false;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            } else {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }
            ITeacher teacher = getTeacher(id);
            if (teacher != null && executionCourse != null) {
                IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
                professorship = persistentProfessorship.readByTeacherAndExecutionCoursePB(teacher,
                        executionCourse);
            }
        } catch (Exception e) {
            return false;
        }
        return professorship != null;
    }

}