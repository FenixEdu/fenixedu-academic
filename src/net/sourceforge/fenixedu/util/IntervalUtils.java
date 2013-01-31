package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class IntervalUtils {
	/**
	 * Merges a set of interval lists into a single list of non-intersecting
	 * intervals by merging all overlaping intervals into one.
	 * 
	 * @param a
	 *            set of lists of {@link Interval}s
	 * @return a list of {@link Interval}s
	 */
	public static List<Interval> mergeIntervalLists(List<Interval>... lists) {
		SortedSet<Interval> totalSpace = new TreeSet<Interval>(START_TIME_INTERVAL_COMPARATOR);
		for (List<Interval> list : lists) {
			totalSpace.addAll(list);
		}
		return mergeIntervalSortedSet(totalSpace);
	}

	public static List<Interval> mergeIntervalLists(Interval... intervals) {
		SortedSet<Interval> totalSpace = new TreeSet<Interval>(START_TIME_INTERVAL_COMPARATOR);
		for (Interval interval : intervals) {
			totalSpace.add(interval);
		}
		return mergeIntervalSortedSet(totalSpace);
	}

	private static List<Interval> mergeIntervalSortedSet(SortedSet<Interval> totalSpace) {
		List<Interval> result = new ArrayList<Interval>();
		Interval current = null;
		for (Interval interval : totalSpace) {
			if (current == null) {
				current = interval;
			} else if (!current.overlaps(interval)) {
				result.add(current);
				current = interval;
			} else {
				current = mergeIntervals(current, interval);
			}
		}
		if (current != null) {
			result.add(current);
		}
		return result;
	}

	/*
	 * Returns the smallest interval that can contain both interval arguments.
	 * 
	 * @return An {@link Interval}
	 */
	public static Interval mergeIntervals(Interval i1, Interval i2) {
		DateTime start = i1.getStart().isBefore(i2.getStart()) ? i1.getStart() : i2.getStart();
		DateTime end = i1.getEnd().isAfter(i2.getEnd()) ? i1.getEnd() : i2.getEnd();
		return new Interval(start, end);
	}

	private static final Comparator<Interval> START_TIME_INTERVAL_COMPARATOR = new Comparator<Interval>() {
		@Override
		public int compare(Interval i1, Interval i2) {
			return i1.getStart().equals(i2.getStart()) ? i1.getEnd().compareTo(i2.getEnd()) : i1.getStart().compareTo(
					i2.getStart());
		}
	};
}
