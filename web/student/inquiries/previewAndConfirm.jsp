<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
table.tdwith50px td { width: 50px; }
table.tdwith90px td { width: 90px; }
.aleft { text-align: left !important; }
.acenter { text-align: center !important; }
.bold { font-weight: bold !important; }
</style>

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<h2><strong>Inqueritos Resultados</strong></h2>

1st Block: <br/>
<logic:iterate id="question" name="studentInquiry" property="firstPageFirstBlock.questions" >
	<bean:write name="question" property="label" /> - <bean:write name="question" property="value" /><br/>
</logic:iterate>

2nd Block: <br/>
<logic:iterate id="question" name="studentInquiry" property="firstPageSecondBlock.questions" >
	<bean:write name="question" property="label" /> - <bean:write name="question" property="value" /><br/>
</logic:iterate>

3rd Block: <br/>
<logic:iterate id="question" name="studentInquiry" property="firstPageThirdBlock.questions" >
	<bean:write name="question" property="label" /> - <bean:write name="question" property="value" /><br/>
</logic:iterate>

4th Block: <br/>
<logic:iterate id="question" name="studentInquiry" property="firstPageFourthBlock.questions" >
	<bean:write name="question" property="label" /> - <bean:write name="question" property="value" /><br/>
</logic:iterate>

5th Block: <br/>
<logic:iterate id="question" name="studentInquiry" property="firstPageFifthBlock.questions" >
	<bean:write name="question" property="label" /> - <bean:write name="question" property="value" /><br/>
</logic:iterate>

<div class="forminline dinline">
	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="confirm"/>
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<html:submit><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
		
	<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
</div>		