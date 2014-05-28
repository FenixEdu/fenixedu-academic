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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- showStudentPerformanceGrid.jsp -->

<h2><bean:message key="title.tutorship.student.lowPerformance" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="<%= "/studentLowPerformance.do"%>" id="prescriptionForm">
	
	<html:hidden property="method" value="viewStudentsState"/>
  	<fr:edit  id="prescriptionBean" name="prescriptionBean" schema="tutorship.tutorate.student.lowPerformance.prescriptionBean" >
  	
  	</fr:edit>


</fr:form>
<logic:present name="job">
	<bean:define id="job" name="job" type="net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob"/>
		<div class="success0 mtop0" style="width:600px;">
			<p class="mvert05">
			<bean:message key="link.tutorship.student.lowPerformance.confirmation" bundle="PEDAGOGICAL_COUNCIL" arg0="<%= job.getDescription().toString() %>"/></p>
		</div> 
</logic:present>


<logic:notEmpty name="queueJobList">

	<h3 class="mtop15 mbottom05"><bean:message key="label.gep.latest.requests" bundle="GEP_RESOURCES" /></h3>
	
	<fr:view name="queueJobList" schema="tutorship.tutorate.student.lowPerformance.latestJobs">
    	<fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle1 mtop05" />
    		<fr:property name="columnClasses" value=",,,acenter,,,,,," />
			<fr:property name="link(Download)" value="/downloadQueuedJob.do?method=downloadFile"/>
			<fr:property name="param(Download)" value="externalId/id"/>
			<fr:property name="bundle(Download)" value="GEP_RESOURCES"/>
			<fr:property name="visibleIf(Download)" value="done"/>
			<fr:property name="module(Download)" value=""/>
			
			<fr:property name="link(sendJob)" value="/pedagogicalCouncil/studentLowPerformance.do?method=resendJob"/>
			<fr:property name="param(sendJob)" value="externalId/id"/>
			<fr:property name="key(sendJob)" value="label.sendJob"/>
			<fr:property name="bundle(sendJob)" value="GEP_RESOURCES"/>
			<fr:property name="visibleIf(sendJob)" value="isNotDoneAndCancelled"/>
			<fr:property name="module(sendJob)" value=""/>
			
			<fr:property name="link(Cancel)" value="/pedagogicalCouncil/studentLowPerformance.do?method=cancelQueuedJob"/>
			<fr:property name="param(Cancel)" value="externalId/id"/>
			<fr:property name="key(Cancel)" value="label.cancel"/>
			<fr:property name="bundle(Cancel)" value="GEP_RESOURCES"/>
			<fr:property name="visibleIf(Cancel)" value="isNotDoneAndNotCancelled"/>
			<fr:property name="module(Cancel)" value=""/>
			
	
		</fr:layout>
	</fr:view>
</logic:notEmpty>
