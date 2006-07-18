<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<p>
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
					<strong><bean:message key="label.masterDegree.coordinator.selectedDegree"/></strong> 
					<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
					<br />
					<strong><bean:message key="label.masterDegree.coordinator.executionYear"/></strong>
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />					
					<br />
				</logic:present>
				
				<!-- Tutor's name and number -->
				<strong><bean:message key="label.tutor"/>:&nbsp;</strong>
				<bean:write name="infoTeacher" property="infoPerson.nome" />
				&nbsp;-&nbsp;
				<bean:write name="infoTeacher" property="teacherNumber" />
			</p>
		</td>
	</tr>
</table>