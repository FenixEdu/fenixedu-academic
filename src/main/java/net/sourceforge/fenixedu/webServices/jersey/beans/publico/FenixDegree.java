package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

public class FenixDegree {

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

    private String year;
    private String id;
    private String name;
    private String type;
    private String acronym;
    private String typeName;
    private List<String> campus;
    private FenixDegreeInfo info;
    private List<FenixTeacher> teachers;

    public FenixDegree() {

    }

    public FenixDegree(String year, String id, String name, String type, String acronym, String typeName, List<String> campus,
            FenixDegreeInfo info, List<FenixTeacher> teachers) {
        super();
        this.year = year;
        this.id = id;
        this.name = name;
        this.type = type;
        this.acronym = acronym;
        this.typeName = typeName;
        this.campus = campus;
        this.info = info;
        this.teachers = teachers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getCampus() {
        return campus;
    }

    public void setCampus(List<String> campus) {
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
