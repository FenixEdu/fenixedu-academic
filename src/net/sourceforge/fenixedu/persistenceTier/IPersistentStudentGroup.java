/*
 * Created on 12/Mai/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudentGroup;

/**
 * @author asnr and scpo
 * 
 */
public interface IPersistentStudentGroup extends IPersistentObject {

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber(Integer attendsSetID,
            Integer studentGroupNumber) throws ExcepcaoPersistencia;

    public List<IStudentGroup> readAllStudentGroupByAttendsSetAndShift(Integer attendsSetID,
            Integer shiftID) throws ExcepcaoPersistencia;
}
