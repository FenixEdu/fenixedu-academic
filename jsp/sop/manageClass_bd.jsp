<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<br />

<h2>Manipular Turma</h2>

<br />
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
			<html:form action="/manageClass">
				<html:hidden property="method" value="prepareAddShifts"/>
				<html:hidden property="page" value="0"/>
			
				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit styleClass="inputbutton">
					<bean:message key="label.shifts.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10"></td>
		<td>
			<html:form action="/manageClass">
				<html:hidden property="method" value="viewSchedule"/>
				<html:hidden property="page" value="0"/>
			
				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit styleClass="inputbutton">
					<bean:message key="label.shcedule.view"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10"></td>
		<td>
			<html:form action="/manageClasses">
				<html:hidden property="method" value="listClasses"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>

<br />
<html:form action="/manageClass" focus="className">

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value= "1"/>

	<span class="error"><html:errors/></span>
   	<br />
   	<html:text property="className"/>
   	<br />
   	<html:submit styleClass="inputbuttonSmall">
   		<bean:message key="label.change"/>
   	</html:submit>
</html:form>

<br/>
Turnos associados a turma:
<br />
<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">

	<html:form action="/removeShifts">
		<html:hidden property="method" value="removeShifts"/>
		<html:hidden property="page" value="1"/>

		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
					 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.remove.shift"/>')
	</bean:define>			

	<table>
		<tr>
			<td class="listClasses-header" rowspan="2">
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="property.executionCourse"/>
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="property.shift.name"/>
			</td>
	        <td class="listClasses-header" rowspan="2">
	        	<bean:message key="property.shift.type"/>
	        </td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="property.shift.capacity"/>
			</td>
			<td class="listClasses-header" colspan="5">
	        	<bean:message key="property.lessons"/>
	        </td>
			<td class="listClasses-header" rowspan="2">
	        </td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="property.weekday"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.time.start"/>
			</td>
		       <td class="listClasses-header">
		       	<bean:message key="property.time.end"/>
	    	   </td>
			<td class="listClasses-header">
				<bean:message key="property.room"/>
			</td>
			<td class="listClasses-header">
		       	<bean:message key="property.capacity"/>
		       </td>
		</tr>
		<logic:iterate id="infoShift" name="<%= SessionConstants.SHIFTS %>">
			<bean:define id="infoShiftOID" name="infoShift" property="idInternal"/>
			<bean:define id="infoExecutionCourseOID" name="infoShift" property="infoDisciplinaExecucao.idInternal"/>
			<bean:define id="infoShiftLessonList" name="infoShift" property="infoLessons"/>
			<bean:define id="numberOfLessons">
				<%= ((List) pageContext.findAttribute("infoShiftLessonList")).size() %>
			</bean:define>
		<tr align="center">
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<html:multibox property="selectedItems">
					<bean:write name="infoShift" property="idInternal"/>
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
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
            	<bean:write name="infoShift" property="tipo"/>
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
					<bean:write name="infoLesson" property="infoSala.nome"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
				</td>
	        </logic:iterate>
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
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
						<bean:write name="infoLesson" property="infoSala.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoLesson" property="infoSala.capacidadeNormal"/>
					</td>
				</tr>
	        </logic:iterate>
	</logic:iterate>
	</table>







	<html:submit styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="link.schedules.remove"/>
	</html:submit>			
  </html:form>
</logic:present>

<logic:notPresent name="<%= SessionConstants.SHIFTS %>" scope="request">
	<span class="error"><bean:message key="errors.shifts.none"/></span>	
</logic:notPresent>