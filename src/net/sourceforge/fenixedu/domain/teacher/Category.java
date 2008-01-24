/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class Category extends Category_Base implements Comparable<Category> {

    public Category(CategoryType categoryType, String code, String shortName, String longName,
	    Boolean canBeExecutionCourseResponsible, Integer weight) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setShortName(shortName);
	setLongName(longName);
	setCanBeExecutionCourseResponsible(canBeExecutionCourseResponsible);
	setWeight(weight);
	setCategoryType(categoryType);
    }

    @Override
    public void setCategoryType(CategoryType categoryType) {
	if (categoryType == null) {
	    throw new DomainException("error.Category.empty.categoryType");
	}
	super.setCategoryType(categoryType);
    }

    @Override
    public void setShortName(String shortName) {
	if (shortName == null) {
	    throw new DomainException("error.Category.empty.shortName");
	}
	super.setShortName(shortName);
    }

    @Override
    public void setLongName(String longName) {
	if (longName == null) {
	    throw new DomainException("error.Category.empty.longName");
	}
	super.setLongName(longName);
    }

    @Override
    public void setCanBeExecutionCourseResponsible(Boolean canBeExecutionCourseResponsible) {
	if (canBeExecutionCourseResponsible == null) {
	    throw new DomainException("error.Category.empty.canBeExecutionCourseResponsible");
	}
	super.setCanBeExecutionCourseResponsible(canBeExecutionCourseResponsible);
    }

    public int compareTo(Category category) {
	int categoryTypeCompare = getCategoryType().compareTo(category.getCategoryType());
	if (categoryTypeCompare == 0) {
	    final int weightCompare = this.getWeight().compareTo(category.getWeight());
	    return weightCompare == 0 ? this.getLongName().compareTo(category.getLongName()) : weightCompare;
	}
	return categoryTypeCompare;
    }

    public boolean isTeacherCareerCategory() {
	return isTeacherCategoryType()
		&& (((getLongName().equals("ASSISTENTE") && getCode().equals("AST"))
			|| (getLongName().equals("PROFESSOR CATEDRATICO") && getCode().equals("PCA"))
			|| (getLongName().equals("PROFESSOR AUXILIAR") && getCode().equals("PAX"))
			|| (getLongName().equals("PROFESSOR ASSOCIADO") && getCode().equals("PAS")) || (getLongName().equals(
			"ASSISTENTE ESTAGIARIO") && (getCode().equals("ASE") || getCode().equals("ASG")))));
    }

    public boolean isTeacherMonitorCategory() {
	return isTeacherCategoryType() && (getCode().equals("MNL") || getCode().equals("MNT"));
    }

    public boolean isTeacherCategoryMostImportantThan(Category anotherCategory) {
	if (!anotherCategory.isTeacherCategoryType() || !isTeacherCategoryType()) {
	    throw new DomainException("error.Category.invalid.categories");
	}
	return (getWeight().intValue() < anotherCategory.getWeight().intValue());
    }

    public boolean isTeacherCategoryType() {
	return getCategoryType() != null && getCategoryType().equals(CategoryType.TEACHER);
    }

    public static Category readCategoryByCodeAndNameInPT(String code, String ptName) {
	for (Category category : (Set<Category>) RootDomainObject.readAllDomainObjects(Category.class)) {
	    if (category.getName().getContent(Language.pt).equalsIgnoreCase(ptName) && category.getCode().equalsIgnoreCase(code)) {
		return category;
	    }
	}
	return null;
    }

    public static Set<Category> readTeacherCategories() {
	Set<Category> result = new HashSet<Category>();
	Set<Category> categorysSet = RootDomainObject.getInstance().getCategorysSet();
	for (Category category : categorysSet) {
	    if(category.isTeacherCategoryType()) {
		result.add(category);
	    }
	}	
	return result;
    }
    
    public static Set<Category> readEmployeeCategories() {
	Set<Category> result = new HashSet<Category>();
	Set<Category> categorysSet = RootDomainObject.getInstance().getCategorysSet();
	for (Category category : categorysSet) {
	    if(!category.isTeacherCategoryType()) {
		result.add(category);
	    }
	}	
	return result;
    }
}
