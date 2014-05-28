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

import net.sourceforge.fenixedu.domain.Degree;

public class FenixDegreeExtended extends FenixDegree {

    public static class FenixDegreeInfo {
        private String description;
        private String objectives;
        private String designFor;
        private String requisites;
        private String profissionalExits;
        private String history;
        private String operationRegime;
        private String gratuity;
        private String links;

        public FenixDegreeInfo(String description, String objectives, String designFor, String requisites,
                String profissionalExits, String history, String operationRegime, String gratuity, String links) {
            super();
            this.description = description;
            this.objectives = objectives;
            this.designFor = designFor;
            this.requisites = requisites;
            this.profissionalExits = profissionalExits;
            this.history = history;
            this.operationRegime = operationRegime;
            this.gratuity = gratuity;
            this.links = links;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getObjectives() {
            return objectives;
        }

        public void setObjectives(String objectives) {
            this.objectives = objectives;
        }

        public String getDesignFor() {
            return designFor;
        }

        public void setDesignFor(String designFor) {
            this.designFor = designFor;
        }

        public String getRequisites() {
            return requisites;
        }

        public void setRequisites(String requisites) {
            this.requisites = requisites;
        }

        public String getProfissionalExits() {
            return profissionalExits;
        }

        public void setProfissionalExits(String profissionalExits) {
            this.profissionalExits = profissionalExits;
        }

        public String getHistory() {
            return history;
        }

        public void setHistory(String history) {
            this.history = history;
        }

        public String getOperationRegime() {
            return operationRegime;
        }

        public void setOperationRegime(String operationRegime) {
            this.operationRegime = operationRegime;
        }

        public String getGratuity() {
            return gratuity;
        }

        public void setGratuity(String gratuity) {
            this.gratuity = gratuity;
        }

        public String getLinks() {
            return links;
        }

        public void setLinks(String links) {
            this.links = links;
        }

    }

    public static class FenixTeacher {
        private String name;
        private String istId;
        private List<String> mails;
        private List<String> urls;

        public FenixTeacher(String name, String istId, List<String> mails, List<String> urls) {
            super();
            this.name = name;
            this.istId = istId;
            this.mails = mails;
            this.urls = urls;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIstId() {
            return istId;
        }

        public void setIstId(String istId) {
            this.istId = istId;
        }

        public List<String> getMails() {
            return mails;
        }

        public void setMails(List<String> mails) {
            this.mails = mails;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

    }

    private String academicTerm;
    private String type;
    private String typeName;
    private String url;
    private List<FenixSpace> campus;
    private FenixDegreeInfo info;
    private List<FenixTeacher> teachers;

    public FenixDegreeExtended(String academicTerm, Degree degree, String type, String typeName, String url,
            List<FenixSpace> campus, FenixDegreeInfo info, List<FenixTeacher> teachers) {
        super(degree, true);
        this.type = type;
        this.typeName = typeName;
        this.url = url;
        this.campus = campus;
        this.info = info;
        this.teachers = teachers;
        this.academicTerm = academicTerm;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getAcronym() {
        return acronym;
    }

    @Override
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FenixSpace> getCampus() {
        return campus;
    }

    public void setCampus(List<FenixSpace> campus) {
        this.campus = campus;
    }

    public FenixDegreeInfo getInfo() {
        return info;
    }

    public void setInfo(FenixDegreeInfo info) {
        this.info = info;
    }

    public List<FenixTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<FenixTeacher> teachers) {
        this.teachers = teachers;
    }

}
