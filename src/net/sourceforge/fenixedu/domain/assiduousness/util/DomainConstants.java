/*
 * Created on Apr 21, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.util.WeekDay;

/**
 * @author velouria@velouria.org
 * 
 */
public class DomainConstants {

    // Semana de trabalho
    public final static WorkWeek WORKDAYS = new WorkWeek(EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY));

    // Marcacoes
    // TODO um bocado foleiro...
    public final static EnumSet<AttributeType> WORKED_ATTRIBUTES_SET = EnumSet
	    .range(AttributeType.WORKED1, AttributeType.WORKED5);

    public final static Attributes WORKED_ATTRIBUTES = new Attributes(WORKED_ATTRIBUTES_SET);

}
