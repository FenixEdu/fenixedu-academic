/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class AnnouncementTest extends TestCase {

    private ISite site;

    private IAnnouncement announcement;

    protected void setUp() throws Exception {
        super.setUp();

        IExecutionCourse executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        site = new Site();
        site.setIdInternal(0);
        site.setAssociatedAnnouncements(new ArrayList());
        site.setExecutionCourse(executionCourse);

        announcement = new Announcement();
        announcement.setIdInternal(0);
        announcement.setTitle("Title");
        announcement.setInformation("Information");
        announcement.setSite(site);
        announcement.setCreationDate(Calendar.getInstance().getTime());

        site.getAssociatedAnnouncements().add(announcement);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEditAnnouncement() {
        try {
            announcement.editAnnouncement(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
            assertEquals("Different Announcement Title!", "Title", announcement.getTitle());
            assertEquals("Different Announcement Information!", "Information", announcement
                    .getInformation());
            assertEquals("Different Announcement Site!", site, announcement.getSite());
        }

        try {
            announcement.editAnnouncement("TitleEdited", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
            assertEquals("Different Announcement Title!", "Title", announcement.getTitle());
            assertEquals("Different Announcement Information!", "Information", announcement
                    .getInformation());
            assertEquals("Different Announcement Site!", site, announcement.getSite());
        }

        try {
            announcement.editAnnouncement(null, "InformationEdited");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
            assertEquals("Different Announcement Title!", "Title", announcement.getTitle());
            assertEquals("Different Announcement Information!", "Information", announcement
                    .getInformation());
            assertEquals("Different Announcement Site!", site, announcement.getSite());
        }

        final Date dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        announcement.editAnnouncement("TitleEdited", "InformationEdited");
        sleep(1000);
        final Date dateAfterEdition = Calendar.getInstance().getTime();

        assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
        assertEquals("Different Announcement Title!", "TitleEdited", announcement.getTitle());
        assertEquals("Different Announcement Information!", "InformationEdited", announcement
                .getInformation());
        assertEquals("Different Announcement Site!", site, announcement.getSite());
        assertTrue("Expected CreationDate Before ModificationDate", announcement.getCreationDate()
                .before(announcement.getLastModifiedDate()));
        assertTrue("Expected ModificationDate After an initial timestamp", announcement
                .getLastModifiedDate().after(dateBeforeEdition));
        assertTrue("Expected ModificationDate Before an initial timestamp", announcement
                .getLastModifiedDate().before(dateAfterEdition));
    }

    public void testDeleteAnnouncement() {
        assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncements().size());
        announcement.deleteAnnouncement();
        assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncements().size());
        assertNull("Different Element!", announcement.getSite());
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
