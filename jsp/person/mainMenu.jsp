<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.navheader.person"  /></li>
	<li><html:link page="/visualizePersonalInfo.do" titleKey="link.title.visualizeInformation"  ><bean:message key="label.person.visualizeInformation"  /></html:link></li>
	<li><html:link page="/changePersonalInfoDispatchAction.do?method=prepare" titleKey="link.title.person.changeContacts"  ><bean:message key="label.person.changeContacts"  /></html:link></li>
	<li><html:link page="/changePasswordForward.do" titleKey="link.title.person.changePassword" ><bean:message key="label.person.changePassword"  /></html:link></li>
	<li><html:link page="/manageHomepage.do?method=prepare" titleKey="link.manage.homepage"><bean:message key="link.manage.homepage"  /></html:link></li>
	<%-- <li><html:link page="/sendSms.do?method=prepare" ><bean:message key="label.person.sendSms"  /></html:link></li>  --%>
	<li class="navheader"><bean:message key="label.navheader.services"  /></li>
	<li><html:link page="/findPerson.do?method=prepareFindPerson" ><bean:message key="label.person.findPerson"  /></html:link></li>
	<li><html:link page="/organizationalStructure/structurePage.faces"><bean:message key="label.orgUnit"  /></html:link></li>
</ul>
