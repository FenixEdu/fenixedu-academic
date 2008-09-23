package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCategories extends FenixService {

    public List run() throws FenixServiceException {
	List<InfoCategory> result = new ArrayList<InfoCategory>();

	for (Category category : Category.readTeacherCategories()) {
	    result.add(InfoCategory.newInfoFromDomain(category));
	}

	return result;
    }

}
