package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.services.Service;

public class AuthorizationGroupBean implements Serializable, Comparable<AuthorizationGroupBean> {

    private static final long serialVersionUID = -8809011815711452960L;

    private PersistentAcademicAuthorizationGroup group;

    private AcademicOperationType operation;

    private Set<AcademicProgram> programs;

    private Set<AdministrativeOffice> offices;

    static public Comparator<AuthorizationGroupBean> COMPARATOR_BY_LOCALIZED_NAME = new Comparator<AuthorizationGroupBean>() {
        @Override
        public int compare(final AuthorizationGroupBean p1, final AuthorizationGroupBean p2) {
            String operationName1 = p1.getOperation().getLocalizedName();
            String operationName2 = p2.getOperation().getLocalizedName();
            int res = operationName1.compareTo(operationName2);
            return res;
        }
    };

    public AuthorizationGroupBean() {
        super();
        this.programs = new HashSet<AcademicProgram>();
        this.offices = new HashSet<AdministrativeOffice>();
    }

    public AuthorizationGroupBean(PersistentAcademicAuthorizationGroup group) {
        super();
        setGroup(group);
    }

    public long getId() {
        return group == null ? -1 : group.getOid();
    }

    public PersistentAcademicAuthorizationGroup getGroup() {
        return group;
    }

    private void setGroup(PersistentAcademicAuthorizationGroup group) {
        this.group = group;
        this.operation = group.getOperation();
        this.programs = new HashSet<AcademicProgram>(group.getProgramSet());
        this.offices = new HashSet<AdministrativeOffice>(group.getOfficeSet());
    }

    public AcademicOperationType getOperation() {
        return operation;
    }

    public void setOperation(AcademicOperationType operation) {
        this.operation = operation;
    }

    public boolean getNewObject() {
        return group == null;
    }

    public Set<AcademicProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<AcademicProgram> programs) {
        this.programs = programs;
    }

    public Set<AdministrativeOffice> getOffices() {
        return offices;
    }

    public void setOffices(Set<AdministrativeOffice> offices) {
        this.offices = offices;
    }

    @Service
    public void edit() {
        if (!group.hasDeletedRootDomainObject()) {
            setGroup(group.changeOperation(operation));
        }
    }

    @Service
    public void create(Party party, Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        for (PersistentAccessGroup accessGroup : party.getPersistentAccessGroup()) {
            if (accessGroup instanceof PersistentAcademicAuthorizationGroup && !accessGroup.hasDeletedRootDomainObject()) {
                if (((PersistentAcademicAuthorizationGroup) accessGroup).getOperation().equals(operation)) {
                    throw new DomainException("error.person.already.has.permission.of.type", operation.getLocalizedName());
                }
            }
        }
        setGroup(new PersistentAcademicAuthorizationGroup(operation, newPrograms, newOffices));
        party.addPersistentAccessGroup(group);
    }

    @Service
    public void editAuthorizationPrograms(Set<AcademicProgram> newPrograms, Set<AdministrativeOffice> newOffices) {
        if (!group.hasDeletedRootDomainObject()) {
            setGroup(group.changeProgramsAndOffices(newPrograms, newOffices));
        }
    }

    @Service
    public void delete(Party party) {
        if (group.getMember().size() > 1) {
            group.revoke(party);
        } else {
            group.delete();
        }
    }

    @Override
    public int compareTo(AuthorizationGroupBean bean) {
        if (this.group == bean.group) {
            return 0;
        }
        if (this.group == null) {
            return 1;
        }
        if (bean.group == null) {
            return -1;
        }
        return this.operation.compareTo(bean.operation);
    }

}
