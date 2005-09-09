/*
 * Created on 17/Ago/2004
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExportGrouping;

/**
 * @author joaosa & rmalo
 */
public interface IPersistentExportGrouping extends IPersistentObject{

    public IExportGrouping readBy(Integer groupingID, Integer executionCourseID)
    	throws ExcepcaoPersistencia;
   
    public List readAllByGrouping(Integer groupingID) 
    	throws ExcepcaoPersistencia;

	public List readAllByExecutionCourse(Integer executionCourseID) 
		throws ExcepcaoPersistencia;
		
}
