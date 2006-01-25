/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.website;


import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainTestBase;
import net.sourceforge.fenixedu.domain.cms.Content;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:35:49,23/Jan/2006
 * @version $Id$
 */
public class WebsiteMandatoryContentsTest extends DomainTestBase
{
	private WebsiteType type;

	private JSPContent jspContent;

	private ExecutionCourseWebsite website;

	private WebsiteSection section;

	private WebsiteSection section2;

	public void setUp() throws Exception
	{
		super.setUp();
		this.instantiateInstanceMembers();
		jspContent.setName("JspContent 1");
		website.setName("Site da cadeira de PO");
		section.setName("Secção 1");
		section2.setName("Secção 2");
		website.addChildren(section);
		type.addMandatoryContents(section2);

		WebsiteType.WebSiteTypeWebsite.add(type, website);
		WebsiteType.WebSiteTypeMandatoryContent.add(type, jspContent);

	}

	private void instantiateInstanceMembers()
	{
		this.type = new WebsiteType();
		this.jspContent = new JSPContent();
		this.website = new ExecutionCourseWebsite();
		this.section = new WebsiteSection();
		this.section2 = new WebsiteSection();

	}

	public void testSiteHasSiteTypeMandatoryContentsInItsComponentCollection()
	{
		Collection<Content> siteContents = this.website.getChildren();
		assertTrue("#AT1 Site does not contain its own section,", siteContents.contains(this.section));
		assertTrue("#AT2 Site does not contain mandatory type section,", siteContents.contains(this.section2));
		assertTrue("#AT3 Site does not contain mandatory jsp content,", siteContents.contains(this.jspContent));
	}

	public void testSiteWithNoTypeHasOnlyItsComponents()
	{
		this.website.removeType();
		Collection<Content> siteContents = this.website.getChildren();
		assertTrue("#AT1 Site does not contain its own section,", siteContents.contains(this.section));
		assertFalse("#AF1 Site contains mandatory type section and it should not,", siteContents.contains(this.section2));
		assertFalse("#AF2 Site contains mandatory jsp content and it should not,", siteContents.contains(this.jspContent));
	}

	public void testSiteContentsContentChangedByChangingItsTypeMandatoryContents()
	{
		Collection<Content> siteContents = this.website.getChildren();
		assertTrue("#AT1 Site does not contain its own section,", siteContents.contains(this.section));
		assertTrue("#AT2 Site does not contain mandatory type section,", siteContents.contains(this.section2));
		assertTrue("#AT3 Site does not contain mandatory jsp content,", siteContents.contains(this.jspContent));

		WebsiteType.WebSiteTypeMandatoryContent.remove(type, jspContent);
		siteContents = this.website.getChildren();
		assertTrue("#AT1 Site does not contain its own section,", siteContents.contains(this.section));
		assertTrue("#AT2 Site does not contain mandatory type section,", siteContents.contains(this.section2));
		assertFalse("#AF1 Site contains mandatory jsp content and it should not,", siteContents.contains(this.jspContent));
	}

	public void testWebsiteContentsChangedByDirectlyEditItsCollectionInsteadOfViaAddAndRemoveMethods()
	{
		Collection<Content> siteContents = this.website.getChildren();
		try
		{
			siteContents.remove(section);
			fail("#F1 I was able to directly edit the unmodifiable collection returned by website's getChildren method");
		}
		catch (UnsupportedOperationException exception)
		{
			//its ok, this is expected
		}
	}
	
	public void testWebsiteContentRemovedViaClassRemoveMethod()
	{
		Content.ContentHierarchy.remove(this.section,this.website);
		Collection<Content> siteContents = this.website.getChildren();
		assertFalse("#AF1 Site still have its old section,", siteContents.contains(this.section));
		assertTrue("#AT1 Site does not contain mandatory type section,", siteContents.contains(this.section2));
		assertTrue("#AT2 Site does not contain mandatory jsp content,", siteContents.contains(this.jspContent));
	}
}
