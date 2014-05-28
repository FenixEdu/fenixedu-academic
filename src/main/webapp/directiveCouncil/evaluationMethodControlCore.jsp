<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<%-- 
 This is a JSP to be only included, should never be refered directly
--%>

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


		<p class="mtop15 mbottom05"><bean:message key="label.total.execution.courses"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="total"/></strong></p>
		<p class="mvert05"><bean:message key="label.execution.courses.with.evaluation.method"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withEvaluationMethod"/></strong></p>
		<p class="mtop05 mbottom05"><bean:message key="label.execution.courses.without.evaluation.method"/>: <strong><bean:write name="executionCourseWithNoEvaluationMethodSearchBean" property="withoutEvaluationMethod"/></strong></p>

<h3><bean:message key="label.execution.courses.without.evaluation.method"/></h3>

	<table>
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
						<logic:present name="executionCourse" property="site">
							<bean:define id="executionCourse" name="executionCourse" toScope="request"/>
							<app:contentLink name="executionCourse" property="site" target="_blank">
								<bean:write name="executionCourse" property="nome"/>
							</app:contentLink>
						</logic:present>
						<logic:notPresent name="executionCourse" property="site">
							<bean:write name="executionCourse" property="nome"/>
						</logic:notPresent>
					</td>
					<td>
						<logic:iterate id="professorship" name="executionCourse" property="professorships">
							<logic:equal name="professorship" property="responsibleFor" value="true">
								<p>
									<bean:define id="email" name="professorship" property="person.email"/>
									<a href="mailto:<%= email %>">
										<bean:write name="professorship" property="person.name"/>
										(<bean:write name="professorship" property="person.istUsername"/>)
									</a>
								</p>
							</logic:equal>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="professorship" name="executionCourse" property="professorships">
							<logic:notEqual name="professorship" property="responsibleFor" value="true">
								<p>
									<bean:define id="email" name="professorship" property="person.email"/>
									<a href="mailto:<%= email %>">
										<bean:write name="professorship" property="person.name"/>
										(<bean:write name="professorship" property="person.istUsername"/>)
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
