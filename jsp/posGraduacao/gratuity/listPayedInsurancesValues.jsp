<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.listPayedInsurances"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/listPayedInsurances.do" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="generateList"/>
	<table border="0">
		<tr>
			<td><bean:message key="label.executionYear" />:&nbsp;</td>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID">
				<html:option value="" key="label.masterDegree.administrativeOffice.allExecutionYears" />
				<html:options collection="executionYears"  property="idInternal" labelProperty="year" />
			</html:select></td>
		</tr>	
		<tr align="left">
			<td>
				<bean:message key="label.masterDegree.gratuity.dateInterval" />:&nbsp;
      		</td>
      		<td>				
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginDateDay" property="beginDateDay">
					<html:options collection="days" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginDateMonth" property="beginDateMonth">
					<html:options collection="months" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginDateYear" property="beginDateYear">
					<html:options collection="years" property="value" labelProperty="label"/>
				</html:select>				
			</td>          
			<td> <bean:message key="label.masterDegree.gratuity.dateIntervalRange"/> </td>
      		<td>				
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateDay" property="endDateDay">
					<html:options collection="days" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateMonth" property="endDateMonth">
					<html:options collection="months" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateYear" property="endDateYear">
					<html:options collection="years" property="value" labelProperty="label"/>
				</html:select>				
			</td>  			
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.masterDegree.gratuity.list"/>
	</html:submit>
</html:form>

</center>
