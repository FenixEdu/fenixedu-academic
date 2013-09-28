package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

public class FenixCourseGroup {

    public static class Grouping {

        public static class Group {

            public static class Student {
                String istId;
                String name;

                public Student(String istId, String name) {
                    super();
                    this.istId = istId;
                    this.name = name;
                }

                public String getIstId() {
                    return istId;
                }

                public void setIstId(String istId) {
                    this.istId = istId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

            }

            Integer number;
            List<Student> students;

            public Group(Integer number, List<Student> students) {
                super();
                this.number = number;
                this.students = students;
            }

            public Integer getNumber() {
                return number;
            }

            public void setNumber(Integer number) {
                this.number = number;
            }

            public List<Student> getStudents() {
                return students;
            }

            public void setStudents(List<Student> students) {
                this.students = students;
            }

        }

        String name;
        String description;
        List<Group> groups;

        public Grouping(String name, String description, List<Group> groups) {
            super();
            this.name = name;
            this.description = description;
            this.groups = groups;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }

    }

    String name;
    String year;
    Integer semester;
    List<Grouping> groupings;

    public FenixCourseGroup(String name, String year, Integer semester, List<Grouping> groupings) {
        super();
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.groupings = groupings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<Grouping> getGroupings() {
        return groupings;
    }

    public void setGroupings(List<Grouping> groupings) {
        this.groupings = groupings;
    }

}
