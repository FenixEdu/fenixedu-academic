<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<p>
<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
</p>
<logic:present name="<%=PresentationConstants.EXECUTION_COURSE_LIST_KEY%>">
	<table>
		<tr>	
			<td>		
				<logic:notEmpty name="<%=PresentationConstants.EXECUTION_COURSE_LIST_KEY%>">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<th class="listClasses-header"><bean:message key="label.copySite.course" />
							</th>
							<th class="listClasses-header">&nbsp;
							</th>
						</tr>
			
						<logic:iterate id="executionCourse" name="<%=PresentationConstants.EXECUTION_COURSE_LIST_KEY%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
							<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
							<tr>	 			
								<td class="listClasses" style="text-align:left"><bean:write name="executionCourse" property="nome"/>
								</td>
								<td class="listClasses">
									<bean:define id="executionCourseId" name="executionCourse" property="externalId"/> 
									
									&nbsp;<html:link page="<%="/copySiteExecutionCourse.do?method=chooseFeaturesToCopy&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;executionCourseId=" + executionCourseId.toString() %>">
										<bean:message key="link.copySite.do"/>
									</html:link>
								</td>
			 				</tr>
			 			</logic:iterate>						
					</table>
				</logic:notEmpty>	 	
			</td>
		</tr>
	</table>
</logic:present>