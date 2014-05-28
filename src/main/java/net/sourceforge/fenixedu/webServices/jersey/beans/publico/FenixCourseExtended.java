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

        String id;
        String program;
        List<BiblioRef> bibliographicReferences;
        List<Degree> degrees;

        public FenixCompetence(String id, String program, List<BiblioRef> bibliographicReferences, List<Degree> degrees) {
            super();
            this.id = id;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    List<FenixCompetence> competences;
    List<FenixTeacher> teachers;

    public FenixCourseExtended(String acronym, String name, String evaluationMethod, String academicTerm,
            Integer numberOfAttendingStudents, String announcementLink, String summaryLink, String url,
            List<FenixCompetence> competences, List<FenixTeacher> teachers) {
        super();
        this.acronym = acronym;
        this.name = name;
        this.academicTerm = academicTerm;
        this.evaluationMethod = evaluationMethod;
        this.numberOfAttendingStudents = numberOfAttendingStudents;
        this.announcementLink = announcementLink;
        this.summaryLink = summaryLink;
        this.competences = competences;
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

    public List<FenixCompetence> getCompetences() {
        return competences;
    }

    public void setMoreInfo(List<FenixCompetence> competences) {
        this.competences = competences;
    }

    public List<FenixTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<FenixTeacher> teachers) {
        this.teachers = teachers;
    }

}
