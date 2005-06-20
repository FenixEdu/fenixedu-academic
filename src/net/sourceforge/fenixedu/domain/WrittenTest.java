/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ana e Ricardo
 * 
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest() {
        this.setOjbConcreteClass(WrittenTest.class.getName());
    }

    public String toString() {
        return "[WRITTEN_TEST:" + " id= '" + this.getIdInternal() + "'\n" + " day= '" + this.getDay()
                + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '" + this.getEnd()
                + "'\n" + "";
    }

    public boolean equals(Object obj) {
        if (obj instanceof IWrittenTest) {
            IWrittenTest writtenTestObj = (IWrittenTest) obj;
            return this.getIdInternal().equals(writtenTestObj.getIdInternal());
        }

        return false;
    }

}
