<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<bean:define id="nextAction" name="action" type="java.lang.String"/>
<h2><bean:message key="<%= "link." + nextAction %>" /></h2>
<p class="mtop2"><span class="error0"><html:errors /></span></p>

<span class="error0 mtop0">
	<html:messages id="message" message="true">
		<bean:write name="message" />
	</html:messages>
</span>

<logic:present name="assiduousnessExportChoices">
	<fr:form action="<%="/exportAssiduousness.do?method="+nextAction%>">
		
		<logic:equal name="assiduousnessExportChoices" property="canChooseDateType" value="true">
			<fr:edit id="assiduousnessExportChoicesDatesTypes" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice.datesType">
				<fr:hidden slot="action" value="<%=nextAction %>" />
				<fr:destination name="assiduousnessExportChoicesPostBack" path="/exportAssiduousness.do?method=chooseAssiduousnessExportChoicesPostBack" />
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mvert0" />
					<fr:property name="columnClasses" value="width12em,inobullet width20em,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<logic:notEmpty name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType">
			<logic:equal name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType" value="MONTHS">
				<fr:edit id="assiduousnessExportChoicesDates" name="assiduousnessExportChoices" schema="choose.assiduousnessExportChoicesDates.yearMonth">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
						<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>

			<logic:equal name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType" value="DATES">
				<fr:edit id="assiduousnessExportChoicesDates" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice.dates">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
						<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>

			<fr:edit id="assiduousnessExportChoices" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
					<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
		</logic:notEmpty>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.export" />
			</html:submit>
		</p>
	</fr:form>
</logic:present>
