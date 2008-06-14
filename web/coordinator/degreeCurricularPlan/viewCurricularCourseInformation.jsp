<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="java.lang.Boolean, java.util.Calendar" %>
<logic:present name="infoCurriculum">
	<logic:notPresent name="executionYear"> 
		<%-- want to see current curricular course information --%>
		<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></h2>
	</logic:notPresent>
	<logic:present name="executionYear"> 
		<%-- want to see curricular course information history --%>
		<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="executionYear"/></h2>
	</logic:present>
	<p>
		<logic:present name="executionYear">
			(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") %>">
				<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
			</html:link>)
			<br />
		</logic:present>
		<bean:define id="curricularCourseCode" name="infoCurriculum" property="infoCurricularCourse.idInternal"/>
		<bean:define id="curricularCourseName" name="infoCurriculum" property="infoCurricularCourse.name"/>
		(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;infoCurricularCourseName=" + curricularCourseName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
			<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></font>
		</html:link>)
	</p>
	<bean:define id="canEdit"><%=pageContext.findAttribute("canEdit")%></bean:define>
	
	<table>

		<tr>
			<td><br />
				<logic:equal name="canEdit" value="false">
					<%-- user has no authorization to edit information --%>
					<b><bean:message key="label.generalObjectives"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">
					<%-- user has authorization to edit information --%>
					<b><bean:message key="label.generalObjectives"/></b>&nbsp;
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)
				</logic:equal>
				<br />
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="generalObjectives">
					<bean:write name="infoCurriculum" property="generalObjectives"/>
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="generalObjectives">
				<bean:define id="labelGenObj"><bean:message key="label.generalObjectives"/></bean:define>
					<i><bean:message key="label.nonExisting" arg0="<%=labelGenObj%>"/></i>
				</logic:empty>
			</td>
		</tr>

		<tr>
			<td><br /><br />
				<logic:equal name="canEdit" value="false">
					<b><bean:message key="label.operacionalObjectives"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">
					<b><bean:message key="label.operacionalObjectives"/></b>&nbsp;
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)			
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
					<bean:write name="infoCurriculum" property="operacionalObjectives"/>
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="operacionalObjectives">
					<bean:define id="labelOpObj"><bean:message key="label.operacionalObjectives"/></bean:define>					
					<i><bean:message key="label.nonExisting" arg0="<%=labelOpObj%>"/></i>
				</logic:empty>
			</td>
		</tr>

		<tr>
			<td><br /><br />
				<logic:equal name="canEdit" value="false">
					<b><bean:message key="label.program"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">	
					<b><bean:message key="label.program"/></b>&nbsp;			
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="program">
					<bean:write name="infoCurriculum" property="program"/>
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="program">
					<bean:define id="labelProg"><bean:message key="label.program"/></bean:define>					
					<i><bean:message key="label.nonExisting" arg0="<%=labelProg%>"/></i>
				</logic:empty>
			</td>
		</tr>

		<tr>
			<td><br /><br />
				<logic:equal name="canEdit" value="false">
					<b><bean:message key="label.generalObjectives.eng"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">
					<b><bean:message key="label.generalObjectives.eng"/></b>&nbsp;
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;language=English" %>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="generalObjectivesEn">
					<bean:write name="infoCurriculum" property="generalObjectivesEn"/>
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="generalObjectivesEn">
					<bean:define id="labelGenObjEn"><bean:message key="label.generalObjectives.eng"/></bean:define>					
					<i><bean:message key="label.nonExisting" arg0="<%=labelGenObjEn%>"/></i>
				</logic:empty>
			</td>
		</tr>

		<tr>
			<td><br /><br />
				<logic:equal name="canEdit" value="false">
					<b><bean:message key="label.operacionalObjectives.eng"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">				
					<b><bean:message key="label.operacionalObjectives.eng"/></b>&nbsp;
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;language=English" %>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="operacionalObjectivesEn">
					<bean:write name="infoCurriculum" property="operacionalObjectivesEn"/>
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="operacionalObjectivesEn">
					<bean:define id="labelOpObjEn"><bean:message key="label.operacionalObjectives.eng"/></bean:define>					
					<i><bean:message key="label.nonExisting" arg0="<%=labelOpObjEn%>"/></i>
				</logic:empty>
			</td>
		</tr>

		<tr>
			<td><br /><br />
				<logic:equal name="canEdit" value="false">
					<b><bean:message key="label.program.eng"/></b>
				</logic:equal>
				<logic:equal name="canEdit" value="true">
					<b><bean:message key="label.program.eng"/></b>&nbsp;
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode")  +"&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;language=English" %>">
						<font color="#0066CC"><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></font>
					</html:link>)
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:notEmpty name="infoCurriculum" property="programEn">
					<bean:write name="infoCurriculum" property="programEn"/><br />
				</logic:notEmpty>
				<logic:empty name="infoCurriculum" property="programEn">
					<bean:define id="labelProgEn"><bean:message key="label.program.eng"/></bean:define>					
					<i><bean:message key="label.nonExisting" arg0="<%=labelProgEn%>"/></i>
				</logic:empty>
			</td>
		</tr>
		<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.mandatory">
			<tr>
				<td><br /><br />
					<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
							<b><bean:message key="label.coordinator.degreeCurricularPlan.cc.mandatory"/></b>
					</logic:equal>
					<logic:notEqual name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
							<b><bean:message key="label.coordinator.degreeCurricularPlan.cc.non.mandatory"/></b>
					</logic:notEqual>
				</td>
			</tr>
		</logic:notEmpty>

		<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.basic">
			<tr>
				<td><br /><br />
					<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
							<b><bean:message key="label.manager.curricularCourse.message.basic"/></b>
					</logic:equal>
					<logic:notEqual name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
							<b><bean:message key="label.manager.curricularCourse.message.non.basic"/></b>
					</logic:notEqual>
				</td>
			</tr>				
		</logic:notEmpty>

		<tr>	
			<td><br /><br />		
				<b><bean:message key="label.manager.curricularCourseScopes"/></b>		
				<logic:empty name="infoCurriculum" property="infoCurricularCourse.infoScopes">
					<i><bean:message key="label.manager.curricularCourseScopes.nonExisting"/></i>
					<br /><br />
				</logic:empty>
				<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.infoScopes">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<th class="listClasses-header" rowspan="2"><bean:message key="label.manager.curricularCourseScope.curricularYear" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="label.manager.curricularCourseScope.curricularSemester" />
							</th>
							<th class="listClasses-header" colspan="4"><bean:message key="label.coordinator.degreeCurricularPlan.scope.numberHours" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="message.manager.curricular.course.maxIncrementNac" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="message.manager.curricular.course.minIncrementNac" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="message.manager.curricular.course.credits" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="message.manager.curricular.course.ectsCredits" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="message.manager.curricular.course.weight" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="label.manager.curricularCourseScope.branch" />
							</th>
							<th class="listClasses-header" rowspan="2"><bean:message key="label.manager.curricularCourseScope.beginDate" />
							</th>
						</tr>
						<tr>
							<th class="listClasses-header"><bean:message key="label.coordinator.degreeCurricularPlan.scope.theoretical" />
							</th>
							<th class="listClasses-header"><bean:message key="label.coordinator.degreeCurricularPlan.scope.practical" />
							</th>
							<th class="listClasses-header"><bean:message key="label.coordinator.degreeCurricularPlan.scope.theoreticalPractical" />
							</th>
							<th class="listClasses-header"><bean:message key="label.coordinator.degreeCurricularPlan.scope.laboratorial" />
							</th>						
						</tr>
						<logic:iterate id="curricularCourseScope" name="infoCurriculum" property="infoCurricularCourse.infoScopes">
							<bean:define id="infoBranch" name="curricularCourseScope" property="infoBranch"/>
							<bean:define id="infoCurricularSemester" name="curricularCourseScope" property="infoCurricularSemester"/>
							<bean:define id="infoCurricularYear" name="infoCurricularSemester" property="infoCurricularYear"/>
							<tr>
								<td class="listClasses"><bean:write name="infoCurricularYear" property="year"/>
								</td>
								<td class="listClasses"><bean:write name="infoCurricularSemester" property="semester"/>
								</td>	 			
<%--								<td class="listClasses"><bean:write name="curricularCourseScope" property="theoreticalHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="praticalHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="theoPratHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="labHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="maxIncrementNac"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="minIncrementNac"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="credits"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="ectsCredits"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="weigth"/>
								</td> 
--%>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.theoreticalHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.praticalHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.theoPratHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.labHours"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.maximumValueForAcumulatedEnrollments"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.minimumValueForAcumulatedEnrollments"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.credits"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.ectsCredits"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourseScope" property="infoCurricularCourse.enrollmentWeigth"/>
								</td>

								<td class="listClasses">
									<logic:notEmpty name="infoBranch" property="prettyCode">
										<bean:write name="infoBranch" property="prettyCode"/>
									</logic:notEmpty>
									<logic:empty name="infoBranch" property="prettyCode">
										&nbsp;
									</logic:empty>
								</td>
								<td class="listClasses">
									<bean:define id="beginDate" name="curricularCourseScope" property="beginDate" type="java.util.Calendar"/>
									<%=beginDate.get(Calendar.DAY_OF_MONTH)%>/<%=String.valueOf(beginDate.get(Calendar.MONTH) + 1)%>/<%=beginDate.get(Calendar.YEAR)%>
								</td>
		 					</tr>
		 				</logic:iterate>			
					</table>
				</logic:notEmpty>
			</td>
		</tr>
		
		<tr>	
			<td><br /><br />		
				<b><bean:message key="label.manager.executionCourses"/></b>
				<logic:empty name="infoCurriculum" property="infoCurricularCourse.infoAssociatedExecutionCourses">
					<br />
					<i><bean:message key="label.manager.executionCourses.nonExisting" bundle="MANAGER_RESOURCES" /></i>
				</logic:empty>
	
				<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.infoAssociatedExecutionCourses">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<th class="listClasses-header"><bean:message key="label.manager.executionCourse.name" bundle="MANAGER_RESOURCES" />
							</th>
							<th class="listClasses-header"><bean:message key="label.manager.executionCourse.code" bundle="MANAGER_RESOURCES" />
							</th>
							<th class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" bundle="MANAGER_RESOURCES" />
							</th>
							<th class="listClasses-header"><bean:message key="label.manager.executionCourse.site" bundle="MANAGER_RESOURCES" />
							</th>
						</tr>
			
						<logic:iterate id="executionCourse" name="infoCurriculum" property="infoCurricularCourse.infoAssociatedExecutionCourses">
							<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
							<tr>	 			
								<td class="listClasses"><bean:write name="executionCourse" property="nome"/>
								</td>
								<td class="listClasses"><bean:write name="executionCourse" property="sigla"/>
								</td>
								<td class="listClasses"><bean:write name="infoExecutionPeriod" property="name"/> - <bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
								</td>
								<td class="listClasses">
									<bean:define id="hasSite" name="executionCourse" property="hasSite"/>
									<logic:equal name="hasSite" value="true">
										<bean:message key="label.manager.yes" bundle="MANAGER_RESOURCES" />
									</logic:equal>
									<logic:notEqual name="hasSite" value="true">
										<bean:message key="label.manager.no" bundle="MANAGER_RESOURCES" />
									</logic:notEqual>
								</td>
			 				</tr>
			 			</logic:iterate>
					</table>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
</logic:present>