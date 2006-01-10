/*
 * Created on 17/Ago/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author joaosa & rmalo
 */

public class ExportGroupingOJB extends ObjectFenixOJB implements
        IPersistentExportGrouping {

    public ExportGrouping readBy(Integer groupingID, Integer executionCourseID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyGrouping", groupingID);
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);

        return (ExportGrouping) queryObject(ExportGrouping.class, criteria);
    }

    public List readAllByGrouping(Integer groupingID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyGrouping", groupingID);

        return queryList(ExportGrouping.class, criteria);
    }

    public List readAllByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);

        return queryList(ExportGrouping.class, criteria);
    }
}
