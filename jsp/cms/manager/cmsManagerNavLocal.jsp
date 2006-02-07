<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<strong>Gestão de CMS</strong>
<p><strong>&raquo; 
	<html:link module="/cms" action="/personalGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="CMS_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="CMS_RESOURCES" key="link.cmsConfiguration" />
	</html:link>	
</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/executionCourseWebsiteManagement?method=viewAll" >
		<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsite.label" />
	</html:link>	
</strong></p>
<p><strong>&raquo;
    <html:link module="/cms" action="/websiteTypeManagement?method=start" >
        <bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.label" />
    </html:link>    
</strong></p>
