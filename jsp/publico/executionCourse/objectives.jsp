<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="link.objectives"/>
</h2>

<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionPeriod"/>

<logic:iterate id="entry" name="executionCourse" property="curricularCoursesIndexedByCompetenceCourse">
	<bean:define id="competenceCourse" name="entry" property="key"/>
	<logic:equal name="competenceCourse" property="curricularStage.name" value="APPROVED">
		<h3>
			<fr:view name="competenceCourse" property="nameI18N"/>
			<br/>
			<logic:iterate id="curricularCourse" name="entry" property="value" indexId="i">
				<logic:notEqual name="i" value="0"><br/></logic:notEqual>
				<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
				<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
				<bean:message key="label.in"/>
				<fr:view name="degree" property="nameI18N"/>
			</logic:iterate>
		</h3>
		<blockquote>
			<h4>
				<bean:message key="label.generalObjectives"/>
			</h4>
			<logic:present name="competenceCourse" property="objectives">
				<fr:view name="competenceCourse" property="objectivesI18N">
					<fr:layout>
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="false" />
					</fr:layout>
				</fr:view>
			</logic:present>
		</blockquote>
	</logic:equal>
</logic:iterate>

	<logic:iterate id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"
			name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
		<logic:notEqual name="curricularCourse" property="isBolonha" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>

				<h3 class="mtop2">
					<%-- <fr:view name="curricularCourse" property="nameI18N"/> --%>
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<fr:view name="degree" property="nameI18N"/>
				</h3>

					<logic:present name="curriculum">
						<h4 class="mbottom05 greytxt">
							<bean:message key="label.generalObjectives"/>
						</h4>
						<fr:view name="curriculum" property="generalObjectivesI18N">
							<fr:layout name="html">
								<fr:property name="escaped" value="false" />
							</fr:layout>
						</fr:view>
						<logic:notEmpty name="curriculum" property="operacionalObjectives">
							<h4 class="mbottom05 greytxt">
								<bean:message key="label.operacionalObjectives"/>
							</h4>
							<div class="mtop05" style="line-height: 1.5em;">
							<fr:view name="curriculum" property="operacionalObjectivesI18N">
								<fr:layout name="html">
									<fr:property name="escaped" value="false" />
									<fr:property name="newlineAware" value="false" />
								</fr:layout>
							</fr:view>
							</div>
						</logic:notEmpty>
					</logic:present>
					<logic:notPresent name="curriculum">
						<p><em><bean:message key="message.objectives.not.defined"/></em></p>
					</logic:notPresent>

		</logic:notEqual>
	</logic:iterate>

<%--
<h3 class="mtop2">Licenciatura (5 anos) em Engenharia Informática e de Computadores - Alameda</h3>
<h4 class="mbottom05 greytxt">Objectivos Gerais</h4>
<p class="mtop05">Vivamus bibendum massa vel pede. Mauris quis felis quis diam luctus consectetuer. Sed ullamcorper eleifend arcu. Nulla facilisi. Suspendisse tincidunt rhoncus ipsum. Etiam in dui. Mauris molestie arcu eget diam pretium laoreet. Phasellus hendrerit bibendum nisl. Mauris odio. Nulla facilisi. Proin rhoncus. Nulla convallis augue sit amet urna. Proin tempor, elit sit amet viverra blandit, elit mauris consectetuer neque, nec rhoncus lorem felis vitae dolor.</p>
<h4 class="mbottom05 greytxt">Objectivos Operacionais</h4>
<p class="mtop05">Vivamus bibendum massa vel pede. Mauris quis felis quis diam luctus consectetuer. Sed ullamcorper eleifend arcu. Nulla facilisi. Suspendisse tincidunt rhoncus ipsum. Etiam in dui. Mauris molestie arcu eget diam pretium laoreet. Phasellus hendrerit bibendum nisl. Mauris odio. Nulla facilisi. Proin rhoncus. Nulla convallis augue sit amet urna. Proin tempor, elit sit amet viverra blandit, elit mauris consectetuer neque, nec rhoncus lorem felis vitae dolor.</p>
--%>
	