<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="enum" %>

<logic:present role="RESEARCHER">		
	 <div id="version">
		<html:form action="/changeLocaleTo.do">
		 	<enum:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.Language" bundle="ENUMERATION_RESOURCES" />
		 	<html:select property="newLanguage" onchange="this.form.submit()" value="<%= net.sourceforge.fenixedu.util.LanguageUtils.getLanguage().toString() %>">
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>
		 
		</html:form>
	</div>
</logic:present>
