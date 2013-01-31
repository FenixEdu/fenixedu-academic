package net.sourceforge.fenixedu.domain;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class CurricularYearList {

	private final List<Integer> curricularYears;

	public CurricularYearList(Iterable<Integer> curricularYears) {
		super();
		if (curricularYears == null) {
			throw new IllegalArgumentException("exception.null.values");
		}
		this.curricularYears = Lists.newArrayList(curricularYears);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		for (Integer year : curricularYears) {
			if (buffer.length() > 0) {
				buffer.append(',');
			}
			buffer.append(year);
		}

		return buffer.toString();
	}

	private static Splitter SPLITTER = Splitter.on(',');

	public static CurricularYearList internalize(String data) {

		Iterable<Integer> years = Iterables.transform(SPLITTER.split(data), new Function<String, Integer>() {

			@Override
			public Integer apply(String str) {
				return Integer.parseInt(str);
			}

		});

		return new CurricularYearList(years);

	}

	public List<Integer> getYears() {
		return curricularYears;
	}

	/*
	 * The value -1 in the year list represents all the years of the selected
	 * degree
	 */
	public boolean hasAll() {
		return curricularYears.contains(-1);
	}

}
