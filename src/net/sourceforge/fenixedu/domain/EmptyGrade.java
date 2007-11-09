package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;

public class EmptyGrade extends Grade {
    
    protected EmptyGrade() {
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public String getValue() {
        return null;
    }
    
    @Override
    public GradeScale getGradeScale() {
        return null;
    }
    
    @Override
    public int compareTo(Grade otherGrade) {
	return otherGrade.isEmpty() ? 0 : -1;
    }
    
    static protected boolean qualifiesAsEmpty(String value) {
	if (value != null) {
	    value = value.trim();
	}
	return StringUtils.isEmpty(value) || value.equals("null");
    }
    
    @Override
    public String exportAsString() {
        return null;
    }
    
    @Override
    public boolean isNumeric() {
        return false;
    }
    
    @Override
    public boolean isApproved() {
        return false;
    }
    
    @Override
    public boolean isNotApproved() {
        return true;
    }
    
    @Override
    public boolean isNotEvaluated() {
        return true;
    }
    
}
