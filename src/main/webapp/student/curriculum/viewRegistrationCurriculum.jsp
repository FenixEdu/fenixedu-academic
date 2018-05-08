<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>
<%@page import="org.fenixedu.academic.domain.student.Registration"%>
<%@page import="org.fenixedu.academic.domain.student.curriculum.ICurriculum"%>
<%@page import="org.fenixedu.academic.dto.student.RegistrationCurriculumBean"%>
<html:xhtml />

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="registrationCurriculumBean" name="registrationCurriculumBean" type="org.fenixedu.academic.dto.student.RegistrationCurriculumBean"/>
<%
	final Registration registration = ((RegistrationCurriculumBean) registrationCurriculumBean).getRegistration();
	request.setAttribute("registration", registration);

	ICurriculum curriculum = registrationCurriculumBean.getCurriculum();
	request.setAttribute("curriculum", curriculum);

    // rawGrade
	request.setAttribute("rawGrade", curriculum.getRawGrade());
	
	// curricular year
	request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());
	curriculum = registrationCurriculumBean.getCurriculum(registrationCurriculumBean.getExecutionYear());
	
	final BigDecimal sumEctsCredits = curriculum.getSumEctsCredits();
	request.setAttribute("sumEctsCredits", sumEctsCredits);
		
	final Integer curricularYear = curriculum.getCurricularYear();
	request.setAttribute("curricularYear", curricularYear);
%>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:equal name="curriculum" property="empty" value="true">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:equal name="curriculum" property="empty" value="false">
		
	<logic:empty name="executionYear">
    	<div class="panel panel-default" style="margin-top: 10px">
  			<div class="panel-heading">
    			<h3 class="panel-title"><strong><bean:message key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h3>
  			</div>
  			<div class="panel-body">
				<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
					<p><b><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></b></p>
					<p><bean:write name="registrationCurriculumBean" property="finalGrade.value"/></p>
					<p><b><bean:message key="conclusion.date" bundle="ACADEMIC_OFFICE_RESOURCES"/></b></p>
					<p><bean:write name="registrationCurriculumBean" property="conclusionDate"/></p>
				</logic:equal>
				<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
					<div class="alert alert-warning" role="alert"><bean:message key="registration.not.submitted.to.conclusion.process" bundle="ACADEMIC_OFFICE_RESOURCES"/></div>
				</logic:equal>
			</div>
		</div>
	</logic:empty>

	<h3><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<h4><bean:message key="label.summary" bundle="ACADEMIC_OFFICE_RESOURCES"/></h4>
    <table class="tstyle4 thlight tdcenter mtop15">
        <tr>
            <th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
            <th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
            <th><bean:message key="average" bundle="STUDENT_RESOURCES"/></th>
            <th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
        </tr>
        <tr>
            <bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>
            <td><bean:write name="curricularEntriesCount"/></td>
            <td><bean:write name="sumEctsCredits"/></td>
            <logic:notEmpty name="executionYear">
                <td><bean:write name="rawGrade" property="value"/></td>
                <td><bean:write name="curricularYear"/></td>
            </logic:notEmpty>
            <logic:empty name="executionYear">
                <logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
                    <td><bean:write name="rawGrade" property="value"/></td>
                    <td><bean:write name="curricularYear"/></td>
                </logic:equal>
                <logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
                    <td><bean:write name="registrationCurriculumBean" property="finalGrade.value"/></td>
                    <td>-</td>
                </logic:equal>          
            </logic:empty>
        </tr>
    </table>

	<logic:present name="visibleEctsConvertedGrade">
		<h4><bean:message key="label.gradeConversionTable" bundle="ACADEMIC_OFFICE_RESOURCES"/></h4>
	    <table class="tstyle4 thlight tdcenter mtop15">
		    <tr>
		        <th><bean:message key="diploma.supplement.four.four.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
		        <td>10</td>
		        <td>11</td>
		        <td>12</td>
		        <td>13</td>
		        <td>14</td>
		        <td>15</td>
		        <td>16</td>
		        <td>17</td>
		        <td>18</td>
		        <td>19</td>
		        <td>20</td>
		    </tr>
	    	<tr>
		    	<th>%</th>
		   		<logic:iterate name = "ectsGradePercentagesTable" id="percentage">	
		    		<td class="width50px"><bean:write name="percentage"/></td>
		    	</logic:iterate>
	    	</tr>
		    <tr>
		    	<th><bean:message key="diploma.supplement.four.four.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
	    		<logic:iterate name = "ectsGradeConversionTable" id="convert">
		    		<td><bean:write name="convert"/></td>
		    	</logic:iterate>
		    </tr>
		</table>
	</logic:present>

		<p>
			<h4><bean:message key="label.grades" bundle="ACADEMIC_OFFICE_RESOURCES"/></h4>
			<fr:view name="curriculum">
				<logic:present name="ectsGradeConversionTable">
					<fr:layout> <fr:property name="visibleEctsConvertedGrade" value="false"/> </fr:layout>
				</logic:present>                              
			</fr:view>
		</p>
	
</logic:equal>
