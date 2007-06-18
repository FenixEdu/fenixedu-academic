package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;
import net.sourceforge.fenixedu.renderers.validators.RequiredValidator;

public class GradeInputRenderer extends InputRenderer {
    
    private boolean required = false;
    
    private Integer maxLength;
    
    private String size;
    

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Grade grade = (Grade) object;
		HtmlInlineContainer container = new HtmlInlineContainer();
		
		MetaSlot slot = (MetaSlot) getInputContext().getMetaObject();
		
		final HtmlGradeTextInput value = new HtmlGradeTextInput(isRequired());
		value.bind(slot);

		value.setMaxLength(getMaxLength());
		value.setSize(getSize());
		
		HtmlMenu menu = new HtmlMenu();
		menu.setName(slot.getKey().toString() + "_scale");

		menu.createDefaultOption(RenderUtils.getResourceString("renderers.menu.default.title"));
		for (GradeScale scale : GradeScale.values()) {
		    menu.createOption(RenderUtils.getEnumString(scale)).setValue(scale.getName());
		}
		
		if (grade != null) {
		    value.setValue(grade.getValue());
		    menu.setValue(grade.getGradeScale().getName());
		}

		
		menu.setController(new HtmlController() {

		    @Override
		    public void execute(IViewState viewState) {
			HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
			value.setConverter(new GradeConverter(component.getValue()));
		    }
		    
		});
		
		container.addChild(value);
		container.addChild(menu);

		return container;
	    }
	    
	};
    }
    
    private static class GradeConverter extends Converter {
	
	private GradeScale gradeScale;
	
	public GradeConverter(String gradeScaleName) {
	    if(gradeScaleName != null && gradeScaleName.length() > 0) {
		gradeScale = GradeScale.valueOf(gradeScaleName);
	    }
	}

	@Override
	public Object convert(Class type, Object value) {
	    String gradeValue = (String) value;
	    if(gradeValue == null || gradeValue.length() == 0) {
		return Grade.createEmptyGrade();
	    } else {
		return Grade.createGrade(gradeValue, getGradeScale());
	    }
	}

	public GradeScale getGradeScale() {
	    return gradeScale;
	}

	public void setGradeScale(GradeScale gradeScale) {
	    this.gradeScale = gradeScale;
	}
	
    }
    
    private static class HtmlGradeTextInput extends HtmlTextInput {
	
	public HtmlGradeTextInput(final boolean required) {
	   super.setValidator(new HtmlGradeTextInputValidator(required, this)); 
	}
	
	@Override
	public void setValidator(HtmlValidator validator) {
	    
	}
	
	@Override
	public void setValidator(MetaSlot slot) {
	    
	}
		
	private static class HtmlGradeTextInputValidator extends RequiredValidator {
	
	    private boolean required;
	    private Object[] arguments;
	    
	    public HtmlGradeTextInputValidator(boolean required, HtmlGradeTextInput htmlGradeTextInput) {
		super(htmlGradeTextInput);
		setRequired(required);
	    }

	    public boolean isRequired() {
	        return required;
	    }

	    public void setRequired(boolean required) {
	        this.required = required;
	    }
	    
	    public Object[] getArguments() {
	        return arguments;
	    }

	    public void setArguments(Object ... arguments) {
	        this.arguments = arguments;
	    }

	    @Override
	    protected String getResourceMessage(String message) {
		return RenderUtils.getFormatedResourceString(message, getArguments());
	    }
	    
	    @Override
	    public void performValidation() {
		if(isRequired()) {
		    super.performValidation();
		}
		
		if(isValid()) {
		    HtmlGradeTextInput htmlGradeTextInput = (HtmlGradeTextInput) getComponent();
		    GradeConverter gradeConverter = (GradeConverter) htmlGradeTextInput.getConverter();
		    GradeScale gradeScale = gradeConverter.getGradeScale();
		    
		    String value = getComponent().getValue().trim();
		    if(value != null && value.length() > 0) {
			if(gradeScale == null) {
			    setValid(false);
			    setMessage("renderers.validator.grade.no.grade.scale");
			} else {
			    if(!gradeScale.belongsTo(value)) {
				setValid(false);
				setMessage("renderers.validator.grade.invalid.grade.value");
				setArguments(value, RenderUtils.getEnumString(gradeScale));
			    }
			}
		    }
		}
	    }	    
	}	
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
