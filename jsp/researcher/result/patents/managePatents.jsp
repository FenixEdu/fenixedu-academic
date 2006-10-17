<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<em>Patentes</em> <!-- tobundle -->
	<h2/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></h2>
	
	<ul>
		<li>
			<html:link module="/researcher" page="/resultPatents/prepareCreate.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.create.link"/>
			</html:link>
		</li>
	</ul>
	
	<%-- Action messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Result Patents Listing --%>
	<%--
	<h3 class='cd_heading'><span><bean:message key="researcher.ResultPatent.list.label" bundle="RESEARCHER_RESOURCES"/></span></h3>
	--%>
	
	<logic:empty name="resultPatents">
		<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.emptyList"/></p>
	</logic:empty>
	
	<logic:notEmpty name="resultPatents">
		<p class="mtop15 mbottom05"><bean:message key="researcher.ResultPatent.list.label" bundle="RESEARCHER_RESOURCES"/>:</p>
		<fr:view name="resultPatents" layout="tabular" schema="result.patentShortList" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop0"/>
				<fr:property name="columnClasses" value=",acenter width5em,acenter width5em,acenter"/>
				<fr:property name="sortBy" value="lastModificationDate=desc"/>
				
				<fr:property name="link(edit)" value="/resultPatents/prepareEdit.do"/>
				<fr:property name="param(edit)" value="idInternal/resultId"/>
				<fr:property name="key(edit)" value="link.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="1"/>
	
				<fr:property name="link(delete)" value="/resultPatents/prepareDelete.do"/>
				<fr:property name="param(delete)" value="idInternal/resultId"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
