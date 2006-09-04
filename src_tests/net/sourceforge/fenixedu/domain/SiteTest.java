/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.util.MultiLanguageString;

public class SiteTest extends DomainTestBase {

    private Site site;

    private Section parentSection;

    protected void setUp() throws Exception {
        super.setUp();

        ExecutionCourse executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        site = new Site();
        site.setIdInternal(0);
        site.setExecutionCourse(executionCourse);
        site.setInitialStatement("initialStatement");
        site.setIntroduction("introduction");
        site.setMail("mail");
        site.setAlternativeSite("alternativeSite");

        parentSection = new Section();
        parentSection.setIdInternal(0);
        parentSection.setName(new MultiLanguageString(Language.pt, "ParentSection"));
        parentSection.setSite(site);
        parentSection.setSuperiorSection(null);
        parentSection.setSectionOrder(0);
    }

    public void testEdit() {
        site.edit("initialStatementEdited", "introductionEdited", "mailEdited", "alternativeSiteEdited");
        checkIfSiteAttributesAreCorrect("initialStatementEdited", "introductionEdited", "mailEdited",
                "alternativeSiteEdited", 0, 1);
    }

    public void testCreateAnnouncement() {
        try {
            site.createAnnouncement(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
        }

        try {
            site.createAnnouncement("Title", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
        }

        try {
            site.createAnnouncement(null, "Information");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
        }

        final Date dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createAnnouncement("Title", "Information");
        sleep(1000);
        final Date dateAfterCreation = Calendar.getInstance().getTime();

        assertEquals("Size unexpected!", 1, site.getAssociatedAnnouncementsCount());
        Announcement announcement = (Announcement) site.getAssociatedAnnouncements().get(0);

        assertEquals("Different Announcement Title!", "Title", announcement.getTitle());
        assertEquals("Different Announcement Information!", "Information", announcement.getInformation());
        assertEquals("Different Announcement Site!", site, announcement.getSite());
        assertTrue("Expected CreationDate After an initial timestamp!", announcement.getCreationDate()
                .after(dateBeforeCreation));
        assertTrue("Expected CreationDate Before an end timestamp!", announcement.getCreationDate()
                .before(dateAfterCreation));
        assertTrue("Expected ModificationDate After an initial timestamp!", announcement
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected ModificationDate Before an initial timestamp!", announcement
                .getLastModifiedDate().before(dateAfterCreation));
    }

    public void testCreateSection() {
        Date dateBeforeCreation;
        Date dateAfterCreation;
        Section section;

        try {
            site.createSection(null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        try {
            site.createSection(new MultiLanguageString("SectionName"), null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        try {
            site.createSection(null, parentSection, 1);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        // Create Section to a Site at last position
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SectionNameLast"), null, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 2, site.getAssociatedSectionsCount());
        section = (Section) site.getAssociatedSections().get(0); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameLast", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different ParentSection Order!", new Integer(0), parentSection.getSectionOrder());
        assertEquals("Different Section Order!", new Integer(1), section.getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create Section to a Site between "ParentSection" and
        // "SectionNameLast"
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SectionNameBetween"), null, 1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 3, site.getAssociatedSectionsCount());
        section = (Section) site.getAssociatedSections().get(0); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameBetween", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different Section Order!", new Integer(1), section.getSectionOrder());
        assertEquals("Different ParentSection Order!", new Integer(0), parentSection.getSectionOrder());
        assertEquals("Different \"SectionNameLast\" Order!", new Integer(2), site.getAssociatedSections().get(
                1).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create Section to a Site at start position
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SectionNameStart"), null, 0);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 4, site.getAssociatedSectionsCount());
        section = (Section) site.getAssociatedSections().get(0); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameStart", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different Section Order!", new Integer(0), section.getSectionOrder());
        assertEquals("Different ParentSection Order!", new Integer(1), parentSection.getSectionOrder());
        assertEquals("Different \"SectionNameBetween\" Order!", new Integer(2), site
                .getAssociatedSections().get(1).getSectionOrder());
        assertEquals("Different \"SectionNameLast\" Order!", new Integer(3), site.getAssociatedSections().get(
                2).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create SubSection to a Site and ParentSection
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SubSectionNameStart"), parentSection, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 5, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 1, parentSection.getAssociatedSectionsCount());
        section = (Section) parentSection.getAssociatedSections().get(0); // Last
        // Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameStart", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(0), section.getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create SubSection to a Site and ParentSection at last postion
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SubSectionNameLast"), parentSection, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 6, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 2, parentSection.getAssociatedSectionsCount());
        section = (Section) parentSection.getAssociatedSections().get(0); // Last
        // Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameLast", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(1), section.getSectionOrder());
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(0), parentSection
                .getAssociatedSections().get(1).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create SubSection to a Site and ParentSection between
        // "SubSectionNameStart" and "SubSectionNameLast"
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SubSectionNameBetween"), parentSection, 1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 7, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 3, parentSection.getAssociatedSectionsCount());
        section = (Section) parentSection.getAssociatedSections().get(0); // Last
        // Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameBetween", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(1), section.getSectionOrder());
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(0), parentSection
                .getAssociatedSections().get(2).getSectionOrder());
        assertEquals("Different \"SubSectionNameLast\" Order!", new Integer(2), parentSection
                .getAssociatedSections().get(1).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());

        // Create SubSection to a Site and ParentSection at start position
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection(new MultiLanguageString("SubSectionNameFirst"), parentSection, 0);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        assertEquals("Size unexpected!", 8, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 4, parentSection.getAssociatedSectionsCount());
        section = (Section) parentSection.getAssociatedSections().get(0); // Last
        // Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameFirst", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(0), section.getSectionOrder());
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(1), parentSection
                .getAssociatedSections().get(3).getSectionOrder());
        assertEquals("Different \"SubSectionNameBetween\" Order!", new Integer(2), parentSection
                .getAssociatedSections().get(1).getSectionOrder());
        assertEquals("Different \"SubSectionNameLast\" Order!", new Integer(3), parentSection
                .getAssociatedSections().get(2).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section
                .getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section
                .getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
    }
    
    private void checkIfSiteAttributesAreCorrect(final String initialStatement,
            final String introduction, final String mail, String alternativeSite,
            final int announcementsSize, final int sectionsSize) {

        assertEquals("Different Site InitialStatement!", initialStatement, site.getInitialStatement());
        assertEquals("Different Site Introduction!", introduction, site.getIntroduction());
        assertEquals("Different Site Mail!", mail, site.getMail());
        assertEquals("Different Site AlternativeSite!", alternativeSite, site.getAlternativeSite());
        assertEquals("Size unexpected!", announcementsSize, site.getAssociatedAnnouncementsCount());
        assertEquals("Size unexpected!", sectionsSize, site.getAssociatedSectionsCount());
    }

}
