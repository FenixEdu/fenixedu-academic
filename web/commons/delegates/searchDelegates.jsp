<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.findDelegates" bundle="DELEGATES_RESOURCES" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="DELEGATES_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<p class="mtop15 mbottom05">
	<b><bean:message key="label.delegates.searchDelegates.searchByDegree" bundle="DELEGATES_RESOURCES" /></b>
	<html:link page="/findDelegates.do?method=prepare&amp;searchByName=true">
		<bean:message key="link.findDelegatesByName" bundle="DELEGATES_RESOURCES"/>
	</html:link>,
	<html:link page="/findDelegates.do?method=prepare&amp;searchByNumber=true">
		<bean:message key="link.findDelegatesByNumber" bundle="DELEGATES_RESOURCES"/>
	</html:link>,	
	<html:link page="/findDelegates.do?method=prepare&amp;searchByDelegateType=true">
		<bean:message key="link.findDelegatesByDelegateType" bundle="DELEGATES_RESOURCES"/>
	</html:link>,
	<html:link page="/findDelegates.do?method=prepare&amp;searchByDegree=true">
		<bean:message key="link.findDelegatesByDegree" bundle="DELEGATES_RESOURCES"/>
	</html:link>
</p>

<logic:present name="searchByNameBean" >
	<p class="color888 mvert05">
		<bean:message key="label.delegates.searchDelegates.searchByName.help" bundle="DELEGATES_RESOURCES" /></p>
		
	<fr:form action="/findDelegates.do?method=searchByName">
		<fr:edit id="searchByNameBean" name="searchByNameBean" layout="tabular-editable" schema="delegates.searchDelegates.searchByName">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright tdleft mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width110px,width350px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/findDelegates.do?method=prepare&searchByName=true" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width110px"></td>
				<td class="width350px">
					<html:submit><bean:message key="label.button.searchDelegates" bundle="DELEGATES_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<logic:present name="searchByNumberBean" >
	<p class="color888 mvert05">
		<bean:message key="label.delegates.searchDelegates.searchByNumber.help" bundle="DELEGATES_RESOURCES" /></p>
		
	<fr:form action="/findDelegates.do?method=searchByNumber">
		<fr:edit id="searchByNumberBean" name="searchByNumberBean" layout="tabular-editable" schema="delegates.searchDelegates.searchByNumber">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright tdleft mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width110px,width200px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/findDelegates.do?method=prepare&searchByNumber=true" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width110px"></td>
				<td class="width200px">
					<html:submit><bean:message key="label.button.searchDelegates" bundle="DELEGATES_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<logic:present name="searchByDelegateTypeBean" >
	<logic:notPresent name="searchByDelegateTypeBean" property="delegateType" >
		<p class="color888 mvert05">
			<bean:message key="label.delegates.searchDelegates.searchByDelegateType.help" bundle="DELEGATES_RESOURCES" /></p>
		
		<fr:form action="/findDelegates.do?method=searchByDelegateType">
			<fr:edit id="searchByDelegateTypeBean" name="searchByDelegateTypeBean" layout="tabular-editable" schema="delegates.searchDelegates.searchByDelegateType">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright tdleft mtop0 mbottom0"/>
					<fr:property name="columnClasses" value="width110px,width325px,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/findDelegates.do?method=prepare&searchByDelegateType=true" />
			</fr:edit>
			<table class="tstyle5 gluetop mtop0">
				<tr>
					<td class="width110px"></td>
					<td class="width325px">
						<html:submit><bean:message key="label.button.searchDelegates" bundle="DELEGATES_RESOURCES"/></html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
	</logic:notPresent>
	
	<logic:present name="searchByDelegateTypeBean" property="delegateType" >
		<p class="color888 mvert05">
			<bean:message key="label.delegates.searchDelegates.searchByDelegateType.chooseCurricularYear.help" bundle="DELEGATES_RESOURCES" /></p>
			
		<fr:form action="/findDelegates.do?method=searchByDelegateType">
			<fr:edit id="searchByDelegateTypeBean" name="searchByDelegateTypeBean" layout="tabular-editable" schema="delegates.searchDelegates.selectDelegateYearType">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright tdleft mtop0 mbottom0"/>
					<fr:property name="columnClasses" value="width110px,width250px,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/findDelegates.do?method=prepare&searchByDelegateType=true" />
			</fr:edit>
			<table class="tstyle5 gluetop mtop0">
				<tr>
					<td class="width110px"></td>
					<td class="width250px">
						<html:submit><bean:message key="label.button.searchDelegates" bundle="DELEGATES_RESOURCES"/></html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
	</logic:present>
</logic:present>

<logic:present name="searchByDegreeBean" >
	<p class="color888 mvert05">
		<bean:message key="label.delegates.searchDelegates.searchByDegree.help" bundle="DELEGATES_RESOURCES" /></p>
		
	<fr:form action="/findDelegates.do?method=searchByDegree&amp;showBackLink=true">
		<fr:edit id="searchByDegreeBean" name="searchByDegreeBean" layout="tabular-editable" schema="delegates.searchDelegates.selectDegreeTypeAndDegree">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright tdleft mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width80px,width500px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/findDelegates.do?method=prepareSearchByDegreeType" />
			<fr:destination name="post-back-degree-type" path="/findDelegates.do?method=prepareSearchByDegree" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width80px"></td>
				<td class="width500px">
					<html:submit><bean:message key="label.button.searchDelegates" bundle="DELEGATES_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>



