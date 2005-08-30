<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice" %>

<html:form action="/studentPersonalDataAuthorization?method=registerPersonalDataInquiryAnswer">

<div class="infoselected">
	<h2><bean:message key="label.enrollment.personalData.inquiry"/></h2>
</div>

<br/>
<html:errors/>
<br/>
<b><bean:message key="label.enrollment.personalData.authorization"/></b>
<br/><br/>
<table cellpadding=5>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="answer" value="<%= StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.professionalEnds"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="answer" value="<%= StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.nonComericalEnds"/></td>	
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:radio property="answer" value="<%= StudentPersonalDataAuthorizationChoice.ALL_ENDS.toString() %>" /></td>
	<td><bean:message key="label.enrollment.personalData.allEnds"/></td>	
</tr>
</table>
<br/>

<html:radio property="answer" value="<%= StudentPersonalDataAuthorizationChoice.NO_END.toString() %>" />&nbsp;&nbsp;
<b><bean:message key="label.enrollment.personalData.noAuthorization"/></b>

<br/><br/>
<html:submit styleClass="inputbutton">Continuar</html:submit>
</html:form>