<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.students.listMarks" /></h2>
<span class="error"><html:errors/></span>
<logic:present name="<%=SessionConstants.MASTER_DEGREE_LIST%>">
	<%--<bean:define id="degreeCurricularPlansList" name="degreeCurricularPlans"/>--%>
	<bean:message key="title.masterDegree.administrativeOffice.chooseMasterDegree" />:
	<br /><br />
	<table>
	   <!-- Master Degrees -->
		<logic:iterate id="degreeElem" name="<%=SessionConstants.MASTER_DEGREE_LIST%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegree">
			<tr>
				<td>
					<html:link page="<%= "/marksManagement.do?method=prepareChooseDegreeCurricularPlan&amp;degreeId=" + degreeElem.getIdInternal()%>">
						<bean:write name="degreeElem" property="nome"/>
						&nbsp;-&nbsp;<bean:write name="degreeElem" property="sigla"/>
					</html:link>
				</td>
	   		</tr>
	   	</logic:iterate>
	</table>
	<br />
</logic:present>
<logic:notPresent name="<%=SessionConstants.MASTER_DEGREE_LIST%>">
	<bean:message key="error.masterDegree.noDegrees" />
</logic:notPresent>