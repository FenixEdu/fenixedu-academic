<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.viewExtraWorkAuthorizations" /></h2>

<span class="error0"><html:errors/></span>
<fr:form action="/manageExtraWorkAuthorization.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="extraWorkAuthorizationForm" property="method" value="showExtraWorkAuthorizations" />

	<fr:edit id="extraWorkAuthorizationSearch" name="extraWorkAuthorizationSearchBean" schema="input.extraWorkAuthorizationSearch">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
</fr:form>
			
<logic:notEmpty name="extraWorkAuthorizationSearchBean" property="extraWorkAuthorizations">
	<fr:view name="extraWorkAuthorizationSearchBean" property="extraWorkAuthorizations" schema="show.extraWorkAuthorization">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thleft thlight"/>
			<fr:property name="link(viewExtraWorkAuthorization)" value="/manageExtraWorkAuthorization.do?method=viewExtraWorkAuthorization" />
			<fr:property name="key(viewExtraWorkAuthorization)" value="link.viewExtraWorkAuthorization" />
			<fr:property name="param(viewExtraWorkAuthorization)" value="extraWorkAuthorization.idInternal/authorizationID" />
			<fr:property name="bundle(viewExtraWorkAuthorization)" value="ASSIDUOUSNESS_RESOURCES" />
		</fr:layout>
	</fr:view>		
</logic:notEmpty>		
