package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class NewTestElement extends NewTestElement_Base {

    public NewTestElement() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public List<NewPresentationMaterial> getOrderedPresentationMaterials() {
        List<NewPresentationMaterial> presentationMaterials = new ArrayList<NewPresentationMaterial>(getPresentationMaterials());

        Collections.sort(presentationMaterials, Positionable.POSITION_COMPARATOR);

        return presentationMaterials;
    }

    /**
     * Cleans sorting of presentation materials. Should be called after deleting
     * a presentation material.
     */
    public void resortPresentationMaterials() {
        int i = 1;
        for (NewPresentationMaterial presentationMaterial : this.getOrderedPresentationMaterials()) {
            presentationMaterial.setPosition(i++);
        }
    }

    public void delete() {
        if (this.getSection() != null) {
            this.setSection(null);
        }

        for (NewPresentationMaterial presentationMaterial : getPresentationMaterials()) {
            presentationMaterial.delete();
        }

        this.setRootDomainObject(null);

        this.deleteDomainObject();
    }

    protected void initCopy(NewTestElement testElement, HashMap<Object, Object> transformationMap) {
        for (NewPresentationMaterial material : this.getPresentationMaterials()) {
            NewPresentationMaterial materialCopy = material.copy();

            transformationMap.put(material, materialCopy);

            materialCopy.setPosition(material.getPosition());
            materialCopy.setTestElement(testElement);
        }
    }

    public abstract NewTestElement copy(HashMap<Object, Object> trasformationMap);

    public abstract void cleanTransformation(HashMap<Object, Object> transformationMap);

    public List<Integer> getPath() {
        List<Integer> path = new ArrayList<Integer>();

        NewSection section = this.getSection();
        NewTest test = section.getTest();

        path.add(this.getSectionPosition());

        while (!section.equals(test)) {
            path.add(0, section.getSectionPosition());
            section = section.getSection();
        }

        return path;
    }

    public NewTest getTest() {
        return this.getSection().getTest();
    }

    public boolean isVisible(Person person) {
        return false;
    }

    public boolean isVisible() {
        return this.isVisible(AccessControl.getPerson());
    }

    public boolean isCorrectable(Person person) {
        return true;
    }

    public boolean isAnswered(Person person) {
        return true;
    }

    public boolean isAnswered() {
        return true;
    }

    public int getAllUncorrectedQuestionsCount(Person person) {
        return 0;
    }

    public void publishGrades() {
    }

    public TestsGrade getFinalGrade() {
        return null;
    }

    public TestsGrade getFinalGrade(Person person) {
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial> getPresentationMaterials() {
        return getPresentationMaterialsSet();
    }

    @Deprecated
    public boolean hasAnyPresentationMaterials() {
        return !getPresentationMaterialsSet().isEmpty();
    }

    @Deprecated
    public boolean hasSection() {
        return getSection() != null;
    }

    @Deprecated
    public boolean hasSectionPosition() {
        return getSectionPosition() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
