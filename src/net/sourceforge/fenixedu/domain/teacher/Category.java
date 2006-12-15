/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class Category extends Category_Base implements Comparable {

    public Category() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public int compareTo(Object o) {
	Category category = (Category) o;
	final int weightCompare = this.getWeight().compareTo(category.getWeight());
	return weightCompare == 0 ? this.getLongName().compareTo(category.getLongName()) : weightCompare;
    }

    public boolean isCareerCategory() {
	return ((getLongName().equals("ASSISTENTE") && getCode().equals("AST"))
		|| (getLongName().equals("PROFESSOR CATEDRATICO") && getCode().equals("PCA"))
		|| (getLongName().equals("PROFESSOR AUXILIAR") && getCode().equals("PAX"))
		|| (getLongName().equals("PROFESSOR ASSOCIADO") && getCode().equals("PAS")) || (getLongName()
		.equals("ASSISTENTE ESTAGIARIO") && (getCode().equals("ASE") || getCode().equals("ASG"))));
    }

    public boolean isMonitorCategory() {
	return getCode().equals("MNL") || getCode().equals("MNT");
    }

    public static Category readCategoryByCodeAndNameInPT(String code, String ptName) {
	for (Category category : (Set<Category>) RootDomainObject.readAllDomainObjects(Category.class)) {
	    if (category.getName().getContent(Language.pt).equalsIgnoreCase(ptName)
		    && category.getCode().equalsIgnoreCase(code)) {
		return category;
	    }
	}
	return null;
    }

    public boolean isMostImportantThan(Category anotherCategory) {
	return (getWeight().intValue() < anotherCategory.getWeight().intValue());
    }
}
