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
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page import="java.util.List"%>

<jsp:include page="/commons/contextExecutionDegreeAndCurricularYear.jsp"/>

<h2><bean:message key="link.manage.turnos"/></h2>

<h3>Adicionar Turnos</h3>

<bean:define id="schoolClass" name="context.classView" />

<logic:present name="<%= PresentationConstants.SHIFTS %>" scope="request">
	<html:form action="/addShifts" focus="selectedItems">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="add"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

        <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                     value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CLASS_VIEW_OID %>" property="<%= PresentationConstants.CLASS_VIEW_OID %>"
					 value="${schoolClass.externalId}"/>

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
            	<bean:write name="infoShift" property="shift.shiftTypesPrettyPrint"/>
            </td>
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

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.add"/>
		</html:submit>			
	</p>
	
	</html:form>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.SHIFTS %>" scope="request">
	<p class="warning0">
		<span class="error"><!-- Error messages go here --><bean:message key="errors.shifts.none"/></span>	
	</p>
</logic:notPresent>