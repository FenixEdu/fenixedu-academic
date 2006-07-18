<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="message.label.Hints"/>
		</td>
	</tr>
</table>
	<html:form action="sendMailToAllStudents.do" method="get">

	<table>
		<tr>
			<td>
				<b><bean:message key="label.email.FromName"/></b>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.fromName" property="fromName" size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.From"/></b>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.from" property="from"  size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.Subject"/></b>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject"  size="50"/>
			</td>
		</tr>
		<tr valign="top">
			<td  valign="top">
				<b><bean:message key="label.email.Body"/></b>
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.text" cols="50" rows="10" property="text"/>
			</td>
		</tr>
	</table>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode" property="shiftCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode" property="studentGroupCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<%-- TODO: this must be redisigned to be fully reusable--%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidaciesSend" property="candidaciesSend"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="send"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.seminaryID" property="seminaryID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.case1ID" property="case1ID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.case2ID" property="case2ID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.case3ID" property="case3ID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.case4ID" property="case4ID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.case5ID" property="case5ID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.modalityID" property="modalityID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseID" property="courseID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.themeID" property="themeID" />  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.approved" property="approved" />  

   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" property="submition"><bean:message key="button.sendMail"/></html:submit>
</html:form>

