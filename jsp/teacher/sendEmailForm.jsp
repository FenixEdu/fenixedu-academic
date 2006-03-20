<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
				<html:text property="fromName" size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.From"/></b>
			</td>
			<td>
				<html:text property="from"  size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.Subject"/></b>
			</td>
			<td>
				<html:text property="subject"  size="50"/>
			</td>
		</tr>
		<tr valign="top">
			<td  valign="top">
				<b><bean:message key="label.email.Body"/></b>
			</td>
			<td>
				<html:textarea cols="50" rows="10" property="text"/>
			</td>
		</tr>
	</table>
	<html:hidden property="shiftCode"/>
	<html:hidden property="studentGroupCode"/>
	<html:hidden property="objectCode"/>
	<%-- TODO: this must be redisigned to be fully reusable--%>
	<html:hidden property="candidaciesSend"/>
	<html:hidden property="method" value="send"/>
	<html:hidden property="seminaryID" />
	<html:hidden property="case1ID" />
	<html:hidden property="case2ID" />
	<html:hidden property="case3ID" />
	<html:hidden property="case4ID" />
	<html:hidden property="case5ID" />
	<html:hidden property="degreeID"/>
	<html:hidden property="modalityID" />
	<html:hidden property="courseID" />
	<html:hidden property="themeID" />  
	<html:hidden property="approved" />  

   <html:submit property="submition"><bean:message key="button.sendMail"/></html:submit>
</html:form>

