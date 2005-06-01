/*
 * 
 * Created on 2003/08/21
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SearchExecutionCourses implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear, String executionCourseName)
            throws FenixServiceException, ExcepcaoPersistencia {

        List result = null;

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            final IExecutionPeriod executionPeriod = (IExecutionPeriod) sp
                    .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class,
                            infoExecutionPeriod.getIdInternal());
            IExecutionDegree executionDegree = null;

            if (infoExecutionDegree != null) {
                executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree().readByOID(
                        ExecutionDegree.class, infoExecutionDegree.getIdInternal());
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
                        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
                                .readCourseReportByExecutionCourse(executionCourse.getIdInternal());
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
                private Integer getApproved(List enrolments) {
                    int approved = 0;
                    Iterator iter = enrolments.iterator();
                    while (iter.hasNext()) {
                        IEnrolment enrolment = (IEnrolment) iter.next();
                        EnrollmentState enrollmentState = enrolment.getEnrollmentState();
                        if (enrollmentState.equals(EnrollmentState.APROVED)) {
                            approved++;
                        }
                    }
                    return new Integer(approved);
                }

                private Integer getEvaluated(List enrolments) {
                    int evaluated = 0;
                    Iterator iter = enrolments.iterator();
                    while (iter.hasNext()) {
                        IEnrolment enrolment = (IEnrolment) iter.next();
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
                            curricularCourse.getIdInternal(), executionPeriod.getIdInternal());
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
                    ISuportePersistente spTemp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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

                    List shifts = spTemp.getITurnoPersistente().readByExecutionCourse(executionCourse.getIdInternal());
                    Iterator iterator = shifts.iterator();
                    while (iterator.hasNext()) {
                        IShift shift = (IShift) iterator.next();

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

        return result;
    }
}