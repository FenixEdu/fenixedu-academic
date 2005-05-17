function initEditor() {
//Usage:initRTE(imagesPath, includesPath, cssFile, genXHTML)
	initRTE("images/", "../javaScript/editor/", "", true);
}

function writeTextEditor(width, heigth, htmlText) {
//Usage:writeRichText(fieldname, htmlText, width, height, buttons, readOnly)
	writeRichText('rte1', htmlText, width, heigth, true, false);
}

function writeMultipleTextEditor(string, width, heigth, htmlText){
	writeRichText(string, htmlText, width, heigth, true, false);
}

function isBrowserAllowed() {
	var browserName = navigator.appName;
	var result = true;
	if (browserName == "Netscape") {}
	else if (browserName == "Microsoft Internet Explorer") {}
	else result = false;

	return result;
}

function update(){
	updateRTE('rte1');
	return document.forms[0].rte1.value;
}

function update1(){
	updateRTE('rte1');
	return document.forms[0].rte1.value;
}function update2(){
	updateRTE('rte2');
	return document.forms[0].rte2.value;
}function update3(){
	updateRTE('rte3');
	return document.forms[0].rte3.value;
}function update4(){
	updateRTE('rte4');
	return document.forms[0].rte4.value;
}function update5(){
	updateRTE('rte5');
	return document.forms[0].rte5.value;
}function update6(){
	updateRTE('rte6');
	return document.forms[0].rte6.value;
}