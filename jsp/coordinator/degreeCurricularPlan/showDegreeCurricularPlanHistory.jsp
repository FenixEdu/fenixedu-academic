<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="DataBeans.InfoCurricularCourseScope, Util.Data, java.util.Map, java.lang.Integer" %>

<h2><bean:message key="label.coordinator.degreeCurricular.history"/></h2>
<logic:present name="curricularCourseScopesHashMap">
	<logic:iterate id="curricularCourseScopeElem" name="allCurricularCourseScopes" type="InfoCurricularCourseScope" length="1">
		<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
	</logic:iterate>
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
			<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.beginDate"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.endDate"/></td>
		</tr>
		<bean:define id="equalScopesSize" value="0"/>
		<logic:iterate id="curricularCourseScopeElem" name="allCurricularCourseScopes" type="InfoCurricularCourseScope">
			<logic:equal name="equalScopesSize" value="0"> 
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
						<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.beginDate"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.endDate"/></td>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<bean:define id="moreThanOne" name="allCurricularCourseScopes" type="java.util.List"/>
				<logic:iterate id="scopeEntry" name="curricularCourseScopesHashMap" type="Map.Entry">
					<logic:equal name="curricularCourseScopeElem" property="idInternal" value="<%= scopeEntry.getKey().toString() %>">
						<bean:size id="scopesSize" name="scopeEntry" property="value"/>
						<bean:define id="equalScopesSize" value="<%= scopesSize.toString()%>"/>				
						<bean:define id="moreThanOne" name="scopeEntry" property="value" type="java.util.List"/>				
					</logic:equal>
				</logic:iterate>
				<logic:equal name="equalScopesSize" value="1">
					<tr>
						<td class="listClasses">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
						</td>
						<td class="listClasses">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
						</td>
						<td class="listClasses" style="text-align:left">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name"/>
						</td>
						<td class="listClasses">
							<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
						</td>
						<td class="listClasses">
							<bean:define id="beginDate" name="curricularCourseScopeElem" property="beginDate" type="java.util.Calendar"/>
							<%= Data.format2DayMonthYear(beginDate.getTime())%>
						</td>
						<td class="listClasses">
							<logic:empty name="curricularCourseScopeElem" property="endDate">
							&nbsp;
							</logic:empty>
							<logic:notEmpty name="curricularCourseScopeElem" property="endDate">
							<bean:define id="endDate" name="curricularCourseScopeElem" property="endDate" type="java.util.Calendar"/>
							<%= Data.format2DayMonthYear(endDate.getTime())%>
							</logic:notEmpty>
						</td>
					</tr>	
					<bean:define id="equalScopesSize" value="0" />
				</logic:equal>
						
				<logic:greaterThan name="equalScopesSize" value="1">
					<bean:define id="write" value="1"/>
					<logic:iterate id="scope" name="moreThanOne" type="InfoCurricularCourseScope">
						<tr>
							<logic:equal name="write" value="1">
								<td rowspan="<%= equalScopesSize %>" class="listClasses">
									<bean:write name="scope" property="infoCurricularSemester.infoCurricularYear.year"/>
								</td>
								<td rowspan="<%= equalScopesSize %>" class="listClasses">
									<bean:write name="scope" property="infoCurricularSemester.semester"/>
								</td>
								<td rowspan="<%= equalScopesSize %>" class="listClasses" style="text-align:left">
									<bean:write name="scope" property="infoCurricularCourse.name"/>
								</td>
								<td rowspan="<%= equalScopesSize %>" class="listClasses">
									<bean:write name="scope" property="infoBranch.prettyCode"/>
								</td>
								<bean:define id="write" value="0"/>
							</logic:equal>
							<td class="listClasses">
								<bean:define id="beginDate" name="scope" property="beginDate" type="java.util.Calendar"/>
								<%= Data.format2DayMonthYear(beginDate.getTime())%>
							</td>
							<td class="listClasses">
								<logic:empty name="scope" property="endDate">
								&nbsp;
								</logic:empty>
								<logic:notEmpty name="scope" property="endDate">
								<bean:define id="endDate" name="scope" property="endDate" type="java.util.Calendar"/>
								<%= Data.format2DayMonthYear(endDate.getTime())%>
								</logic:notEmpty>
							</td>
						</tr>
					</logic:iterate>
					<bean:define id="equalScopesSize">
						<%= String.valueOf(Integer.valueOf(equalScopesSize).intValue() - 1) %>
					</bean:define>
				</logic:greaterThan>
			</logic:equal>
			<logic:greaterThan name="equalScopesSize" value="0">
				<bean:define id="equalScopesSize">
					<%= String.valueOf(Integer.valueOf(equalScopesSize).intValue() - 1) %>
				</bean:define>
			</logic:greaterThan>
		</logic:iterate>
		
	</table>
</logic:present>