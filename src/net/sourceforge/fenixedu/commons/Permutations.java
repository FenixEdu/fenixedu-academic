package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.List;

public class Permutations implements java.util.Enumeration {
	protected List inArray;

	protected int n, m;

	protected int[] index;

	protected boolean hasMore = true;

	public Permutations(List inArray) {

		this(inArray, inArray.size());
	}

	public Permutations(List inArray, int m) {

		this.inArray = inArray;
		this.n = inArray.size();
		this.m = m;

		check(n, m);

		index = new int[n];
		for (int i = 0; i < n; i++) {
			index[i] = i;
		}

		reverseAfter(m - 1);
	}

	public void check(int n, int m) {
		if (n < 0) {
			throw new RuntimeException("n, the number of items, must be " + "greater than 0");
		}
		if (n < m) {
			throw new RuntimeException("n, the number of items, must be >= m, " + "the number selected");
		}
		if (m < 0) {
			throw new RuntimeException("m, the number of selected items, must be >= 0");
		}
	}

	public boolean hasMoreElements() {
		return hasMore;
	}

	protected void moveIndex() {

		int i = rightmostDip();
		if (i < 0) {
			hasMore = false;
			return;
		}

		int leastToRightIndex = i + 1;
		for (int j = i + 2; j < n; j++) {
			if (index[j] < index[leastToRightIndex] && index[j] > index[i]) {
				leastToRightIndex = j;
			}
		}

		int t = index[i];
		index[i] = index[leastToRightIndex];
		index[leastToRightIndex] = t;

		if (m - 1 > i) {
			reverseAfter(i);
			reverseAfter(m - 1);
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

	protected void reverseAfter(int i) {

		int start = i + 1;
		int end = n - 1;
		while (start < end) {
			int t = index[start];
			index[start] = index[end];
			index[end] = t;
			start++;
			end--;
		}

	}

	protected int rightmostDip() {
		for (int i = n - 2; i >= 0; i--) {
			if (index[i] < index[i + 1]) {
				return i;
			}
		}
		return -1;

	}
}