<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<ul>
	<li class="navheader"><bean:message key="label.navheader.person"  /></li>
	<li><html:link page="/visualizePersonalInfo.do" titleKey="link.title.visualizeInformation"  ><bean:message key="label.person.visualizeInformation"  /></html:link></li>
	<%-- <li><html:link page="/changePersonalInfoDispatchAction.do?method=prepare" titleKey="link.title.person.changeContacts"  ><bean:message key="label.person.changeContacts"  /></html:link></li> --%>
	<li><html:link page="/changePasswordForward.do" titleKey="link.title.person.changePassword" ><bean:message key="label.person.changePassword"  /></html:link></li>
	<li><html:link page="/manageHomepage.do?method=prepare" titleKey="link.manage.homepage"><bean:message key="link.manage.homepage"  /></html:link></li>
	<li><html:link page="/parking.do?method=prepareParking"><bean:message key="label.parking"  bundle="PARKING_RESOURCES"/></html:link></li>
	<%-- <li><html:link page="/sendSms.do?method=prepare" ><bean:message key="label.person.sendSms"  /></html:link></li>  --%>


	<!-- Vigilancy link for people who do not have ROLE TEACHER -->

	<logic:notEmpty name="UserView" property="person.vigilants">
		<logic:notPresent name="UserView" property="person.teacher">
	 		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></li>
			<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></html:link></li>
		</logic:notPresent>

	</logic:notEmpty>

	
	
</ul>