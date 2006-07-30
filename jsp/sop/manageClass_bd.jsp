<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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
		<td width="10"></td>
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
		<td width="10"></td>
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

<br />
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

	<span class="error"><!-- Error messages go here --><html:errors /></span>
   	<br />
   	<bean:define id="degree" type="net.sourceforge.fenixedu.domain.Degree" name="schoolClass" property="executionDegree.degreeCurricularPlan.degree"/>
   	<bean:define id="curricularYear" type="java.lang.Integer" name="schoolClass" property="anoCurricular"/>
   	<%= degree.constructSchoolClassPrefix(curricularYear) %>
   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
   		<bean:message key="label.change"/>
   	</html:submit>
</html:form>

<br/>
Turnos associados a turma:
<br />
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

	<table>
		<tr>
			<th class="listClasses-header" rowspan="2">
			</th>
			<th class="listClasses-header" rowspan="2">
				<bean:message key="property.executionCourse"/>
			</th>
			<th class="listClasses-header" rowspan="2">
				<bean:message key="property.shift.name"/>
			</th>
	        <th class="listClasses-header" rowspan="2">
	        	<bean:message key="property.shift.type"/>
	        </th>
			<th class="listClasses-header" rowspan="2">
				<bean:message key="property.shift.capacity"/>
			</th>
			<th class="listClasses-header" colspan="5">
	        	<bean:message key="property.lessons"/>
	        </th>
			<th class="listClasses-header" rowspan="2">
	        </th>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="property.weekday"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="property.time.start"/>
			</th>
		       <th class="listClasses-header">
		       	<bean:message key="property.time.end"/>
	    	   </th>
			<th class="listClasses-header">
				<bean:message key="property.room"/>
			</th>
			<th class="listClasses-header">
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
		<tr align="center">
			<logic:equal name="numberOfLessons" value="0">
				<td class="listClasses">
			</logic:equal>
			<logic:notEqual name="numberOfLessons" value="0">
	        	<td class="listClasses" rowspan="<%= pageContext.findAttribute("numberOfLessons") %>">
			</logic:notEqual>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
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
            	<bean:message name="infoShift" property="tipo.name" bundle="ENUMERATION_RESOURCES"/>
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
					<logic:notEmpty name="infoLesson" property="infoSala.nome">	
						<bean:write name="infoLesson" property="infoSala.nome"/>
					</logic:notEmpty>	
				</td>
				<td class="listClasses">
					<logic:notEmpty name="infoLesson" property="infoSala.nome">
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
						<logic:notEmpty name="infoLesson" property="infoSala.nome">
							<bean:write name="infoLesson" property="infoSala.nome"/>
						</logic:notEmpty>	
					</td>
					<td class="listClasses">
						<logic:notEmpty name="infoLesson" property="infoSala.nome">
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
	<span class="error"><!-- Error messages go here --><bean:message key="errors.shifts.none"/></span>	
</logic:notPresent>