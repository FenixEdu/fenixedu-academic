<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="executionCourse">

	<h2><bean:message key="label.lessonPlannings.managent"/></h2>
	
	<ul>
		<li>
			<html:link page="/manageExecutionCourse.do?method=prepareCreateLessonPlanning&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.create.lessonPlanning"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=prepareImportLessonPlannings&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.import.lessonPlanning"/>
			</html:link>
		</li>
	</ul>

	<div class="infoop2">
		<bean:message key="label.lessonsPlanning.instructions"/>
	</div>
		
	<bean:define id="uri" toScope="page" type="java.lang.String">/manageExecutionCourse.do?method=lessonPlannings&executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>

	
	<fr:form>
		<fr:edit id="lessonPlanningAvailable" name="executionCourse" property="site" schema="lessonPlanningAvailable" nested="true">
			<fr:destination name="postBack" path="<%= uri %>"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
		</fr:edit>
	</fr:form>

	<bean:define id="edit">/manageExecutionCourse.do?method=prepareEditLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0</bean:define>	
	<bean:define id="delete">/manageExecutionCourse.do?method=deleteLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0</bean:define>	
	<bean:define id="moveUp">/manageExecutionCourse.do?method=moveUpLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0</bean:define>
	<bean:define id="moveDown">/manageExecutionCourse.do?method=moveDownLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0</bean:define>	

	<logic:iterate id="lessonPlannings" name="lessonPlanningsMap">
		<logic:notEmpty name="lessonPlannings" property="value">
			<h3 class="mtop2"><span class="underline1"><bean:message key="label.lessons"/> <bean:message name="lessonPlannings" property="key.name" bundle="APPLICATION_RESOURCES"/></span></h3>
			<bean:define id="deleteLessonPlanings">/manageExecutionCourse.do?method=deleteLessonPlannings&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&shiftType=<bean:write name="lessonPlannings" property="key"/></bean:define>			
			<ul>
				<li><html:link titleKey="link.delete.all.lessonPlannings.by.type.title" action="<%= deleteLessonPlanings %>" onclick="return confirm('Tem a certeza que deseja apagar todos os planos deste tipo?')"><bean:message key="link.delete.all.lessonPlannings.by.type"/></html:link></li>
			</ul>
		</logic:notEmpty>
		<logic:iterate id="lessonPlanning" name="lessonPlannings" property="value" indexId="index">
			<div style="width: 550px;">
				<p class="mtop2 mbottom0"><em><bean:message key="label.lessonPlanning"/></em> <em><bean:write name="lessonPlanning" property="orderOfPlanning"/></em></p>		
	
				<fr:view name="lessonPlanning" schema="ViewLessonPlanning">
					<fr:layout name="flow">
						<fr:property name="classes" value="coutput1"/>
						<fr:property name="labelTerminator" value=""/>
						<fr:property name="labelExcluded" value="true"/>
						<fr:property name="eachClasses" value="bold," />
						<fr:property name="eachInline" value="false" />
					</fr:layout>
				</fr:view>
				
				<p class="mtop05">
					<html:link page="<%= edit %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
						<bean:message key="link.edit"/>
					</html:link>, 
					<html:link page="<%= delete %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
						<bean:message key="link.delete"/>
					</html:link>&nbsp;&nbsp;
					<span style="color: #555;"><bean:message key="label.moveTo"/>: </span>
					<html:link page="<%= moveUp %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
						<bean:message key="link.move.up"/>
					</html:link>, 
					<html:link page="<%= moveDown %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
						<bean:message key="link.move.down"/>
					</html:link>
				</p>
			</div>
		</logic:iterate>				
	</logic:iterate>

</logic:present>