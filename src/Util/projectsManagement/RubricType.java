/*
 * Created on Jan 12, 2005
 *
 */
package Util.projectsManagement;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 * 
 */
public class RubricType extends FenixUtil {

    public static final String REVENUE_RUBRIC_TABLE_NAME = "v_rub_receita";

    public static final String EXPENSES_RUBRIC_TABLE_NAME = "v_rub_despesa";

    public static final String PROJECT_TYPE_RUBRIC_TABLE_NAME = "v_tipos_projectos";

    public static final String EXPLORATION_UNITS_RUBRIC_TABLE_NAME = "V_UNID_EXPLORACAO";

    // OVHTYPES

    public static final String REVENUE_RUBRIC_TYPE = "revenueRubric";

    public static final String EXPENSES_RUBRIC_TYPE = "expensesRubric";

    public static final String PROJECT_TYPE_RUBRIC_TYPE = "projectTypesRubric";

    public static final String EXPLORATION_UNITS_RUBRIC_TYPE = "explorationUnitsRubric";

    public static final RubricType REVENUE_RUBRIC = new RubricType(REVENUE_RUBRIC_TYPE);

    public static final RubricType EXPENSES_RUBRIC = new RubricType(EXPENSES_RUBRIC_TYPE);

    public static final RubricType PROJECT_TYPE_RUBRIC = new RubricType(PROJECT_TYPE_RUBRIC_TYPE);

    public static final RubricType EXPLORATION_UNITS_RUBRIC = new RubricType(EXPLORATION_UNITS_RUBRIC_TYPE);

    private String rubricTableName;

    public RubricType(String rubricType) {
        if (rubricType.equals(REVENUE_RUBRIC_TYPE))
            this.rubricTableName = REVENUE_RUBRIC_TABLE_NAME;
        else if (rubricType.equals(EXPENSES_RUBRIC_TYPE))
            this.rubricTableName = EXPENSES_RUBRIC_TABLE_NAME;
        else if (rubricType.equals(PROJECT_TYPE_RUBRIC_TYPE))
            this.rubricTableName = PROJECT_TYPE_RUBRIC_TABLE_NAME;
        else if (rubricType.equals(EXPLORATION_UNITS_RUBRIC_TYPE))
            this.rubricTableName = EXPLORATION_UNITS_RUBRIC_TABLE_NAME;
    }

    public static RubricType getRubricType(String rubricType) {
        if (rubricType.equals(REVENUE_RUBRIC_TYPE))
            return REVENUE_RUBRIC;
        else if (rubricType.equals(EXPENSES_RUBRIC_TYPE))
            return EXPENSES_RUBRIC;
        else if (rubricType.equals(PROJECT_TYPE_RUBRIC_TYPE))
            return PROJECT_TYPE_RUBRIC;
        else if (rubricType.equals(EXPLORATION_UNITS_RUBRIC_TYPE))
            return EXPLORATION_UNITS_RUBRIC;
        return null;
    }

    public String getRubricTableName() {
        return rubricTableName;
    }
}
