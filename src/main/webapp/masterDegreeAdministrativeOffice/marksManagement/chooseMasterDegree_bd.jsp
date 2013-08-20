<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message key="label.students.listMarks" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="<%=PresentationConstants.MASTER_DEGREE_LIST%>">
	<%--<bean:define id="degreeCurricularPlansList" name="degreeCurricularPlans"/>--%>
	<bean:message key="title.masterDegree.administrativeOffice.chooseMasterDegree" />:
	<br /><br />
	<table>
	   <!-- Master Degrees -->
		<logic:iterate id="degreeElem" name="<%=PresentationConstants.MASTER_DEGREE_LIST%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree">
			<tr>
				<td>
					<html:link page="<%= "/marksManagement.do?method=prepareChooseDegreeCurricularPlan&amp;degreeId=" + degreeElem.getExternalId()%>">
						<bean:write name="degreeElem" property="nome"/>
						&nbsp;-&nbsp;<bean:write name="degreeElem" property="sigla"/>
					</html:link>
				</td>
	   		</tr>
	   	</logic:iterate>
	</table>
	<br />
</logic:present>
<logic:notPresent name="<%=PresentationConstants.MASTER_DEGREE_LIST%>">
	<bean:message key="error.masterDegree.noDegrees" />
</logic:notPresent>