package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalCategory extends ProfessionalCategory_Base implements Comparable<ProfessionalCategory> {

    public ProfessionalCategory(final String giafId, final MultiLanguageString name, final CategoryType categoryType) {
	super();
	check(giafId, "");
	check(name, "");
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafId(giafId);
	setName(name);
	setCategoryType(categoryType);
    }

    @Service
    public void edit(final MultiLanguageString name, final CategoryType categoryType) {
	check(name, "");
	setName(name);
	setCategoryType(categoryType);
    }

    @Override
    public int compareTo(ProfessionalCategory otherProfessionalCategory) {
	int categoryTypeCompare = getCategoryType().compareTo(otherProfessionalCategory.getCategoryType());
	if (categoryTypeCompare == 0) {
	    if (getWeight() == null) {
		return otherProfessionalCategory.getWeight() == null ? getName().compareTo(otherProfessionalCategory.getName())
			: -1;
	    } else {
		if (otherProfessionalCategory.getWeight() == null) {
		    return 1;
		} else {
		    final int weightCompare = getWeight().compareTo(otherProfessionalCategory.getWeight());
		    return weightCompare == 0 ? getName().compareTo(otherProfessionalCategory.getName()) : weightCompare;
		}
	    }

	}
	return categoryTypeCompare;
    }

    protected boolean isTeacherCategoryType() {
	return getCategoryType().equals(CategoryType.TEACHER);
    }

    public boolean isTeacherCareerCategory() {
	return isTeacherCategoryType() && !isTeacherMonitorCategory();
    }

    public boolean isTeacherMonitorCategory() {
	return isTeacherCategoryType() && getName().getContent(Language.pt).matches("(?i).*Monitor.*");
    }

    public boolean isTeacherProfessorCategory() {
	return isTeacherCategoryType() && !isTeacherMonitorCategory()
		&& !getName().getContent(Language.pt).matches("(?i).*Convidado.*")
		&& !getName().getContent(Language.pt).matches("(?i).*Equip.*");
    }

    public boolean isTeacherProfessorCategoryAboveAssistant() {
	return isTeacherCategoryType() && !isTeacherMonitorCategory() && !isTeacherAssistantCategory()
		&& isTeacherProfessorCategory();
    }

    public boolean isTeacherAssistantCategory() {
	return isTeacherCategoryType() && !isTeacherMonitorCategory()
		&& getName().getContent(Language.pt).matches("(?i).*Assistente.*");
    }
}
