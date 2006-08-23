<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.objectives" /></h2>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.objectives.explanation" />
		</td>
	</tr>
</table>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="info">
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="curriculum">
	<bean:define id="curricularCourse" name="curriculum" property="curricularCourse"/>
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
		<bean:message key="label.in"/>
		<bean:write name="degree" property="nome"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<blockquote>
		<html:form action="/editObjectives">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editObjectives"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<bean:define id="curriculumID" type="java.lang.Integer" name="curriculum" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curriculumID" property="curriculumID" value="<%= curriculumID.toString() %>"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:message key="label.generalObjectives"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives"  property="generalObjectives" cols="50" rows="8"/>
			<br/>
			<h4>
				<bean:message key="label.generalObjectives.eng"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn"  property="generalObjectivesEn" cols="50" rows="8"/>
			<h4>
				<bean:message key="label.operacionalObjectives"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives"  property="operacionalObjectives" cols="50" rows="8"/>
			<br/>
			<h4>
				<bean:message key="label.operacionalObjectives.eng"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn"  property="operacionalObjectivesEn" cols="50" rows="8"/>

			<br/>
			<br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>