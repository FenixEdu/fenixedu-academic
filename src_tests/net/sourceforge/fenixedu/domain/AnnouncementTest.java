/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

public class AnnouncementTest extends DomainTestBase {

    private ISite site;

    private IAnnouncement announcement;

    protected void setUp() throws Exception {
        super.setUp();

        IExecutionCourse executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        site = new Site();
        site.setIdInternal(0);
        site.setExecutionCourse(executionCourse);

        announcement = new Announcement();
        announcement.setIdInternal(0);
        announcement.setTitle("Title");
        announcement.setInformation("Information");
        announcement.setSite(site);
        announcement.setCreationDate(Calendar.getInstance().getTime());
    }

    public void testEditAnnouncement() {
        try {
            announcement.edit(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfAnnouncementAttributesAreCorrect(1, "Title", "Information", this.site);
        }

        try {
            announcement.edit("TitleEdited", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfAnnouncementAttributesAreCorrect(1, "Title", "Information", this.site);
        }

        try {
            announcement.edit(null, "InformationEdited");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfAnnouncementAttributesAreCorrect(1, "Title", "Information", this.site);
        }

        final Date dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        announcement.edit("TitleEdited", "InformationEdited");
        sleep(1000);
        final Date dateAfterEdition = Calendar.getInstance().getTime();

        checkIfAnnouncementAttributesAreCorrect(1, "TitleEdited", "InformationEdited", this.site);
        assertTrue("Expected CreationDate Before ModificationDate", announcement.getCreationDate()
                .before(announcement.getLastModifiedDate()));
        assertTrue("Expected ModificationDate After an initial timestamp", announcement
                .getLastModifiedDate().after(dateBeforeEdition));
        assertTrue("Expected ModificationDate Before an initial timestamp", announcement
                .getLastModifiedDate().before(dateAfterEdition));
    }

    public void testDeleteAnnouncement() {
        assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncementsCount());
        announcement.delete();
        assertEquals("Size unexpected in AssociatedAnnouncements!", 0, site
                .getAssociatedAnnouncementsCount());
        assertNull("Expected Null Site in Announcement!", announcement.getSite());
    }

    private void checkIfAnnouncementAttributesAreCorrect(final int size, final String title,
            final String information, final ISite site) {
        assertEquals("Size unexpected!", size, site.getAssociatedAnnouncementsCount());
        assertEquals("Different Announcement Title!", title, announcement.getTitle());
        assertEquals("Different Announcement Information!", information, announcement.getInformation());
        assertEquals("Different Announcement Site!", site, announcement.getSite());
    }

}
