<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="DataBeans.InfoCurricularCourseScope, java.lang.Boolean, Util.Data, java.util.Calendar" %>
<logic:present name="infoCurriculum">
	<logic:notPresent name="executionYear">
		<h2><bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></h2>
	</logic:notPresent>
	<logic:present name="executionYear">
		<h2><bean:write name="infoCurriculum" property="infoCurricularCourse.name"/>&nbsp;-&nbsp;<bean:write name="executionYear"/></h2>
	</logic:present>
	<p>
		<logic:present name="executionYear">
			<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") %>">
				(<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>)
			</html:link><br />
		</logic:present>
		<bean:define id="curricularCourseCode" name="infoCurriculum" property="infoCurricularCourse.idInternal"/>
		<bean:define id="curricularCourseName" name="infoCurriculum" property="infoCurricularCourse.name"/>
		<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;infoCurricularCourseName=" + curricularCourseName%>">
			(<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></font>)
		</html:link>
	</p>
	<table>
		<logic:notEmpty name="infoCurriculum" property="generalObjectives">
			<tr>
				<td><h3><bean:message key="label.generalObjectives"/></h3></td>
			</tr>
			<tr>
				<td><bean:write name="infoCurriculum" property="generalObjectives"/></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
		<tr>
			<td><h3><bean:message key="label.operacionalObjectives"/></h3></td>
		</tr>
		<tr>
			<td><bean:write name="infoCurriculum" property="operacionalObjectives"/></td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="program">
		<tr>
			<td><h3><bean:message key="label.program"/></h3></td>
		</tr>
		<tr>
			<td><bean:write name="infoCurriculum" property="program"/></td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="generalObjectivesEn">
		<tr>
			<td><h3><bean:message key="label.generalObjectives.eng"/></h3></td>
		</tr>
		<tr>
			<td><bean:write name="infoCurriculum" property="generalObjectivesEn"/></td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="operacionalObjectivesEn">
		<tr>
			<td><h3><bean:message key="label.operacionalObjectives.eng"/></h3></td>
		</tr>
		<tr>
			<td><bean:write name="infoCurriculum" property="operacionalObjectivesEn"/></td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="programEn">
		<tr>
			<td><h3><bean:message key="label.program.eng"/></h3></td>
		</tr>
		<tr>
			<td><bean:write name="infoCurriculum" property="programEn"/><br /></td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="lastModificationDate">
		<tr>
			<td>
				<bean:define id="lastModificationDate" name="infoCurriculum" property="lastModificationDate" type="java.util.Date"/>
				<h3><bean:message key="label.coordinator.degreeSite.lastModificationDate"/>
				<%= Data.format2DayMonthYear(lastModificationDate)%></h3>		
			</td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.mandatory">
		<tr>
			<td>
				<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
						<h3><bean:message key="label.coordinator.degreeCurricularPlan.cc.mandatory"/></h3>
				</logic:equal>
				<logic:notEqual name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
						<h3><bean:message key="label.coordinator.degreeCurricularPlan.cc.non.mandatory"/></h3>
				</logic:notEqual>
			</td>
		</tr>
		</logic:notEmpty>
		<logic:notEmpty name="infoCurriculum" property="infoCurricularCourse.basic">
		<tr>
			<td>
				<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
						<h3><bean:message key="label.manager.curricularCourse.message.basic"/></h3>
				</logic:equal>
				<logic:notEqual name="infoCurriculum" property="infoCurricularCourse.basic" value="<%=Boolean.TRUE.toString()%>">
						<h3><bean:message key="label.manager.curricularCourse.message.non.basic"/></h3>
				</logic:notEqual>
			</td>
		</tr>				
		</logic:notEmpty>
		<tr>	
			<td>		
				<h3><bean:message key="label.manager.curricularCourseScopes"/></h3>		
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
			<td>		
				<h3><bean:message key="label.manager.executionCourses"/></h3>
				<logic:empty name="infoCurriculum" property="infoCurricularCourse.infoAssociatedExecutionCourses">
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