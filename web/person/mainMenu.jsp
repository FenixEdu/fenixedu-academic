<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<ul>
	<li class="navheader"><bean:message key="label.navheader.person"  /></li>
	<li><html:link page="/visualizePersonalInfo.do" titleKey="link.title.visualizeInformation"  ><bean:message key="label.person.visualizeInformation"  /></html:link></li>
	<li><html:link page="/changePasswordForward.do" titleKey="link.title.person.changePassword" ><bean:message key="label.person.changePassword"  /></html:link></li>
	<li><html:link page="/parking.do?method=prepareParking"><bean:message key="label.parking"  bundle="PARKING_RESOURCES"/></html:link></li>
	<li><html:link page="/validateEmail.do?method=prepare"><bean:message key="label.validate.email"/></html:link></li>

    <li class="navheader"><bean:message key="label.homepage"  /></li>
    <li><html:link page="/manageHomepage.do?method=options" titleKey="link.homepage.options"><bean:message key="link.homepage.options"  /></html:link></li>
    <li><html:link page="/manageHomepage.do?method=sections" titleKey="link.manage.homepage.content"><bean:message key="link.manage.homepage.content" /></html:link></li>

	<!-- Vigilancy link for people who do not have ROLE TEACHER -->

	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.vigilants">
		<logic:notPresent name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher">
	 		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></li>
			<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></html:link></li>
		</logic:notPresent>

	</logic:notEmpty>


	<logic:present role="MANAGER">
		<logic:notEqual name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId" value="ist24518"> 
			<logic:notEqual name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId" value="ist24421">
				<li>
					<html:link page="/contentManagement.do?method=viewContainer">
					Root Portal
					</html:link>
					<html:link page="/portalManagement.do?method=prepare">
					Meta Domain Objects
					</html:link>
				</li>
			
			<!-- Functionalities -->
				<li><html:link page="/functionalities/module/viewRoot.do">Funcionalidades</html:link></li>
			</logic:notEqual>
		</logic:notEqual>
	</logic:present>
	
</ul>