/*
 * Created on 12/Mai/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentStudentGroup extends IPersistentObject{
    public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber (IAttendsSet attendsSet,Integer studentGroupNumber) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByAttendsSet (IAttendsSet attendsSet) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByAttendsSetAndShift (IAttendsSet attendsSet, IShift shift) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByShift (IShift shift) throws ExcepcaoPersistencia;	
}
