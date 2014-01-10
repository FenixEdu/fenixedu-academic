package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import net.sourceforge.fenixedu.domain.Degree;

public class FenixDegree {
    protected String id;
    protected String name;
    protected String acronym;

    public FenixDegree() {
        this(null, null, null);
    }

    public FenixDegree(Degree degree) {
        this(degree.getExternalId(), degree.getPresentationName(), degree.getSigla());
    }

    public FenixDegree(String id, String name, String acronym) {
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
