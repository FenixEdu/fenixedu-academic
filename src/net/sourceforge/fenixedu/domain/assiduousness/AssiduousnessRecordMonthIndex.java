package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class AssiduousnessRecordMonthIndex extends AssiduousnessRecordMonthIndex_Base {
    
    public AssiduousnessRecordMonthIndex(final AssiduousnessRecord assiduousnessRecord) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousnessRecord.getAssiduousness());
	addAssiduousnessRecords(assiduousnessRecord);
	setYearMonth(new Partial(
		new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
		new int[] { assiduousnessRecord.getDate().getYear(), assiduousnessRecord.getDate().getMonthOfYear() }));
    }

    public boolean intersects(DateTime beginDate, DateTime endDate) {
	final Partial partial = getYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return beginDate.getYear() <= year && beginDate.getMonthOfYear() <= month
		&& endDate.getYear() >= year && endDate.getMonthOfYear() >= month;
    }

    public boolean contains(LocalDate localDate) {
	final Partial partial = getYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return localDate.getYear() == year && localDate.getMonthOfYear() == month;
    }

    public boolean contains(final Partial partialToMatch) {
	final Partial partial = getYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return partialToMatch.get(DateTimeFieldType.year()) == year && partialToMatch.get(DateTimeFieldType.monthOfYear()) == month;
    }

}
