<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<html:form action="/createExecutionCourses" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseDegreeCurricularPlans"/>

	<logic:messagesPresent message="true" property="success">
		<p>
			<span class="success0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
					<bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	<logic:messagesPresent message="true" property="error">
		<p>
			<span class="error0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
					<bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>

	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.degreeType" />:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType" size="1">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>	
		</tr>
		<tr>
			<td>
				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
				</html:submit>
			</td>
		</tr>
	</table>

</html:form>