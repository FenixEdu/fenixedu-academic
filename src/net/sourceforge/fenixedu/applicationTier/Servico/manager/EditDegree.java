package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import pt.utl.ist.fenix.tools.util.i18n.Language;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditDegree extends Service {

	public void run(final Integer idInternal, final String code, final String name,
			final String nameEn, final DegreeType degreeType, final GradeScale gradeScale, ExecutionYear executionYear)
			throws FenixServiceException, ExcepcaoPersistencia {
        if (idInternal == null || name == null || nameEn == null || code == null || degreeType == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = rootDomainObject.readDegreeByOID(idInternal);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getSigla().equalsIgnoreCase(code)
                || !degreeToEdit.getNameFor(executionYear).getContent(Language.pt).equalsIgnoreCase(name)
                || !degreeToEdit.getTipoCurso().equals(degreeType)) {
            
        	final List<Degree> degrees = Degree.readOldDegrees();
            
            // assert unique degree code and unique pair name/type
            for (Degree degree : degrees) {
                if (degree != degreeToEdit) {
                    if (degree.getSigla().equalsIgnoreCase(code)) {
                        throw new FenixServiceException("error.existing.code");
                    }
                    if ((degree.getNameFor(executionYear).getContent(Language.pt).equalsIgnoreCase(name) || degree.getNameEn()
                            .equalsIgnoreCase(nameEn))
                            && degree.getTipoCurso().equals(degreeType)) {
                        throw new FenixServiceException("error.existing.name.and.type");
                    }
                }
            }
        }
        degreeToEdit.edit(name, nameEn, code, degreeType, gradeScale, executionYear);
    }

}
