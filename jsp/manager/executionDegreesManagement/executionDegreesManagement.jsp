<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>Editar <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></h2>

<html:messages id="messages" message="true">
	<span class="error"><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteExecutionDegrees"/>
	
	<div class='simpleblock4'>
	<fieldset class='lfloat'>
	
	<p><label><strong><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/></strong>:</label>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType" onchange="this.form.method.value='readDegreeCurricularPlans';this.form.submit();" >
			<html:options collection="degreeTypes" property="value" labelProperty="label" /> 
		</html:select>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</p>
	
	<logic:notEmpty name="degreeCurricularPlans">
		<p><label><strong><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan"/></strong>:</label>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID" onchange="this.form.method.value='readExecutionDegrees';this.form.submit();">
			<html:options collection="degreeCurricularPlans" property="value" labelProperty="label" /> 
		</html:select>
		<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
		</p>
	</logic:notEmpty>
	
	</fieldset>
	</div>

<span class="error"><html:errors/><br/></span>
<br/>
<logic:notEmpty name="executionDegrees">

		<table cellpadding='0' border='0'>
			<tr>
				<td class="listClasses-header"></td>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/> </th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/>
					1� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/>
					1� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/>
					2� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/>
					2� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.examsSpecialSeason"/>					
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason1"/>					
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason2"/>					
				</th>
				<th class='listClasses-header'> 
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionSpecialSeason"/>					
				</th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.temporaryExamMap"/> </th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.coordinator"/> </th>
				<th class='listClasses-header'> </th>
			</tr>
			<logic:iterate id="executionDegree" name="executionDegrees">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
							<bean:write name="executionDegree" property="idInternal"/>
						</html:multibox>
					</td>
					<td class='listClasses'>
						<bean:write name="executionDegree" property="executionYear.year" /> 
					</td>
					<td class='listClasses'>
						<bean:define id="periodLessonsFirstSemester" name="executionDegree" property="periodLessonsFirstSemester" />
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodLessonsFirstSemester" property="start.time" />
						</dt:format>
						<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodLessonsFirstSemester" property="end.time" />
						</dt:format>
					</td>
					<td class='listClasses'>
						<bean:define id="periodExamsFirstSemester" name="executionDegree" property="periodExamsFirstSemester" />
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodExamsFirstSemester" property="start.time" />
						</dt:format>
						<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodExamsFirstSemester" property="end.time" />
						</dt:format>
					</td>
					<td class='listClasses'>
						<bean:define id="periodLessonsSecondSemester" name="executionDegree" property="periodLessonsSecondSemester" />
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodLessonsSecondSemester" property="start.time" />
						</dt:format>
						<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodLessonsSecondSemester" property="end.time" />
						</dt:format>
					</td>
					<td class='listClasses'>
						<bean:define id="periodExamsSecondSemester" name="executionDegree" property="periodExamsSecondSemester" />
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodExamsSecondSemester" property="start.time" />
						</dt:format>
						<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="periodExamsSecondSemester" property="end.time" />
						</dt:format>
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree" property="periodExamsSpecialSeason">
							<bean:define id="periodExamsSpecialSeason" name="executionDegree" property="periodExamsSpecialSeason" />
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="periodExamsSpecialSeason" property="start.time" />
							</dt:format>
							<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="periodExamsSpecialSeason" property="end.time" />
							</dt:format>
						</logic:notEmpty>
						<logic:empty name="executionDegree" property="periodExamsSpecialSeason">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.periodNotDefined" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree" property="periodGradeSubmissionNormalSeasonFirstSemester">
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionNormalSeasonFirstSemester.start.time" />
							</dt:format>
							<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionNormalSeasonFirstSemester.end.time" />
							</dt:format>
						</logic:notEmpty>
						<logic:empty name="executionDegree" property="periodGradeSubmissionNormalSeasonFirstSemester">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.periodNotDefined" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree" property="periodGradeSubmissionNormalSeasonSecondSemester">
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionNormalSeasonSecondSemester.start.time" />
							</dt:format>
							<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionNormalSeasonSecondSemester.end.time" />
							</dt:format>
						</logic:notEmpty>
						<logic:empty name="executionDegree" property="periodGradeSubmissionNormalSeasonSecondSemester">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.periodNotDefined" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree" property="periodGradeSubmissionSpecialSeason">
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionSpecialSeason.start.time" />
							</dt:format>
							<p> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" /> </p>
							<dt:format pattern="dd/MM/yyyy">
								<bean:write name="executionDegree" property="periodGradeSubmissionSpecialSeason.end.time" />
							</dt:format>
						</logic:notEmpty>
						<logic:empty name="executionDegree" property="periodGradeSubmissionSpecialSeason">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.periodNotDefined" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<logic:equal name="executionDegree" property="temporaryExamMap" value="true">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.yes" />
						</logic:equal>
						<logic:notEqual name="executionDegree" property="temporaryExamMap" value="true">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.no" />
						</logic:notEqual>
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree"  property="coordinatorsList">
							<logic:iterate id="coordinator" name="executionDegree"  property="coordinatorsList">
								<bean:write name="coordinator" property="teacher.person.name" />
								<br/>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="executionDegree"  property="coordinatorsList">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.noCoordinatorsList" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<html:link module="/manager" action="/executionDegreesManagement.do?method=prepareEditExecutionDegree" paramId="executionDegreeID" paramName="executionDegree" paramProperty="idInternal">
							<bean:message bundle="MANAGER_RESOURCES" key="link.edit"/>
						</html:link>
						, <br/>
						<html:link module="/manager" action="/executionDegreesManagement.do?method=readCoordinators" paramId="executionDegreeID" paramName="executionDegree" paramProperty="idInternal">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.executionDegree.coordinators" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<bean:define id="onclick">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.execution.degrees"/>')
		</bean:define>
		<br/>			
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%=onclick.toString() %>'><bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.executionDegrees"/></html:submit>

	</logic:notEmpty>


</html:form>
