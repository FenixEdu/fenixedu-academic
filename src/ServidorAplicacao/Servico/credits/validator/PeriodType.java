/*
 * Created on Dec 3, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits.validator;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author jpvl
 */
public class PeriodType extends ValuedEnum
{
    public final static int SUPPORT_LESSON_PERIOD_TYPE = 1;
    public final static int LESSON_PERIOD_TYPE = 2;
    public final static int INSTITUTION_WORKING_TIME_PERIOD_TYPE = 3;

    public final static PeriodType SUPPORT_LESSON_PERIOD = new PeriodType("support.lesson.type",
            PeriodType.SUPPORT_LESSON_PERIOD_TYPE);
    public final static PeriodType LESSON_PERIOD = new PeriodType("lesson.type",
            PeriodType.LESSON_PERIOD_TYPE);
    public final static PeriodType INSTITUTION_WORKING_TIME_PERIOD = new PeriodType(
            "institution.working.time.type", PeriodType.INSTITUTION_WORKING_TIME_PERIOD_TYPE);

    private PeriodType(String name, int value)
    {
        super(name, value);
    }

}