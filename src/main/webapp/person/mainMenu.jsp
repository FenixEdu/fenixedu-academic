<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

<html:xhtml/>

<ul>
	<li class="navheader"><bean:message key="label.navheader.person"  /></li>
	<li><html:link page="/visualizePersonalInfo.do" titleKey="link.title.visualizeInformation"  ><bean:message key="label.person.visualizeInformation"  /></html:link></li>
	<li><html:link page="/changePasswordForward.do" titleKey="link.title.person.changePassword" ><bean:message key="label.person.changePassword"  /></html:link></li>
	<li><html:link page="/identificationCard.do?method=prepare"><bean:message key="label.identification.card"  bundle="APPLICATION_RESOURCES"/></html:link></li>
	<li><html:link page="/parking.do?method=prepareParking"><bean:message key="label.parking"  bundle="PARKING_RESOURCES"/></html:link></li>
	<li><html:link page="/validateEmail.do?method=prepare"><bean:message key="label.validate.email"/></html:link></li>
	<li><html:link page="/irsDeclaration.do?method=viewIrsDocumentInformation"><bean:message key="label.irs.information" bundle="APPLICATION_RESOURCES"/></html:link></li>
	<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.openAffiliationEvent">
	<li><html:link page="/payments.do?method=viewAccount"><bean:message key="label.accounting.person.payments.title" bundle="ACCOUNTING_RESOURCES"/></html:link></li>
	</logic:present>
	

    <li class="navheader"><bean:message key="label.homepage"  /></li>
    <li><html:link page="/manageHomepage.do?method=options" titleKey="link.homepage.options"><bean:message key="link.homepage.options"  /></html:link></li>
    <li><html:link page="/manageHomepage.do?method=sections" titleKey="link.manage.homepage.content"><bean:message key="link.manage.homepage.content" /></html:link></li>

	<!-- Vigilancy link for people who do not have ROLE TEACHER -->

	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.vigilantWrappers">
		<logic:notPresent role="TEACHER">
	 		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></li>
			<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></html:link></li>
		</logic:notPresent>
	</logic:notEmpty>

	<logic:present role="MANAGER">
		<li class="navheader"><bean:message key="label.person.system.configuration"/></li>
		<logic:notEqual name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId" value="ist24518"> 
			<logic:notEqual name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId" value="ist24421">
				<li>
					<html:link page="/contentManagement.do?method=viewContainer">
						<bean:message key="label.person.system.configuration.root.portal"/>
					</html:link>
					<html:link page="/portalManagement.do?method=prepare">
						<bean:message key="label.person.system.configuration.meta.domain.objects"/>
					</html:link>
				</li>
				<!-- Functionalities -->
				<li>
					<html:link page="/functionalities/module/viewRoot.do">
						<bean:message key="label.person.system.configuration.functionalities"/>
					</html:link>
				</li>
			</logic:notEqual>
		</logic:notEqual>
		<li>
			<html:link page="/irsDeclaration.do?method=edit" titleKey="link.title.irsDeclaration">
				<bean:message key="label.person.edit.irs.declaration.link"  />
			</html:link>
		</li>
	</logic:present>
	
	<!-- FOR LOAD TESTING -->
	<html:link styleId="loadTesting" page="/loadTesting.do?method=loadTesting" style="display:none"/>
</ul>
