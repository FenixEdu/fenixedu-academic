package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Grade implements Serializable {
    
    private static Grade emptyGrade = new EmptyGrade();
    
    private static Map<String, Grade> gradeMap = new HashMap<String, Grade>();
    
    private String value;
    
    private GradeScale gradeScale;
    
    protected Grade() {
	
    }
    
    protected Grade(String value, GradeScale gradeScale) {
	if(value == null || gradeScale == null) {
	    throw new DomainException("error.grade.invalid.argument");
	}

	if(!gradeScale.belongsTo(value)) {
	    throw new DomainException("error.grade.invalid.grade");
	}
	
	setValue(value);
	setGradeScale(gradeScale);
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value.trim();
    }

    public GradeScale getGradeScale() {
        return gradeScale;
    }
    
    private void setGradeScale(GradeScale gradeScale) {
	this.gradeScale = gradeScale;
    }
    
    public static Grade createGrade(String value, GradeScale gradeScale) {
	Grade grade = gradeMap.get(exportAsString(gradeScale, value));
	if(grade == null) {
	    grade = new Grade(value, gradeScale);
	    gradeMap.put(grade.exportAsString(), grade);
	}
	return grade;
    }
    
    public static Grade createEmptyGrade() {
	return emptyGrade;
    }
    
    public static Grade importFromString(String string) {
	if(string == null || string.equals("")) {
	    return emptyGrade;
	}
	
	String[] tokens = string.split(":");
	return createGrade(tokens[1], GradeScale.valueOf(tokens[0]));
    }

    public String exportAsString() {
	return exportAsString(getGradeScale(), getValue());
    }
    
    private static String exportAsString(GradeScale gradeScale, String value) {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(gradeScale);
	stringBuilder.append(":");
	stringBuilder.append(value.trim());
	
	return stringBuilder.toString();	
    }
    
    public boolean isEmpty() {
	return false;
    }
    
    public boolean isNumeric() {
	return StringUtils.isNumeric(getValue()); 
    }
    
}
