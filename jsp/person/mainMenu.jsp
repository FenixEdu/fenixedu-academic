<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

    <html:link page="/visualizePersonalInfo.do"><bean:message key="label.person.visualizeInformation" /></html:link><br/>
    <html:link page="/changePersonalInfoDispatchAction.do?method=prepare"><bean:message key="label.person.changeInformation" /></html:link><br/>
    <html:link page="/changePasswordForward.do"><bean:message key="label.person.changePassword" /></html:link><br/>
