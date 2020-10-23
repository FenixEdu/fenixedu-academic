<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List"%>

<jsp:include page="/commons/contextClassAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turmas"/> <span class="small"><c:out value="${executionDegree.executionDegree.degreeCurricularPlan.name}" /></span></h2>

<h3>Manipular Turma <span class="small"><c:out value="${schoolClass.nome}" /></span></h3>


<html:link styleClass="btn btn-primary btn-sm" page="/manageClass.do?method=prepareAddShifts&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}&class_oid=${classOID}">
	<bean:message key="label.shifts.add"/>
</html:link>

<html:link styleClass="btn btn-primary btn-sm" page="/manageClass.do?method=viewSchedule&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}&class_oid=${classOID}">
	<bean:message key="label.shcedule.view"/>
</html:link>

<html:link styleClass="btn btn-primary btn-sm" page="/manageClasses.do?method=listClasses&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}">
	<bean:message key="label.return"/>
</html:link>

<html:form action="/manageClass" focus="className">

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CLASS_VIEW_OID %>" property="<%= PresentationConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>
	
	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

   	<bean:define id="degree" type="org.fenixedu.academic.domain.Degree" name="schoolClass" property="executionDegree.degreeCurricularPlan.degree"/>
   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
   	
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="btn btn-primary btn-xs">
   		<bean:message key="label.change"/>
   	</html:submit>

</html:form>


<p class="mtop15 mbottom05">Turnos associados a turma:</p>

<logic:present name="<%= PresentationConstants.SHIFTS %>" scope="request">

	<html:form action="/removeShifts">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeShifts"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

        <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                     value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CLASS_VIEW_OID %>" property="<%= PresentationConstants.CLASS_VIEW_OID %>"
					 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.remove.shift"/>')
	</bean:define>			

	<table class="tstyle4 thlight tdcenter mtop05">
		<tr>
			<th rowspan="2">
			</th>
			<th rowspan="2">
				<bean:message key="property.executionCourse"/>
			</th>
			<th rowspan="2">
				<bean:message key="property.shift.name"/>
			</th>
	        <th rowspan="2">
	        	<bean:message key="property.shift.type"/>
	        </th>
			<th rowspan="2">
				<bean:message key="property.shift.capacity"/>
			</th>
			<th colspan="5">
	        	<bean:message key="property.lessons"/>
	        </th>
			<th rowspan="2">
	        </th>
		</tr>
		<tr>
			<th>
				<bean:message key="property.weekday"/>
			</th>
			<th>
				<bean:message key="property.time.start"/>
			</th>
		       <th>
		       	<bean:message key="property.time.end"/>
	    	   </th>
			<th>
				<bean:message key="property.room"/>
			</th>
			<th>
		       	<bean:message key="property.capacity"/>
		       </th>
		</tr>
		<logic:iterate id="infoShift" name="<%= PresentationConstants.SHIFTS %>">
			<bean:define id="infoShiftOID" name="infoShift" property="externalId"/>
			<bean:define id="infoExecutionCourseOID" name="infoShift" property="infoDisciplinaExecucao.externalId"/>
			<bean:define id="infoShiftLessonList" name="infoShift" property="infoLessons"/>
			<bean:define id="numberOfLessons">
				<%= ((List) pageContext.findAttribute("infoShiftLessonList")).size() %>
			</bean:define>
		<tr>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
					<bean:write name="infoShift" property="externalId"/>
				</html:multibox>
			</td>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<bean:write name="infoShift" property="infoDisciplinaExecucao.sigla"/>
			</td>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<html:link page="<%= "/manageShift.do?method=prepareEditShift&amp;page=0&amp;"
               							+ PresentationConstants.SHIFT_OID
			  							+ "="
               				   			+ pageContext.findAttribute("infoShiftOID")
               				   			+ "&amp;"
			  							+ PresentationConstants.EXECUTION_COURSE_OID
  										+ "="
  										+ pageContext.findAttribute("infoExecutionCourseOID")
               				   			+ "&amp;"
			  							+ PresentationConstants.ACADEMIC_INTERVAL
  										+ "="
  										+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  										+ "&amp;"
  										+ PresentationConstants.CURRICULAR_YEAR_OID
			  							+ "="
  										+ pageContext.findAttribute("curricularYearOID")
  										+ "&amp;"
			  							+ PresentationConstants.EXECUTION_DEGREE_OID
  										+ "="
  										+ pageContext.findAttribute("executionDegreeOID") %>">
						<bean:write name="infoShift" property="nome"/>
					</html:link>
			</td>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<bean:write name="infoShift" property="shift.shiftTypesPrettyPrint"/>            </td>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<bean:write name="infoShift" property="lotacao"/>
            </td>

			<logic:equal name="numberOfLessons" value="0">
	         	<td></td>
	         	<td></td>
	         	<td></td>
	         	<td></td>
	         	<td></td>
			</logic:equal>

       		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1">
              	<td>
					<bean:write name="infoLesson" property="diaSemana"/> 
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="infoLesson" property="inicio.timeInMillis"/>
					</dt:format>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="infoLesson" property="fim.timeInMillis"/>
					</dt:format>
				</td>
				<td>
					<logic:notEmpty name="infoLesson" property="infoSala">	
						<bean:write name="infoLesson" property="infoSala.nome"/>
					</logic:notEmpty>	
				</td>
				<td>
					<logic:notEmpty name="infoLesson" property="infoSala">
						<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
					</logic:notEmpty>	
				</td>
	        </logic:iterate>
			<logic:equal name="numberOfLessons" value="0">
				<td>
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
               		<html:link page="<%= "/manageClass.do?method=removeShift&amp;"
               							+ PresentationConstants.SHIFT_OID
			  							+ "="
               				   			+ pageContext.findAttribute("infoShiftOID")
               				   			+ "&amp;"
			  							+ PresentationConstants.CLASS_VIEW_OID
  										+ "="
  										+ pageContext.findAttribute("classOID")
               				   			+ "&amp;"
                                        + PresentationConstants.ACADEMIC_INTERVAL
               		                    + "="
               		                    + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  										+ "&amp;"
  										+ PresentationConstants.CURRICULAR_YEAR_OID
			  							+ "="
  										+ pageContext.findAttribute("curricularYearOID")
  										+ "&amp;"
			  							+ PresentationConstants.EXECUTION_DEGREE_OID
  										+ "="
  										+ pageContext.findAttribute("executionDegreeOID") %>"
		  								onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message key="link.schedules.remove"/>
					</html:link>
			</td>

		</tr>
          	<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
				<tr>
            	  	<td>
						<bean:write name="infoLesson" property="diaSemana"/> 
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<logic:notEmpty name="infoLesson" property="infoSala">
							<bean:write name="infoLesson" property="infoSala.nome"/>
						</logic:notEmpty>	
					</td>
					<td>
						<logic:notEmpty name="infoLesson" property="infoSala">
							<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
						</logic:notEmpty>	
					</td>
				</tr>
	        </logic:iterate>
	</logic:iterate>
	</table>







	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="link.schedules.remove"/>
	</html:submit>			
  </html:form>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.SHIFTS %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.shifts.none"/></span>	
	</p>
</logic:notPresent>