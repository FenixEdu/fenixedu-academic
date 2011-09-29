<%@page import="net.sourceforge.fenixedu.util.Money"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.Event"%>
<%@page import="java.util.Set"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
<!--


#operations {
    margin:20px 0 20px;
}

.grey-box {
    max-width:340px;
    height:110px;
    display:block;
    margin:0 0 10px 0;
    padding:5px 20px 10px;
    float:left;
}

.grey-box,
.infoop7 {
    background:whiteSmoke !important;
    border:1px solid #ececec !important;
    border-radius:3px;
}

.first-box {
    margin-right:30px;
}
.micro-pagamentos .infoop7 .tstyle2 td,
.micro-pagamentos .infoop7 .tstyle2 th {
    background:transparent;
    border-bottom: 1px solid #ddd;
    border-top: 1px solid #ddd;
}


.montante input[type="text"] {
    font-size:18px;
}
.montante input[type="text"] {
    padding:4px;
    text-align:right;
}


.cf:before,
.cf:after {
    content:"";
    display:block;
}
.cf:after {
    clear:both;
}
.cf {
    zoom:1;
}
-->
</style>

<html:xhtml />

		<h3><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.search.person.header"/></h3> 
		<p>
			<fr:form action="/operator.do?method=startPage">
				<table><tr>
					<td>
						<fr:edit id="searchBean" name="searchBean">
							<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchBean">
								<fr:slot name="searchString" key="label.micropayments.search.person.field">
									<fr:property name="size" value="15"/>
								</fr:slot>
							</fr:schema>
							<fr:layout name="tabular">
								<fr:property name="classes" value="dinline" />
								<fr:property name="columnClasses" value=",,tdclear tderror1" />
							</fr:layout>
						</fr:edit>
					</td>
					<td>
						<html:submit>
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.view.profile" />
						</html:submit>
					</td>
				</tr></table>
			</fr:form>
		</p>
		<logic:present name="people">
			<logic:empty name="people">
				<span class="error0">
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.client.not.found" />
				</span>
			</logic:empty>
		</logic:present>

