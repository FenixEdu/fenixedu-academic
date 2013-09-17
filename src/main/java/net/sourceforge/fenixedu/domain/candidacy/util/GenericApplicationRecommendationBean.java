package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation;
import pt.ist.fenixframework.Atomic;

public class GenericApplicationRecommendationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String title;
    protected String name;
    protected String institution;
    protected String email;

    public GenericApplicationRecommendationBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Atomic
    public void requestRecommendation(final GenericApplication application) {
        new GenericApplicationRecomentation(application, title, name, institution, email);
    }

}
