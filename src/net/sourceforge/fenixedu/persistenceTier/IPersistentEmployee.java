/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author jpvl
 */
public interface IPersistentEmployee extends IPersistentObject {
    public IEmployee readByNumber(Integer number) throws ExcepcaoPersistencia;

    public IEmployee readByIdInternal(int idInternal) throws ExcepcaoPersistencia;

    public IEmployee readByPerson(int keyPerson) throws ExcepcaoPersistencia;

    public IEmployee readByPerson(IPerson person) throws ExcepcaoPersistencia;

    public List readHistoricByKeyEmployee(int keyEmployee) throws ExcepcaoPersistencia;
}