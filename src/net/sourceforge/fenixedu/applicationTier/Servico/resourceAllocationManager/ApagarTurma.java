/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Serviço ApagarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

public class ApagarTurma extends Service {

    public Boolean run(InfoClass infoClass) {
	final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal());

	// TODO: ----------------------------
	for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
	    shift.checkXpto();
	}
	// TODO: ----------------------------

	schoolClass.delete();
	return Boolean.TRUE;
    }

}
