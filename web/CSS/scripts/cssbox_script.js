//<script>
/*
* Box Model Fix 0.7 (c) by Thor Larholm, www.jscript.dk
* License under GPL
*
*/

function FixBoxModel(bForce){
	if(!window.external)return;
	var compat = document.compatMode;
	if(typeof compat=="string"&&compat!="BackCompat")return;
	for(var i=0, S=document.styleSheets, il=S.length, C, R; i<il; i++){
		C = S[i]; if(!C) continue;
		FixBoxModelCollection( C, bForce );
	}
}

function FixBoxModelCollection( oStyleCol, bForce ){
	var I = oStyleCol.imports;
	for(var a=0, al=(I?I.length:0); a<al; a++){
		FixBoxModelCollection( I[a], bForce );
	}
	var R = oStyleCol.rules; if(!R) return;
	for(var j=0, jl=R.length, CR, CS; j<jl; j++){
		CR = R[j]; if(!CR) continue;
		CS = CR.style; if(!CS) continue;
		FixBoxModelStyle( CS, bForce );
	}
}
function FixBoxModelStyle(oStyle, bForce){
	if(!oStyle|| (oStyle.FixBoxModelDone&&!bForce) ) return;
	var p = FixBoxModel_parseInt;
	var cWidth = p(oStyle.width), cHeight = p(oStyle.height);
	var wBorder = p(oStyle.borderLeftWidth) + p(oStyle.borderRightWidth);
	var hBorder = p(oStyle.borderTopWidth) + p(oStyle.borderBottomWidth);
	var wPadding = p(oStyle.paddingLeft) + p(oStyle.paddingRight);
	var hPadding = p(oStyle.paddingTop) + p(oStyle.paddingBottom);
	if(wBorder>0||wPadding>0) oStyle.width = cWidth + wBorder + wPadding;
	if(hBorder>0||hPadding>0) oStyle.height = cHeight + hBorder + hPadding;
	oStyle.FixBoxModelDone = true;
}
function FixBoxModel_parseInt( s ){
	return parseInt(s,10)||0;
}
FixBoxModel();