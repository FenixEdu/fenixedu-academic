<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<em><bean:message key="label.teacher.tutor.operations" /></em>
<h2><bean:message key="label.teacher.tutor.edit.students"/></h2>

<logic:messagesPresent message="true">
	<ul class="list7 mtop2 warning0" style="list-style: none;">
		<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
			<li>
				<span><!-- Error messages go here --><bean:write name="message" /></span>
			</li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<logic:present name="student">
	<logic:notEmpty name="student">
		<table class="tstyle1 thlight tdcenter">
			<tr>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.name" /></th>
				<td><bean:write name="student" property="name"/></td>
			</tr><tr>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.number" /></th>
				<td><bean:write name="student" property="number"/></td>
			</tr><tr>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.phone" /></th>
				<td><bean:write name="student" property="person.defaultPhoneNumber"/></td>
			</tr><tr>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.email" /></th>
				<td>
					<bean:define id="mail" name="student" property="person.institutionalOrDefaultEmailAddressValue"/>
					<html:link href="<%= "mailto:"+ mail %>">
						<bean:write name="student" property="person.institutionalOrDefaultEmailAddressValue"/>
					</html:link>
				</td>
			</tr>
		</table>
		<fr:edit id="kkkkk"
					name="tutorshipLog"
					type="net.sourceforge.fenixedu.domain.TutorshipLog"
					schema="teacher.student.tutorship.log"
					action="/viewStudentsByTutor.do?method=viewStudentsByTutor">
			<fr:layout>
	    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
	        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		    </fr:layout>
		    <fr:destination name="invalid"/>
		</fr:edit>
	</logic:notEmpty>
	<logic:empty name="student"><bean:message key="error.tutor.noStudent" /></logic:empty>
</logic:present>
<logic:notPresent name="student"><bean:message key="error.tutor.noStudent" /></logic:notPresent>