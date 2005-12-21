package net.sourceforge.fenixedu.domain;

public class DegreeCurricularPlanMembersGroup extends DegreeCurricularPlanMembersGroup_Base {

    public DegreeCurricularPlanMembersGroup() {
        super();
    }

    public DegreeCurricularPlanMembersGroup(IPerson creator, IDegreeCurricularPlan degreeCurricularPlan) {
        super();
        setCreator(creator);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

}
