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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="java.util.List"%>
<html:xhtml/>

<jsp:include page="/commons/contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turnos"/> 
	<span class="small">${context_selection_bean.executionDegree.degreeCurricularPlan.name} - ${context_selection_bean.curricularYear.year}º ano (${context_selection_bean.academicInterval.pathName})</span></h2>

<jsp:include page="context.jsp"/>

<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span>

<h3 class="mbottom05"><bean:message key="title.create.shift"/></h3>

<html:form action="/manageShifts" focus="nome">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createShift"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
			 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<table class="tstyle5 thlight thright mtop05">
		<tr>
			<th>
				<bean:message key="property.turno.disciplina"/>:
			</th>
			<td>
				<bean:define id="executionCourseList" name="<%= PresentationConstants.EXECUTION_COURSE_LIST_KEY %>"/>
				<html:select bundle="HTMLALT_RESOURCES" property="courseInitials" size="1" 
					onchange="this.form.method.value='listExecutionCourseCourseLoads';this.form.submit();">
					<html:option value=""><!-- w3c complient --></html:option>
					<html:options property="sigla" labelProperty="nome" collection="executionCourseList"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.turno.types"/>:
			</th>
			<td>			
				<logic:notEmpty name="tiposAula">									
					<logic:iterate id="tipoAula" name="tiposAula">
						<html:multibox property="shiftTiposAula">
							<bean:write name="tipoAula" property="value"/>
						</html:multibox>
						<bean:write name="tipoAula" property="label"/>
					</logic:iterate>				
				</logic:notEmpty>		
				<logic:empty name="tiposAula">
					--
				</logic:empty>					
			</td>
		</tr>
        <tr>
            <th>
                <bean:message key="property.turno.capacity"/>:
            </th>
            <td>
                <html:text bundle="HTMLALT_RESOURCES" altKey="text.lotacao" property="lotacao" size="11" maxlength="20"/>
            </td>
        </tr>		
	</table>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.create"/>
	</html:submit>

	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>

</html:form>


<h3 class="mtop2 mbottom05"><bean:message key="title.manage.turnos"/></h3>

<logic:present name="<%= PresentationConstants.SHIFTS %>" scope="request">
  <html:form action="/deleteShifts">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteShifts"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.delete.shift"/>')
	</bean:define>

	<table class="tstyle4 mtop05">
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
			<th colspan="6">
	        	<bean:message key="property.lessons"/>
	        </th>
			<th rowspan="2">
	        </th>
		</tr>
		<tr>
			<th>
				<bean:message bundle="SOP_RESOURCES" key="label.lesson.week"/>
			</th>
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
			<bean:define id="numberOfLessons"><%= ((List) pageContext.findAttribute("infoShiftLessonList")).size() %></bean:define>
		<tr align="center">
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
					<bean:write name="infoShift" property="externalId"/>
				</html:multibox>
			</td>
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<bean:write name="infoShift" property="infoDisciplinaExecucao.sigla"/>
			</td>
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
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
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>            	
            	<bean:write name="infoShift" property="shift.shiftTypesPrettyPrint"/>            	            	
            </td>
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<bean:write name="infoShift" property="lotacao"/>
            </td>

			<logic:equal name="numberOfLessons" value="0">
	         	<td class="listClasses"></td>
	         	<td class="listClasses"></td>
	         	<td class="listClasses"></td>
	         	<td class="listClasses"></td>
	         	<td class="listClasses"></td>
			</logic:equal>

       		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1">
           	  	<td class="listClasses">
					<bean:write name="infoLesson" property="occurrenceWeeksAsString"/>
				</td>
              	<td class="listClasses">
					<bean:write name="infoLesson" property="diaSemana"/> 
				</td>
				<td class="listClasses">
					<dt:format pattern="HH:mm">
						<bean:write name="infoLesson" property="inicio.timeInMillis"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<dt:format pattern="HH:mm">
						<bean:write name="infoLesson" property="fim.timeInMillis"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<logic:notEmpty name="infoLesson" property="infoSala">
						<bean:write name="infoLesson" property="infoSala.nome"/>
					</logic:notEmpty>	
				</td>
				<td class="listClasses">
					<logic:notEmpty name="infoLesson" property="infoSala">
						<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
					</logic:notEmpty>	
				</td>
	        </logic:iterate>
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
              		<html:link page="<%= "/manageShifts.do?method=deleteShift&amp;page=0&amp;"
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
  										+ pageContext.findAttribute("executionDegreeOID") %>"
  										onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message key="link.delete"/>
				</html:link>
			</td>

		</tr>
          	<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
				<tr>
            	  	<td class="listClasses">
						<bean:write name="infoLesson" property="occurrenceWeeksAsString"/>
					</td>
            	  	<td class="listClasses">
						<bean:write name="infoLesson" property="diaSemana"/> 
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<logic:notEmpty name="infoLesson" property="infoSala">
							<bean:write name="infoLesson" property="infoSala.nome"/>
						</logic:notEmpty>	
					</td>
					<td class="listClasses">
						<logic:notEmpty name="infoLesson" property="infoSala">
							<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
						</logic:notEmpty>	
					</td>
				</tr>
	        </logic:iterate>
	</logic:iterate>
	</table>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="label.delete"/>
	</html:submit>
  </html:form>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.SHIFTS %>" scope="request">
	<p>
		<em><!-- Error messages go here --><bean:message key="errors.shifts.none"/></em>	
	</p>
</logic:notPresent>
