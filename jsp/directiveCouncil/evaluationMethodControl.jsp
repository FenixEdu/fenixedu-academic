<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<logic:present role="DIRECTIVE_COUNCIL">
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<html:messages id="message" message="true" bundle="DEFAULT">
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</html:messages>

	<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean1" action="/evaluationMethodControl.do?method=search"
			 name="executionCourseWithNoEvaluationMethodSearchBean"
			 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
			 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
  			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	   	</fr:layout>	    	
	</fr:edit>
	<logic:present name="executionCourses">
		<bean:define id="executionPeriod" name="executionCourseWithNoEvaluationMethodSearchBean" property="executionPeriod"/>
		<bean:define id="degreeTypes" name="executionCourseWithNoEvaluationMethodSearchBean" property="degreeTypes"/>

		<fr:form action="/evaluationMethodControl.do?method=exportToCSV">
			<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean2"
					 name="executionCourseWithNoEvaluationMethodSearchBean"
					 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
					 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean.xls">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
  					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			   	</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.download.csv"/></html:submit>
		</fr:form>

		<fr:form action="/evaluationMethodControl.do?method=exportToXLS">
			<fr:edit id="executionCourseWithNoEvaluationMethodSearchBean3"
					 name="executionCourseWithNoEvaluationMethodSearchBean"
					 type="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean"
					 schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean.xls">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
  					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			   	</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.download.xls"/></html:submit>
		</fr:form>

		<br/>
		<bean:message key="label.total.execution.courses"/>: <bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="total"/>
		<br/>
		<bean:message key="label.execution.courses.with.evaluation.method"/>: <bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withEvaluationMethod"/>
		<br/>
		<bean:message key="label.execution.courses.without.evaluation.method"/>: <bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withoutEvaluationMethod"/>
		<br/>
		<table>
			<tr>
				<th  class="listClasses-header">
					<bean:message key="label.executionCourse.name"/>
				</th>
				<th  class="listClasses-header">
					<bean:message key="label.executionCourse.responsible.teachers"/>
				</th>
				<th  class="listClasses-header">
					<bean:message key="label.executionCourse.other.teachers"/>
				</th>
				<th  class="listClasses-header">
					<bean:message key="label.executionCourse.degrees"/>
				</th>
			</tr>
			<logic:iterate id="executionCourse" name="executionCourses">
				<tr>
					<td  class="listClasses">
						<bean:write name="executionCourse" property="nome"/>
					</td>
					<td  class="listClasses">
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
					<td  class="listClasses">
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
					<td  class="listClasses">
						<logic:iterate id="degree" indexId="i" name="executionCourse" property="degreesSortedByDegreeName">
							<logic:notEqual name="i" value="0">
								,
							</logic:notEqual>
							<bean:write name="degree" property="sigla"/>
						</logic:iterate>
					</td>
				</tr>				
			</logic:iterate>
		</table>
	</logic:present>
</logic:present>
