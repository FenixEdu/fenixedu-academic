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
	/*if ( checkboxes[0].checked == false ) { 
		checkboxes[0].checked = true; 
	} else { 
		checkboxes[0].checked = false;
	}*/
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


    <span class="error"><!-- Error messages go here --><html:errors /></span>
	<bean:size id="studentsListSize" name="studentsComponent" property="infoAttends"/>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="message.students.explanation"/>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>
<h2>
	<bean:write name="commonComponent" property="executionCourse.nome" />
</h2>


<html:form action="/studentsByCurricularCourse.do" method="post">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readStudents"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />


<table width="100%">
	<tr>
		<th width="25%">
			<bean:message key="label.selectStudents"/>
		</th>
		<th width="25%">
			<bean:message key="label.attends.courses"/>
		</th>
		<th width="50%">
			<bean:message key="label.selectShift"/>
		</th>
	</tr>
	
	<tr valign="top">
		<td>
			<table>
				<tr>
					<td>
						<bean:message key="label.attends.allStudents"/>
					</td>
					<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.ALL.toString() %>" onclick="invertSelect(document.forms[0].enrollmentType)" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.attends.enrolledStudents"/>
					</td>
					<td>		
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.ENROLLED.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.attends.improvementStudents"/>
					</td>
					<td>		
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.IMPROVEMENT.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.attends.notEnrolledStudents"/>
					</td>
					<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.NOT_ENROLLED.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.attends.specialSeason"/>
					</td>
					<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentType" property="enrollmentType" value="<%= AttendacyStateSelectionType.SPECIAL_SEASON.toString() %>" onclick="cleanSelect(document.forms[0].enrollmentType)" />
					</td>
				</tr>
			</table>				
		</td>
		<td>
			<bean:define id="degreeCurricularPlans" name="studentsComponent" property="infoDegreeCurricularPlans"/>
			<table>
				<tr>
					<td>
						<bean:message key="label.attends.allCourses"/>
					</td>
					<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coursesIDs" property="coursesIDs" value="0" onclick="invertSelect(document.forms[0].coursesIDs)"/>
					</td>
				</tr>
		
				<logic:iterate id="dcp" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan" name="degreeCurricularPlans">
					<bean:define id="dcpID" name="dcp" property="idInternal"/>
					<bean:define id="degree" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree" name="dcp" property="infoDegree"/>
					<tr>
						<td>
							<%
								String sigla = degree.getSigla();
								Date dataInicio = dcp.getInitialDate();
								Calendar dataInicioCalendar = new GregorianCalendar();
								dataInicioCalendar.setTime(dataInicio);						
								int year = dataInicioCalendar.get(Calendar.YEAR);
								
								out.print(sigla + " " + year);
							%>
						</td>
						<td>
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coursesIDs" property="coursesIDs" value="<%= dcpID.toString() %>" onclick="cleanSelect(document.forms[0].coursesIDs)"/>
						</td>					
					</tr>
				</logic:iterate>
			</table>			
		</td>
		<td>
			<table>
				<tr>
					<td>
						<bean:message key="label.attends.allShifts"/>
					</td>
					<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIDs" property="shiftIDs" value="0" onclick="invertSelect(document.forms[0].shiftIDs)"/>
					</td>
				</tr>	
				
				<bean:define id="shifts" name="studentsComponent" property="infoShifts" />
				<logic:iterate type="net.sourceforge.fenixedu.dataTransferObject.InfoShift" name="shifts" id="shift">
					<tr>
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
						<td>
							<%= text %>
						</td>
						<td>
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftIDs" property="shiftIDs" value="<%= shift.getIdInternal().toString() %>"
								onclick="cleanSelect(document.forms[0].shiftIDs)"/>
						</td>
					</tr>
				</logic:iterate>
			</table>			
		</td>
	</tr>
</table>	
		
<br/>
<table>
	<tr valign="top">
		<td>
			<bean:message key="label.viewPhoto" />
		</td>
		<td colspan="3">
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewPhoto"  property="viewPhoto" />
		</td>
	</tr>
</table>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" property="submition"><bean:message key="button.selectShift"/></html:submit>

<table cellspacing="1" cellpadding="1">
	<tr>
		<td colspan="3">
			<h3>
				<%=studentsComponent.getInfoAttends().size()%> <bean:message key="message.attendingStudents"/>
			</h3>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3">
			<html:link page="/getTabSeparatedStudentList.do" name="spreadSheetLinkArgs">
				<bean:message key="link.getExcelSpreadSheet"/><br/>
			</html:link>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
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
				<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
					<bean:message key="label.photo" /> 
			   </th>
			 </logic:equal>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.number" /> 
		   </th>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.numberOfEnrollments" />
		   </th>
		   <th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.attends.enrollmentState"/>
		   </th>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.Degree" /> 
		   </th>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.name" />
		   </th>
		   
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
					<th class="listClasses-header" colspan="<%= colspan.toString() %>">
						<bean:message key="label.projectGroup"/>
					</th>
			</logic:notEmpty>
		   </logic:present>
	   
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.mail" />
		   </th>
		   <% int shiftColSpan=((List)classTypes).size(); %>
         <% Integer shiftColSpanInteger = new Integer(shiftColSpan); %>
         <% if (shiftColSpan>0) {%>
		 <th class="listClasses-header" colspan="<%= shiftColSpanInteger %>" >
                <bean:message key="label.attends.shifts"/>
            </th>
        <%		}%>
       
		</tr>    		
		<tr>
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
				<logic:iterate name="groupsProperties" id="gp" type="net.sourceforge.fenixedu.dataTransferObject.InfoGrouping">
					<th class="listClasses-header">
					<bean:write name="gp" property="name"/>
					</th>
				</logic:iterate>
			</logic:notEmpty>
		   </logic:present>
		   
			<logic:iterate id="classType" type="net.sourceforge.fenixedu.domain.ShiftType" name="classTypes">
			  <th class="listClasses-header" >
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
        	  </th>
          	</logic:iterate>
         
		</tr>    		

		<bean:define id="attendacies" name="studentsComponent" property="infoAttends"/>

    	<logic:iterate id="attend" type="net.sourceforge.fenixedu.dataTransferObject.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups" name="attendacies"> 
			<bean:define id="attendacy" type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta" name="attend" property="infoAttends"/>
			<tr>
				<logic:equal name="viewPhoto" value="true">
					<td class="listClasses">
						<bean:define id="aluno" name="attendacy" property="aluno"/>
						<bean:define id="infoPerson" name="aluno" property="infoPerson"/>			
						<bean:define id="personID" name="infoPerson" property="idInternal"/>
		      			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
					</td>
				</logic:equal>
				<td class="listClasses">
					<bean:write name="attendacy" property="aluno.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:define id="numEnrollments" name="attend" property="numberOfEnrollments"/>
					
					<logic:notEqual value="0" name="numEnrollments" >
						<bean:write name="numEnrollments"/>
					</logic:notEqual>
					<logic:equal value="0" name="numEnrollments" >
						<bean:message key="label.null"/>
					</logic:equal>
		
				</td>
				<td class="listClasses">

					<logic:present name="attendacy" property="infoEnrolment">
						<bean:define id="enrollmentEvaluationType" 
							type="EnrolmentEvaluationType" 
							name="attendacy" property="infoEnrolment.enrolmentEvaluationType"/>
						
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
					<logic:notPresent name="attendacy" property="infoEnrolment">
						<bean:message key="label.attends.enrollmentState.notEnrolled"/>
					</logic:notPresent>
				</td>
				<td class="listClasses">
					<bean:define id="infoDCP" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan" name="attend" property="attendingStudentInfoDCP"/>
					<bean:define id="degree" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree" name="infoDCP" property="infoDegree"/>
					
					<%
						String sigla = degree.getSigla();
						Date dataInicio = infoDCP.getInitialDate();
						Calendar dataInicioCalendar = new GregorianCalendar();
						dataInicioCalendar.setTime(dataInicio);						
						int year = dataInicioCalendar.get(Calendar.YEAR);
						
						out.print(sigla + " " + year);					
					%>

				</td>
				<td  class="listClasses" title="<bean:write name="attendacy" property="aluno.infoPerson.nome"/>">
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
				<td class="listClasses">
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
				<%--	<html:link page="/viewStudentGroupInformation.do" name="parameters">--%>
				<%
							out.print(groupNumber);
				%>
				<%--	</html:link>--%>
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
				<td class="listClasses">
					<logic:present name="attendacy" property="aluno.infoPerson.email">
						<bean:define id="mail" name="attendacy" property="aluno.infoPerson.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attendacy" property="aluno.infoPerson.email"/></html:link>
					</logic:present>
					<logic:notPresent  name="attendacy" property="aluno.infoPerson.email">
						&nbsp;
					</logic:notPresent>
				</td>
				
				<logic:iterate id="classType" name="classTypes" type="ShiftType">
		          <td class="listClasses">
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

	
<br/>
<br/>

	
<table>
	<tr><td><strong><bean:message key="label.attends.summary"/></strong></td></tr>
	<tr>
		<th class="listClasses-header"><bean:message key="label.attends.summary.enrollmentsNumber"/></th>
		<th class="listClasses-header"><bean:message key="label.attends.summary.studentsNumber"/></th>
	</tr>
	<logic:iterate id="enrollmentNumber" name="attendsSummary" property="numberOfEnrollments">
		<tr>
			<td class="listClasses"><bean:write name="enrollmentNumber"/></td>
			<td class="listClasses"><%= ((net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary)attendsSummary).getEnrollmentDistribution().get(enrollmentNumber).toString() %></td>
		</tr>
	</logic:iterate> 
</table>


</html:form>
<br/>
<br/>