<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
Turmas associadas ao turno:
<br />
<logic:present name="shift" property="infoClasses">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden property="method" value="removeClasses"/>
	<html:hidden property="page" value="1"/>

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

	<table cellpadding="0" border="0">
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="label.name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="link.schedules.remove"/>
			</td>				
		</tr>		
	
		<bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.remove.class"/>')
		</bean:define>
	<logic:iterate id="shiftClass" name="shift" property="infoClasses">
		<bean:define id="classOID" name="shiftClass" property="idInternal"/>
			<tr>
              	<td class="listClasses">
					<html:multibox property="selectedItems">
						<bean:write name="shiftClass" property="idInternal"/>
					</html:multibox>
				</td>
				<td nowrap="nowrap" class="listClasses">
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ SessionConstants.CLASS_VIEW_OID
							+ "="
							+ pageContext.findAttribute("classOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute("curricularYearOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<div align="center">
							<jsp:getProperty name="shiftClass" property="nome" />
						</div>
					</html:link>
				</td>
				<td nowrap="nowrap" class="listClasses">
					<div align="center">
						<html:link page="<%= "/manageShift.do?method=removeClass&amp;"
								+ SessionConstants.CLASS_VIEW_OID
							  	+ "="
							  	+ pageContext.findAttribute("classOID")
							  	+ "&amp;"
								+ SessionConstants.SHIFT_OID
							  	+ "="
							  	+ pageContext.findAttribute("shiftOID")
							  	+ "&amp;"
								+ SessionConstants.EXECUTION_COURSE_OID
							  	+ "="
							  	+ pageContext.findAttribute("executionCourseOID")
							  	+ "&amp;"
								+ SessionConstants.EXECUTION_PERIOD_OID
							  	+ "="
							  	+ pageContext.findAttribute("executionPeriodOID")
							  	+ "&amp;"
							  	+ SessionConstants.CURRICULAR_YEAR_OID
								+ "="
							  	+ pageContext.findAttribute("curricularYearOID")
							  	+ "&amp;"
								+ SessionConstants.EXECUTION_DEGREE_OID
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
	<html:submit styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="link.schedules.remove"/>
	</html:submit>
  </html:form>
</logic:present>
<logic:notPresent name="shift" property="infoClasses">
	<span class="error">
		<bean:message key="message.shift.classes.none"/>
	</span>
</logic:notPresent>
