<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/> </h2>
		
	<html:link module="/researcher" page="/patents/patentsManagement.do?method=prepareCreatePatent">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.createPatentUseCase.title" />
	</html:link>
	<br/>
	<br/>
	
	<%-- Action messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Result Patents Listing --%>
	<h4><bean:message key="researcher.result.patent.listPatentsUseCase.title" bundle="RESEARCHER_RESOURCES"/></h4>
	<logic:empty name="resultPatents">
		<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.listPatentsUseCase.emptyList"/></p>
	</logic:empty>
	
	<logic:notEmpty name="resultPatents">
		<fr:view name="resultPatents" schema="result.patentShortList" >
			<fr:layout name="tabular">
				<fr:property name="link(edit)" value="/patents/patentsManagement.do?method=prepareEditPatent"/>
				<fr:property name="param(edit)" value="idInternal/resultId"/>
				<fr:property name="key(edit)" value="link.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="1"/>
	
				<fr:property name="link(delete)" value="/patents/patentsManagement.do?method=prepareDeletePatent"/>
				<fr:property name="param(delete)" value="idInternal/resultId"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<br/>
