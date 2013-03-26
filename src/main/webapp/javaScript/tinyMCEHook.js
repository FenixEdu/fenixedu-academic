function hasTinyMCE() {
	return tinyMCE.settings != null;
}

function insertLink(url, name) {
	if(hasTinyMCE()) {
		tinyMCE.execCommand('mceInsertContent', false, '<a href="' + url + '">' + name + '</a> ');
	}
}

if(hasTinyMCE()) {
	switchGlobal();
}