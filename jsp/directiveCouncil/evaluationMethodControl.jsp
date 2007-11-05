<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<logic:present role="DIRECTIVE_COUNCIL">

	<em><bean:message key="DIRECTIVE_COUNCIL" /></em>
	<h2><bean:message key="label.evaluationMethodControl"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	<html:messages id="message" message="true" bundle="DEFAULT">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>
	
	<fr:form action="/evaluationMethodControl.do?method=search">
		<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean1"
				 name="executionCourseWithNoEvaluationMethodSearchBean"
				 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
				 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thmiddle thlight inobullet"/>
	  			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		   	</fr:layout>	    	
		</fr:edit>
		<html:submit><bean:message key="button.submit" /></html:submit>
	</fr:form>
	
	<logic:present name="executionCourses">
		<bean:define id="executionPeriod" name="executionCourseWithNoEvaluationMethodSearchBean" property="executionPeriod"/>
		<bean:define id="degreeTypes" name="executionCourseWithNoEvaluationMethodSearchBean" property="degreeTypes"/>

	<table class="mtop15">
		<tr>
			<td>
				<fr:form action="/evaluationMethodControl.do?method=exportToCSV">
					<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean2"
							 name="executionCourseWithNoEvaluationMethodSearchBean"
							 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
							 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean.xls">
					</fr:edit>
					<html:submit><bean:message key="button.download.csv"/></html:submit>
				</fr:form>
			</td>
			<td>
				<fr:form action="/evaluationMethodControl.do?method=exportToXLS">
					<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean3"
							 name="executionCourseWithNoEvaluationMethodSearchBean"
							 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
							 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean.xls">
					</fr:edit>
					<html:submit><bean:message key="button.download.xls"/></html:submit>
				</fr:form>
			</td>
		</tr>
	</table>


		<p class="mtop15 mbottom05"><bean:message key="label.total.execution.courses"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="total"/></strong></p>
		<p class="mvert05"><bean:message key="label.execution.courses.with.evaluation.method"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withEvaluationMethod"/></strong></p>
		<p class="mtop05 mbottom05"><bean:message key="label.execution.courses.without.evaluation.method"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withoutEvaluationMethod"/></strong></p>

		<table class="tstyle4">
			<tr>
				<th>
					<bean:message key="label.executionCourse.name"/>
				</th>
				<th>
					<bean:message key="label.executionCourse.responsible.teachers"/>
				</th>
				<th>
					<bean:message key="label.executionCourse.other.teachers"/>
				</th>
				<th>
					<bean:message key="label.executionCourse.degrees"/>
				</th>
				<th>
					<bean:message key="label.executionCourse.departments"/>
				</th>
			</tr>
			<logic:iterate id="executionCourse" name="executionCourses">
				<tr>
					<td>
						<bean:write name="executionCourse" property="nome"/>
					</td>
					<td>
						<logic:iterate id="professorship" name="executionCourse" property="professorships">
							<logic:equal name="professorship" property="responsibleFor" value="true">
								<p>
									<bean:define id="email" name="professorship" property="teacher.person.email"/>
									<a href="mailto:<%= email %>">
										<bean:write name="professorship" property="teacher.person.name"/>
										(<bean:write name="professorship" property="teacher.teacherNumber"/>)
									</a>
								</p>
							</logic:equal>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="professorship" name="executionCourse" property="professorships">
							<logic:notEqual name="professorship" property="responsibleFor" value="true">
								<p>
									<bean:define id="email" name="professorship" property="teacher.person.email"/>
									<a href="mailto:<%= email %>">
										<bean:write name="professorship" property="teacher.person.name"/>
										(<bean:write name="professorship" property="teacher.teacherNumber"/>)
									</a>
								</p>
							</logic:notEqual>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="degree" indexId="i" name="executionCourse" property="degreesSortedByDegreeName">
							<logic:notEqual name="i" value="0">
								,
							</logic:notEqual>
							<bean:write name="degree" property="sigla"/>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="department" indexId="i" name="executionCourse" property="departments">
							<logic:notEqual name="i" value="0">
								,
							</logic:notEqual>
							<bean:write name="department" property="name"/>
						</logic:iterate>
					</td>
				</tr>				
			</logic:iterate>
		</table>
	</logic:present>
</logic:present>
