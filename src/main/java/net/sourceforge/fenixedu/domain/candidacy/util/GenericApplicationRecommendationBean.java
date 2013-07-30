package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation;
import pt.ist.fenixWebFramework.services.Service;

public class GenericApplicationRecommendationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected String institution;
    protected String email;

    public GenericApplicationRecommendationBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Service
    public void requestRecommendation(final GenericApplication application) {
        new GenericApplicationRecomentation(application, name, institution, email);
    }

}
