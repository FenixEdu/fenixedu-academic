/*
 * Created on 1/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 1/Jul/2003 fenix-branch ServidorAplicacao.Servico.sop
 *  
 */
public class AddShiftToClasses implements IServico {

    /**
     *  
     */
    public AddShiftToClasses() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "AddShiftToClasses";
    }

    private static AddShiftToClasses service = new AddShiftToClasses();

    public static AddShiftToClasses getService() {
        return service;
    }

    public void run(Integer keyShift, String[] classesList) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurmaPersistente persistentClass = sp.getITurmaPersistente();

            IShift shift = new Shift(keyShift);
            shift = (IShift) persistentShift.readByOID(Shift.class, keyShift);
            if (shift == null || classesList == null) {
                throw new InvalidArgumentsServiceException();
            }
            persistentShift.simpleLockWrite(shift);
            int iter = 0;
            int length = classesList.length;
            while (iter < length) {
                Integer keyClass = new Integer(classesList[iter]);
                ISchoolClass dClass = (ISchoolClass) persistentClass.readByOID(SchoolClass.class, keyClass);
                if (dClass == null) {
                    throw new InvalidArgumentsServiceException();
                }
                shift.getAssociatedClasses().add(dClass);
                persistentClass.simpleLockWrite(dClass);
                dClass.getAssociatedShifts().add(shift);

                iter++;
            }

        } catch (ExcepcaoPersistencia e) {
        }

    }

}