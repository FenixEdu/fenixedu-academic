<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGrouping" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGroupProjectStudents" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>

<logic:present name="attendsSummary">
<logic:present name="siteView">
<logic:present name="shifts">
<bean:define id="attendacies" name="attendsSummary" property="attends" />	
<bean:define id="shifts" name="shifts" property="infoShifts" type="java.util.List"/>
<bean:define id="studentsComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudents"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>
<bean:define id="infosGroups" name="infosGroups" type="java.util.List"/>
<bean:define id="projects" name="projects" type="java.util.List"/>
    <span class="error"><html:errors/></span>
	<bean:size id="studentsListSize" name="studentsComponent" property="students"/>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="message.students.explanation"/>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<!--
	<tr>
		<td class="infoop">
			<bean:message key="message.enrollmentsWarning" />
		</td>
	</tr>
-->
</table>
<h2>
	<bean:write name="commonComponent" property="executionCourse.nome" />
</h2>


<html:form action="/studentsByCurricularCourse.do" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readStudents"/>
<table>
	<tr valign="top">
		<td>
			<bean:message key="label.viewPhoto" />
		</td>
		<td colspan="3">
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewPhoto"  property="viewPhoto" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.selectShift"/>
		</td>
		<td colspan="3">

			<html:select bundle="HTMLALT_RESOURCES" altKey="select.shiftCode" property="shiftCode">
							<option value="null">
								<bean:message key="label.select.SelectShift"/>
							</option>
							<logic:iterate type="net.sourceforge.fenixedu.dataTransferObject.InfoShift" name="shifts" id="shift">
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
										if(lesson.getInfoSala() != null){
											text += lesson.getInfoSala().getNome();
										}	
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
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" property="submition"><bean:message key="button.selectShift"/></html:submit>
					</td>
		</tr>
</table>
<table cellspacing="1" cellpadding="1">
	<tr>
		<td colspan="3">
			<h3>
				<%=studentsComponent.getStudents().size()%> <bean:message key="message.enrolledStudents"/>
			</h3>
		</td>
	</tr>
	<tr valign="top">
	       <td colspan="3">
	
		<%Map spreadSheetArgs = new TreeMap(request.getParameterMap());
			request.setAttribute("spreadSheetArgs",spreadSheetArgs);%>
			<bean:define id="spreadSheetLinkArgs" type="java.util.Map" name="spreadSheetArgs"/>
				<html:link page="/getTabSeparatedStudentList.do" name="spreadSheetLinkArgs">
				<bean:message key="link.getExcelSpreadSheet"/><br/>
			</html:link>
			 <h2>
				<logic:present name="studentsComponent" property="infoCurricularCourseScope">  		
						&nbsp;-&nbsp;&nbsp;-&nbsp;				
						<bean:define id="ano" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year" />
						<bean:define id="semestre" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.semester" />					
						<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla" />
						<logic:notEqual name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name" value="">
							<bean:message key="property.curricularCourse.branch" />
							<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name"/>&nbsp;
						</logic:notEqual>						
						<bean:message key="label.year" arg0="<%= String.valueOf(ano) %>"/>
						<bean:message key="label.period" arg0="<%= String.valueOf(semestre) %>"/>
			   </logic:present>
	   		</h2><br />
		   </td>
		</tr> 
		<tr>
     	   <bean:define id="colspan" value="1"/>
     	   <bean:define id="rowspan" value="2"/>
		   <logic:present name="projects">
			<logic:notEmpty name="projects">
				<bean:size id="colspanAux" name="projects"/>
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
				<bean:message key="label.Degree" /> 
		   </th>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.name" />
		   </th>
		   <logic:present name="projects">
			<logic:notEmpty name="projects">
					<th class="listClasses-header" colspan="<%= colspan.toString() %>">
						<bean:message key="label.projectGroup"/>
					</th>
			</logic:notEmpty>
		   </logic:present>
			<th class="listClasses-header" rowspan="<%= rowspan.toString() %>">
				<bean:message key="label.mail" />
		   </th>
		    <bean:define id="theoreticalHours" name="commonComponent" property="executionCourse.theoreticalHours" />
           <bean:define id="praticalHours" name="commonComponent" property="executionCourse.praticalHours" />
           <bean:define id="theoPratHours" name="commonComponent" property="executionCourse.theoPratHours" />
           <bean:define id="labHours" name="commonComponent" property="executionCourse.labHours" />
           <% int shiftColSpan=0; %>
           <logic:greaterThan value="0" name="theoreticalHours" >
          <% shiftColSpan=shiftColSpan+1; %>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="praticalHours" >
          <% shiftColSpan=shiftColSpan+1; %>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="theoPratHours" >
         <% shiftColSpan=shiftColSpan+1; %>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="labHours" >
           <% shiftColSpan=shiftColSpan+1; %>
         </logic:greaterThan>
         <% Integer shiftColSpanInteger = new Integer(shiftColSpan); %>
         <% if (shiftColSpan>0) {%>
         <th class="listClasses-header" colspan="<%= shiftColSpanInteger %>" >
                Turnos
            </th>
            <%     }%>
		</tr>    		
		<tr>
		   <logic:present name="projects">
			<logic:notEmpty name="projects">
				<logic:iterate name="projects" id="project" type="net.sourceforge.fenixedu.dataTransferObject.InfoGrouping">
					<th class="listClasses-header">
<%--					<bean:message key="label.projectGroup"/>&nbsp; --%>
					<bean:write name="project" property="name"/>
					</th>
				</logic:iterate>
			</logic:notEmpty>
		   </logic:present>
		    <logic:greaterThan value="0" name="theoreticalHours" >
          <th class="listClasses-header" >
             Teórico
          </th>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="praticalHours" >
          <th class="listClasses-header" >
             Prático
          </th>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="theoPratHours" >
          <th class="listClasses-header" >
             Teórico-Prático
          </th>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="labHours" >
          <th class="listClasses-header" >
             Laboratorial
          </th>
         </logic:greaterThan>
		</tr>    		
		
		<bean:define id="mailingList" value=""/>

    	<logic:iterate id="attendacy" type="net.sourceforge.fenixedu.dataTransferObject.InfoAttendWithEnrollment" name="attendacies"> 
			
			<tr>
				<logic:equal name="viewPhoto" value="true">
					<td class="listClasses">
						<bean:define id="aluno" name="attendacy" property="aluno"/>
						<bean:define id="infoPerson" name="aluno" property="infoPerson"/>			
						<bean:define id="personID" name="infoPerson" property="idInternal"/>
		      			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
					</td>
				</logic:equal>
				<td class="listClasses">
					<bean:write name="attendacy" property="aluno.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="attendacy" property="enrollments"/>
					<%--<logic:present name="attendacy" property="infoEnrolment">
						<bean:message key="message.yes"/>
					</logic:present>
					<logic:notPresent name="attendacy" property="infoEnrolment">
						<bean:message key="message.no"/>
					</logic:notPresent> --%>
				</td>
				<td class="listClasses">
					<logic:present name="attendacy" property="infoEnrolment">
						<bean:write name="attendacy" property="infoEnrolment.infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/>
					</logic:present>
					<logic:notPresent name="attendacy" property="infoEnrolment">
						N/A
					</logic:notPresent>
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
				for (Iterator projectsIterator= projects.iterator(); projectsIterator.hasNext();)
				{
					InfoGrouping element= (InfoGrouping) projectsIterator.next();
					int projectIdInternal = element.getIdInternal().intValue();
					int groupNumber =-1;
					int shiftCode = -1;
					int executionCourseCode = -1;
					int groupPropertiesCode = -1;
					int studentGroupCode = -1;
					Integer studentNumber = attendacy.getAluno().getNumber();
				
					for (Iterator groupsIterator= infosGroups.iterator(); groupsIterator.hasNext();)		
					{
					
						InfoGroupProjectStudents groupInfo= (InfoGroupProjectStudents) groupsIterator.next();
						if (projectIdInternal == groupInfo.getStudentGroup().getInfoGrouping().getIdInternal().intValue() &&
							groupInfo.isStudentMemberOfThisGroup(studentNumber))
						{
							groupNumber = groupInfo.getStudentGroup().getGroupNumber().intValue();
							studentGroupCode = groupInfo.getStudentGroup().getIdInternal().intValue();
							shiftCode = groupInfo.getStudentGroup().getInfoShift().getIdInternal().intValue();
							executionCourseCode = groupInfo.getStudentGroup().getInfoShift().getInfoDisciplinaExecucao().getIdInternal().intValue();
							break;
						}
					}
				%>
				<td class="listClasses">
				<%request.setAttribute("parameters",new TreeMap());%>
				<bean:define name="parameters" type="java.util.TreeMap" id="parameters"/>
				<%
					if (groupNumber != -1)
					{
					parameters.put("shiftCode",new Integer(shiftCode));
					parameters.put("studentGroupCode",new Integer(studentGroupCode));
					parameters.put("method","viewStudentGroupInformation");
					parameters.put("objectCode",new Integer(executionCourseCode));
					parameters.put("groupPropertiesCode",new Integer(projectIdInternal));
				%>
					<html:link page="/viewStudentGroupInformation.do" name="parameters">
				<%
							out.print(groupNumber);
				%>
					</html:link>
				<%
					}
					else
						out.print("N/A");
				%>
				</td>
				<%
				 }
				%>
				<td class="listClasses">
					&nbsp;
					<logic:notEmpty name="attendacy" property="aluno.infoPerson.email">
						<bean:define id="mail" name="attendacy" property="aluno.infoPerson.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="mail"/></html:link>
					</logic:notEmpty>
				</td>
				<logic:greaterThan value="0" name="theoreticalHours" >
          <td class="listClasses">
            <logic:present name="attendacy" property="infoShifts">
            <bean:define id="map" name="attendacy" property="infoShifts" type="java.util.Map"/>
            <% if (((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("T"))==null)
            {
              out.print("N/A");
            } else {
             out.print(((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("T")).getNome());
              } %>  
            </logic:present>
          </td>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="praticalHours" >
          <td class="listClasses">
            <logic:present name="attendacy" property="infoShifts">
            <bean:define id="map" name="attendacy" property="infoShifts" type="java.util.Map"/>
            <% if (((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("P"))==null)
            {
              out.print("N/A");
            } else {
             out.print(((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("P")).getNome());
              } %>  
            </logic:present>
          </td>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="theoPratHours" >
          <td class="listClasses">
          <logic:present name="attendacy" property="infoShifts">
            <bean:define id="map" name="attendacy" property="infoShifts" type="java.util.Map"/>
            <% if (((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("TP"))==null)
            {
              out.print("N/A");
            } else {
             out.print(((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("TP")).getNome());
              } %>  
            </logic:present>
          </td>
         </logic:greaterThan>
         <logic:greaterThan value="0" name="labHours" >
          <td class="listClasses">
            <logic:present name="attendacy" property="infoShifts">
            <bean:define id="map" name="attendacy" property="infoShifts" type="java.util.Map"/>
            <% if (((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("L"))==null)
            {
              out.print("N/A");
            } else {
             out.print(((net.sourceforge.fenixedu.dataTransferObject.InfoShift)map.get("L")).getNome());
              } %>  
            </logic:present>
          </td>
         </logic:greaterThan>
			</tr>
    	</logic:iterate>
		
</table>
<br/>
<br/>
 <strong>Resumo:</strong>
<table>
	<tr>
		<th class="listClasses-header">Número de inscrições</th>
		<th class="listClasses-header">Número de Alunos</th>
	</tr>
	<logic:iterate id="enrollmentNumber" name="attendsSummary" property="numberOfEnrollments">
	<tr>
		<td class="listClasses"><bean:write name="enrollmentNumber"/></td>
		<td class="listClasses"><%= ((net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary)pageContext.findAttribute("attendsSummary")).getEnrollmentDistribution().get(enrollmentNumber).toString() %></td>
	</tr>
	</logic:iterate> 
</table>
</html:form>
<br/>
<br/> 

    
</logic:present>
</logic:present>
</logic:present>