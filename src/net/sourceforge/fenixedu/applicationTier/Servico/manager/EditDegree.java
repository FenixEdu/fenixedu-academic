package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditDegree extends Service {

    public void run(InfoDegree infoDegree) throws FenixServiceException, ExcepcaoPersistencia {
        if (infoDegree.getIdInternal() == null || infoDegree.getNome() == null || infoDegree.getNameEn() == null
                || infoDegree.getSigla() == null || infoDegree.getTipoCurso() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = (Degree) persistentSupport.getIPersistentObject().readByOID(Degree.class,
                infoDegree.getIdInternal());

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getSigla().equalsIgnoreCase(infoDegree.getSigla())
                || !degreeToEdit.getNome().equalsIgnoreCase(infoDegree.getNome())
                || !degreeToEdit.getTipoCurso().equals(infoDegree.getTipoCurso())) {
            
            final List<Degree> degrees = (List<Degree>) persistentSupport.getICursoPersistente()
                    .readAllFromOldDegreeStructure();

            // assert unique degree code and unique pair name/type
            for (Degree degree : degrees) {
                if (degree != degreeToEdit) {
                    if (degree.getSigla().equalsIgnoreCase(infoDegree.getSigla())) {
                        throw new FenixServiceException("error.existing.code");
                    }
                    if ((degree.getNome().equalsIgnoreCase(infoDegree.getNome()) || degree.getNameEn()
                            .equalsIgnoreCase(infoDegree.getNameEn()))
                            && degree.getTipoCurso().equals(infoDegree.getTipoCurso())) {
                        throw new FenixServiceException("error.existing.name.and.type");
                    }
                }
            }
        }
        degreeToEdit.edit(infoDegree.getNome(), infoDegree.getNameEn(), infoDegree.getSigla(), infoDegree
                .getTipoCurso(), infoDegree.getGradeScale());
    }

}
