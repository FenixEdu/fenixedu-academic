function initEditor() {
//Usage:initRTE(imagesPath, includesPath, cssFile, genXHTML)
	initRTE("images/", "../javaScript/editor/", "", true);
}

function writeTextEditor(width, heigth, htmlText) {
//Usage:writeRichText(fieldname, htmlText, width, height, buttons, readOnly)
	writeRichText('rte1', htmlText, width, heigth, true, false);
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

