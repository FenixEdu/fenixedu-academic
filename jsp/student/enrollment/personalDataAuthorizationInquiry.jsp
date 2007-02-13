<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice" %>
<html:xhtml/>

<html:form action="/studentPersonalDataAuthorization?method=registerPersonalDataInquiryAnswer">


<h2><bean:message key="label.enrollment.personalData.inquiry"/></h2>

<div class="infoop2">
<p><bean:message key="label.info.dislocatedStudent.inquiry"/>:</p>
</div>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop2"><b><bean:message key="label.enrollment.personalData.authorization"/></b></p>


<p>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.PROFESSIONAL_ENDS.toString() %>" />
	<bean:message key="label.enrollment.personalData.professionalEnds"/>	
</p>

<p>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.SEVERAL_ENDS.toString() %>" />
	<bean:message key="label.enrollment.personalData.nonComericalEnds"/>	
</p>

<p>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.ALL_ENDS.toString() %>" />
	<bean:message key="label.enrollment.personalData.allEnds"/>	
</p>

<p>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.authorizationAnswer" property="authorizationAnswer" value="<%= StudentPersonalDataAuthorizationChoice.NO_END.toString() %>" />
	<b><bean:message key="label.enrollment.personalData.noAuthorization"/></b>
</p>

<p class="mtop15">
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
</p>

</html:form>

<p class="mtop2"><em><bean:message key="label.enrollment.personalData.changes"/></em></p>