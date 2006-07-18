<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>

<ul>
	<li class="navheader">Gest�o de CMS</li>
	<li>
	<html:link module="/cms" action="/personalGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="CMS_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</li>
	<li>
	<html:link module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="CMS_RESOURCES" key="link.cmsConfiguration" />
	</html:link>
	</li>
	<li>
	<html:link module="/cms" action="/executionCourseWebsiteManagement?method=viewAll" >
		<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsite.label" />
	</html:link>
	</li>
	<li>
    <html:link module="/cms" action="/websiteTypeManagement?method=start" >
        <bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.label" />
    </html:link>  
	</li>
	<li>
    <html:link module="/cms" action="/functionalityLinkManagement?method=start" >
        <bean:message bundle="CMS_RESOURCES" key="cms.functionalityLinkManagement.label" />
    </html:link>
	</li>
	<li>
    <html:link module="/cms" action="/mailSender?method=start" >
        <bean:message bundle="CMS_RESOURCES" key="cms.mailSender.label" />
    </html:link> 
	</li>
</ul>

