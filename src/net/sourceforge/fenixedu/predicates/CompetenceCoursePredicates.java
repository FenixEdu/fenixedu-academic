/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CompetenceCoursePredicates {

    public static final AccessControlPredicate<CompetenceCourse> readPredicate = new AccessControlPredicate<CompetenceCourse>() {

        public boolean evaluate(CompetenceCourse competenceCourse) {

            if (!competenceCourse.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getUserView().getPerson();
            if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return true;
            }

            boolean isDegreeCurricularPlansMember = false;
            try {
                isDegreeCurricularPlansMember = isMemberOfDegreeCurricularPlansGroup(person);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                return false;
            }

            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || isDegreeCurricularPlansMember;
            case APPROVED:
                return true;
            default:
                return false;
            }

        }

    };

    public static final AccessControlPredicate<CompetenceCourse> writePredicate = new AccessControlPredicate<CompetenceCourse>() {

        public boolean evaluate(CompetenceCourse competenceCourse) {

            if (!competenceCourse.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getUserView().getPerson();
            
            if(person.hasRole(RoleType.MANAGER)){
                return true;
            }
            
            boolean isDegreeCurricularPlansMember = false;
            try {
                isDegreeCurricularPlansMember = isMemberOfDegreeCurricularPlansGroup(person);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                return false;
            }

            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || isDegreeCurricularPlansMember;
            case APPROVED:
                return false;
            default:
                return false;
            }

        }
    };

    public static final AccessControlPredicate<CompetenceCourse> editCurricularStagePredicate = new AccessControlPredicate<CompetenceCourse>(){
    
        public boolean evaluate(CompetenceCourse competenceCourse) {

            Person person = AccessControl.getUserView().getPerson();
            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);
            
            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
            case APPROVED:
                return person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
            default:
                return false;
            }
            
        }
    
    };

        
    
    private static boolean isMemberOfDegreeCurricularPlansGroup(Person person)
            throws ExcepcaoPersistencia {
        Collection<DegreeCurricularPlan> degreeCurricularPlans = RootDomainObject.getInstance().getDegreeCurricularPlans();

        Collection<IGroup> groups = new ArrayList<IGroup>();
        for (DegreeCurricularPlan plan : degreeCurricularPlans) {
            Group curricularPlanMembersGroup = plan.getCurricularPlanMembersGroup();
            if (curricularPlanMembersGroup != null) {
                groups.add(curricularPlanMembersGroup);
            }
        }

        return new GroupUnion(groups).isMember(person);
    }

    private static boolean isMemberOfCompetenceCourseGroup(CompetenceCourse competenceCourse,
            Person person) {
        Group competenceCourseMembersGroup = competenceCourse.getDepartmentUnit().getDepartment()
                .getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup != null) {
            return competenceCourseMembersGroup.isMember(person);
        }
        return false;
    }

}
