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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<p class="mtop2 mbottom05"><strong>Turmas associadas ao turno:</strong></p>

<logic:present name="shift" property="infoClasses">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeClasses"/>

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

	<table class="tstyle4 thlight tdcenter">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="link.schedules.remove"/>
			</th>				
		</tr>		
	
		<bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.remove.class"/>')
		</bean:define>
	<logic:iterate id="shiftClass" name="shift" property="infoClasses">
		<bean:define id="classOID" name="shiftClass" property="externalId"/>
			<tr>
              	<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
						<bean:write name="shiftClass" property="externalId"/>
					</html:multibox>
				</td>
				<td>
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ PresentationConstants.CLASS_VIEW_OID
							+ "="
							+ pageContext.findAttribute("classOID")
							+ "&amp;"
							+ PresentationConstants.ACADEMIC_INTERVAL
							+ "="
							+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute("curricularYearOID")
							+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<div align="center">
							<bean:write name="shiftClass" property="nome" />
						</div>
					</html:link>
				</td>
				<td>
					<div align="center">
						<html:link page="<%= "/manageShift.do?method=removeClass&amp;"
								+ PresentationConstants.CLASS_VIEW_OID
							  	+ "="
							  	+ pageContext.findAttribute("classOID")
							  	+ "&amp;"
								+ PresentationConstants.SHIFT_OID
							  	+ "="
							  	+ pageContext.findAttribute("shiftOID")
							  	+ "&amp;"
								+ PresentationConstants.EXECUTION_COURSE_OID
							  	+ "="
							  	+ pageContext.findAttribute("executionCourseOID")
							  	+ "&amp;"
							  	+ PresentationConstants.ACADEMIC_INTERVAL
					            + "="
					            + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							  	+ "&amp;"
							  	+ PresentationConstants.CURRICULAR_YEAR_OID
								+ "="
							  	+ pageContext.findAttribute("curricularYearOID")
							  	+ "&amp;"
								+ PresentationConstants.EXECUTION_DEGREE_OID
							  	+ "="
								+ pageContext.findAttribute("executionDegreeOID") %>"
									onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
							<bean:message key="link.schedules.remove"/>
						</html:link>
					</div>
				</td>
			</tr>
	</logic:iterate>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
			<bean:message key="link.schedules.remove"/>
		</html:submit>
	</p>
  </html:form>
</logic:present>

<logic:notPresent name="shift" property="infoClasses">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:message key="message.shift.classes.none"/>
		</span>
	</p>
</logic:notPresent>
