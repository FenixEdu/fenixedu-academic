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
		setOjbConcreteClass(getClass().getName());
	}

	public List<NewPresentationMaterial> getOrderedPresentationMaterials() {
		List<NewPresentationMaterial> presentationMaterials = new ArrayList<NewPresentationMaterial>(
				getPresentationMaterials());

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
			this.removeSection();
		}

		for (NewPresentationMaterial presentationMaterial : getPresentationMaterials()) {
			presentationMaterial.delete();
		}

		this.removeRootDomainObject();

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
		return this.isVisible(AccessControl.getUserView().getPerson());
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
	
	public Grade getFinalGrade() {
		return null;
	}
	
	public Grade getFinalGrade(Person person) {
		return null;
	}

}
