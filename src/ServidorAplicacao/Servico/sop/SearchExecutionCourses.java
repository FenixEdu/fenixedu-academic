/*
 * 
 * Created on 2003/08/21
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.gesdis.InfoSiteEvaluationStatistics;
import Dominio.CurricularYear;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IEnrollment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import Util.EnrollmentState;
import Util.NumberUtils;
import Util.TipoAula;

public class SearchExecutionCourses implements IServico {

    private static SearchExecutionCourses _servico = new SearchExecutionCourses();

    /**
     * The singleton access method of this class.
     */
    public static SearchExecutionCourses getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private SearchExecutionCourses() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "SearchExecutionCourses";
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear, String executionCourseName)
            throws FenixServiceException {

        List result = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            final IExecutionPeriod executionPeriod = (IExecutionPeriod) sp
                    .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class,
                            infoExecutionPeriod.getIdInternal());
            ICursoExecucao executionDegree = null;

            if (infoExecutionDegree != null) {
                executionDegree = (ICursoExecucao) sp.getIPersistentExecutionDegree().readByOID(
                        CursoExecucao.class, infoExecutionDegree.getIdInternal());
            }

            ICurricularYear curricularYear = null;
            if (infoCurricularYear != null) {
                curricularYear = (ICurricularYear) sp.getIPersistentCurricularYear().readByOID(
                        CurricularYear.class, infoCurricularYear.getIdInternal());
            }

            List executionCourses = sp.getIPersistentExecutionCourse()
                    .readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(executionPeriod,
                            executionDegree, curricularYear, executionCourseName);

            result = (List) CollectionUtils.collect(executionCourses, new Transformer() {

                public Object transform(Object arg0) {
                    InfoExecutionCourse infoExecutionCourse = null;
                    try {

                        // Get the occupancy Levels
                        infoExecutionCourse = getOccupancyLevels(arg0);

                        // Check if the curricular Loads are all the
                        // same

                        checkEqualLoads(arg0, infoExecutionCourse, executionPeriod);

                        // fill infomation regarding to teacher report

                        getTeacherReportInformation(infoExecutionCourse, arg0);

                    } catch (ExcepcaoPersistencia e) {
                    }
                    return infoExecutionCourse;
                }

                private void getTeacherReportInformation(InfoExecutionCourse infoExecutionCourse,
                        Object arg0) throws ExcepcaoPersistencia {

                    IExecutionCourse executionCourse = (IExecutionCourse) arg0;

                    if (executionCourse.getAssociatedCurricularCourses() != null) {
                        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

                        InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
                        int enrolledInCurricularCourse = 0;
                        int evaluated = 0;
                        int approved = 0;
                        Iterator iter = executionCourse.getAssociatedCurricularCourses().iterator();
                        while (iter.hasNext()) {
                            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

                            List enrolled = getEnrolled(executionCourse.getExecutionPeriod(),
                                    curricularCourse, sp);
                            if (enrolled != null) {
                                enrolledInCurricularCourse += enrolled.size();
                                evaluated += getEvaluated(enrolled).intValue();
                                approved += getApproved(enrolled).intValue();
                            }
                        }
                        infoSiteEvaluationStatistics
                                .setEnrolled(new Integer(enrolledInCurricularCourse));
                        infoSiteEvaluationStatistics.setEvaluated(new Integer(evaluated));
                        infoSiteEvaluationStatistics.setApproved(new Integer(approved));

                        infoExecutionCourse
                                .setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);

                        IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
                        ICourseReport courseReport = persistentCourseReport
                                .readCourseReportByExecutionCourse(executionCourse);
                        if (courseReport != null) {
                            infoExecutionCourse
                                    .setCourseReportFilled(courseReport.getReport() != null ? new String(
                                            "true")
                                            : new String("false"));
                        }
                    }
                }

                /**
                 * @param curricularCourses
                 * @param sp
                 * @return
                 */
                private Integer getApproved(List enrolments) throws ExcepcaoPersistencia {
                    int approved = 0;
                    Iterator iter = enrolments.iterator();
                    while (iter.hasNext()) {
                        IEnrollment enrolment = (IEnrollment) iter.next();
                        EnrollmentState enrollmentState = enrolment.getEnrollmentState();
                        if (enrollmentState.equals(EnrollmentState.APROVED)) {
                            approved++;
                        }
                    }
                    return new Integer(approved);
                }

                private Integer getEvaluated(List enrolments) throws ExcepcaoPersistencia {
                    int evaluated = 0;
                    Iterator iter = enrolments.iterator();
                    while (iter.hasNext()) {
                        IEnrollment enrolment = (IEnrollment) iter.next();
                        EnrollmentState enrollmentState = enrolment.getEnrollmentState();
                        if (enrollmentState.equals(EnrollmentState.APROVED)
                                || enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
                            evaluated++;
                        }
                    }
                    return new Integer(evaluated);
                }

                private List getEnrolled(IExecutionPeriod executionPeriod,
                        ICurricularCourse curricularCourse, ISuportePersistente sp)
                        throws ExcepcaoPersistencia {
                    IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
                    List enrolments = persistentEnrolment.readByCurricularCourseAndExecutionPeriod(
                            curricularCourse, executionPeriod);
                    return enrolments;
                }

                private void checkEqualLoads(Object arg0, InfoExecutionCourse infoExecutionCourse,
                        IExecutionPeriod executionPeriod) {
                    IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                    infoExecutionCourse.setEqualLoad(Boolean.TRUE.toString());

                    Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator();
                    while (iterator.hasNext()) {
                        ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                        if ((!executionCourse.getTheoPratHours().equals(
                                curricularCourse.getTheoPratHours()))
                                || (!executionCourse.getTheoreticalHours().equals(
                                        curricularCourse.getTheoreticalHours()))
                                || (!executionCourse.getPraticalHours().equals(
                                        curricularCourse.getPraticalHours()))
                                || (!executionCourse.getLabHours()
                                        .equals(curricularCourse.getLabHours()))) {
                            infoExecutionCourse.setEqualLoad(Boolean.FALSE.toString());
                            break;
                        }
                    }
                }

                private InfoExecutionCourse getOccupancyLevels(Object arg0) throws ExcepcaoPersistencia {
                    InfoExecutionCourse infoExecutionCourse;
                    // Get the associated Shifs
                    ISuportePersistente spTemp = SuportePersistenteOJB.getInstance();
                    IExecutionCourse executionCourse = (IExecutionCourse) arg0;

                    // FIXME: Find a better way to get the total
                    // capacity for
                    // each type of Shift
                    Integer theoreticalCapacity = new Integer(0);
                    Integer theoPraticalCapacity = new Integer(0);
                    Integer praticalCapacity = new Integer(0);
                    Integer labCapacity = new Integer(0);
                    Integer doubtsCapacity = new Integer(0);
                    Integer reserveCapacity = new Integer(0);

                    List shifts = spTemp.getITurnoPersistente().readByExecutionCourse(executionCourse);
                    Iterator iterator = shifts.iterator();
                    while (iterator.hasNext()) {
                        ITurno shift = (ITurno) iterator.next();

                        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
                            theoreticalCapacity = new Integer(theoreticalCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        } else if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
                            theoPraticalCapacity = new Integer(theoPraticalCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        } else if (shift.getTipo().equals(new TipoAula(TipoAula.DUVIDAS))) {
                            doubtsCapacity = new Integer(doubtsCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        } else if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
                            labCapacity = new Integer(labCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        } else if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
                            praticalCapacity = new Integer(praticalCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        } else if (shift.getTipo().equals(new TipoAula(TipoAula.RESERVA))) {
                            reserveCapacity = new Integer(reserveCapacity.intValue()
                                    + shift.getLotacao().intValue());
                        }

                    }
                    //CLONER
                    //infoExecutionCourse = (InfoExecutionCourse) Cloner
                    //        .get(executionCourse);
                    infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                    List capacities = new ArrayList();

                    if (theoreticalCapacity.intValue() != 0) {
                        capacities.add(theoreticalCapacity);
                    }
                    if (theoPraticalCapacity.intValue() != 0) {
                        capacities.add(theoPraticalCapacity);
                    }
                    if (doubtsCapacity.intValue() != 0) {
                        capacities.add(doubtsCapacity);
                    }
                    if (labCapacity.intValue() != 0) {
                        capacities.add(labCapacity);
                    }
                    if (praticalCapacity.intValue() != 0) {
                        capacities.add(praticalCapacity);
                    }
                    if (reserveCapacity.intValue() != 0) {
                        capacities.add(reserveCapacity);
                    }

                    int total = 0;

                    if (!capacities.isEmpty()) {
                        total = ((Integer) Collections.min(capacities)).intValue();
                    }

                    if (total == 0) {
                        infoExecutionCourse.setOccupancy(new Double(-1));
                    } else {
                        infoExecutionCourse.setOccupancy(NumberUtils.formatNumber(
                                new Double((new Double(executionCourse.getAttendingStudents().size())
                                        .floatValue() * 100 / total)), 1));
                    }
                    return infoExecutionCourse;
                }
            });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }
        return result;
    }
}