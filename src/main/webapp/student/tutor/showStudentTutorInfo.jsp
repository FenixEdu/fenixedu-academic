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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml />

<h2><bean:message key="label.student.tutorship.tutorshipInfoTitle" bundle="APPLICATION_RESOURCES"/></h2>

<!-- AVISOS E ERROS -->
<p>
	<span class="error0"><html:errors /></span>
</p>


<ul class="mbottom15">
	<li>
	    <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="http://tutorado.ist.utl.pt/" target="_blank">
            <bean:message key="link.teacher.tutorship.gepTutorshipPage" bundle="APPLICATION_RESOURCES"/>
	    </html:link>
	</li>
</ul>

<!-- ACTUAL TUTOR INFORMATION -->
<logic:present name="actualTutor">
	<logic:notEmpty name="actualTutor" >
		<h3 class="mbottom05"><bean:message key="label.student.tutorship.actualTutorInfo" bundle="APPLICATION_RESOURCES"/></h3>
		<%-- Foto --%>
		<div style="float: right;" class="printhidden">
			<bean:define id="personId" name="personID" />
			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personId.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
		</div>
		<fr:view name="actualTutor" layout="tabular" schema="student.tutorship.tutorInfo" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
				<fr:property name="rowClasses" value="bold,,,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="actualTutor" >
	<p class="mvert2"><em><bean:message key="label.student.tutorship.noActualTutor" bundle="APPLICATION_RESOURCES"/>.</em></p>
</logic:notPresent>


<!-- PAST TUTORS INFORMATION -->
<logic:present name="pastTutors">
	<logic:notEmpty name="pastTutors" >
	<h3 class="mbottom05"><bean:message key="label.student.tutorship.pastTutorsInfo" bundle="APPLICATION_RESOURCES"/></h3>
		<logic:iterate id="pastTutorsId" name="pastTutors">
			<fr:view name="pastTutorsId" layout="tabular" schema="student.tutorship.tutorInfo" >
				<fr:layout>
					<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
				</fr:layout>
			</fr:view>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<!-- STUDENTS STATISTICS -->
<logic:present name="studentStatistics">
	<logic:notEmpty name="studentStatistics" >
	<h3 class="mbottom05"><bean:message key="label.student.statistics.table" bundle="APPLICATION_RESOURCES"/></h3>
			<fr:view name="studentStatistics" layout="tabular-sortable" schema="student.statistics.table" >
				<%-- <fr:layout>
					<fr:property name="classes" value="tstyle2 thlight thright"/>
				</fr:layout>--%>
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value="nowrap acenter,acenter,acenter,acenter,acenter"/>
				<fr:property name="suffixes" value="º sem,,,%, "/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="executionPeriod,totalEnrolmentsNumber,approvedEnrolmentsNumber,approvedRatio,aritmeticAverage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/viewTutorInfo.do?method=prepare") %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "executionPeriod" : request.getParameter("sortBy") %>"/>
			</fr:layout>
			</fr:view>
	</logic:notEmpty>
</logic:present>
