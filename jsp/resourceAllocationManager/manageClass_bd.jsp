<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>


<p class="mbottom05">O curso seleccionado &eacute;:</p>
<strong><jsp:include page="context.jsp"/></strong>


<h3>Manipular Turma</h3>


<table class="mbottom1">
	<tr>
		<td>
			<html:form action="/manageClass">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareAddShifts"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
			
				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.shifts.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td></td>
		<td>
			<html:form action="/manageClass">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewSchedule"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
			
				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.shcedule.view"/>
				</html:submit>			
			</html:form>
		</td>
		<td></td>
		<td>
			<html:form action="/manageClasses">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listClasses"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>


<html:form action="/manageClass" focus="className">

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>
	
	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

   	<bean:define id="degree" type="net.sourceforge.fenixedu.domain.Degree" name="schoolClass" property="executionDegree.degreeCurricularPlan.degree"/>
   	<bean:define id="curricularYear" type="java.lang.Integer" name="schoolClass" property="anoCurricular"/>
   	<%= degree.constructSchoolClassPrefix(curricularYear) %>
   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
   	
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
   		<bean:message key="label.change"/>
   	</html:submit>

</html:form>


<p class="mtop15 mbottom05">Turnos associados a turma:</p>

<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">

	<html:form action="/removeShifts">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeShifts"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
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
		<logic:iterate id="infoShift" name="<%= SessionConstants.SHIFTS %>">
			<bean:define id="infoShiftOID" name="infoShift" property="idInternal"/>
			<bean:define id="infoExecutionCourseOID" name="infoShift" property="infoDisciplinaExecucao.idInternal"/>
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
					<bean:write name="infoShift" property="idInternal"/>
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
               							+ SessionConstants.SHIFT_OID
			  							+ "="
               				   			+ pageContext.findAttribute("infoShiftOID")
               				   			+ "&amp;"
			  							+ SessionConstants.EXECUTION_COURSE_OID
  										+ "="
  										+ pageContext.findAttribute("infoExecutionCourseOID")
               				   			+ "&amp;"
			  							+ SessionConstants.EXECUTION_PERIOD_OID
  										+ "="
  										+ pageContext.findAttribute("executionPeriodOID")
  										+ "&amp;"
  										+ SessionConstants.CURRICULAR_YEAR_OID
			  							+ "="
  										+ pageContext.findAttribute("curricularYearOID")
  										+ "&amp;"
			  							+ SessionConstants.EXECUTION_DEGREE_OID
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
               							+ SessionConstants.SHIFT_OID
			  							+ "="
               				   			+ pageContext.findAttribute("infoShiftOID")
               				   			+ "&amp;"
			  							+ SessionConstants.CLASS_VIEW_OID
  										+ "="
  										+ pageContext.findAttribute("classOID")
               				   			+ "&amp;"
			  							+ SessionConstants.EXECUTION_PERIOD_OID
  										+ "="
  										+ pageContext.findAttribute("executionPeriodOID")
  										+ "&amp;"
  										+ SessionConstants.CURRICULAR_YEAR_OID
			  							+ "="
  										+ pageContext.findAttribute("curricularYearOID")
  										+ "&amp;"
			  							+ SessionConstants.EXECUTION_DEGREE_OID
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

<logic:notPresent name="<%= SessionConstants.SHIFTS %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.shifts.none"/></span>	
	</p>
</logic:notPresent>