/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author Susana Fernandes
 */
public class StudentTestLog extends DomainObject implements IStudentTestLog {

    private IStudent student;

    private Integer keyStudent;

    private IDistributedTest distributedTest;

    private Integer keyDistributedTest;

    private Date date;

    private String event;

    public StudentTestLog() {
    }

    public StudentTestLog(Integer testId) {
        setIdInternal(testId);
    }

    public Date getDate() {
        return date;
    }

    public IDistributedTest getDistributedTest() {
        return distributedTest;
    }

    public String getEvent() {
        return event;
    }

    public Integer getKeyDistributedTest() {
        return keyDistributedTest;
    }

    public Integer getKeyStudent() {
        return keyStudent;
    }

    public IStudent getStudent() {
        return student;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDistributedTest(IDistributedTest test) {
        distributedTest = test;
    }

    public void setEvent(String string) {
        event = string;
    }

    public void setKeyDistributedTest(Integer integer) {
        keyDistributedTest = integer;
    }

    public void setKeyStudent(Integer integer) {
        keyStudent = integer;
    }

    public void setStudent(IStudent student) {
        this.student = student;
    }

}