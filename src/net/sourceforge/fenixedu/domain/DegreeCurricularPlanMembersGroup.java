package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeCurricularPlanMembersGroup extends DegreeCurricularPlanMembersGroup_Base {

    public DegreeCurricularPlanMembersGroup() {
        super();
    }

    public DegreeCurricularPlanMembersGroup(Person creator, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        setCreator(creator);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    @Override
    public void delete() {
        for (UserGroup part : this.getParts()) {
            this.removePart(part);            
            
            Person person = ((PersonGroup) part).getPerson();
            if (person.hasRole(RoleType.BOLONHA_MANAGER)) {
                if (!person.belongsToOtherGroupsWithSameRole(this)) {
                    person.removeRoleByType(RoleType.BOLONHA_MANAGER);
                }
            }
            
            if (!part.hasAnyAggregators()) {
                part.delete();
            }
        }
        super.deleteDomainObject();
    }

}
