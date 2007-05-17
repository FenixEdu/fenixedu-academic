package net.sourceforge.fenixedu.domain;

public enum UnitSiteBannerRepeatType {
	HORIZONTAL("repeat-x"),
	VERTICAL("repeat-y"),
	BOTH("repeat");
	
	private String representation;
	
	UnitSiteBannerRepeatType(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return this.representation;
	}
	
}
