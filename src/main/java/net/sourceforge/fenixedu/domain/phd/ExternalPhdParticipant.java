package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalPhdParticipant extends ExternalPhdParticipant_Base {

    private ExternalPhdParticipant() {
        super();
    }

    ExternalPhdParticipant(final PhdIndividualProgramProcess process, final PhdParticipantBean bean) {
        this();
        init(process);
        edit(bean.getName(), bean.getTitle(), bean.getQualification(), bean.getWorkLocation(), bean.getInstitution(),
                bean.getEmail());
        edit(bean.getCategory(), bean.getAddress(), bean.getPhone());
    }

    void edit(final String name, final String title, final String qualification, final String workLocation,
            final String institution, final String email) {

        String[] args = {};
        if (name == null || name.isEmpty()) {
            throw new DomainException("error.ExternalPhdParticipant.invalid.name", args);
        }

        setName(name);
        setTitle(title);
        setQualification(qualification);
        setWorkLocation(workLocation);
        setInstitution(institution);
        setEmail(email);
    }

    void edit(final String category, final String address, final String phone) {
        setCategory(category);
        setAddress(address);
        setPhone(phone);
    }

    @Override
    public void edit(final PhdParticipantBean bean) {
        String obj = bean.getName();
        String[] args = {};
        if (obj == null || obj.isEmpty()) {
            throw new DomainException("error.ExternalPhdParticipant.invalid.name", args);
        }

        super.edit(bean);

        setName(bean.getName());
        setQualification(bean.getQualification());
        setAddress(bean.getAddress());
        setEmail(bean.getEmail());
        setPhone(bean.getPhone());
    }

    @Override
    public boolean isTeacher() {
        return false;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasQualification() {
        return getQualification() != null;
    }

    @Deprecated
    public boolean hasPhone() {
        return getPhone() != null;
    }

    @Deprecated
    public boolean hasEmail() {
        return getEmail() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

}
