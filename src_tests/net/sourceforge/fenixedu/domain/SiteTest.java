/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

public class SiteTest extends DomainTestBase {

    private ISite site;
    private ISection parentSection;

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
        parentSection.setName("ParentSection");
        parentSection.setSite(site);
        parentSection.setSuperiorSection(null);
        parentSection.setSectionOrder(0);                
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testEditSite() {
        try {
            site.edit(null, null, "mailEdited", "alternativeSiteEdited");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Site InitialStatement!", "initialStatement", site
                    .getInitialStatement());
            assertEquals("Different Site Introduction!", "introduction", site.getIntroduction());
            assertEquals("Different Site Mail!", "mail", site.getMail());
            assertEquals("Different Site AlternativeSite!", "alternativeSite", site.getAlternativeSite());
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        try {
            site.edit("initialStatementEdited", null, null, "alternativeSiteEdited");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Site InitialStatement!", "initialStatement", site
                    .getInitialStatement());
            assertEquals("Different Site Introduction!", "introduction", site.getIntroduction());
            assertEquals("Different Site Mail!", "mail", site.getMail());
            assertEquals("Different Site AlternativeSite!", "alternativeSite", site.getAlternativeSite());
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        try {
            site.edit(null, "introductionEdited", "mailEdited", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Site InitialStatement!", "initialStatement", site
                    .getInitialStatement());
            assertEquals("Different Site Introduction!", "introduction", site.getIntroduction());
            assertEquals("Different Site Mail!", "mail", site.getMail());
            assertEquals("Different Site AlternativeSite!", "alternativeSite", site.getAlternativeSite());
            assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
        }

        site.edit("initialStatementEdited", "introductionEdited", "mailEdited",
                "alternativeSiteEdited");
        assertEquals("Different Site InitialStatement!", "initialStatementEdited", site
                .getInitialStatement());
        assertEquals("Different Site Introduction!", "introductionEdited", site.getIntroduction());
        assertEquals("Different Site Mail!", "mailEdited", site.getMail());
        assertEquals("Different Site AlternativeSite!", "alternativeSiteEdited", site
                .getAlternativeSite());
        assertEquals("Size unexpected!", 0, site.getAssociatedAnnouncementsCount());
        assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());
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
        IAnnouncement announcement = (IAnnouncement) site.getAssociatedAnnouncements(0);

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
        ISection section;
        
        try {
            site.createSection(null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 1, site.getAssociatedSectionsCount());            
        }
        
        try {
            site.createSection("SectionName", null, null);
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
        site.createSection("SectionNameLast", null, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 2, site.getAssociatedSectionsCount());
        section = (ISection) site.getAssociatedSections(1); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameLast", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different ParentSection Order!", new Integer(0), parentSection.getSectionOrder());
        assertEquals("Different Section Order!", new Integer(1), section.getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create Section to a Site between "ParentSection" and "SectionNameLast"
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SectionNameBetween", null, 1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 3, site.getAssociatedSectionsCount());
        section = (ISection) site.getAssociatedSections(2); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameBetween", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different Section Order!", new Integer(1), section.getSectionOrder());        
        assertEquals("Different ParentSection Order!", new Integer(0), parentSection.getSectionOrder());
        assertEquals("Different \"SectionNameLast\" Order!", new Integer(2), site.getAssociatedSections(1).getSectionOrder());        
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create Section to a Site at start position
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SectionNameStart", null, 0);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 4, site.getAssociatedSectionsCount());
        section = (ISection) site.getAssociatedSections(3); // Last Inserted
        assertEquals("Different Section Name!", "SectionNameStart", section.getName());
        assertNull("Expected Null ParentSection!", section.getSuperiorSection());
        assertEquals("Different Section Order!", new Integer(0), section.getSectionOrder());                
        assertEquals("Different ParentSection Order!", new Integer(1), parentSection.getSectionOrder());
        assertEquals("Different \"SectionNameBetween\" Order!", new Integer(2), site.getAssociatedSections(2).getSectionOrder());        
        assertEquals("Different \"SectionNameLast\" Order!", new Integer(3), site.getAssociatedSections(1).getSectionOrder());        
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create SubSection to a Site and ParentSection
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SubSectionNameStart", parentSection, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 5, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 1, parentSection.getAssociatedSectionsCount());
        section = (ISection) parentSection.getAssociatedSections(0); // Last Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameStart", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(0), section.getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create SubSection to a Site and ParentSection at last postion
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SubSectionNameLast", parentSection, -1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 6, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 2, parentSection.getAssociatedSectionsCount());
        section = (ISection) parentSection.getAssociatedSections(1); // Last Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameLast", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(1), section.getSectionOrder()); 
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(0), parentSection.getAssociatedSections(0).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create SubSection to a Site and ParentSection between "SubSectionNameStart" and "SubSectionNameLast"
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SubSectionNameBetween", parentSection, 1);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 7, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 3, parentSection.getAssociatedSectionsCount());
        section = (ISection) parentSection.getAssociatedSections(2); // Last Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameBetween", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(1), section.getSectionOrder()); 
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(0), parentSection.getAssociatedSections(0).getSectionOrder());
        assertEquals("Different \"SubSectionNameLast\" Order!", new Integer(2), parentSection.getAssociatedSections(1).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
        
        // Create SubSection to a Site and ParentSection at start position
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        site.createSection("SubSectionNameFirst", parentSection, 0);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();        
        assertEquals("Size unexpected!", 8, site.getAssociatedSectionsCount());
        assertEquals("Size unexpected!", 4, parentSection.getAssociatedSectionsCount());
        section = (ISection) parentSection.getAssociatedSections(3); // Last Inserted
        assertEquals("Different SubSection Name!", "SubSectionNameFirst", section.getName());
        assertEquals("Different ParentSection!", parentSection, section.getSuperiorSection());
        assertEquals("Different SubSection Order!", new Integer(0), section.getSectionOrder()); 
        assertEquals("Different \"SubSectionNameStart\" Order!", new Integer(1), parentSection.getAssociatedSections(0).getSectionOrder());
        assertEquals("Different \"SubSectionNameBetween\" Order!", new Integer(2), parentSection.getAssociatedSections(2).getSectionOrder());
        assertEquals("Different \"SubSectionNameLast\" Order!", new Integer(3), parentSection.getAssociatedSections(1).getSectionOrder());
        assertTrue("Expected LastModificationDate After an initial timestamp!", section.getLastModifiedDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an end timestamp!", section.getLastModifiedDate().before(dateAfterCreation));
        assertEquals("Different Section Site", site, section.getSite());        
        assertNotNull("Expected Not Null Section items!", section.getAssociatedItems());
        assertNotNull("Expected Not Null Section SubSections!", section.getAssociatedSections());
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
