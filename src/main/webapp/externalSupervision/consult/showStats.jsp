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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.section.viewStudent"/></h2>

<div style="float: right;" class="printhidden">
	<bean:define id="personID" name="sessionBean" property="student.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<bean:define id="personId" name="sessionBean" property="student.externalId"/>

<table>
	<tr>
		<td>
			<fr:view name="sessionBean" property="student" schema="view-student-info">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="name,externalId"/>				
					<fr:property name="classes" value="tstyle1 thleft thlight" />
					<fr:property name="columnClasses" value=",,,,,tdclear tderror1" />
				</fr:layout>
			</fr:view>
		</td>
		
		<td style="vertical-align: top; padding-top: 1em;">
			
			<p class="mtop0 pleft1 asd">
				<span class="dblock pbottom03">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<logic:present name="noCurriculum"><logic:equal name="noCurriculum" value="true">
					<bean:message key="link.showStats.viewCurriculum" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
					</logic:equal></logic:present>
					<logic:notPresent name="noCurriculum">
					<html:link page="/viewCurriculum.do?method=prepareForSupervisor" paramName="sessionBean" paramProperty="student.externalId" paramId="personId">
						<bean:message key="link.showStats.viewCurriculum" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
					</html:link>
					</logic:notPresent>
				</span>
				<span class="dblock pbottom03">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/viewTimetable.do?method=prepareForSupervisor" paramName="sessionBean" paramProperty="student.externalId" paramId="personId">
						<bean:message key="link.showStats.viewSchedule" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
					</html:link>
				</span>
				<span class="dblock pbottom03">
						<logic:present name="hasDissertations"><logic:equal name="hasDissertations" value="true">
						<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
						<html:link page="/viewDissertation.do?method=chooseDissertation" paramName="sessionBean" paramProperty="student.externalId" paramId="personId">
							<bean:message key="link.showStats.viewDissertation" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
						</html:link>
						</logic:equal></logic:present>
				</span>
			</p>
		
		</td>

	</tr>
</table>



<!-- STUDENTS STATISTICS -->
<logic:present name="studentStatistics">
	<logic:notEmpty name="studentStatistics" >
	<h3 class="mbottom05"><bean:message key="label.student.statistics.table" bundle="APPLICATION_RESOURCES"/></h3>
			<fr:view name="studentStatistics" layout="tabular-sortable" schema="student.statistics.table" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value="nowrap acenter,acenter,acenter,acenter,acenter"/>
				<fr:property name="suffixes" value="º sem,,,%, "/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="executionPeriod,totalEnrolmentsNumber,approvedEnrolmentsNumber,approvedRatio,aritmeticAverage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/viewStudent.do?method=showStats&personId=" + personId.toString()) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "executionPeriod=desc" : request.getParameter("sortBy") %>"/>
			</fr:layout>
			</fr:view>
	</logic:notEmpty>
</logic:present>