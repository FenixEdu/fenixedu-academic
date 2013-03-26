<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.CompetenceCourse"%>

<h2><bean:message key="link.program" /></h2>

<div class="infoop2">
	<bean:message key="label.program.explanation" />
</div>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="warning0"><!-- w3c complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="executionCourse">

	<bean:define id="showNote" value="false" toScope="request"/>
	
	<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester" />

	<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" type="net.sourceforge.fenixedu.domain.CurricularCourse">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>

		<logic:equal name="curricularCourse" property="bolonhaDegree" value="true">
			<bean:define id="competenceCourse" name="curricularCourse" property="competenceCourse"/>
			
			<p class="mtop15 mbottom025 color777">
				<bean:write name="degree" property="presentationName"/>
			</p>
			<h3 class="mtop025">
				<%= ((CompetenceCourse) competenceCourse).getName(executionPeriod) %>
				<logic:equal name="competenceCourse" property="curricularStage.name" value="PUBLISHED">
					<bean:define id="showNote" value="true" toScope="request"/>
					<span style="font-size: 0.7em; font-weight: normal; background: #ffa;"><bean:message key="label.competenceCourse.notApproved"/>(*)</span>
				</logic:equal>
			</h3>
			<blockquote>
				<h4>
					<bean:message key="title.program"/>
				</h4>
				<%= ((CompetenceCourse) competenceCourse).getProgram(executionPeriod) %>
				<bean:define id="programEn" value="<%= ((CompetenceCourse) competenceCourse).getProgramEn(executionPeriod) %>" />
				<logic:present name="programEn">
					<br/>
					<h4>
						<bean:message key="title.program.eng"/>
					</h4>
					<%= programEn %>
				</logic:present>
			</blockquote>
		</logic:equal>

		<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>

				<h3 class="mtop2">
					<bean:write name="degree" property="presentationName"/>
					<br/>
					<bean:write name="curricularCourse" property="name"/>
				</h3>
				<blockquote>
					<logic:present name="curriculum">
						<h4>
							<bean:message key="title.program"/>
						</h4>
						<bean:write name="curriculum" property="program" filter="false"/>
						<logic:present name="curriculum" property="programEn">
							<br/>
							<h4>
								<bean:message key="title.program.eng"/>
							</h4>
							<bean:write name="curriculum" property="programEn" filter="false"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.program.not.defined"/>
					</logic:notPresent>
				</blockquote>
				<logic:present name="curriculum">
					<% if ((lastCurriculum == curriculum && (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()))
						|| curriculum.getProgram() == null || curriculum.getProgramEn() == null
						|| curriculum.getProgram().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getProgramEn().equals(org.apache.commons.lang.StringUtils.EMPTY)) { %>

						<bean:define id="url" type="java.lang.String">/editProgram.do?method=prepareEditProgram&amp;curriculumID=<bean:write name="curriculum" property="idInternal"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="button.edit"/>
						</html:link>
					<% } %>
				</logic:present>
				<logic:notPresent name="curriculum">
					<% if (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()) { %>
						<bean:define id="url" type="java.lang.String">/createProgram.do?method=prepareCreateProgram&amp;curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="button.create"/>
						</html:link>
					<% } %>
				</logic:notPresent>
		</logic:notEqual>

	</logic:iterate>

<logic:equal name="showNote" value="true">
		<p  class="mtop2"><em>* <bean:message key="label.competenceCourse.notApproved.note"/></em></p>
	</logic:equal>
	
</logic:present>