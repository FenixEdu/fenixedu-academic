package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurricularCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentCurricularCourse extends IPersistentObject {
    
    public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
    public ArrayList readAll() throws ExcepcaoPersistencia;
    public void lockWrite(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}