<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType" %>
<%@ page import="net.sourceforge.fenixedu.util.AttendacyStateSelectionType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoDegree" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGrouping" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoForReadStudentsWithAttendsByExecutionCourse" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>

<script language="Javascript" type="text/javascript">
<!--
var selectDegree = true;
var selectShift = true;

function invertSelect(checkboxes){
	for (var i=1; i<checkboxes.length; i++){
		var e = checkboxes[i];
		if (checkboxes[0].checked == true) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect(checkboxes) {
	var allSelected=true;

	for (var i=1; i<checkboxes.length; i++){
		var e = checkboxes[i];
		allSelected = allSelected && (e.checked == true);
	}
	
	checkboxes[0].checked = allSelected;
}
// -->
</script>


<bean:define id="studentsComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoForReadStudentsWithAttendsByExecutionCourse"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>
<bean:define id="classTypes" type="java.util.List" name="studentsComponent" property="classTypes" />
<bean:define id="groupsProperties"	type="List" name="studentsComponent" property="infoGroupProperties"/>
<bean:define id="executionCourse"	type="InfoExecutionCourse" name="studentsComponent" property="infoExecutionCourse"/>
<bean:define id="attendsSummary"	type="InfoAttendsSummary" name="studentsComponent" property="infoAttendsSummary"/>

<h2>Alunos de <bean:write name="commonComponent" property="executionCourse.nome" /></h2>

    <span class="error"><!-- Error messages go here --><html:errors /></span>
	<bean:size id="studentsListSize" name="studentsComponent" property="infoAttends"/>


<div class="infoop5">
	<bean:message key="message.students.explanation"/>
</div>


<html:form action="/studentsByCurricularCourse.do" method="post">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readStudents"/>


<table class="tstyle1 mtop15 tdtop">
	<tr>
		<th>
			<bean:message key="label.selectStudents"/>
		</th>
		<th>
			<bean:message key="label.attends.courses"/>
		</th>
		<th>
			<bean:message key="label.selectShift"/>
		</th>
	</tr>
	
	<tr>
		<td>
			<p>	
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.ALL.toString() %>" onclick="invertSelect(document.forms[0].enrollmentType)" />
			<bean:message key="label.attends.allStudents"/>
			</p>
			<p>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.ENROLLED.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
			<bean:message key="label.attends.enrolledStudents"/>
			</p>
			<p>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.IMPROVEMENT.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
			<bean:message key="label.attends.improvementStudents"/>
			</p>
			<p>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.NOT_ENROLLED.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
			<bean:message key="label.attends.notEnrolledStudents"/>
			</p>
			<p>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.SPECIAL_SEASON.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
			<bean:message key="label.attends.specialSeason"/>
			</p>
		</td>
		<td>
			<bean:define id="degreeCurricularPlans" name="studentsComponent" property="infoDegreeCurricularPlans"/>
				<p>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coursesIDs" property="coursesIDs" value="0" onclick="invertSelect(document.forms[0].coursesIDs)"/>
					<bean:message key="label.attends.allCourses"/>
				</p>
		
				<logic:iterate id="dcp" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan" name="degreeCurricularPlans">
					<bean:define id="dcpID" name="dcp" property="idInternal"/>
					<bean:define id="degree" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree" name="dcp" property="infoDegree"/>
					<p>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coursesIDs" property="coursesIDs" value="<%= dcpID.toString() %>" onclick="cleanSelect(document.forms[0].coursesIDs)"/>
						<bean:write name="dcp" property="name"/>
					</p>
				</logic:iterate>
			
		</td>
		<td>
			<p>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIDs" property="shiftIDs" value="0" onclick="invertSelect(document.forms[0].shiftIDs)"/>
				<bean:message key="label.attends.allShifts"/>
			</p>
				<bean:define id="shifts" name="studentsComponent" property="infoShifts" />
				<logic:iterate type="net.sourceforge.fenixedu.dataTransferObject.InfoShift" name="shifts" id="shift">
					<p>
							<%
								String text = new String();
								text += shift.getNome();
								text += "   (";
								for (Iterator iterator= shift.getInfoLessons().iterator(); iterator.hasNext();)
								{
									InfoLesson lesson= (InfoLesson) iterator.next();
									text += lesson.getDiaSemana().toString() + "  ";
									text += lesson.getInicio().get(Calendar.HOUR_OF_DAY) + ":";
									text += lesson.getInicio().get(Calendar.MINUTE) + "-";
									text += lesson.getFim().get(Calendar.HOUR_OF_DAY) + ":";
									text += lesson.getFim().get(Calendar.MINUTE) + " ";
									if(lesson.getInfoSala() != null){
										text += lesson.getInfoSala().getNome();
									}
									if (iterator.hasNext())
										text += " ;";
								}
								text += ")"; 
							%>

							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIDs" property="shiftIDs" value="<%= shift.getIdInternal().toString() %>"
								onclick="cleanSelect(document.forms[0].shiftIDs)"/>

							<%= text %>
					</p>
				</logic:iterate>
		</td>
	</tr>
	
	<tr>
		<td colspan="3">
			<bean:message key="label.viewPhoto" />
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewPhoto"  property="viewPhoto" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" property="submition"><bean:message key="button.selectShift"/></html:submit>
		</td>
	</tr>
</table>	



<p>
	<h3><%=studentsComponent.getInfoAttends().size()%> <bean:message key="message.attendingStudents"/></h3>
	<p>
		<html:link page="/mailSender.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/>
		</html:link>
	</p>
	<p>
		<html:link page="/getTabSeparatedStudentList.do" name="spreadSheetLinkArgs">
			<bean:message key="link.getExcelSpreadSheet"/><br/>
		</html:link>
	</p>
</p>



<table class="tstyle1 tdcenter">
	<tr>
     	   <bean:define id="colspan" value="1"/>
     	   <bean:define id="rowspan" value="2"/>
			
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
				<bean:size id="colspanAux" name="groupsProperties"/>
				<bean:define id="colspan">
					<bean:write name="colspanAux"/>
				</bean:define>
			</logic:notEmpty>
		   </logic:present>

			<logic:equal name="viewPhoto" value="true">
				<th rowspan="<%= rowspan.toString() %>">
					<bean:message key="label.photo" /> 
			   </th>
			 </logic:equal>
			<th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.number" /> 
		   </th>
			<th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.numberOfEnrollments" />
		   </th>
		   <th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.attends.enrollmentState"/>
		   </th>
			<th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.Degree" /> 
		   </th>
			<th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.name" />
		   </th>
		   
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
					<th colspan="<%= colspan.toString() %>">
						<bean:message key="label.projectGroup"/>
					</th>
			</logic:notEmpty>
		   </logic:present>
	   
			<th rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.mail" />
		   </th>
		   <% int shiftColSpan=((List)classTypes).size(); %>
         <% Integer shiftColSpanInteger = new Integer(shiftColSpan); %>
         <% if (shiftColSpan>0) {%>
		 <th colspan="<%= shiftColSpanInteger %>" >
                <bean:message key="label.attends.shifts"/>
            </th>
        <%		}%>
       
		</tr>    		
		<tr>
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
				<logic:iterate name="groupsProperties" id="gp" type="net.sourceforge.fenixedu.dataTransferObject.InfoGrouping">
					<th>
					<bean:write name="gp" property="name"/>
					</th>
				</logic:iterate>
			</logic:notEmpty>
		   </logic:present>
		   
			<logic:iterate id="classType" type="net.sourceforge.fenixedu.domain.ShiftType" name="classTypes">
			  <th >
				<bean:define id="classTypeInt" name="classType" property="name"/>

				<logic:equal name="classTypeInt" value='<%= ShiftType.TEORICA.toString()  %>' >
					<bean:message key="label.attends.shifts.theoretical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.PRATICA.toString() %>' >
					<bean:message key="label.attends.shifts.practical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.TEORICO_PRATICA.toString() %>' >
					<bean:message key="label.attends.shifts.theo-practical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.LABORATORIAL.toString() %>' >
					<bean:message key="label.attends.shifts.laboratory"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.SEMINARY.toString() %>' >
					<bean:message key="label.attends.shifts.seminary"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.PROBLEMS.toString() %>' >
					<bean:message key="label.attends.shifts.problems"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.FIELD_WORK.toString() %>' >
					<bean:message key="label.attends.shifts.fieldwork"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.TRAINING_PERIOD.toString() %>' >
					<bean:message key="label.attends.shifts.trainingperiod"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= ShiftType.TUTORIAL_ORIENTATION.toString() %>' >
					<bean:message key="label.attends.shifts.tutorialorientation"/>
				</logic:equal>
        	  </th>
          	</logic:iterate>
         
		</tr>    		

		<bean:define id="attendacies" name="studentsComponent" property="infoAttends"/>

    	<logic:iterate id="attend" type="net.sourceforge.fenixedu.dataTransferObject.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups" name="attendacies"> 
			<bean:define id="attendacy" type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta" name="attend" property="infoAttends"/>
			<tr>
				<logic:equal name="viewPhoto" value="true">
					<td>
						<bean:define id="aluno" name="attendacy" property="aluno"/>
						<bean:define id="infoPerson" name="aluno" property="infoPerson"/>			
						<bean:define id="personID" name="infoPerson" property="idInternal"/>
		      			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
					</td>
				</logic:equal>
				<td>
					<bean:write name="attendacy" property="aluno.number"/>&nbsp;
				</td>
				<td>
					<bean:define id="numEnrollments" name="attend" property="numberOfEnrollments"/>
					
					<logic:notEqual value="0" name="numEnrollments" >
						<bean:write name="numEnrollments"/>
					</logic:notEqual>
					<logic:equal value="0" name="numEnrollments" >
						<bean:message key="label.null"/>
					</logic:equal>
		
				</td>
				<td>

					<logic:present name="attendacy" property="enrolmentEvaluationType">
						<bean:define id="enrollmentEvaluationType" type="EnrolmentEvaluationType" 
							name="attendacy" property="enrolmentEvaluationType"/>
						
						<logic:equal name="enrollmentEvaluationType" value='<%= EnrolmentEvaluationType.NORMAL.toString() %>' >
							<bean:message key="label.attends.enrollmentState.normal"/>
						</logic:equal>
						<logic:equal name="enrollmentEvaluationType" value='<%= EnrolmentEvaluationType.IMPROVEMENT.toString() %>' >
							<bean:message key="label.attends.enrollmentState.improvement"/>
						</logic:equal>
						<logic:equal name="enrollmentEvaluationType" value='<%= EnrolmentEvaluationType.SPECIAL_SEASON.toString() %>' >
							<bean:message key="label.attends.enrollmentState.specialSeason"/>
						</logic:equal>
					</logic:present>
					<logic:notPresent name="attendacy" property="enrolmentEvaluationType">
						<bean:message key="label.attends.enrollmentState.notEnrolled"/>
					</logic:notPresent>
				</td>
				<td>
					<bean:define id="infoDCP" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan" name="attend" property="attendingStudentInfoDCP"/>
					<bean:define id="degree" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree" name="infoDCP" property="infoDegree"/>

					<bean:write name="infoDCP" property="name"/>
				</td>
				<td  title="<bean:write name="attendacy" property="aluno.infoPerson.nome"/>">
						<bean:define id="shortName" value="" type="java.lang.String"/>
						<%
							shortName = attendacy.getAluno().getInfoPerson().getNome();
							String[] names = shortName.split(" ");
							String firstName = names[0];
							String lastName = names[names.length-1];					
							shortName = firstName + " " + lastName;
							out.print(shortName);
						%>
				</td>
				<%
				Integer executionCourseId = executionCourse.getIdInternal();
				
				for (Iterator groupsPropertiesIterator=((List)groupsProperties).iterator(); groupsPropertiesIterator.hasNext();)
				{
					InfoGrouping gProperties = (InfoGrouping) groupsPropertiesIterator.next();
					Integer groupPropertiesIdInternal = gProperties.getIdInternal();

					Integer studentGroupId = null;
					Integer shiftId = null;
					Integer groupNumber = null;
									
					Map studentGroups = attend.getInfoStudentGroups();
					InfoStudentGroup infoStudentGroup = (InfoStudentGroup)studentGroups.get(gProperties.getName());
					
					if (infoStudentGroup != null){
						studentGroupId = infoStudentGroup.getIdInternal();
						if(infoStudentGroup.getInfoShift() != null){//temporary correction, verify if this makes sence in data
							shiftId = infoStudentGroup.getInfoShift().getIdInternal();
						}
						groupNumber = infoStudentGroup.getGroupNumber();
					}

				%>
				<td>
				<%request.setAttribute("parameters",new TreeMap());%>
				<bean:define name="parameters" type="java.util.TreeMap" id="parameters"/>
				<%
					if (studentGroupId != null)
					{
						parameters.put("shiftCode",shiftId);
						parameters.put("studentGroupCode",studentGroupId);
						parameters.put("method","viewStudentGroupInformation");
						parameters.put("objectCode",executionCourseId);
						parameters.put("groupPropertiesCode",groupPropertiesIdInternal);
				%>
					<html:link page="/viewStudentGroupInformation.do" name="parameters">
				<%
							out.print(groupNumber);
				%>
					</html:link>
				<%
					}
					else{
				%>
					<bean:message key="label.notAvailable"/>
				<%
				 }
				%>
				</td>
				<%
				 }
				%>
				<td>
					<logic:present name="attendacy" property="aluno.infoPerson.email">
						<bean:define id="mail" name="attendacy" property="aluno.infoPerson.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attendacy" property="aluno.infoPerson.email"/></html:link>
					</logic:present>
					<logic:notPresent  name="attendacy" property="aluno.infoPerson.email">
						&nbsp;
					</logic:notPresent>
				</td>
				
				<logic:iterate id="classType" name="classTypes" type="ShiftType">
		          <td>
		            <bean:define id="map" name="attend" property="infoShifts" type="java.util.Map"/>

					<% 
					if (((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get(classType.getSiglaTipoAula()))==null){
					%>
						<bean:message key="label.notAvailable"/>
					<%
		            }
		            else{
		            	out.print(((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get(classType.getSiglaTipoAula())).getNome());
					}
					%>
		          </td>
		    	</logic:iterate>
			</tr>

		</logic:iterate>
		
</table>

	

<p class="mtop2 mbottom05"><bean:message key="label.attends.summary"/></p>
<table class="tstyle1 tdcenter mtop05">
	<tr>
		<th><bean:message key="label.attends.summary.enrollmentsNumber"/></th>
		<th><bean:message key="label.attends.summary.studentsNumber"/></th>
	</tr>
	<logic:iterate id="enrollmentNumber" name="attendsSummary" property="numberOfEnrollments">
		<tr>
			<td><bean:write name="enrollmentNumber"/></td>
			<td><%= ((net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary)attendsSummary).getEnrollmentDistribution().get(enrollmentNumber).toString() %></td>
		</tr>
	</logic:iterate> 
</table>


</html:form>
<br/>
<br/>