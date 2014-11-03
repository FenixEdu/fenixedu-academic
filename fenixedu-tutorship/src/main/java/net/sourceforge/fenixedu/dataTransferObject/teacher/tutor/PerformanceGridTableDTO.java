/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;

public class PerformanceGridTableDTO extends DataTranferObject {
    private ExecutionYear studentEntryYear;

    private ExecutionYear monitoringYear;

    private List<PerformanceGridLine> performanceGridTableLines;

    public PerformanceGridTableDTO(ExecutionYear studentEntryYear, ExecutionYear monitoringYear) {
        setStudentEntryYear(studentEntryYear);
        setMonitoringYear(monitoringYear);
        this.performanceGridTableLines = new ArrayList<PerformanceGridLine>();
    }

    public ExecutionYear getMonitoringYear() {
        return (monitoringYear == null) ? null : this.monitoringYear;
    }

    public void setMonitoringYear(ExecutionYear monitoringYear) {
        this.monitoringYear = monitoringYear;
    }

    public ExecutionYear getStudentEntryYear() {
        return (studentEntryYear == null) ? null : this.studentEntryYear;
    }

    public void setStudentEntryYear(ExecutionYear studentEntryYear) {
        this.studentEntryYear = studentEntryYear;
    }

    public List<PerformanceGridLine> getPerformanceGridTableLines() {
        return performanceGridTableLines;
    }

    public void setPerformanceGridTableLines(List<PerformanceGridLine> performanceGridTableLines) {
        this.performanceGridTableLines = performanceGridTableLines;
    }

    public PerformanceGridLine getNewPerformanceGridLine(Tutorship tutorship) {
        return new PerformanceGridLine(tutorship);
    }

    public void addPerformanceGridLine(PerformanceGridLine newLine) {
        this.performanceGridTableLines.add(newLine);

    }

    /*
     * Performance Grid Line (one for each student)
     */
    public class PerformanceGridLine {
        private Registration registration;

        private Tutorship tutorship;

        private Double aritmeticAverage;

        private Integer approvedEnrolmentsNumber;

        private List<Enrolment> enrolmentsWithLastExecutionPeriod;

        private List<Integer> approvedRatioBySemester = new ArrayList<Integer>(2);

        private List<PerformanceGridLineYearGroup> studentPerformanceByYear = new ArrayList<PerformanceGridLineYearGroup>(5);

        public PerformanceGridLine(Tutorship tutoship) {
            setTutorship(tutoship);
            setRegistration(tutoship.getStudentCurricularPlan().getRegistration());
        }

        public Registration getRegistration() {
            return (registration);
        }

        public void setRegistration(Registration registration) {
            this.registration = registration;
        }

        public Tutorship getTutorship() {
            return (tutorship);
        }

        public void setTutorship(Tutorship tutorship) {
            this.tutorship = tutorship;
        }

        public List<Integer> getApprovedRatioBySemester() {
            return approvedRatioBySemester;
        }

        public void setApprovedRatioBySemester(List<Integer> approvedRatioBySemester) {
            this.approvedRatioBySemester = approvedRatioBySemester;
        }

        public Integer getApprovedRatioFirstSemester() {
            return approvedRatioBySemester.iterator().next();
        }

        public Integer getApprovedRatioSecondSemester() {
            return approvedRatioBySemester.get(1);
        }

        public Double getAritmeticAverage() {
            return aritmeticAverage;
        }

        public void setAritmeticAverage(Double aritmeticAverage) {
            this.aritmeticAverage = aritmeticAverage;
        }

        public Integer getApprovedEnrolmentsNumber() {
            return approvedEnrolmentsNumber;
        }

        public void setApprovedEnrolmentsNumber(Integer approvedEnrolmentsNumber) {
            this.approvedEnrolmentsNumber = approvedEnrolmentsNumber;
        }

        public String getEntryGrade() {
            if (this.getRegistration().getEntryGrade() != null) {
                return String.format("%.2f", this.getRegistration().getEntryGrade());
            } else {
                return ("-");
            }
        }

        public List<PerformanceGridLineYearGroup> getStudentPerformanceByYear() {
            return this.studentPerformanceByYear;
        }

        public void setStudentPerformanceByYear(List<PerformanceGridLineYearGroup> studentPerformanceByYear) {
            this.studentPerformanceByYear = studentPerformanceByYear;
        }

        public List<Enrolment> getEnrolmentsWithLastExecutionPeriod() {
            return this.enrolmentsWithLastExecutionPeriod;
        }

        public void setEnrolmentsWithLastExecutionPeriod(List<Enrolment> enrolmentsWithLastExecutionPeriod) {
            this.enrolmentsWithLastExecutionPeriod = enrolmentsWithLastExecutionPeriod;
        }

        public ExecutionYear getCurrentMonitoringYearYear() {
            return getMonitoringYear();
        }

        public PerformanceGridLineYearGroup getNewPerformanceGridLineYearGroup() {
            return new PerformanceGridLineYearGroup();
        }

        /*
         * Student curricular information to present in each curricular year of
         * the performance grid
         */
        public class PerformanceGridLineYearGroup implements Serializable {
            private List<Enrolment> firstSemesterEnrolments;
            private List<Enrolment> secondSemesterEnrolments;
            private double enrolledFirstSemesterECTS;
            private double approvedFirstSemesterECTS;
            private double enrolledSecondSemesterECTS;
            private double approvedSecondSemesterECTS;

            public PerformanceGridLineYearGroup() {
                firstSemesterEnrolments = new ArrayList<Enrolment>();
                secondSemesterEnrolments = new ArrayList<Enrolment>();
                enrolledFirstSemesterECTS = 0.0;
                approvedFirstSemesterECTS = 0.0;
                enrolledSecondSemesterECTS = 0.0;
                approvedSecondSemesterECTS = 0.0;
            }

            public List<Enrolment> getFirstSemesterEnrolments() {
                List<Enrolment> enrolments = new ArrayList<Enrolment>();
                for (Enrolment enrolmentDR : this.firstSemesterEnrolments) {
                    enrolments.add(enrolmentDR);
                }
                return enrolments;
            }

            public void setFirstSemesterEnrolments(List<Enrolment> firstSemesterEnrolments) {
                this.firstSemesterEnrolments = new ArrayList<Enrolment>();
                for (Enrolment enrolment : firstSemesterEnrolments) {
                    this.firstSemesterEnrolments.add(enrolment);
                }
                updateInformationECTS();
            }

            public List<Enrolment> getSecondSemesterEnrolments() {
                List<Enrolment> enrolments = new ArrayList<Enrolment>();
                for (Enrolment enrolmentDR : this.secondSemesterEnrolments) {
                    enrolments.add(enrolmentDR);
                }
                return enrolments;
            }

            public void setSecondSemesterEnrolments(List<Enrolment> secondSemesterEnrolments) {
                this.secondSemesterEnrolments = new ArrayList<Enrolment>();
                for (Enrolment enrolment : secondSemesterEnrolments) {
                    this.secondSemesterEnrolments.add(enrolment);
                }
                updateInformationECTS();
            }

            public double getEnrolledFirstSemesterECTS() {
                return enrolledFirstSemesterECTS;
            }

            public void setEnrolledFirstSemesterECTS(double enrolledFirstSemesterECTS) {
                this.enrolledFirstSemesterECTS = enrolledFirstSemesterECTS;
            }

            public double getApprovedFirstSemesterECTS() {
                return approvedFirstSemesterECTS;
            }

            public void setApprovedFirstSemesterECTS(double approvedFirstSemesterECTS) {
                this.approvedFirstSemesterECTS = approvedFirstSemesterECTS;
            }

            public double getEnrolledSecondSemesterECTS() {
                return enrolledSecondSemesterECTS;
            }

            public void setEnrolledSecondSemesterECTS(double enrolledSecondSemesterECTS) {
                this.enrolledSecondSemesterECTS = enrolledSecondSemesterECTS;
            }

            public double getApprovedSecondSemesterECTS() {
                return approvedSecondSemesterECTS;
            }

            public void setApprovedSecondSemesterECTS(double approvedSecondSemesterECTS) {
                this.approvedSecondSemesterECTS = approvedSecondSemesterECTS;
            }

            public void addEnrolmentToSemester(DegreeModuleScope scope, CurricularCourse curricular, Enrolment enrolment) {
                if (curricular.isAnual()) {
                    this.firstSemesterEnrolments.add(enrolment);
                    this.secondSemesterEnrolments.add(enrolment);
                } else {
                    if (scope.isFirstSemester()) {
                        this.firstSemesterEnrolments.add(enrolment);
                    } else if (scope.isSecondSemester()) {
                        this.secondSemesterEnrolments.add(enrolment);
                    }
                }
                updateInformationECTS();
            }

            public void updateInformationECTS() {
                setEnrolledFirstSemesterECTS(0.0);
                setApprovedFirstSemesterECTS(0.0);
                setEnrolledSecondSemesterECTS(0.0);
                setApprovedSecondSemesterECTS(0.0);
                for (Enrolment enrolment : getFirstSemesterEnrolments()) {
                    if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                        this.approvedFirstSemesterECTS += enrolment.getEctsCreditsForCurriculum().doubleValue();
                    }
                    this.enrolledFirstSemesterECTS += enrolment.getEctsCreditsForCurriculum().doubleValue();
                }
                for (Enrolment enrolment : getSecondSemesterEnrolments()) {
                    if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                        this.approvedSecondSemesterECTS += enrolment.getEctsCreditsForCurriculum().doubleValue();
                    }
                    this.enrolledSecondSemesterECTS += enrolment.getEctsCreditsForCurriculum().doubleValue();
                }
            }
        }
    }
}
