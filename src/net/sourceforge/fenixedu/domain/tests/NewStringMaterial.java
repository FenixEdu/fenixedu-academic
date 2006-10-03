package net.sourceforge.fenixedu.domain.tests;

public class NewStringMaterial extends NewStringMaterial_Base {

	public NewStringMaterial() {
		super();
	}

	public NewStringMaterial(NewTestElement testElement, Boolean inline, String text) {
		this();

		this.init(testElement, inline);

		this.setText(text);
	}

	public NewPresentationMaterialType getPresentationMaterialType() {
		return NewPresentationMaterialType.STRING;
	}

	@Override
	public NewPresentationMaterial copy() {
		return new NewStringMaterial(this.getTestElement(), this.getInline(), this.getText());
	}

}
