package net.sourceforge.fenixedu.domain;

public class DegreeCurricularPlanMembersGroup extends DegreeCurricularPlanMembersGroup_Base {

    public DegreeCurricularPlanMembersGroup() {
        super();
    }

    public DegreeCurricularPlanMembersGroup(Person creator, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        setCreator(creator);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

}
