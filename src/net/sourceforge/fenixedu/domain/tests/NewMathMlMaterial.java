package net.sourceforge.fenixedu.domain.tests;

public class NewMathMlMaterial extends NewMathMlMaterial_Base {
	
	public static final String MATHML_HEADER = "<!DOCTYPE math PUBLIC \"-//W3C//DTD MathML 2.0//EN\" \"http://www.w3.org/Math/DTD/mathml2/mathml2.dtd\">";
	
    public  NewMathMlMaterial() {
        super();
    }

	public NewMathMlMaterial(NewTestElement testElement, Boolean inline, String mathMl) {
		this();

		this.init(testElement, inline);

		this.setMathMl(mathMl);
	}
	
	@Override
	public void setMathMl(String mathMl) {
		if(!mathMl.contains("<!DOCTYPE")) {
			super.setMathMl(MATHML_HEADER + mathMl);
		} else {
			super.setMathMl(mathMl);
		}
	}

	@Override
	public NewPresentationMaterialType getPresentationMaterialType() {
		return NewPresentationMaterialType.MATHML;
	}

	@Override
	public NewPresentationMaterial copy() {
		return new NewMathMlMaterial(this.getTestElement(), this.getInline(), this.getMathMl());
	}
    
}
