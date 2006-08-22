/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadShiftsByClass extends Service {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {
        SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(
                infoClass.getIdInternal());

        List<Shift> shifts = schoolClass.getAssociatedShifts();

        return CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                return infoShift;
            }
        });
    }

}