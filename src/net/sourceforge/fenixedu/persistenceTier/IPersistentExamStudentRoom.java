/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExamStudentRoom;

/**
 * @author João Mota
 *  
 */
public interface IPersistentExamStudentRoom extends IPersistentObject {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia;

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia;

    public IExamStudentRoom readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia;

}