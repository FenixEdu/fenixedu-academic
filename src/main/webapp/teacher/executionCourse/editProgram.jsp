<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.program" /></h2>

<div class="infoop2">
	<bean:message key="label.program.explanation" />
</div>


<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="warning0"><!--  w3c complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="curriculum">
	<bean:define id="curriculum" name="curriculum" type="net.sourceforge.fenixedu.domain.Curriculum"/>
	<bean:define id="curricularCourse" name="curriculum" property="curricularCourse"/>
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3 class="mtop15">
		<bean:write name="degree" property="presentationName"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<bean:define id="url" type="java.lang.String">/editProgram.do?method=editProgram&amp;executionCourseID=<bean:write name="executionCourse" property="externalId"/></bean:define>
	<bean:define id="curricularCourse" name="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"/>

	<bean:define id="curriculumFactoryEditCurriculum" name="curricularCourse" property="curriculumFactoryEditCurriculum" type="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryEditCurriculum"/>
	<logic:notEqual name="executionCourse" property="executionPeriod.executionYear.state.stateCode" value="C">
		<%
			curriculumFactoryEditCurriculum.setCurriculum(curriculum);
		%>
	</logic:notEqual>

	<fr:edit name="curriculumFactoryEditCurriculum"
			schema="net.sourceforge.fenixedu.domain.Curriculum.Program"
			action="<%= url %>"
			>
		<fr:layout name="flow">
			<fr:property name="eachClasses" value="flowblock"/>
		</fr:layout>
	</fr:edit>
</logic:present>