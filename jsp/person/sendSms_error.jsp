<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.person.sendSms"/></h2>

<html:messages id="msg" message="true">
	<span class="sucessfulOperarion"><bean:write name="msg"/></span><br/>
</html:messages>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
	
<p />
<html:link page="/viewSmsDeliveryReports.do">		
	<bean:message key="link.person.sms.viewDeliveryReports"/>
</html:link>