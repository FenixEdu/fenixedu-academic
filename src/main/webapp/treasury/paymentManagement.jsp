<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<style>

#operations {
	margin:20px 0 20px;
}

.grey-box {
	max-width:270px;
	/* height:110px; */
	display:block;
	margin:0 0 10px 0;
	padding:5px 20px 10px;
	/* float:left; */
}

.grey-box,
.infoop7 {
	background: #f5f5f5 !important;
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

table.search-clients {
/* width: 100%; */
margin: 10px 0;
border-collapse: collapse;
}
table.search-clients td {
padding: 4px;
}
table.search-clients td.search-clients-name {
padding: 4px 0 4px 12px;
}
table.search-clients td.search-clients-photo div {
border: 1px solid #ddd;
padding: 4px 4px 4px 4px;
background: #fff;
float: left;
}
table.search-clients td.search-clients-photo img {
float: left;
}
table.search-clients td.search-clients-link {
white-space: nowrap;
}

input {
	padding:2px;
}

</style>

<h2><bean:message bundle="TREASURY_RESOURCES" key="portal.treasury" /></h2>

<p>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.description"/>
</p>

<logic:present name="searchBean">

	<div id="operations" class="cf"> 
		<div class="grey-box first-box">
			<jsp:include page="searchPersonForm.jsp"/>
		
			<bean:define id="people" name="searchBean" property="searchResult" toScope="request"/>

		</div>
		
		<div class="mtop3">
			
			<logic:empty name="people">
				<logic:notEmpty name="searchBean" property="searchString">
					<p>
						<em><bean:message bundle="TREASURY_RESOURCES" key="label.string.no.results.found"/></em>
					</p>
				</logic:notEmpty>
			</logic:empty>
			
			<logic:notEmpty name="people">
				<bean:size id="personCount" name="people"/>
				<logic:equal name="personCount" value="1">
					<jsp:include page="paymentManagementForPerson.jsp"/>
				</logic:equal>
				<logic:notEqual name="personCount" value="1">
					<jsp:include page="selectPersonForPaymentManagement.jsp"/>
				</logic:notEqual>
			</logic:notEmpty>
			
		</div>
	</div>

</logic:present>
