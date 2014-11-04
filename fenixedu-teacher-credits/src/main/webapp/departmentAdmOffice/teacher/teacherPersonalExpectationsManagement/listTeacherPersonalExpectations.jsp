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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.visualize.teachers.expectations"/></h2>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">

	<logic:notEmpty name="executionYearBean">
		<fr:form action="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectationsForSelectedExecutionYear">
			<div class="mtop2 mbottom1">
			<bean:message key="label.common.executionYear"/>:
			<fr:edit id="executionYear" name="executionYearBean" slot="executionYear"> 
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:destination name="postback" path="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectationsForSelectedExecutionYear"/>
				</fr:layout>
			</fr:edit>	
			</div>
		</fr:form>
	
		<logic:notEmpty name="teachersPersonalExpectations">
		
			<p>
				<html:link page="/listTeachersPersonalExpectations.do?method=exportToExcel" paramId="executionYearId" paramName="executionYearBean" paramProperty="executionYear.externalId">
					<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
					<bean:message key="link.export.to.excel"/>						
				</html:link>&nbsp;&nbsp;&nbsp;			
				<html:link page="/listTeachersPersonalExpectations.do?method=exportToCSV" paramId="executionYearId" paramName="executionYearBean" paramProperty="executionYear.externalId">
					<html:img border="0" src="<%= request.getContextPath() + "/images/icon_csv.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
					<bean:message key="link.export.to.csv"/>						
				</html:link>
			</p>
		
			<bean:define id="executionYear" name="executionYearBean" property="executionYear" type="org.fenixedu.academic.domain.ExecutionYear"/>
			<table class="tstyle2 thleft thlight mtop15">			
				<tr>
					<th><bean:message key="label.teacher.name" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.teacher.number" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>	
					<th><bean:message key="label.teacher.category" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
					<th><bean:message key="label.teacher.expectation" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
					<th><bean:message key="label.teacher.auto.evaluation" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.teacher.evaluation" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
				</tr>
				<logic:iterate id="mapEntry" name="teachersPersonalExpectations">		
					<bean:define id="teacher" name="mapEntry" property="key" type="org.fenixedu.academic.domain.Teacher"/>				
					<tr>
						<td>
							<logic:notEmpty name="mapEntry" property="value">
								<html:link page="/listTeachersPersonalExpectations.do?method=seeTeacherPersonalExpectation" paramId="teacherPersonalExpectationID" paramName="mapEntry" paramProperty="value.externalId">
									<bean:write name="teacher" property="person.name"/>
								</html:link>
							</logic:notEmpty>
							<logic:empty name="mapEntry" property="value">					
								<bean:write name="teacher" property="person.name"/>
							</logic:empty>							
						</td>							
						<td class="acenter"><bean:write name="teacher" property="teacherId"/></td>
						<td class="acenter">
							<logic:notEmpty name="teacher" property="category">
								<bean:write name="teacher" property="category.name"/>
							</logic:notEmpty>
							<logic:empty name="teacher" property="category">
								--
							</logic:empty>
						</td>
						<td class="acenter">
							<logic:empty name="mapEntry" property="value">
								<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</logic:empty>
							<logic:notEmpty name="mapEntry" property="value">
								<bean:message key="label.yes" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</logic:notEmpty>
						</td>
						<td class="acenter">
							<logic:empty name="mapEntry" property="value">
								<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</logic:empty>
							<logic:notEmpty name="mapEntry" property="value">
								<logic:notEmpty name="mapEntry" property="value.autoEvaluation">
									<bean:message key="label.yes" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:notEmpty>
								<logic:empty name="mapEntry" property="value.autoEvaluation">
									<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:empty>	
							</logic:notEmpty>
						</td>
						<td class="acenter">
							<logic:empty name="mapEntry" property="value">
								<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</logic:empty>
							<logic:notEmpty name="mapEntry" property="value">
								<logic:notEmpty name="mapEntry" property="value.tutorComment">
									<bean:message key="label.yes" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:notEmpty>
								<logic:empty name="mapEntry" property="value.tutorComment">
									<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:empty>	
							</logic:notEmpty>						
						</td>
					</tr>																							
				</logic:iterate>			
			</table>	
		</logic:notEmpty>		
	</logic:notEmpty>
</logic:present>
