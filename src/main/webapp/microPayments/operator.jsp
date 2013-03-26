<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
<!--



#operations {
	margin:20px 0 20px;
}

.grey-box {
	max-width:340px;
	/* height:110px; */
	display:block;
	margin:0 0 10px 0;
	padding:5px 20px 10px;
	float:left;
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
width: 100%;
margin: 10px 0;
border-collapse: collapse;
}
table.search-clients td {
padding: 4px;
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

-->
</style>


<html:xhtml />

<h2><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.header"/></h2>

<p>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.description"/>
</p>

<div id="operations" class="cf"> 
	<div class="grey-box first-box"> 
		<jsp:include page="searchPersonForm.jsp"/>
	</div>
	<div class="grey-box">
		<jsp:include page="searchMovementsForm.jsp"/>
	</div> 
</div> 
