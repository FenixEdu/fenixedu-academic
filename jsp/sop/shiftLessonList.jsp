<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<br />
<br />
Aulas já atribuidas ao turno
<br />
	<logic:present name="shift" property="infoLessons">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden property="method" value="deleteLessons"/>
	<html:hidden property="page" value="1"/>

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<td class="listClasses-header">
				</td>
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
				<td class="listClasses-header">
		        </td>
				<td class="listClasses-header">
		        </td>
			</tr>
			<bean:define id="deleteConfirm">
				return confirm('<bean:message key="message.confirm.delete.lesson"/>')
			</bean:define>			
			<logic:iterate id="lesson" name="shift" property="infoLessons">
				<bean:define id="lessonOID" name="lesson" property="idInternal"/>
				<tr align="center">
              		<td class="listClasses">
						<html:multibox property="selectedItems">
							<bean:write name="lesson" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="lesson" property="diaSemana"/>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<bean:write name="lesson" property="infoSala.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="lesson" property="infoSala.capacidadeNormal"/>
					</td>
					<td class="listClasses">
	               		<html:link page="<%= "/manageLesson.do?method=prepareEdit&amp;page=0&amp;"
    	           							+ SessionConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ SessionConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ SessionConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
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
							<bean:message key="link.edit"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%= "/manageLesson.do?method=deleteLesson&amp;page=0&amp;"
    	           							+ SessionConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ SessionConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ SessionConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
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
								<bean:message key="link.delete"/>
						</html:link>					
					</td>
				</tr>
			</logic:iterate>
		</table>
		<html:submit styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
			<bean:message key="link.delete"/>
		</html:submit>
	  </html:form>
	</logic:present>
	<logic:notPresent name="shift" property="infoLessons">
		<span class="error">
			<bean:message key="message.shift.lessons.none"/>
		</span>
		<br />
	</logic:notPresent>