<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
Turmas a que turno pertence:
<br />
<logic:present name="shift" property="infoClasses">
	<table cellpadding="0" border="0">
		<tr>
			<!-- Table headers: Nome e Apagar -->
			<td class="listClasses-header">
				<bean:message key="label.name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="link.remove"/>
			</td>				
		</tr>		
	
		<bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.remove.class"/>')
		</bean:define>
	<logic:iterate id="shiftClass" name="shift" property="infoClasses">
		<bean:define id="classOID" name="shiftClass" property="idInternal"/>
			<tr>
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
							<bean:message key="link.remove"/>
						</html:link>
					</div>
				</td>
			</tr>
	</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="shift" property="infoClasses">
	<span class="error">
		<bean:message key="message.shift.classes.none"/>
	</span>
</logic:notPresent>
