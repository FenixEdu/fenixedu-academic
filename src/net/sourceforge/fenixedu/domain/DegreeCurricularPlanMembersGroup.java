package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;

public class DegreeCurricularPlanMembersGroup extends GroupUnion {

    private DomainReference<DegreeCurricularPlan> curricularPlan;

    public DegreeCurricularPlanMembersGroup(DegreeCurricularPlan degreeCurricularPlan) {
        super();
        
        curricularPlan = new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

    public DegreeCurricularPlan getCurricularPlan() {
        return this.curricularPlan.getObject();
    }
    
    /**
     * FIXME: check this with Luís Egídio
     */
//    @Override
//    public void delete() {
//        for (UserGroup part : this.getParts()) {
//            this.removePart(part);            
//            
//            Person person = ((PersonGroup) part).getPerson();
//            if (person.hasRole(RoleType.BOLONHA_MANAGER)) {
//                if (!person.belongsToOtherGroupsWithSameRole(this)) {
//                    person.removeRoleByType(RoleType.BOLONHA_MANAGER);
//                }
//            }
//            
//            if (!part.hasAnyAggregators()) {
//                part.delete();
//            }
//        }
//        super.deleteDomainObject();
//    }

}
