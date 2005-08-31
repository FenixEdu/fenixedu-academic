/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

/**
 * @author Susana Fernandes
 */
public class Question extends Question_Base {

    public void delete() {
        getMetadata().setNumberOfMembers(new Integer(getMetadata().getNumberOfMembers().intValue() - 1));
        removeMetadata();
        deleteDomainObject();
    }

}
