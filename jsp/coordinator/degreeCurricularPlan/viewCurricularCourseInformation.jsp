<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="DataBeans.InfoCurricularCourseScope, java.lang.Boolean, Util.Data, java.util.Calendar" %>
<logic:present name="infoCurriculum">
	<logic:notPresent name="executionYear"> 
		<%-- want to see current curricular course information --%>
<%--		<h2><bean:message key="label.coordinator.degreeCurricularPlan.current.information"/>&nbsp;-&nbsp;<str:capitalizeAllWords><str:lowerCase><bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></str:lowerCase></str:capitalizeAllWords></h2> --%>
		<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></h2>
	</logic:notPresent>
	<logic:present name="executionYear"> 
		<%-- want to see curricular course information history --%>
<%--		<h2><bean:message key="label.coordinator.degreeCurricularPlan.history.information"/>&nbsp;-&nbsp;<str:capitalizeAllWords><str:lowerCase><bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></str:lowerCase></str:capitalizeAllWords>&nbsp;-&nbsp;<bean:write name="executionYear"/></h2> --%>
		<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="executionYear"/></h2>
	</logic:present>
	<p>
		<logic:present name="executionYear">
			(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") %>">
				<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
			</html:link>)
			<br />
		</logic:present>
		<bean:define id="curricularCourseCode" name="infoCurriculum" property="infoCurricularCourse.idInternal"/>
		<bean:define id="curricularCourseName" name="infoCurriculum" property="infoCurricularCourse.name"/>
		(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;infoCurricularCourseName=" + curricularCourseName%>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=generalObjectives" %>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=operacionalObjectives" %>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=program" %>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=generalObjectivesEn" %>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=operacionalObjectivesEn" %>">
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
					(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;whatToEdit=programEn" %>">
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

		<%--
		<logic:notEmpty name="infoCurriculum" property="lastModificationDate">
			<tr>
				<td><br /><br />
					<bean:define id="lastModificationDate" name="infoCurriculum" property="lastModificationDate" type="java.util.Date"/>
					<b><bean:message key="label.coordinator.degreeSite.lastModificationDate"/>
					<%= Data.format2DayMonthYear(lastModificationDate)%></b>		
				</td>
			</tr>
		</logic:notEmpty>--%>

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
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoreticalHours" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.praticalHours" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoPratHours" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.labHours" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.maxIncrementNac" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.minIncrementNac" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.credits" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.ectsCredits" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.weigth" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.beginDate" />
							</td>
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
								<td class="listClasses"><bean:write name="curricularCourseScope" property="theoreticalHours"/>
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
								<td class="listClasses"><bean:write name="infoBranch" property="prettyCode"/>
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
					<i><bean:message key="label.manager.executionCourses.nonExisting"/></i>
				</logic:empty>
	
				<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.infoAssociatedExecutionCourses">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourse.site" />
							</td>
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
										<bean:message key="label.manager.yes"/>
									</logic:equal>
									<logic:notEqual name="hasSite" value="true">
										<bean:message key="label.manager.no"/>
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