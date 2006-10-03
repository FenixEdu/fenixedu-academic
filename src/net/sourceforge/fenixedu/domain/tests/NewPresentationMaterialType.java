package net.sourceforge.fenixedu.domain.tests;

public enum NewPresentationMaterialType {
    STRING {
        @Override
        public Class getImplementingClass() {
            return NewStringMaterial.class;
        }
    },

    PICTURE {
        @Override
        public Class getImplementingClass() {
            return NewPictureMaterial.class;
        }
    },

    MATHML {
        @Override
        public Class getImplementingClass() {
            return NewMathMlMaterial.class;
        }
    };

    public abstract Class getImplementingClass();
    
    public String getName() {
    	return super.name();
    }
}
