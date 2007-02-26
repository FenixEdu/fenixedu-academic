<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<logic:present role="RESEARCHER">

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.createPerson"/></h2>
	
	<script language="JavaScript">
		function check(e,v)
		{	
			var contextPath = '<%= request.getContextPath() %>';	
			if (e.style.display == "none")
			  {
			  e.style.display = "";
			  v.src = contextPath + '/images/toggle_minus10.gif';
			  }
			else
			  {
			  e.style.display = "none";
			  v.src = contextPath + '/images/toggle_plus10.gif';
			  }
		}
	</script>
	
	<bean:define id="result" name="result"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<bean:define id="path" value="<%= "/researcher/resultParticipations/prepareCreateParticipator.do?" + parameters %>"/>
	
	<ul>
		<li><html:link page="<%= "/resultParticipations/prepareEdit.do?" + parameters %>"><bean:message key="link.back" bundle="RESEARCHER_RESOURCES"/></html:link></li>
		<li><html:link page="<%= "/resultParticipations/prepareCreateUnit.do?" + parameters %>"><bean:message key="label.createUnit" bundle="RESEARCHER_RESOURCES"/></html:link></li>
	</ul>

	<span class="infoop2">
		Escolha a unidade à qual quer associar a nova pessoa externa. Caso não exista a unidade em questão poderá sempre criá-la clicando em
		criar unidade.
	</span>
	<un:tree initialUnit="externalRootUnit" unitParamName="unitID" path="<%= path %>" state="true" expanded="true"/>

</logic:present>