<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<html:form action="/createClassificationsForStudents">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createClassifications"/>
	
	<h2><bean:message key="label.limits" bundle="INQUIRIES_RESOURCES"  /></h2>
	
	<table>
		<tr>
			<td><bean:message key="label.entryGradeLimits" bundle="INQUIRIES_RESOURCES"  />:</td>
			<td>
				<logic:iterate id="entryGradeLimit" name="createClassificationsForm" property="entryGradeLimits" indexId="i" >	
					<logic:notEqual name="i" value="0"> , </logic:notEqual>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.entryGradeLimits" property="entryGradeLimits" value='<%= entryGradeLimit.toString() %>' size="4" />%
				</logic:iterate>
			<td>
		</tr>
		<tr>
			<td><bean:message key="label.approvationRatioLimits" bundle="INQUIRIES_RESOURCES"  />:</td>
			<td>
				<logic:iterate id="approvationRatioLimit" name="createClassificationsForm" property="approvationRatioLimits" indexId="i" >	
					<logic:notEqual name="i" value="0"> , </logic:notEqual>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.approvationRatioLimits" property="approvationRatioLimits" value='<%= approvationRatioLimit.toString() %>' size="4" />%
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.arithmeticMeanLimits" bundle="INQUIRIES_RESOURCES"  />:</td>
			<td>
				<logic:iterate id="arithmeticMeanLimit" name="createClassificationsForm" property="arithmeticMeanLimits" indexId="i" >	
					<logic:notEqual name="i" value="0"> , </logic:notEqual>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.arithmeticMeanLimits" property="arithmeticMeanLimits" value='<%= arithmeticMeanLimit.toString() %>' size="4" />%
				</logic:iterate>	
			</td>
		</tr>
	</table>
	
	<br/><br/>
	<h2><bean:message key="label.degreeCurricularPlans" bundle="INQUIRIES_RESOURCES"  /></h2>
		
	<logic:present name="degreeCurricularPlans">
		<table>
			<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
					<tr>
						<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeCurricularPlanID" property="degreeCurricularPlanID" idName="degreeCurricularPlan" value="externalId" /></td>
						<td><bean:message name="degreeCurricularPlan" property="infoDegree.degreeType.name" bundle="ENUMERATION_RESOURCES"/></td>
						<td><bean:write name="degreeCurricularPlan" property="name" /></td>
						<td><bean:write name="degreeCurricularPlan" property="infoDegree.nome" /></td>
					</tr>
			</logic:iterate>
		</table>
		<p/>
		<html:submit>
			<bean:message key="button.createClassifications" bundle="INQUIRIES_RESOURCES"  />
		</html:submit>
	</logic:present>
	
</html:form>
