package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public interface IPersistentDegreeInfo extends IPersistentObject {

    public List readDegreeInfoByDegreeAndExecutionYear(Integer degreeId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia;
}