/*
 * Created on May 3, 2006
 */
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public class GradeValidator extends HtmlValidator {
	
	private boolean required = false; 

    public GradeValidator(Validatable component) {
        super(component);
        setMessage("renderers.validator.grade");
        setKey(true);
    }

    @Override
    public void performValidation() {
        String grade = getComponent().getValue();
        
        if(isRequired() && (grade == null || grade.length() == 0)) {
        	setValid(false);
        	setKey(true);
            setMessage("renderers.validator.required");
        } else {
	        if (grade == null || grade.length() == 0 || grade.equalsIgnoreCase(GradeScale.NA) || grade.equalsIgnoreCase(GradeScale.RE) || grade.equalsIgnoreCase(GradeScale.AP)) {
	            setValid(true);
	        } else {
	            try {
	                Integer integer = Integer.valueOf(grade);
	                
	                if (! (integer >= 10 && integer <= 20)) {
	                    setValid(false);
	                } else {
	                    setValid(true);
	                }
	
	            } catch (NumberFormatException e) {
	                setValid(false);
	            }
	        }
        }
    }
    
    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getFormatedResourceString(message, getComponent().getValue());
    }

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}


}
