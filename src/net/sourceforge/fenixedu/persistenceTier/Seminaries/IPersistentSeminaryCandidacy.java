/*
 * Created on 29/Jul/2003, 10:27:52
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 10:27:52
 *  
 */
public interface IPersistentSeminaryCandidacy extends IPersistentObject {
    public List readByStudentIDAndSeminaryID(Integer studentID, Integer seminaryID)
            throws ExcepcaoPersistencia;

    List readByStudentID(Integer id) throws ExcepcaoPersistencia;

    public List readByUserInput(Integer modalityID, Integer seminaryID, Integer themeID,
            Integer case1Id, Integer case2Id, Integer case3Id, Integer case4Id, Integer case5Id,
            Integer curricularCourseID, Integer degreeID, Boolean approved) throws ExcepcaoPersistencia;
}