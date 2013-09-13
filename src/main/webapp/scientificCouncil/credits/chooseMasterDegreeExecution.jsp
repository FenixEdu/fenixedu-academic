<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewMasterDegreeCredits"/>

	<table class="tstyle5 thlight thright thmiddle">
		<tr>
			<th><bean:message key="label.credits.choose.ExecutionYear"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
					<html:options collection="executionYears" property="externalId" labelProperty="year"/>
				</html:select>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.credits.choose.masterDegreeCurricularPlan"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" size="1" >
					<html:options collection="masterDegreeExecutions" property="externalId" labelProperty="degreeCurricularPlan.name"/>
				</html:select>
			</td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="visualize"/>
		</html:submit>
	</p>
	
</html:form>