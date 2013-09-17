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