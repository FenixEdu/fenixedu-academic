package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCategories extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static List run() throws FenixServiceException {
        List<InfoCategory> result = new ArrayList<InfoCategory>();

        for (ProfessionalCategory category : rootDomainObject.getProfessionalCategories()) {
            if (category.getCategoryType().equals(CategoryType.TEACHER)) {
                result.add(InfoCategory.newInfoFromDomain(category));
            }
        }

        return result;
    }

}