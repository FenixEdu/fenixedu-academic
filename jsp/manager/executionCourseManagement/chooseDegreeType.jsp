<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:form action="/createExecutionCourses" >
	<html:hidden property="method" value="chooseDegreeCurricularPlans"/>

	<table>
		<tr>
			<td>
				<bean:message key="message.degreeType" />:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="degreeType" size="1">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>	
		</tr>
		<tr>
			<td>
				<html:submit>
					<bean:message key="button.manager.executionCourseManagement.continue"/>
				</html:submit>
			</td>
		</tr>
	</table>

</html:form>