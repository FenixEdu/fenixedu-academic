<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<html:xhtml/>

<ul>
	<li class="navheader"><bean:message key="label.navheader.person"  /></li>
	<logic:present name="LOGGED_USER_ATTRIBUTE" property="person.openAffiliationEvent">
		<li><html:link page="/payments.do?method=viewAccount"><bean:message key="label.accounting.person.payments.title" bundle="ACCOUNTING_RESOURCES"/></html:link></li>
	</logic:present>
	

    <li class="navheader"><bean:message key="label.homepage"  /></li>
    <li><html:link page="/manageHomepage.do?method=options" titleKey="link.homepage.options"><bean:message key="link.homepage.options"  /></html:link></li>
    <li><html:link page="/manageHomepage.do?method=sections" titleKey="link.manage.homepage.content"><bean:message key="link.manage.homepage.content" /></html:link></li>

	<li class="navheader"><bean:message key="oauthapps.label"  /></li>
 	<li><html:link page="/externalApps.do?method=manageAuthorizations"><bean:message key="oauthapps.label.manage.authorizations"  bundle="APPLICATION_RESOURCES"/></html:link></li>
	<li><html:link page="/externalApps.do?method=manageApplications"><bean:message key="oauthapps.label.manage.applications"  bundle="APPLICATION_RESOURCES"/></html:link></li>
	<logic:present role="role(MANAGER)">
		<li><html:link page="/externalApps.do?method=viewAllApplications"><bean:message key="oauthapps.label.manage.all.applications"  bundle="APPLICATION_RESOURCES"/></html:link></li>
	</logic:present>
	<!-- Vigilancy link for people who do not have ROLE TEACHER -->

	<logic:notEmpty name="LOGGED_USER_ATTRIBUTE" property="person.vigilantWrappers">
		<logic:notPresent role="role(TEACHER)">
	 		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></li>
			<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></html:link></li>
		</logic:notPresent>
	</logic:notEmpty>

</ul>
