/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public class CurricularRuleManager {

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

        default:
            break;
        }
        return null;
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

    public static void editCurricularRule(CurricularRule curricularRule,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {
        final CurricularRuleType curricularRuleTypeToEdit = curricularRule.getCurricularRuleType();

        switch (curricularRuleTypeToEdit) {
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

        default:
            break;
        }
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
