<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
<li><html:link page="/visualizePersonalInfo.do"><bean:message key="label.person.visualizeInformation" /></html:link></li>
<li><html:link page="/changePersonalInfoDispatchAction.do?method=prepare"><bean:message key="label.person.changeContacts" /></html:link></li>
<li><html:link page="/changePasswordForward.do"><bean:message key="label.person.changePassword" /></html:link></li>
<br /><br />
<li><html:link page="/findPerson.do?method=prepareFindPerson"><bean:message key="label.person.findPerson" /></html:link></li>

</ul>
