<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson" %>


<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.edit"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/editExternalPerson.do?method=edit">
	<html:hidden property="page" value="1"/>
	<html:hidden property="externalPersonID" />

	<table border="0" width="100%" cellspacing="3" cellpadding="10">

		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.name"/>:&nbsp;
			</td>
			<td align="left">
				<html:text property="name" size="45"/>
			</td>
		</tr>

		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.externalPersonWorkLocation"/>:&nbsp;		
			</td>
			<td align="left">
				<html:select property="workLocationID">
			    	<html:options collection="<%= SessionConstants.WORK_LOCATIONS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</td>	
		</tr>

		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.address"/>:&nbsp;
			</td>
			<td align="left" valign="top">
				<html:textarea property="address" rows="7" cols="40" />
			</td>
		</tr>

		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.phone"/>:&nbsp;
			</td>
			<td align="left">
				<html:text property="phone" size="45"/>
			</td>
		</tr>
		
		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.mobile"/>:&nbsp;
			</td>
			<td align="left">
				<html:text property="mobile" size="45"/>
			</td>
		</tr>
		
		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.homepage"/>:&nbsp;
			</td>
			<td align="left">
				<html:text property="homepage" size="45"/>
			</td>
		</tr>
		
		<tr>
			<td align="left">
				<bean:message key="label.masterDegree.administrativeOffice.email"/>:&nbsp;
			</td>
			<td align="left">
				<html:text property="email" size="45"/>
			</td>
		</tr>

		<tr>
			<tr> 
				<td>&nbsp;</td>
			</tr>			
			<td colspan="4" align="center">
				<html:submit styleClass="inputbuttonSmall" >
					<bean:message key="button.submit.masterDegree.externalPerson.edit"/>
				</html:submit>			
			</td>

		</tr>
	</table>
	
</html:form>


</center>