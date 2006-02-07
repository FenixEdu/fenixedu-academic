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

public class CurricularRuleFactory {

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

        default:
            break;
        }
        return null;
    }

    private static CurricularRule createCreditsLimit(DegreeModule degreeModuleToApplyRule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO,
            IPersistentObject persistentObject) throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new CreditsLimit((CourseGroup) degreeModuleToApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumCredits(), parametersDTO.getMaximumCredits());
    }

    private static CurricularRule createDegreeModulesSelectionLimit(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new DegreeModulesSelectionLimit((CourseGroup) degreeModuleToApplyRule,
                contextCourseGroup, begin, end, parametersDTO.getMinimumLimit(), parametersDTO
                        .getMaximumLimit());
    }

    private static CurricularRule createRestrictionEnroledDegreeModule(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModule enroledDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getPrecedenceDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new RestrictionEnroledDegreeModule((CurricularCourse) degreeModuleToApplyRule, enroledDegreeModule,
                contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }

    private static CurricularRule createRestrictionDoneDegreeModule(
            DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleParametersDTO parametersDTO, IPersistentObject persistentObject)
            throws ExcepcaoPersistencia {

        final DegreeModule doneDegreeModule = (DegreeModule) persistentObject.readByOID(
                DegreeModule.class, parametersDTO.getPrecedenceDegreeModuleID());
        final CourseGroup contextCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parametersDTO.getContextCourseGroupID());

        return new RestrictionDoneDegreeModule((CurricularCourse) degreeModuleToApplyRule, doneDegreeModule,
                contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }
}

/*
 * public static CurricularRule createCompositeRuleAND(CurricularRule
 * firstCurricularRule, CurricularRule secondCurricularRule) { return new
 * CompositeRule(LogicOperators.AND, firstCurricularRule, secondCurricularRule); }
 * 
 * public static CurricularRule createCompositeRuleAND(CurricularRule ...
 * curricularRules) { return
 * createCompositeRuleAND(Arrays.asList(curricularRules)); }
 * 
 * public static CurricularRule createCompositeRuleOR(CurricularRule
 * firstCurricularRule, CurricularRule secondCurricularRule) { return new
 * CompositeRule(LogicOperators.OR, firstCurricularRule, secondCurricularRule); }
 * 
 * public static CurricularRule createCompositeRuleOR(CurricularRule ...
 * curricularRules) { return
 * createCompositeRuleOR(Arrays.asList(curricularRules)); }
 * 
 * public static CurricularRule createCompositeRuleNOT(CurricularRule
 * curricularRule) { return new CompositeRule(LogicOperators.NOT,
 * curricularRule, null); }
 * 
 * private static CurricularRule createCompositeRuleAND(final List<CurricularRule>
 * curricularRules) { if (curricularRules.size() == 2) { return
 * createCompositeRuleAND(curricularRules.get(0), curricularRules.get(1)); }
 * else if (curricularRules.size() > 2) { return
 * createCompositeRuleAND(curricularRules.get(0),
 * createCompositeRuleAND(curricularRules.subList(1, curricularRules.size()))); }
 * throw new DomainException("invalid.curricular.rules.number"); }
 * 
 * private static CurricularRule createCompositeRuleOR(final List<CurricularRule>
 * curricularRules) { if (curricularRules.size() == 2) { return
 * createCompositeRuleOR(curricularRules.get(0), curricularRules.get(1)); } else
 * if (curricularRules.size() > 2) { return
 * createCompositeRuleOR(curricularRules.get(0),
 * createCompositeRuleOR(curricularRules.subList(1, curricularRules.size()))); }
 * throw new DomainException("invalid.curricular.rules.number"); }
 */
