/*
 * Created on July 16, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;

/**
 * @author Luis Cruz e Sara Ribeiro
 *  
 */
public class ExecutionPeriodComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoExecutionPeriod infoExecutionPeriod1 = (InfoExecutionPeriod) obj1;
        InfoExecutionPeriod infoExecutionPeriod2 = (InfoExecutionPeriod) obj2;

        return infoExecutionPeriod1.compareTo(infoExecutionPeriod2);
    }

}