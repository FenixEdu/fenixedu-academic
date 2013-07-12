package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class NewPresentationMaterial extends NewPresentationMaterial_Base implements Positionable {

    public NewPresentationMaterial() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public NewPresentationMaterial(NewTestElement testElement, Boolean inline) {
        this();

        this.init(testElement, inline);
    }

    protected void init(NewTestElement testElement, Boolean inline) {
        this.setTestElement(testElement);
        this.setInline(inline);
        this.setPosition(this.getNewPosition());
    }

    private Integer getNewPosition() {
        return this.getTestElement().getPresentationMaterialsSet().size();
    }

    public void delete() {
        NewTestElement testElement = this.getTestElement();
        this.setTestElement(null);

        testElement.resortPresentationMaterials();

        this.setRootDomainObject(null);

        this.deleteDomainObject();
    }

    @Override
    public boolean isFirst() {
        return this.getPosition() == 1;
    }

    @Override
    public boolean isLast() {
        return this.getPosition() == this.getTestElement().getPresentationMaterialsSet().size();
    }

    @Override
    public void switchPosition(Integer relativePosition) {
        int currentPosition = this.getPosition();
        int newPosition = currentPosition + relativePosition;
        NewTestElement testElement = this.getTestElement();

        if (relativePosition < 0 && this.isFirst()) {
            throw new DomainException("could.not.sort.up");
        }

        if (relativePosition > 0 && this.isLast()) {
            throw new DomainException("could.not.sort.down");
        }

        for (NewPresentationMaterial presentationMaterial : testElement.getPresentationMaterials()) {
            if (presentationMaterial.getPosition() == newPosition) {
                presentationMaterial.setPosition(currentPosition);
                break;
            }
        }

        this.setPosition(newPosition);
    }

    public abstract NewPresentationMaterialType getPresentationMaterialType();

    public abstract NewPresentationMaterial copy();

    @Deprecated
    public boolean hasInline() {
        return getInline() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTestElement() {
        return getTestElement() != null;
    }

    @Deprecated
    public boolean hasPosition() {
        return getPosition() != null;
    }

}
