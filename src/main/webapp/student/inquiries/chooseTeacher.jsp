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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="studentInquiry" property="inquiriesRegistry.registration.degree.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="studentInquiry" property="inquiriesRegistry.executionCourse.nome" /></td>
	</tr>
</table>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers" bundle="INQUIRIES_RESOURCES"/></span></h3>

<bean:message key="message.inquiries.fillOptionallyOnlyAttendedClasses" bundle="INQUIRIES_RESOURCES"/>

<table class="tstyle1 thlight mtop05">
	<tr>
		<th></th>
		<th><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>	
		<th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
	</tr>
	<logic:iterate id="teacherInquiry" name="studentInquiry" property="teachersInquiries">
			<tr>
				<td class="acenter">
					<logic:present name="teacherInquiry" property="key.personID">
						<bean:define id="personID" name="teacherInquiry" property="key.personID"/>
						<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
					</logic:present>					
				</td>
				<td>				
					<bean:write name="teacherInquiry" property="key.name"/>
				</td>
				<td>
					<table class="tstyle2 thwhite thleft tdleft width100">					
						<logic:iterate id="teacherInquiryByShift" name="teacherInquiry" property="value">
							<tr>
								<th style="width: 80px;">
									<bean:message name="teacherInquiryByShift" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/>
								</th>
								<td>
									<fr:form action="/studentInquiry.do?method=showTeachersInquiries1stPage">
										<fr:edit name="teacherInquiryByShift" id="teacherInquiry" visible="false"/>
										<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
										<html:submit><bean:message key="link.inquiries.answer" bundle="INQUIRIES_RESOURCES"/></html:submit>
									</fr:form>								
								</td>
								<td>
									<c:if test="${teacherInquiryByShift.filled}"><span class="success0 smalltxt"><bean:message key="label.filled" bundle="INQUIRIES_RESOURCES"/></span></c:if>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
	</logic:iterate>
</table>

<p class="mtop2"><bean:message key="message.inquiries.submitInquiryWhenFinnish" bundle="INQUIRIES_RESOURCES"/></p>

<p class="mbottom0">
	<div class="forminline dinline">
		<fr:form action="/studentInquiry.do?method=showInquiries2ndPage">
			<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
			<html:submit><bean:message key="button.backWithArrow" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>	
		<fr:form action="/studentInquiry.do">
			<html:hidden property="method" value="showPreview"/>
			<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
			<html:submit><bean:message key="button.submitInquiry" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
	</div>
</p>
