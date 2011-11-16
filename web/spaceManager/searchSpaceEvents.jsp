<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.search.spaces.events" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="SPACE_MANAGER">
	
	<logic:notEmpty name="bean">
		<fr:form action="/searchSpace.do?method=searchSpaceEvents">
			<fr:edit id="bean" name="bean" schema="SearchSpaceEvents">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
					<fr:property name="columnClasses" value=",,tderror1 tdclear" />
				</fr:layout>			
			</fr:edit>
			<html:submit>
				<bean:message key="link.search" bundle="SPACE_RESOURCES"/>
			</html:submit>
		</fr:form>	
	</logic:notEmpty>
	
	<logic:notEmpty name="results">
	
		<p class="mtop15 mbottom05"><b><bean:message key="label.search.spaces.events.found" bundle="SPACE_RESOURCES"/>:</b></p>	
	
		<fr:view name="results" schema="SpaceOccupationEventBean">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value="smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt," />
				<%-- <fr:property name="link(view)" value="<%="/manageSpaces.do?method=viewSpace"%>" />
				<fr:property name="param(view)" value="idInternal/spaceID" />
				<fr:property name="key(view)" value="link.view" />
				<fr:property name="bundle(view)" value="SPACE_RESOURCES" />
				<fr:property name="order(view)" value="0" /> --%>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>