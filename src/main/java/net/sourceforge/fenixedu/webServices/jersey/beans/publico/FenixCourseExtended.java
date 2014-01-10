package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegreeExtended.FenixTeacher;

public class FenixCourseExtended {

    public static class FenixCompetence {

        public static class BiblioRef {
            String author;
            String reference;
            String title;
            String year;
            String type;
            String url;

            public BiblioRef(String author, String reference, String title, String year, String type, String url) {
                super();
                this.author = author;
                this.reference = reference;
                this.title = title;
                this.year = year;
                this.type = type;
                this.url = url;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getReference() {
                return reference;
            }

            public void setReference(String reference) {
                this.reference = reference;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

        }

        public static class Degree {
            String id;
            String name;
            String acronym;

            public Degree(String id, String name, String acronym) {
                super();
                this.id = id;
                this.name = name;
                this.acronym = acronym;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAcronym() {
                return acronym;
            }

            public void setAcronym(String acronym) {
                this.acronym = acronym;
            }

        }

        String program;
        List<BiblioRef> bibliographicReferences;
        List<Degree> degrees;

        public FenixCompetence(String program, List<BiblioRef> bibliographicReferences, List<Degree> degrees) {
            super();
            this.program = program;
            this.bibliographicReferences = bibliographicReferences;
            this.degrees = degrees;
        }

        public String getProgram() {
            return program;
        }

        public void setProgram(String program) {
            this.program = program;
        }

        public List<BiblioRef> getBibliographicReferences() {
            return bibliographicReferences;
        }

        public void setBibliographicReferences(List<BiblioRef> bibliographicReferences) {
            this.bibliographicReferences = bibliographicReferences;
        }

        public List<Degree> getDegrees() {
            return degrees;
        }

        public void setDegrees(List<Degree> degrees) {
            this.degrees = degrees;
        }

    }

    String acronym;
    String name;
    String academicTerm;
    String evaluationMethod;
    Integer numberOfAttendingStudents;
    String announcementLink;
    String summaryLink;
    String url;
    List<FenixCompetence> moreInfo;
    List<FenixTeacher> teachers;

    public FenixCourseExtended(String acronym, String name, String evaluationMethod, String academicTerm,
            Integer numberOfAttendingStudents, String announcementLink, String summaryLink, String url,
            List<FenixCompetence> moreInfo, List<FenixTeacher> teachers) {
        super();
        this.acronym = acronym;
        this.name = name;
        this.academicTerm = academicTerm;
        this.evaluationMethod = evaluationMethod;
        this.numberOfAttendingStudents = numberOfAttendingStudents;
        this.announcementLink = announcementLink;
        this.summaryLink = summaryLink;
        this.moreInfo = moreInfo;
        this.teachers = teachers;
        this.url = url;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvaluationMethod() {
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    public Integer getNumberOfAttendingStudents() {
        return numberOfAttendingStudents;
    }

    public void setNumberOfStudents(Integer numberOfAttendingStudents) {
        this.numberOfAttendingStudents = numberOfAttendingStudents;
    }

    public String getAnnouncementLink() {
        return announcementLink;
    }

    public void setAnnouncementLink(String announcementLink) {
        this.announcementLink = announcementLink;
    }

    public String getSummaryLink() {
        return summaryLink;
    }

    public void setSummaryLink(String summaryLink) {
        this.summaryLink = summaryLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FenixCompetence> getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(List<FenixCompetence> moreInfo) {
        this.moreInfo = moreInfo;
    }

    public List<FenixTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<FenixTeacher> teachers) {
        this.teachers = teachers;
    }

}
