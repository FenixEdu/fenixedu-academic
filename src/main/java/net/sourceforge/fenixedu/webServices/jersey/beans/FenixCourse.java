package net.sourceforge.fenixedu.webServices.jersey.beans;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class FenixCourse {
    String id;
    String acronym;
    String name;
    String academicTerm;
    String url;

    public FenixCourse(ExecutionCourse course) {
        setId(course.getExternalId());
        setAcronym(course.getSigla());
        setName(course.getName());
        setAcademicTerm(course.getExecutionPeriod().getQualifiedName());
        setUrl(FenixConfigurationManager.getFenixUrl() + course.getSite().getReversePath());
    }

    public FenixCourse(String id, String acronym, String name) {
        this(id, acronym, name, null, null);
    }

    public FenixCourse(String id, String acronym, String name, String academicTerm, String url) {
        super();
        this.id = id;
        this.acronym = acronym;
        this.name = name;
        this.academicTerm = academicTerm;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @JsonInclude(Include.NON_NULL)
    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    @JsonInclude(Include.NON_NULL)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}