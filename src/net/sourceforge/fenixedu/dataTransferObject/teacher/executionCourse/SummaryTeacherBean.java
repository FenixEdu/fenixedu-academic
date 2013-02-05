package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.util.BundleUtil;

public class SummaryTeacherBean implements Serializable {

    private Professorship professorshipReference;

    private Boolean others;

    public SummaryTeacherBean(Professorship professorship) {
        if (professorship == null) {
            throw new RuntimeException();
        }
        setProfessorship(professorship);
    }

    public SummaryTeacherBean(Boolean ohters) {
        if (ohters == null) {
            throw new RuntimeException();
        }
        setOthers(ohters);
    }

    public Boolean getOthers() {
        return others;
    }

    public void setOthers(Boolean others) {
        this.others = others;
    }

    public Professorship getProfessorship() {
        return this.professorshipReference;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorshipReference = professorship;
    }

    public String getLabel() {
        if (getProfessorship() != null) {
            return getProfessorship().getPerson().getName();
        } else if (getOthers()) {
            return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.others");
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SummaryTeacherBean
                && (getProfessorship() == null || getProfessorship().equals(((SummaryTeacherBean) obj).getProfessorship())) && (getOthers() == null || getOthers()
                .equals(((SummaryTeacherBean) obj).getOthers())));
    }

    @Override
    public int hashCode() {
        return 37 * (((getProfessorship() != null) ? getProfessorship().hashCode() : 37) + ((getOthers() != null) ? getOthers()
                .hashCode() : 37));
    }
}
