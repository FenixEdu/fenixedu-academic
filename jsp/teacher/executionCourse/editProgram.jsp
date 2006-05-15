<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.program.explanation" />
		</td>
	</tr>
</table>

<logic:present name="curriculum">
	<bean:define id="curricularCourse" name="curriculum" property="curricularCourse"/>
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
		<bean:message key="label.in"/>
		<bean:write name="degree" property="nome"/>
	</h3>
	<blockquote>
		<html:form action="/editProgram">
			<html:hidden property="method" value="edit"/>
			<html:hidden property="page" value="1"/>
			<bean:define id="curriculumID" type="java.lang.Integer" name="curriculum" property="idInternal"/>
			<html:hidden property="curriculumID" value="<%= curriculumID.toString() %>"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:write name="curricularCourse" property="name"/>: <bean:message key="title.program"/>
			</h4>
			<html:textarea  property="program" cols="50" rows="8"/>
			<br/>
			<h4>
				<bean:write name="curricularCourse" property="name"/>: <bean:message key="title.program.eng"/>
			</h4>
			<html:textarea  property="programEn" cols="50" rows="8"/>

			<br/>
			<br/>
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>