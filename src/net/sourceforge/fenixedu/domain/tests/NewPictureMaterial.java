package net.sourceforge.fenixedu.domain.tests;

public class NewPictureMaterial extends NewPictureMaterial_Base {

	public NewPictureMaterial() {
		super();
	}

	public NewPictureMaterial(NewTestElement testElement, Boolean inline,
			PictureMaterialFile pictureMaterialFile) {
		this();

		this.init(testElement, inline);

		this.setPictureMaterialFile(pictureMaterialFile);
	}

	@Override
	public NewPresentationMaterialType getPresentationMaterialType() {
		return NewPresentationMaterialType.PICTURE;
	}
	
	@Override
	public void delete() {
		this.getPictureMaterialFile().delete(this);
		
		super.delete();
	}

	@Override
	public NewPresentationMaterial copy() {
		return new NewPictureMaterial(this.getTestElement(), this.getInline(), this.getPictureMaterialFile());
	}

}
