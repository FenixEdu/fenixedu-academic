<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.defineInsuranceValue"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/editInsuranceValue.do" focus="insuranceValue">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="defineInsuranceValue"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<table border="0">
		<tr align="left">
			<td><bean:message key="label.masterDegree.gratuity.insuranceValue"/>:&nbsp;</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.insuranceValue" property="insuranceValue" size="8" />	
			</td>
		</tr>
		<tr align="left">
			<td>
				<bean:message key="label.masterDegree.gratuity.insuranceValueEndDate" />:&nbsp;
      		</td>
      		<td>				
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateDay" property="endDateDay">
					<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateMonth" property="endDateMonth">
					<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.endDateYear" property="endDateYear">
					<html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
				</html:select>				
			</td>          
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.define"/>
	</html:submit>
</html:form>

</center>
