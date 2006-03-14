/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.LogicOperators;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class CurricularRulesManager {

    public static CurricularRule createCurricularRule(DegreeModule degreeModuleToApplyRule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType curricularRuleType,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        switch (curricularRuleType) {

        case PRECEDENCY_APPROVED_DEGREE_MODULE:
            return createRestrictionDoneDegreeModule(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);

        case PRECEDENCY_ENROLED_DEGREE_MODULE:
            return createRestrictionEnroledDegreeModule(degreeModuleToApplyRule, begin, end,
                    parametersDTO, persistentObject);

        case CREDITS_LIMIT:
            return createCreditsLimit(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);

        case DEGREE_MODULES_SELECTION_LIMIT:
            return createDegreeModulesSelectionLimit(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);

        case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
            return createEnrolmentToBeApprovedByCoordinator(degreeModuleToApplyRule, begin, end,
                    parametersDTO, persistentObject);

        case PRECEDENCY_BETWEEN_DEGREE_MODULES:
            return createRestrictionBetweenDegreeModules(degreeModuleToApplyRule, begin, end,
                    parametersDTO, persistentObject);

        case EXCLUSIVENESS:
            return createExclusiveness(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);

        case ANY_CURRICULAR_COURSE:
            return createAnyCurricularCourse(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);
            
        case MINIMUM_NUMBER_OF_CREDITS_TO_ENROL:
            return createMinimumNumberOfCreditsToEnrol(degreeModuleToApplyRule, begin, end, parametersDTO,
                    persistentObject);
        default:
            break;
        }
        return null;
    }

    public static CurricularRule createCompositeRule(DegreeModule degreeModuleToApplyRule, LogicOperators logicOperator,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRule ... curricularRules) {
        return new CompositeRule(degreeModuleToApplyRule, begin, end, logicOperator, curricularRules);
    }
    
    public static void editCurricularRule(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        switch (curricularRule.getCurricularRuleType()) {
        
        case PRECEDENCY_APPROVED_DEGREE_MODULE:
            editRestrictionDoneDegreeModule(curricularRule, parametersDTO, persistentObject);
            break;

        case PRECEDENCY_ENROLED_DEGREE_MODULE:
            editRestrictionEnroledDegreeModule(curricularRule, parametersDTO, persistentObject);
            break;

        case CREDITS_LIMIT:
            editCreditsLimit(curricularRule, parametersDTO, persistentObject);
            break;

        case DEGREE_MODULES_SELECTION_LIMIT:
            editDegreeModulesSelectionLimit(curricularRule, parametersDTO, persistentObject);
            break;

        case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
            editEnrolmentToBeApprovedByCoordinator(curricularRule, parametersDTO, persistentObject);
            break;

        case PRECEDENCY_BETWEEN_DEGREE_MODULES:
            editResctrictionBetweenDegreeModules(curricularRule, parametersDTO, persistentObject);
            break;

        case EXCLUSIVENESS:
            editExclusiveness(curricularRule, parametersDTO, persistentObject);
            break;

        case ANY_CURRICULAR_COURSE:
            editAnyCurricularCourse(curricularRule, parametersDTO, persistentObject);
            break;
            
        case MINIMUM_NUMBER_OF_CREDITS_TO_ENROL:
            editMinimumNumberOfCreditsToEnrol(curricularRule, parametersDTO, persistentObject);
            break;

        default:
            break;
        }
    }
    
    private static CurricularRule createMinimumNumberOfCreditsToEnrol(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new MinimumNumberOfCreditsToEnrol(degreeModuleToApplyRule, contextCourseGroup, begin,
                end, parametersDTO.getMinimumCredits());
    }

    private static CurricularRule createAnyCurricularCourse(DegreeModule degreeModuleToApplyRule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO,
            IPersistentObject persistentObject) throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        final Degree degree = (Degree) persistentObject.readByOID(Degree.class, parametersDTO
                .getSelectedDegreeID());

        final Unit departmentUnit = (Unit) persistentObject.readByOID(Unit.class, parametersDTO
                .getSelectedDepartmentUnitID());

        return new AnyCurricularCourse((CurricularCourse) degreeModuleToApplyRule, contextCourseGroup,
                begin, end, parametersDTO.getCredits(), parametersDTO.getCurricularPeriodInfoDTO().getOrder(),
                parametersDTO.getMinimumYear(), parametersDTO.getMaximumYear(), parametersDTO.getBolonhaDegreeType(),
                degree, departmentUnit);
    }

    private static CurricularRule createExclusiveness(DegreeModule firstExclusiveDegreeModule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO,
            IPersistentObject persistentObject) throws ExcepcaoPersistencia {

        final DegreeModule secondExclusiveDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        final Exclusiveness firstRule = new Exclusiveness(firstExclusiveDegreeModule,
                secondExclusiveDegreeModule, contextCourseGroup, begin, end);

        new Exclusiveness(secondExclusiveDegreeModule, firstExclusiveDegreeModule, contextCourseGroup,
                begin, end);

        return firstRule;
    }

    private static CurricularRule createRestrictionBetweenDegreeModules(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModule precedenceDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new RestrictionBetweenDegreeModules(degreeModuleToApplyRule, precedenceDegreeModule,
                parametersDTO.getMinimumCredits(), contextCourseGroup, begin, end);
    }

    private static CurricularRule createEnrolmentToBeApprovedByCoordinator(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new EnrolmentToBeApprovedByCoordinator(degreeModuleToApplyRule, contextCourseGroup,
                begin, end);
    }

    private static CurricularRule createCreditsLimit(DegreeModule degreeModuleToApplyRule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO,
            IPersistentObject persistentObject) throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new CreditsLimit(degreeModuleToApplyRule, contextCourseGroup, begin, end, parametersDTO
                .getMinimumCredits(), parametersDTO.getMaximumCredits());
    }

    private static CurricularRule createDegreeModulesSelectionLimit(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new DegreeModulesSelectionLimit(degreeModuleToApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumLimit(), parametersDTO.getMaximumLimit());
    }

    private static CurricularRule createRestrictionEnroledDegreeModule(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModule enroledDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        return new RestrictionEnroledDegreeModule((CurricularCourse) degreeModuleToApplyRule,
                enroledDegreeModule, contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(),
                begin, end);
    }

    private static CurricularRule createRestrictionDoneDegreeModule(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModule doneDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        return new RestrictionDoneDegreeModule((CurricularCourse) degreeModuleToApplyRule,
                doneDegreeModule, contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin,
                end);
    }
    
    private static void editMinimumNumberOfCreditsToEnrol(CurricularRule curricularRule, CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject) throws ExcepcaoPersistencia {
        
        final MinimumNumberOfCreditsToEnrol minimumNumberOfCreditsToEnrol = (MinimumNumberOfCreditsToEnrol) curricularRule;
        
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        minimumNumberOfCreditsToEnrol.edit(contextCourseGroup, parametersDTO.getMinimumCredits());
    }

    private static void editAnyCurricularCourse(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {
        
        final AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) curricularRule;
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        final Degree degree = (Degree) persistentObject.readByOID(Degree.class, parametersDTO
                .getSelectedDegreeID());
        final Unit departmentUnit = (Unit) persistentObject.readByOID(Unit.class, parametersDTO
                .getSelectedDepartmentUnitID());
        
        anyCurricularCourse.edit(contextCourseGroup, parametersDTO.getCredits(), parametersDTO
                .getCurricularPeriodInfoDTO().getOrder(), parametersDTO.getMinimumYear(), parametersDTO
                .getMaximumYear(), parametersDTO.getBolonhaDegreeType(), degree, departmentUnit);
    }

    private static void editExclusiveness(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
        final DegreeModule exclusiveDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        exclusiveness.edit(exclusiveDegreeModule, contextCourseGroup);
    }

    private static void editResctrictionBetweenDegreeModules(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {
        final RestrictionBetweenDegreeModules restrictionBetweenDegreeModules = (RestrictionBetweenDegreeModules) curricularRule;

        final DegreeModule precedenceDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        restrictionBetweenDegreeModules.edit(precedenceDegreeModule, parametersDTO.getMinimumCredits(),
                contextCourseGroup);
    }

    private static void editEnrolmentToBeApprovedByCoordinator(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final EnrolmentToBeApprovedByCoordinator enrolmentToBeApprovedByCoordinator = (EnrolmentToBeApprovedByCoordinator) curricularRule;
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        enrolmentToBeApprovedByCoordinator.edit(contextCourseGroup);
    }

    private static void editDegreeModulesSelectionLimit(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModulesSelectionLimit degreeModulesSelectionLimit = (DegreeModulesSelectionLimit) curricularRule;
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        degreeModulesSelectionLimit.edit(contextCourseGroup, parametersDTO.getMinimumLimit(),
                parametersDTO.getMaximumLimit());
    }

    private static void editCreditsLimit(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final CreditsLimit creditsLimit = (CreditsLimit) curricularRule;
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        creditsLimit.edit(contextCourseGroup, parametersDTO.getMinimumCredits(), parametersDTO
                .getMaximumCredits());
    }

    private static void editRestrictionEnroledDegreeModule(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final RestrictionEnroledDegreeModule restrictionEnroledDegreeModule = (RestrictionEnroledDegreeModule) curricularRule;
        final DegreeModule enroledDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        restrictionEnroledDegreeModule.edit(enroledDegreeModule, contextCourseGroup, parametersDTO
                .getCurricularPeriodInfoDTO());
    }

    private static void editRestrictionDoneDegreeModule(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final RestrictionDoneDegreeModule restrictionDoneDegreeModule = (RestrictionDoneDegreeModule) curricularRule;
        final DegreeModule doneDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());
        
        restrictionDoneDegreeModule.edit(doneDegreeModule, contextCourseGroup, parametersDTO
                .getCurricularPeriodInfoDTO());
    }
}
