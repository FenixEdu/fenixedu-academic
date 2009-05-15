<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree"%>

<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >

	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
		<em>
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%=((InfoExecutionDegree)infoExecutionDegree).getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString()%>" />
		<bean:message bundle="GLOBAL_RESOURCES" key="in"/>
		<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />&nbsp;&nbsp;>&nbsp;&nbsp;
		<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.name" />&nbsp;&nbsp;>&nbsp;&nbsp;
		<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
		</em>

	<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="false">
		<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
			<bean:define id="candidates" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT %>"/>
			>&nbsp;&nbsp;<em><strong><bean:message key="label.masterDegree.coordinator.candidates"/></strong>
			<bean:write name="candidates" /></em>
		</logic:equal>
	</logic:equal>

</logic:present>
