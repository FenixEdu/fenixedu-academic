<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="Util.TipoAula" %>
<%@ page import="Util.EnrolmentEvaluationType" %>
<%@ page import="DataBeans.InfoExecutionCourse" %>
<%@ page import="DataBeans.InfoDegreeCurricularPlan" %>
<%@ page import="DataBeans.InfoGroupProperties" %>
<%@ page import="DataBeans.InfoStudentGroup" %>
<%@ page import="DataBeans.InfoLesson" %>
<%@ page import="DataBeans.InfoAttendsSummary" %>
<%@ page import="DataBeans.InfoForReadStudentsWithAttendsByExecutionCourse" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<script language="Javascript" type="text/javascript">
<!--
var select = true;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0; i<document.forms[0].coursesIDs.length; i++){
		var e = document.forms[0].coursesIDs[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect() {
	var allSelected=true;

	for (var i=1; i<document.forms[0].coursesIDs.length; i++){
		var e = document.forms[0].coursesIDs[i];
		allSelected = allSelected && (e.checked == true);
	}
	select = allSelected;
	document.forms[0].coursesIDs[0].checked = allSelected;
}
// -->
</script>


<bean:define id="studentsComponent" name="siteView" property="component" type="DataBeans.InfoForReadStudentsWithAttendsByExecutionCourse"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>
<bean:define id="classTypes" type="java.util.List" name="studentsComponent" property="classTypes" />
<bean:define id="groupsProperties"	type="List" name="studentsComponent" property="infoGroupProperties"/>
<bean:define id="executionCourse"	type="InfoExecutionCourse" name="studentsComponent" property="infoExecutionCourse"/>
<bean:define id="attendsSummary"	type="InfoAttendsSummary" name="studentsComponent" property="infoAttendsSummary"/>


    <span class="error"><html:errors/></span>
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


<html:form action="/studentsByCurricularCourse.do" method="get">
<html:hidden property="objectCode"/>
<html:hidden property="method" value="readStudents"/>
<table>
	<tr valign="top">
		<td>
			<bean:message key="label.viewPhoto" />
		</td>
		<td colspan="3">
			<html:checkbox  property="viewPhoto" />
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr valign="top">
		<td>
			<bean:define id="degreeCurricularPlans" name="studentsComponent" property="infoDegreeCurricularPlans"/>
			<bean:message key="label.attends.courses"/>
		</td>
		<td>
			<table>
				<tr>
					<td>
						<bean:message key="label.attends.allCourses"/>
					</td>
					<td>
						<html:multibox property="coursesIDs" value="0" onclick="invertSelect()"/>
					</td>
				</tr>
		
				<logic:iterate id="dcp" type="DataBeans.InfoDegreeCurricularPlan" name="degreeCurricularPlans">
					<bean:define id="dcpID" name="dcp" property="idInternal"/>
					<tr>
						<td>
							<bean:write name="dcp" property="name"/>
						</td>
						<td>
							<html:multibox property="coursesIDs" value="<%= dcpID.toString() %>" onclick="cleanSelect()"/>
						</td>					
					</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<bean:message key="label.attends.allStudents"/>
		</td>
		<td>
			<html:radio property="hasEnrollment" value="false" ></html:radio>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.attends.enrolledStudents"/>
		</td>
		<td>		
			<html:radio property="hasEnrollment" value="true" ></html:radio>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<bean:message key="label.selectShift"/>
		</td>
		<td colspan="3">

			<html:select property="shiftCode">
							<option value="null">
								<bean:message key="label.select.SelectShift"/>
							</option>
							<bean:define id="shifts" name="studentsComponent" property="infoShifts" />
							<logic:iterate type="DataBeans.InfoShift" name="shifts" id="shift">
								<option value=<bean:write name="shift" property="idInternal"/>
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
										text += lesson.getInfoSala().getNome();
										if (iterator.hasNext())
											text += " ;";
									}
									text += ")"; 
									Integer shiftCode = null;
									try
									{
										shiftCode = new Integer((String)request.getParameter("shiftCode"));
									}
									catch (NumberFormatException ex)
									{}
									if ((shiftCode != null) && (shiftCode.intValue() == shift.getIdInternal().intValue()))
										out.print("selected");
									out.print(">"+text);									
									%>
								</option>
							</logic:iterate>
						</html:select>
					</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<html:submit property="submition"><bean:message key="button.selectShift"/></html:submit>
			</td>
		</tr>
</table>
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
		<%Map sendMailParameters = new TreeMap(request.getParameterMap());
              sendMailParameters.put("method","prepare");
		request.setAttribute("sendMailParameters",sendMailParameters);%>
		<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
		   <html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
		   </html:link>

		<%Map spreadSheetArgs = new TreeMap(request.getParameterMap());
		request.setAttribute("spreadSheetArgs",spreadSheetArgs);%>
			<bean:define id="spreadSheetLinkArgs" type="java.util.Map" name="spreadSheetArgs"/>
				<html:link page="/getTabSeparatedStudentList.do" name="spreadSheetLinkArgs">
				<bean:message key="link.getExcelSpreadSheet"/><br/>
			</html:link>
			</td>
		  
		</tr> 
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
				<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
					<bean:message key="label.photo" /> 
			   </td>
			 </logic:equal>
			<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.numberOfEnrollments" />
		   </td>
		   <td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.attends.enrollmentState"/>
		   </td>
			<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.Degree" /> 
		   </td>
			<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.name" />
		   </td>
		   
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
					<td class="listClasses-header" colspan="<%= colspan.toString() %>">
						<bean:message key="label.projectGroup"/>
					</td>
			</logic:notEmpty>
		   </logic:present>
	   
			<td class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.mail" />
		   </td>
		   <% int shiftColSpan=((List)classTypes).size(); %>
         <% Integer shiftColSpanInteger = new Integer(shiftColSpan); %>
         <% if (shiftColSpan>0) {%>
		 <td class="listClasses-header" colspan="<%= shiftColSpanInteger %>" >
                <bean:message key="label.attends.shifts"/>
            </td>
        <%		}%>
       
		</tr>    		
		<tr>
		   <logic:present name="groupsProperties">
			<logic:notEmpty name="groupsProperties">
				<logic:iterate name="groupsProperties" id="gp" type="DataBeans.InfoGroupProperties">
					<td class="listClasses-header">
					<bean:write name="gp" property="name"/>
					</td>
				</logic:iterate>
			</logic:notEmpty>
		   </logic:present>
		   
			<logic:iterate id="classType" type="Util.TipoAula" name="classTypes">
			  <td class="listClasses-header" >
				<bean:define id="classTypeInt" name="classType" property="tipo"/>

				<logic:equal name="classTypeInt" value='<%= String.valueOf(TipoAula.TEORICA) %>' >
					<bean:message key="label.attends.shifts.theoretical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= String.valueOf(TipoAula.PRATICA) %>' >
					<bean:message key="label.attends.shifts.practical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= String.valueOf(TipoAula.TEORICO_PRATICA) %>' >
					<bean:message key="label.attends.shifts.theo-practical"/>
				</logic:equal>
				<logic:equal name="classTypeInt" value='<%= String.valueOf(TipoAula.LABORATORIAL) %>' >
					<bean:message key="label.attends.shifts.laboratory"/>
				</logic:equal>
        	  </td>
          	</logic:iterate>
         
		</tr>    		

		<bean:define id="attendacies" name="studentsComponent" property="infoAttends"/>

    	<logic:iterate id="attend" type="DataBeans.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups" name="attendacies"> 
			<bean:define id="attendacy" type="DataBeans.InfoFrequenta" name="attend" property="infoAttends"/>
			<tr>
				<logic:equal name="viewPhoto" value="true">
					<td class="listClasses">
						<bean:define id="aluno" name="attendacy" property="aluno"/>
						<bean:define id="infoPerson" name="aluno" property="infoPerson"/>			
						<bean:define id="personID" name="infoPerson" property="idInternal"/>
		      			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>"/>
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
						
						<logic:equal name="enrollmentEvaluationType" value='<%= EnrolmentEvaluationType.NORMAL_OBJ.toString() %>' >
							<bean:message key="label.attends.enrollmentState.normal"/>
						</logic:equal>
						<logic:equal name="enrollmentEvaluationType" value='<%= EnrolmentEvaluationType.IMPROVEMENT_OBJ.toString() %>' >
							<bean:message key="label.attends.enrollmentState.improvement"/>
						</logic:equal>
					</logic:present>
					<logic:notPresent name="attendacy" property="infoEnrolment">
						<bean:message key="label.attends.enrollmentState.notEnrolled"/>
					</logic:notPresent>
				</td>
				<td class="listClasses">
					<bean:write name="attend" property="attendingStudentInfoDCP.name"/>
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
					InfoGroupProperties gProperties = (InfoGroupProperties) groupsPropertiesIterator.next();
					Integer groupPropertiesIdInternal = gProperties.getIdInternal();

					Integer studentGroupId = null;
					Integer shiftId = null;
					Integer groupNumber = null;
									
					Map studentGroups = attend.getInfoStudentGroups();
					InfoStudentGroup infoStudentGroup = (InfoStudentGroup)studentGroups.get(gProperties.getName());
					
					if (infoStudentGroup != null){
						studentGroupId = infoStudentGroup.getIdInternal();
						shiftId = infoStudentGroup.getInfoShift().getIdInternal();
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
				<td class="listClasses">
					<logic:present name="attendacy" property="aluno.infoPerson.email">
						<bean:define id="mail" name="attendacy" property="aluno.infoPerson.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attendacy" property="aluno.infoPerson.email"/></html:link>
					</logic:present>
					<logic:notPresent  name="attendacy" property="aluno.infoPerson.email">
						&nbsp;
					</logic:notPresent>
				</td>
				
				<logic:iterate id="classType" name="classTypes" type="TipoAula">
		          <td class="listClasses">
		            <bean:define id="map" name="attend" property="infoShifts" type="java.util.Map"/>

					<% 
					if (((DataBeans.InfoShift)map.get(classType.getSiglaTipoAula()))==null){
					%>
						<bean:message key="label.notAvailable"/>
					<%
		            }
		            else{
		            	out.print(((DataBeans.InfoShift)map.get(classType.getSiglaTipoAula())).getNome());
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
		<td class="listClasses-header"><bean:message key="label.attends.summary.enrollmentsNumber"/></td>
		<td class="listClasses-header"><bean:message key="label.attends.summary.studentsNumber"/></td>
	</tr>
	<logic:iterate id="enrollmentNumber" name="attendsSummary" property="numberOfEnrollments">
		<tr>
			<td class="listClasses"><bean:write name="enrollmentNumber"/></td>
			<td class="listClasses"><%= ((DataBeans.InfoAttendsSummary)attendsSummary).getEnrollmentDistribution().get(enrollmentNumber).toString() %></td>
		</tr>
	</logic:iterate> 
</table>


</html:form>
<br/>
<br/>