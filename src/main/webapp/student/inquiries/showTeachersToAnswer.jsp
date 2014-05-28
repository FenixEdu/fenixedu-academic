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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiryBean" %>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries.student" bundle="INQUIRIES_RESOURCES"/></h2>

<h3 class="separator2 mtop2">
	<span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers" bundle="INQUIRIES_RESOURCES"/>:</span>
	<b><bean:write name="inquiryBean" property="inquiryRegistry.executionCourse.nome" /></b>
</h3>

<logic:notEmpty name="inquiryBean" property="teachersInquiries">
	<p class="mtop2"><bean:message key="message.inquiries.atentionBeforeFillInTeachers" bundle="INQUIRIES_RESOURCES"/>:</p>
	<ul class="mbottom15">
		<li><bean:message key="message.inquiries.answerYourTeacher" bundle="INQUIRIES_RESOURCES"/></li>
		<li><bean:message key="message.inquiries.answerYourShiftTypes" bundle="INQUIRIES_RESOURCES"/></li>
	</ul>
	
	<table class="tstyle1 thlight mtop05">
		<tr>
			<th><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>	
			<th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
		</tr>
		<logic:iterate id="teacherDto" name="inquiryBean" property="orderedTeachers">
			<% 
				StudentInquiryBean inquiryBean = (StudentInquiryBean)request.getAttribute("inquiryBean");					
				request.setAttribute("shifts",inquiryBean.getTeachersInquiries().get(teacherDto));
			%>
			<tr>
				<td class="aleft nowrap">
					<logic:present name="teacherDto" property="personID">
						<bean:define id="personID" name="teacherDto" property="personID"/>
						<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
					</logic:present>												
					<bean:write name="teacherDto" property="name"/>
				</td>
				<td>
					<table class="tstyle2 thwhite thleft tdleft width100">					
						<logic:iterate id="teacherInquiryByShift" name="shifts">
							<tr>
								<th style="width: 80px;">
									<bean:message name="teacherInquiryByShift" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/>
								</th>
								<td>
									<fr:form action="/studentInquiry.do?method=showTeacherInquiry">
										<fr:edit name="teacherInquiryByShift" id="teacherInquiry" visible="false"/>
										<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
										<logic:equal name="teacherInquiryByShift" property="filled" value="true">
											<html:submit><bean:message key="button.inquiries.edit.evaluation" bundle="INQUIRIES_RESOURCES"/></html:submit>
										</logic:equal>
										<logic:equal name="teacherInquiryByShift" property="filled" value="false">
											<html:submit><bean:message key="link.inquiries.answer" bundle="INQUIRIES_RESOURCES"/></html:submit>
										</logic:equal>
									</fr:form>								
								</td>
								<td>
									<logic:equal name="teacherInquiryByShift" property="filled" value="true">
										<span class="success0 smalltxt"><bean:message key="label.filled" bundle="INQUIRIES_RESOURCES"/></span>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<p class="mtop2"><bean:message key="message.inquiries.submitInquiryWhenFinnish" bundle="INQUIRIES_RESOURCES"/></p>
</logic:notEmpty>

<logic:empty name="inquiryBean" property="teachersInquiries">
	<p class="mvert15">
		<bean:message key="message.inquiries.noTeachersToAnswer" bundle="INQUIRIES_RESOURCES"/>		
	</p>
	<p class="mtop1 mbottom2">
		<bean:message key="message.inquiries.answersNotLinkedToStudent" bundle="INQUIRIES_RESOURCES"/>		
	</p> 
</logic:empty>

<p class="mbottom0">
	<div class="forminline dinline mbottom0">
		<logic:notPresent name="directTeachers">
			<fr:form action="/studentInquiry.do?method=showCurricularInquiry">
				<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
				<html:submit><bean:message key="button.backWithArrow" bundle="INQUIRIES_RESOURCES"/></html:submit>
			</fr:form>
		</logic:notPresent>
		<fr:form action="/studentInquiry.do?method=confirm">
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
			<html:submit><bean:message key="button.submitInquiry" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
	</div>
</p>
