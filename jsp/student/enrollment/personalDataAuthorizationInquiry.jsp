<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice" %>

<html:form action="/studentPersonalDataAuthorization?method=registerPersonalDataInquiryAnswer">


<h2><bean:message key="label.enrollment.personalData.inquiry"/></h2>

<div class="infoop">
<p><bean:message key="label.info.dislocatedStudent.inquiry"/>:</p>
</div>

<html:errors/>
<br/>
<b><bean:message key="label.enrollment.personalData.authorization"/></b>
<br/><br/>
<table cellpadding=5>
<tr>
	<td>&nbsp;</td>
	<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.professionalEnds"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.nonComericalEnds"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.ALL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.allEnds"/></td>	
</tr>
</table>
<br/>

<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.NO_END.toString() %>" />&nbsp;&nbsp;
<b><bean:message key="label.enrollment.personalData.noAuthorization"/></b>

<br/><br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
</html:form>

<br/><br/><br/><br/><br/><br/><br/><br/>
<bean:message key="label.enrollment.personalData.changes"/>