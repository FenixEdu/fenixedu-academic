package net.sourceforge.fenixedu.domain.tests;

import java.util.Comparator;

public interface Positionable {
	public Integer getPosition();
	public void setPosition(Integer position);
	public void switchPosition(Integer relativePosition);
	public boolean isFirst();
	public boolean isLast();
	
	public static class PositionComparator implements Comparator<Positionable> {
		public int compare(Positionable o1, Positionable o2) {
			return o1.getPosition() - o2.getPosition();
		}
	}
	
	public static final PositionComparator POSITION_COMPARATOR = new PositionComparator();
}
