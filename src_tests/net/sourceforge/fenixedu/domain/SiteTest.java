/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class SiteTest extends DomainTestBase {

    private ISite site;

    protected void setUp() throws Exception {
        super.setUp();

        ExecutionCourse executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        site = new Site();
        site.setIdInternal(0);
        site.setAssociatedAnnouncements(new ArrayList());
        site.setExecutionCourse(executionCourse);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateAnnouncement() {
        try {
            site.createAnnouncement(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncements().size());
        }

        try {
            site.createAnnouncement("Title", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncements().size());
        }

        try {
            site.createAnnouncement(null, "Information");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncements().size());
        }

        final Date dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createAnnouncement("Title", "Information");
        sleep(1000);
        final Date dateAfterCreation = Calendar.getInstance().getTime();

        assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
        IAnnouncement announcement = (IAnnouncement) site.getAssociatedAnnouncements().get(0);
        
        assertEquals("Different Announcement Title!", "Title", announcement.getTitle());
        assertEquals("Different Announcement Information!", "Information", announcement.getInformation());
        assertEquals("Different Announcement Site!", site, announcement.getSite());
        assertTrue("Expected CreationDate After an initial timestamp", announcement.getCreationDate()
                .after(dateBeforeCreation));
        assertTrue("Expected CreationDate Before an end timestamp", announcement.getCreationDate()
                .before(dateAfterCreation));
        assertTrue("Expected ModificationDate After an initial timestamp", announcement
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected ModificationDate Before an initial timestamp", announcement
                .getLastModifiedDate().before(dateAfterCreation));
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
