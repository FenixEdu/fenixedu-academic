<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %> 
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
			 
<bean:define id="infoShiftsPercentageList" name="infoShiftPercentageList" scope="request" />

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/executionCourseShiftsPercentageManager">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="accept"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherOID" property="teacherOID"/>
	
	<table width="100%">
		<tr>
			<th rowspan="2" class="listClasses-header"><bean:message key="label.shift"/></th>
			<th rowspan="2" class="listClasses-header"><bean:message key="label.shift.type"/></th>
			<th colspan="4" class="listClasses-header"><bean:message key="label.lessons"/></th>
			<th rowspan="2" class="listClasses-header"><bean:message key="label.professorship.question"/></th>			
			<th rowspan="2" class="listClasses-header"><bean:message key="label.professorship.percentage"/></th>
			<th class="listClasses-header"><bean:message key="label.teacher.applied"/></th>			
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="label.day.of.week"/></th>
			<th class="listClasses-header"><bean:message key="label.lesson.start"/></th>
			<th class="listClasses-header"><bean:message key="label.lesson.end"/></th>
			<th class="listClasses-header"><bean:message key="label.lesson.room"/></th>			
			<th class="listClasses-header"><bean:message key="label.teacher"/> - <bean:message key="label.professorship.percentage"/></th>
		</tr> 
		<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
			<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
			<bean:size id="lessonsSize" name="infoShiftPercentage" property="infoLessons" />	
				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
						<td class="listClasses"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
						<td class="listClasses" colspan="7"> Não tem aulas </td>
					</tr>
				</logic:equal>
				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="infoLesson" name="infoShiftPercentage" property="infoLessons" indexId="indexLessons" >
		        		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
				        <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
			            <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
			            <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
			            
			            <logic:equal name="indexLessons" value="0">
							<tr>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.nome"/></td>
							<td class="listClasses" rowspan="<%= lessonsSize %>"><bean:write name="infoShiftPercentage" property="shift.tipo.siglaTipoAula"/></td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/>
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
							</td>
							<td class="listClasses">
								<logic:notEmpty name="infoLesson" property="infoSala.nome">	
									<bean:write name="infoLesson" property="infoSala.nome"/>					
								</logic:notEmpty>	
							</td>
							
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftProfessorships" property="shiftProfessorships">
									<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</html:multibox>
							</td>	

							<td class="listClasses" rowspan="<%= lessonsSize %>">
						  		<bean:define id="propertyName">
									percentage_<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
								</bean:define>
								<html:text alt='<%= propertyName.toString() %>' property='<%= propertyName.toString() %>' size="4" value=""/> %
							</td>
							<td class="listClasses" rowspan="<%= lessonsSize %>">
								<bean:size id="shiftPercentageSize" name="infoShiftPercentage" property="teacherShiftPercentageList"/>
								<logic:equal name="shiftPercentageSize" value="0">&nbsp;</logic:equal>
								<logic:iterate id="teacherShiftPercentage"	name="infoShiftPercentage" property="teacherShiftPercentageList" indexId="indexPercentage">						
						    		<bean:write name="teacherShiftPercentage" property="infoProfessorship.infoTeacher.infoPerson.nome" />
			 						&nbsp;-&nbsp;<bean:write name="teacherShiftPercentage" property="percentage" />
			 						<br/>
								</logic:iterate> 					
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td class="listClasses">
									<bean:write name="infoLesson" property="diaSemana"/>
								</td>
								<td class="listClasses">
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>				
								</td>
								<td class="listClasses">
									<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>	
								</td>
								<td class="listClasses">
									<logic:notEmpty name="infoLesson" property="infoSala.nome">
										<bean:write name="infoLesson" property="infoSala.nome"/>					
									</logic:notEmpty>	
								</td>						
							</tr>
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
		</logic:iterate>
	</table>
	<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" value="Submeter"/>
</html:form>