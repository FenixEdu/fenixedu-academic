<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.search.spaces" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="role(SPACE_MANAGER)">
	
	<logic:notEmpty name="bean">
		<fr:form action="/searchSpace.do?method=search">
			<fr:edit id="beanWithSpaceNameID" name="bean" schema="SearchSpace">
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
	
	<logic:notEmpty name="spaces">
	
		<p class="mtop15 mbottom05"><b><bean:message key="label.found.spaces" bundle="SPACE_RESOURCES"/>:</b></p>	
	
		<fr:view name="spaces" schema="FoundSpaceInfo">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value="smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt," />
				<fr:property name="link(view)" value="<%="/manageSpaces.do?method=viewSpace"%>" />
				<fr:property name="param(view)" value="externalId/spaceID" />
				<fr:property name="key(view)" value="link.view" />
				<fr:property name="bundle(view)" value="SPACE_RESOURCES" />
				<fr:property name="order(view)" value="0" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
</logic:present>