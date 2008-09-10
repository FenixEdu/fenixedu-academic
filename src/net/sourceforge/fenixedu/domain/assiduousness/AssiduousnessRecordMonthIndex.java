package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class AssiduousnessRecordMonthIndex extends AssiduousnessRecordMonthIndex_Base {

    public AssiduousnessRecordMonthIndex(final Assiduousness assiduousness, final Partial partial) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setPartialYearMonth(partial);
    }

    public boolean intersects(DateTime beginDate, DateTime endDate) {
	final Partial partial = getPartialYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return beginDate.getYear() <= year && beginDate.getMonthOfYear() <= month && endDate.getYear() >= year
		&& endDate.getMonthOfYear() >= month;
    }

    public boolean contains(LocalDate localDate) {
	final Partial partial = getPartialYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return localDate.getYear() == year && localDate.getMonthOfYear() == month;
    }

    public boolean contains(final Partial partialToMatch) {
	final Partial partial = getPartialYearMonth();
	final int year = partial.get(DateTimeFieldType.year());
	final int month = partial.get(DateTimeFieldType.monthOfYear());
	return partialToMatch.get(DateTimeFieldType.year()) == year
		&& partialToMatch.get(DateTimeFieldType.monthOfYear()) == month;
    }

    public static List<AssiduousnessRecord> getAssiduousnessRecordBetweenDates(DateTime beginDate, DateTime endDate) {
	final List<AssiduousnessRecord> assiduousnessRecords = new ArrayList<AssiduousnessRecord>();
	for (final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : getAssiduousnessRecordMonthIndexsSet(beginDate
		.toLocalDate())) {
	    if (assiduousnessRecordMonthIndex.intersects(beginDate, endDate)) {
		assiduousnessRecords.addAll(assiduousnessRecordMonthIndex.getAssiduousnessRecordsSet());
	    }
	}
	return assiduousnessRecords;
    }

    private static List<AssiduousnessRecordMonthIndex> getAssiduousnessRecordMonthIndexsSet(LocalDate date) {
	List<AssiduousnessRecordMonthIndex> result = new ArrayList<AssiduousnessRecordMonthIndex>();
	for (AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : RootDomainObject.getInstance()
		.getAssiduousnessRecordMonthIndexs()) {
	    if (assiduousnessRecordMonthIndex.contains(date)) {
		result.add(assiduousnessRecordMonthIndex);
	    }
	}
	return result;
    }

}
