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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.rectificationHistoric"/></h2>

<fr:view name="enrolmentEvaluation" property="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h3><bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.number"/>)</h3>

<html:errors/>
<logic:messagesPresent message="true">
	<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<logic:present name="enrolmentEvaluation">
	<table class="tstyle4">
	  <tr>
	    <td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.original.grade"/></td>
	    <td><bean:write name="enrolmentEvaluation" property="gradeValue"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="enrolmentEvaluation" property="examDate.time"/></dt:format>)</td>
	  </tr>
	  <logic:iterate id="evaluation" name="rectificationEvaluations">
		  <tr>
		    <td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.rectification"/></td>
		    <td>
		    	<bean:write name="evaluation" property="gradeValue"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="examDate.time"/></dt:format>)<br/>
  			    	<logic:notEmpty name="evaluation" property="markSheet.reason">
	 				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.reason"/>:
			    	<bean:write name="evaluation" property="markSheet.reason"/>
		    	</logic:notEmpty>
		    </td>
		  </tr>  
	  </logic:iterate>
	</table>
	
	<html:form action="/rectifyMarkSheet.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareRectifyMarkSheet" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />			
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />
		
		<bean:define id="markSheetID" name="enrolmentEvaluation" property="markSheet.externalId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" property="msID" value="<%= markSheetID.toString() %>"/>
		<br/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/></html:submit>
	</html:form>
	
</logic:present>
