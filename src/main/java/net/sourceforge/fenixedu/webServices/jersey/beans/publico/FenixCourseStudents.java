package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

public class FenixCourseStudents {

    public static class FenixCourseStudent {

        public static class Evaluation {

            String evaluation;
            String mark;

            public Evaluation(String evaluation, String mark) {
                super();
                this.evaluation = evaluation;
                this.mark = mark;
            }

            public String getEvaluation() {
                return evaluation;
            }

            public void setEvaluation(String evaluation) {
                this.evaluation = evaluation;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

        }

        Integer number;
        String name;
        String degree;
        String degreeId;
        List<Evaluation> evaluations;

        public FenixCourseStudent(Integer number, String name, String degree, String degreeId, List<Evaluation> evaluations) {
            super();
            this.number = number;
            this.name = name;
            this.degree = degree;
            this.degreeId = degreeId;
            this.evaluations = evaluations;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getDegreeId() {
            return degreeId;
        }

        public void setDegreeId(String degreeId) {
            this.degreeId = degreeId;
        }

        public List<Evaluation> getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(List<Evaluation> evaluations) {
            this.evaluations = evaluations;
        }

    }

    Integer enrolmentNumber;
    String name;
    Integer semester;
    String year;
    List<FenixCourseStudent> students;

    public FenixCourseStudents(Integer enrolmentNumber, String name, Integer semester, String year,
            List<FenixCourseStudent> students) {
        super();
        this.enrolmentNumber = enrolmentNumber;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.students = students;
    }

    public Integer getEnrolmentNumber() {
        return enrolmentNumber;
    }

    public void setEnrolmentNumber(Integer enrolmentNumber) {
        this.enrolmentNumber = enrolmentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<FenixCourseStudent> getStudents() {
        return students;
    }

    public void setStudents(List<FenixCourseStudent> students) {
        this.students = students;
    }

}
