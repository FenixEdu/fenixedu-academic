<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->


<ul>
	<li class="navheader"><bean:message key="label.navheader.person" /></li>
	<li><html:link page="/visualizePersonalInfo.do" titleKey="link.title.visualizeInformation"><bean:message key="label.person.visualizeInformation" /></html:link></li>
	<li><html:link page="/changePersonalInfoDispatchAction.do?method=prepare" titleKey="link.title.person.changeContacts"><bean:message key="label.person.changeContacts" /></html:link></li>
	<li><html:link page="/changePasswordForward.do" titleKey="link.title.person.changePassword"><bean:message key="label.person.changePassword" /></html:link></li>
	<br />
	<li><html:link page="/sendSms.do?method=prepare" titleKey=""><bean:message key="label.person.sendSms" /></html:link></li>
	<li><html:link page="/findPerson.do?method=prepareFindPerson" titleKey=""><bean:message key="label.person.findPerson" /></html:link></li>
</ul>
