<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<script language="Javascript" type="text/javascript">
<!--

var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	if(document.forms[0].studentIDs.type=='checkbox'){
		var e = document.forms[0].studentIDs;
		e.checked = select;
	}else{
		for (var i=0; i<document.forms[0].studentIDs.length; i++){
			var e = document.forms[0].studentIDs[i];
			e.checked = select;
		}
	}
}
// -->
</script>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<strong><jsp:include page="context.jsp"/></strong>

<bean:define id="shiftName" name="<%= PresentationConstants.SHIFT %>" property="nome"/>
<bean:define id="shiftId" name="<%= PresentationConstants.SHIFT %>" property="externalId"/>
<bean:define id="shiftType" name="<%= PresentationConstants.SHIFT %>" property="shiftTypesIntegerComparator"/>

<h3>Alunos Inscritos</h3>

<p>
	<logic:present name="<%= PresentationConstants.EXECUTION_COURSE %>" scope="request">
		<bean:write name="<%= PresentationConstants.EXECUTION_COURSE %>" property="nome"/>
	</logic:present>
</p>

<p>Turno: <bean:write name="shiftName"/></p>


<logic:present name="<%= PresentationConstants.STUDENT_LIST %>" scope="request">
<html:form action="/manageShiftStudents">
	<table>
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.number"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="label.mail"/>
			</th>
			<th>
				<bean:message key="label.degree"/>
			</th>
			<th>
			</th>
		</tr>
		<logic:iterate id="shiftEnrolment" name="shift" property="shift.shiftEnrolmentsOrderedByDate">
			<bean:define id="student" name="shiftEnrolment" property="registration"/>
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentIDs" property="studentIDs"><bean:write name="student" property="externalId"/></html:multibox>
				</td>
				<td>
					<bean:write name="student" property="number"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.name"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.email"/>
				</td>
				<td>
                    <logic:present name="student" property="activeStudentCurricularPlan">
    					<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.sigla"/>
                    </logic:present>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy HH:mm:ss">
						<bean:write name="shiftEnrolment" property="createdOn.millis"/>
					</dt:format>
				</td>
			</tr>
		</logic:iterate>
	</table>


<p class="mtop2"><strong><bean:message key="title.transfer.students.shif"/></strong></p>

<p>
	<span class="info"><bean:message key="message.transfer.students.shift.notice"/></span>
</p>

<logic:present name="<%= PresentationConstants.SHIFTS %>" scope="request">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentsShift"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldShiftId" property="oldShiftId" value="<%= pageContext.findAttribute("shiftId").toString() %>"/>

        <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                     value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<th>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="otherShift" name="<%= PresentationConstants.SHIFTS %>">
				<logic:notEqual name="otherShift" property="nome" value="<%= pageContext.findAttribute("shiftName").toString() %>">
					<bean:define id="otherShiftId" name="otherShift" property="externalId"/>
					<bean:define id="otherShiftType" name="otherShift" property="shiftTypesIntegerComparator"/>
					<logic:equal name="shiftType" value="<%= pageContext.findAttribute("otherShiftType").toString() %>">
						<tr>
							<td>
								<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.newShiftId" property="newShiftId" value="<%= pageContext.findAttribute("otherShiftId").toString() %>"/>
							</td>
							<td>
								<bean:write name="otherShift" property="nome"/>
							</td>
						</tr>
					</logic:equal>
				</logic:notEqual>
			</logic:iterate>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.transfer"/></html:submit>
		</p>
</logic:present>

	</html:form> 
</logic:present>

<logic:notPresent name="<%= PresentationConstants.STUDENT_LIST %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.students.none.in.shift"/></span>	
	</p>
</logic:notPresent>