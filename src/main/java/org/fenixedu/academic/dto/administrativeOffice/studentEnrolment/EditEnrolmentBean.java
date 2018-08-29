package org.fenixedu.academic.dto.administrativeOffice.studentEnrolment;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.EctsCompetenceCourseConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsDegreeByCurricularYearConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsInstitutionByCurricularYearConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsInstitutionConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.degreeStructure.EmptyConversionTable;
import org.fenixedu.academic.domain.degreeStructure.NoEctsComparabilityTableFound;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class EditEnrolmentBean implements Serializable {

    private Double credits;
    private Double weight;
    private Grade normalizedGrade;
    private EctsConversionTable ectsConversionTable;
    private final Enrolment enrolment;
    private final StudentCurricularPlan studentCurricularPlan;

    public EditEnrolmentBean(Enrolment enrolment, StudentCurricularPlan studentCurricularPlan) {
        this.enrolment = enrolment;
        this.studentCurricularPlan = studentCurricularPlan;
        this.credits = enrolment.getEctsCredits();
        this.weight = enrolment.getWeigth();
        this.normalizedGrade = enrolment.getEctsGrade(studentCurricularPlan,new DateTime());
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Double getCredits() {
        return credits;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public Enrolment getEnrolment() {  return enrolment; }

    public Grade getNormalizedGrade() {
        if (getEctsConversionTable() instanceof EmptyConversionTable) {
            return normalizedGrade;
        }
        return getEctsConversionTable().convert(enrolment.getGrade());
    }

    public void setNormalizedGrade(Grade normalizedGrade) {
        this.normalizedGrade = normalizedGrade;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public EctsConversionTable getEctsConversionTable() {
        return ectsConversionTable == null ? getEnrolmentEctsConversionTable() : ectsConversionTable;
    }

    public EctsConversionTable getEnrolmentEctsConversionTable() {
        return enrolment.getEctsConversionTable(getStudentCurricularPlan(), new DateTime());
    }

    public void setEctsConversionTable(EctsConversionTable table) {
        ectsConversionTable = table;
    }

    public boolean isEmptyTable() {
        return getEctsConversionTable() instanceof EmptyConversionTable;
    }

    public static final class ConversionTableProvider extends AbstractDomainObjectProvider {
        static final List<Class<? extends EctsConversionTable>> tablesOrder = Stream.of(EmptyConversionTable.class, EctsCompetenceCourseConversionTable.class,
                                                                           EctsDegreeByCurricularYearConversionTable.class, EctsInstitutionByCurricularYearConversionTable.class,
                                                                           EctsInstitutionConversionTable.class).collect(Collectors.toList());
        private EctsTableIndex getIndex(AcademicInterval interval) {
            if (interval == null) {
                return null;
            }
            EctsTableIndex ectsTableIndex = EctsTableIndex.readByYear(interval);
            if (ectsTableIndex == null) {
                return getIndex(interval.getPreviousAcademicInterval());
            }
            return ectsTableIndex;
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final Comparator<EctsConversionTable> cmpEctsTable = (a,b) -> {
                final Class<? extends EctsConversionTable> aClass = a.getClass();
                final Class<? extends EctsConversionTable> bClass = b.getClass();

                if (aClass == bClass) {
                    return a.getExternalId().compareTo(b.getExternalId());
                }

                return Integer.compare(tablesOrder.indexOf(a.getClass()), tablesOrder.indexOf(b.getClass()));
            };

            final Set<EctsConversionTable> ectsTables = new TreeSet<>((a,b) -> {

                if  (a == null && b == null) {
                    return 0;
                }

                if (a == null) {
                    return 1;
                }

                if (b == null) {
                    return -1;
                }

                if (a.equals(b)) {
                    return 0;
                }

                if (a.getYear() != null && b.getYear() != null) {
                    int i = a.getYear().getStart().compareTo(b.getYear().getStart());
                    if (i != 0) {
                        return i;
                    }
                }

                return cmpEctsTable.compare(a, b);
            });
            
            ectsTables.add(EmptyConversionTable.getInstance());

            final EditEnrolmentBean bean = (EditEnrolmentBean) source;
            ectsTables.add(bean.getEctsConversionTable());
            ectsTables.add(bean.getEnrolmentEctsConversionTable());

            final Enrolment enrolment = bean.getEnrolment();
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            final Degree degree = curricularCourse.getDegree();
            final AcademicInterval academicInterval = enrolment.getExecutionYear().getAcademicInterval();
            
            fillup(ectsTables, enrolment, curricularCourse, degree, getIndex(academicInterval));
            fillup(ectsTables, enrolment, curricularCourse, degree, EctsTableIndex.readByYearProcessable(academicInterval, new DateTime()));
            ectsTables.remove(null);
            return ectsTables;
        }

        private void fillup(Set<EctsConversionTable> ectsTables, Enrolment enrolment, CurricularCourse curricularCourse, Degree degree, EctsTableIndex ectsTableIndex) {
            if (ectsTableIndex != null) {
                try {
                    ectsTables.add(ectsTableIndex.getEnrolmentTableBy(curricularCourse.getCompetenceCourse()));
                } catch (NoEctsComparabilityTableFound e) {
                    // don't add this table to the list
                }
                try {
                    ectsTables.add(ectsTableIndex.getEctsConversionTable(degree, enrolment));
                } catch (NoEctsComparabilityTableFound e) {
                    // don't add this table to the list
                }
                
                if (enrolment.getParentCycleCurriculumGroup() == null) {
                    try {
                        ectsTables.add(ectsTableIndex.getEctsConversionTable(Bennu.getInstance().getInstitutionUnit(), enrolment));
                    }catch (NoEctsComparabilityTableFound e) {
                        // don't add this table to the list
                    }
                } else{
                    CurricularYear curricularYear =
                        CurricularYear.readByYear(enrolment.getParentCycleCurriculumGroup()
                                                           .getCurriculum(enrolment.getExecutionYear()).getCurricularYear());
                    try{
                        ectsTables.add(ectsTableIndex.getEnrolmentTableBy(degree, curricularYear));
                    }catch (NoEctsComparabilityTableFound e){
                        // don't add this table to the list

                    }
                    try {
                        ectsTables.add(ectsTableIndex.getEctsConversionTable(Bennu.getInstance().getInstitutionUnit(), enrolment, curricularYear));
                    }catch (NoEctsComparabilityTableFound e) {
                        // don't add this table to the list
                    }

                    try {
                        ectsTables.add(ectsTableIndex.getEctsConversionTable(Bennu.getInstance().getInstitutionUnit(), enrolment));
                    }catch (NoEctsComparabilityTableFound e) {
                        // don't add this table to the list
                    }
                }
            }
        }

    }

}
