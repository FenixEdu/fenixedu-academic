<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html:xhtml/>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
<bean:define id="registrationOID" name="registration" property="externalId"/>

<logic:present name="executionCourse">
<ul>
	<%--
	<li class="navheader">Visualizar:</li>
	<li><span><bean:message key="label.curricular.course.name"/>: <strong><bean:write name="executionCourse" property="nome"/></strong></span></li>
	--%>
	<li>
		<bean:define id="link"><bean:message key="link.shift.enrolement.edit"/></bean:define>
		<html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;registrationOID=" + registrationOID.toString() %>"><bean:message bundle="STUDENT_RESOURCES" key="link.student.seeAllClasses" /></html:link>
	</li>
</ul>
</logic:present>

<ul>
	<li class="navheader">Turmas</li>
	
	<logic:iterate id="schoolClass" name="schoolClassesToEnrol">
	
		<bean:define id="schoolClassId" name="schoolClass" property="externalId"/>
		<bean:define id="schoolClassName" name="schoolClass" property="nome"/>
		
		<bean:define id="classSelected">
			<bean:message key="label.class"/>
		</bean:define>
		
		<li>
			<logic:present name="selectedSchoolClass" >
				<bean:define id="classIdSelected" name="selectedSchoolClass" property="externalId" />
				
				<logic:notEqual name="schoolClassId" value="<%= classIdSelected.toString() %>">		
						
					<logic:present name="executionCourse">
						<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>
						<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;registrationOID=" + registrationOID.toString()
						 + "&amp;classId=" + schoolClassId.toString() + "&amp;executionCourseID=" + executionCourseID.toString() %>">
							<bean:message key="label.class" />&nbsp;<bean:write name="schoolClass" property="nome" />		
						</html:link>
					</logic:present>
					
					<logic:notPresent name="executionCourse">
						<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;registrationOID=" + registrationOID.toString() + "&amp;classId=" + schoolClassId.toString()%>">
							<bean:message key="label.class" />&nbsp;<bean:write name="schoolClass" property="nome" />		
						</html:link>
					</logic:notPresent>					
					
				</logic:notEqual>		
						
				<logic:equal name="schoolClassId" value="<%= classIdSelected.toString() %>"> 
					<span><bean:message key="label.class" />&nbsp;<bean:write name="schoolClass" property="nome" /></span>	
				</logic:equal>
			</logic:present>
						
			<logic:notPresent name="selectedSchoolClass" >
			
				<logic:present name="executionCourse">
					
					<bean:define id="executionCourseID" name="executionCourse" property="externalId" />
					
					<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;registrationOID=" + registrationOID.toString()
					  + "&amp;classId=" + schoolClassId.toString() + "&amp;executionCourseID=" + executionCourseID.toString() %>">
						<bean:message key="label.class" />&nbsp;<bean:write name="schoolClass" property="nome" />						
					</html:link>
				</logic:present>
				
				<logic:notPresent name="executionCourse">
					<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;registrationOID=" + registrationOID.toString()
					  + "&amp;classId=" + schoolClassId.toString() %>">
						<bean:message key="label.class" />&nbsp;<bean:write name="schoolClass" property="nome" />						
					</html:link>
				</logic:notPresent>
				
			</logic:notPresent>						
		</li>
	</logic:iterate> 
	<br />
	<li><html:link page="<%="/studentShiftEnrollmentManager.do?method=start&registrationOID=" + registration.getExternalId().toString()%>"><bean:message key="button.back" /></html:link></li>
</ul>