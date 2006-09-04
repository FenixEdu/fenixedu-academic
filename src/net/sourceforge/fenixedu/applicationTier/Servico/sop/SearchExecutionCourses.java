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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class SearchExecutionCourses extends Service {

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear, String executionCourseName)
            throws ExcepcaoPersistencia {

        List result = null;

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        
        ExecutionDegree executionDegree = null;
        if (infoExecutionDegree != null) {
            executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        }

        CurricularYear curricularYear = null;
        if(infoCurricularYear != null) {
        	curricularYear = rootDomainObject.readCurricularYearByOID(infoCurricularYear.getIdInternal());
        }

        
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        if(executionPeriod != null) {
        	executionCourses = executionPeriod.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(executionDegree.getDegreeCurricularPlan(), curricularYear, executionCourseName);
        }

        result = (List) CollectionUtils.collect(executionCourses, new Transformer() {

            public Object transform(Object arg0) {
                InfoExecutionCourse infoExecutionCourse = null;
                    // Get the occupancy Levels
                    infoExecutionCourse = getOccupancyLevels(arg0);

                    // fill infomation regarding to teacher report
                    getTeacherReportInformation(infoExecutionCourse, arg0);
                return infoExecutionCourse;
            }

            private void getTeacherReportInformation(InfoExecutionCourse infoExecutionCourse, Object arg0) {

                ExecutionCourse executionCourse = (ExecutionCourse) arg0;

                if (executionCourse.getAssociatedCurricularCourses() != null) {

                    InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
                    int enrolledInCurricularCourse = 0;
                    int evaluated = 0;
                    int approved = 0;
                    Iterator iter = executionCourse.getAssociatedCurricularCourses().iterator();
                    while (iter.hasNext()) {
                        CurricularCourse curricularCourse = (CurricularCourse) iter.next();

                        List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
                        if (enrolled != null) {
                            enrolledInCurricularCourse += enrolled.size();
                            evaluated += getEvaluated(enrolled).intValue();
                            approved += getApproved(enrolled).intValue();
                        }
                    }
                    infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolledInCurricularCourse));
                    infoSiteEvaluationStatistics.setEvaluated(new Integer(evaluated));
                    infoSiteEvaluationStatistics.setApproved(new Integer(approved));

                    infoExecutionCourse.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);
                }
            }

            private Integer getApproved(List enrolments) {
                int approved = 0;
                Iterator iter = enrolments.iterator();
                while (iter.hasNext()) {
                    Enrolment enrolment = (Enrolment) iter.next();
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
                    Enrolment enrolment = (Enrolment) iter.next();
                    EnrollmentState enrollmentState = enrolment.getEnrollmentState();
                    if (enrollmentState.equals(EnrollmentState.APROVED)
                            || enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
                        evaluated++;
                    }
                }
                return new Integer(evaluated);
            }

            private InfoExecutionCourse getOccupancyLevels(Object arg0) {
                InfoExecutionCourse infoExecutionCourse;
                // Get the associated Shifs
                ExecutionCourse executionCourse = (ExecutionCourse) arg0;

                // FIXME: Find a better way to get the total
                // capacity for
                // each type of Shift
                Integer theoreticalCapacity = new Integer(0);
                Integer theoPraticalCapacity = new Integer(0);
                Integer praticalCapacity = new Integer(0);
                Integer labCapacity = new Integer(0);
                Integer doubtsCapacity = new Integer(0);
                Integer reserveCapacity = new Integer(0);

                List shifts = executionCourse.getAssociatedShifts();
                Iterator iterator = shifts.iterator();
                while (iterator.hasNext()) {
                    Shift shift = (Shift) iterator.next();

                    if (shift.getTipo().equals(ShiftType.TEORICA)) {
                        theoreticalCapacity = new Integer(theoreticalCapacity.intValue()
                                + shift.getLotacao().intValue());
                    } else if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
                        theoPraticalCapacity = new Integer(theoPraticalCapacity.intValue()
                                + shift.getLotacao().intValue());
                    } else if (shift.getTipo().equals(ShiftType.DUVIDAS)) {
                        doubtsCapacity = new Integer(doubtsCapacity.intValue()
                                + shift.getLotacao().intValue());
                    } else if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
                        labCapacity = new Integer(labCapacity.intValue() + shift.getLotacao().intValue());
                    } else if (shift.getTipo().equals(ShiftType.PRATICA)) {
                        praticalCapacity = new Integer(praticalCapacity.intValue()
                                + shift.getLotacao().intValue());
                    } else if (shift.getTipo().equals(ShiftType.RESERVA)) {
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
                    infoExecutionCourse.setOccupancy(NumberUtils.formatNumber(new Double((new Double(
                            executionCourse.getAttendsCount()).floatValue() * 100 / total)), 1));
                }
                return infoExecutionCourse;
            }
        });

        return result;
    }
}