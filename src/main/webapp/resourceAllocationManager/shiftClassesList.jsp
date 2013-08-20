<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<p class="mtop2 mbottom05"><strong>Turmas associadas ao turno:</strong></p>

<logic:present name="shift" property="infoClasses">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeClasses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

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
