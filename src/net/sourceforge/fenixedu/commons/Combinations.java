package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.List;

public class Combinations implements java.util.Enumeration {
	protected List inArray;

	protected int n, m;

	protected int[] index;

	protected boolean hasMore = true;

	public Combinations(List inArray, int m) {

		this.inArray = inArray;
		this.n = inArray.size();
		this.m = m;

		check(n, m);

		index = new int[m];
		for (int i = 0; i < m; i++) {
			index[i] = i;
		}
	}

	public void check(int n, int m) {
		if (n < 0) {
			throw new RuntimeException("n, the number of items, must be " + "greater than 0");
		}
		if (n < m) {
			throw new RuntimeException("n, the number of items, must be >= m, "
					+ "the number selected");
		}
		if (m < 0) {
			throw new RuntimeException("m, the number of selected items, must be >= 0");
		}
	}

	public boolean hasMoreElements() {
		return hasMore;
	}

	protected void moveIndex() {
		int i = rightmostIndexBelowMax();
		if (i >= 0) {
			index[i] = index[i] + 1;
			for (int j = i + 1; j < m; j++) {
				index[j] = index[j - 1] + 1;
			}
		} else {
			hasMore = false;
		}
	}

	public List nextElement() {
		if (!hasMore) {
			return null;
		}

		List out = new ArrayList(m);
		for (int i = 0; i < m; i++) {
			out.add(i, inArray.get(index[i]));
		}

		moveIndex();
		return out;
	}

	protected int rightmostIndexBelowMax() {

		for (int i = m - 1; i >= 0; i--) {
			if (index[i] < n - m + i) {
				return i;
			}
		}
		return -1;
	}
}