<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="label.summaries"/>
</h2>

<bean:define id="summariesSearchBean" name="executionCourse" property="summariesSearchBean"/>
<!-- Place Renderer Here ... -->

<bean:define id="summaries" name="executionCourse" property="associatedSummaries"/>
<logic:empty name="summaries">
	<h3>
		<bean:message key="message.summaries.not.available" />
	</h3>
</logic:empty>
<logic:notEmpty name="summaries">
	<logic:iterate id="summary" name="summaries" >	
		<bean:define id="summaryId" type="java.lang.Integer" name ="summary" property="idInternal" />
		<div id="<%= "s" + summaryId %>">
			<logic:present name="summary" property="shift">
				<h3>
					<dt:format pattern="dd/MM/yyyy">
						<bean:write name="summary" property="summaryDate.time"/>
					</dt:format>
					<dt:format pattern="HH:mm">
						<bean:write name="summary" property="summaryHour.time"/>
					</dt:format>
				   	
					<logic:present name="summary" property="room">
						<logic:notEmpty name="summary" property="room">
							(<bean:message key="label.room" /> <bean:write name="summary" property="room.nome" />)
			       		</logic:notEmpty>
			       	</logic:present>

					<br/>

					<span class="greytxt">
						<logic:empty name="summary" property="isExtraLesson">
							<bean:message key="label.summary.lesson" />
						</logic:empty>
						<logic:notEmpty name="summary" property="isExtraLesson">
				       		<logic:equal name="summary" property="isExtraLesson" value="false">		
								<bean:message key="label.summary.lesson" />
				       		</logic:equal>
				       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
								<bean:message key="label.extra.lesson" />
				       		</logic:equal>
				       	</logic:notEmpty>
						<bean:write name="summary" property="shift.tipo.fullNameTipoAula" />	
					</span>       	
				</h3>
			</logic:present>
		
			<logic:notPresent name="summary" property="infoShift">
				<bean:message key="label.summary.lesson" />
				&nbsp;
				<dt:format pattern="dd/MM/yyyy">
					<bean:write name="summary" property="summaryDate.time"/>
				</dt:format>
				&nbsp;
				<dt:format pattern="HH:mm">
					<bean:write name="summary" property="summaryHour.time"/>
				</dt:format>
			</logic:notPresent>
		
			<logic:present name="summary" property="title">	
				<logic:notEmpty name="summary" property="title">		
					<p><strong><bean:write name="summary" property="title"/></strong></p>
				</logic:notEmpty>
			</logic:present>
		
			<bean:write name="summary" property="summaryText" filter="false"/><br/>
		
			<div class="details">
				<span class="updated-date">
					<bean:message key="message.modifiedOn" />
					<dt:format pattern="dd/MM/yyyy HH:mm">
						<bean:write name="summary" property="lastModifiedDate.time"/>
					</dt:format>
				</span>
		
				<logic:notEmpty name="summary" property="professorship">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />				
						<bean:write name="summary" property="professorship.teacher.person.nome" />	
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="teacher">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />
						<bean:write name="summary" property="teacher.person.nome" />
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="teacherName">
					<span class="author">
						<bean:write name="summary" property="teacherName" />
					</span>
				</logic:notEmpty>				
		
				<logic:present name="summary" property="studentsNumber">
					<span class="comment">
						<bean:message key="message.presences" />			
						<logic:notEmpty name="summary" property="studentsNumber">			
							<bean:define id="studentsAttended" name="summary" property="studentsNumber" />
							<logic:greaterThan name="studentsAttended" value="0">
								<bean:message key="message.students" arg0="<%= studentsAttended.toString() %>"/>
							</logic:greaterThan>
				
							<logic:lessEqual name="studentsAttended" value="0">
								<i><bean:message key="message.notSpecified" /></i>				
							</logic:lessEqual>
						</logic:notEmpty>
			
						<logic:empty name="summary" property="studentsNumber">			
							<i><bean:message key="message.notSpecified" /></i>								
						</logic:empty>
					</span>
				</logic:present>				
		
				<logic:notPresent name="summary" property="studentsNumber">			
					<span class="comment">
						<bean:message key="message.presences" />
						<i><bean:message key="message.notSpecified" /></i>								
					</span>
				</logic:notPresent>
			</div>

		</div>
	</logic:iterate>
</logic:notEmpty>
