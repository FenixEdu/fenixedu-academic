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

<logic:present name="curricularCourse">
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
		<bean:message key="label.in"/>
		<bean:write name="degree" property="nome"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<blockquote>
		<bean:define id="url" type="java.lang.String">/createObjectives.do?method=createObjectives&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
		<fr:edit name="curricularCourse" property="curriculumFactoryEditCurriculum"
				schema="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculumObjectives"
				action="<%= url %>"
				>
			<fr:layout name="flow">
			</fr:layout>
		</fr:edit>
	</blockquote>
</logic:present>