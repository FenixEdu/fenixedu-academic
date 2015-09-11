/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bound to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoes@qub-it.com 
 *
 * 
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */

var phroper = {};

(function () {

	var confs = {};

	phroper.version = '2.1.0';

	phroper.start = function (showThumbnail, width, height, captions) {
		confs.showThumbnail = showThumbnail;
		confs.width = width;
		confs.height = height;
		confs.captions = captions;
		confs.loaded = false;
		initPhroper(showThumbnail, width, height, captions);
		$('#phroper-canvas').on('dragover', handleDragOver);
		$('#phroper-canvas').on('drop', handleFileSelect);
	};

	phroper.getPicture = function (mode) {
		var picture = $('#phroper-canvas > img').cropper('getCroppedCanvas').toDataURL();
		if (!!mode && mode === 'file') {
			return convertPictureToFile(picture);
		}
		return picture;
	};

	phroper.getThumbnail = function (mode) {
		var picture = $('#phroper-canvas > img').cropper('getCroppedCanvas', {
			width: $('#phroper-thumbnail').width(),
			height: $('#phroper-thumbnail').height()
		}).toDataURL();
		if (!!mode && mode === 'file') {
			return convertPictureToFile(picture);
		}
		return picture;
	};
	
	phroper.testEnvironment = function () {
		return jQuery && jQuery.fn.cropper && !!window.HTMLCanvasElement && !!window.CanvasRenderingContext2D && !!window.File && !!window.FileReader && !!window.FileList && !!window.Blob;

	};

	phroper.reset = function () {
		$('#phroper-canvas > img').cropper('destroy');
		$('#photo-uploader').empty();		
		phroper.start(confs.showThumbnail, confs.width, confs.height, confs.captions);
	};

	phroper.hasLoadedPicture = function () {
		return confs.loaded;
	};

	function initPhroper (showThumbnail, width, height, _captions) {
		var captions = [
			$.isArray(_captions) && _captions[0] ? _captions[0] : 'Drag your lovely picture here',
			$.isArray(_captions) && _captions[1] ? _captions[1] : 'Or, if you prefer...',
			$.isArray(_captions) && _captions[2] ? _captions[2] : 'Select it from your computer.',
			$.isArray(_captions) && _captions[3] ? _captions[3] : 'Loading picture...'
		];
		$('<div id="phroper-canvas">\
				<div id="phroper-title">' + captions[0] + '</div>\
				<div id="phroper-subtitle">' + captions[1] + '</div>\
				<div id="phroper-loading-caption">' + captions[3] + '</div>\
			</div>').appendTo('#photo-uploader');
		if (width) {
			$('#phroper-canvas').css('width', width);
		}
		if (height) {
			$('#phroper-canvas').css('height', height);
		}
		$('#phroper-loading-caption').hide();
		$('<div id="phroper-thumbnail"></div>').appendTo('#photo-uploader');
		if (!showThumbnail) {
			$('#phroper-thumbnail').hide();
		}
		$('<a id="phroper-upload-button" href="#">'+captions[2]+'</a>')
			.appendTo('#phroper-canvas')
			.click( function () {
				$('<input type="file" id="file" name="files[]"/>')
					.css('display', 'none')					
					.on('change', handleFileSelect)
					.click();
			})
			.css('position', 'absolute')
			.css('left', $('#phroper-canvas').outerWidth()/2.0 - $('#phroper-upload-button').outerWidth()/2.0)
			.css('top', $('#phroper-canvas').outerHeight()/2.0 - $('#phroper-upload-button').outerHeight()/2.0);
		$('#phroper-title').css('top', 0.7 * ($('#phroper-canvas').outerHeight()/2.0 - $('#phroper-upload-button').outerHeight()));
		$('#phroper-subtitle').css('top', 0.9 * ($('#phroper-canvas').outerHeight()/2.0 - $('#phroper-upload-button').outerHeight()));
		$('#phroper-loading-caption').css('top', 0.8 * ($('#phroper-canvas').outerHeight()/2.0 - $('#phroper-upload-button').outerHeight()));
		$('<div id="loading-bar">\
				<div id="fountainG_1" class="fountainG"></div>\
				<div id="fountainG_2" class="fountainG"></div>\
				<div id="fountainG_3" class="fountainG"></div>\
				<div id="fountainG_4" class="fountainG"></div>\
				<div id="fountainG_5" class="fountainG"></div>\
				<div id="fountainG_6" class="fountainG"></div>\
				<div id="fountainG_7" class="fountainG"></div>\
				<div id="fountainG_8" class="fountainG"></div>\
			</div>')
		.appendTo('#phroper-canvas')
		.hide()
		.css('top', $('#phroper-upload-button').css('top'));
	}

	function handleFileSelect (evt) {
		evt.stopPropagation();
		evt.preventDefault();

		$('#photo-uploader').off('dragover');
		$('#photo-uploader').off('drop');
		$('#upload-button').css('display', 'none');

		var file = evt.originalEvent.dataTransfer ? evt.originalEvent.dataTransfer.files[0] : evt.originalEvent.target.files[0]; // FileList object.

		if (!file.type.match('image.*')) {
			return;
		}

		activateLoadingScreen();

		setTimeout(function () {
			var reader = new FileReader();
			reader.onload = (function (image) {
				return function (image) {
					loadPicture(image);
				};
			}(file));
			reader.readAsDataURL(file);
		},2000);
	}

	function handleDragOver (evt) {
		evt.stopPropagation();
		evt.preventDefault();
		evt.originalEvent.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
	}

	function loadPicture (file) {
		$('<img src="' + file.target.result + '"/>').appendTo('#phroper-canvas');
		$('#phroper-canvas > img').cropper({
			aspectRatio: 1 / 1,
			preview: '#phroper-thumbnail'
		});
		confs.loaded = true;
		deactivateLoadingScreen();
	}

	function convertPictureToFile (dataURI) {
		// convert base64 to raw binary data held in a string
		// doesn't handle URLEncoded DataURIs
		var byteString = atob(dataURI.split(',')[1]);

		// separate out the mime component
		var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

		// write the bytes of the string to an ArrayBuffer
		var ab = new ArrayBuffer(byteString.length);
		var ia = new Uint8Array(ab);
		for (var i = 0; i < byteString.length; i++) {
			ia[i] = byteString.charCodeAt(i);
		}

		return new Blob(Array.apply( [], ia ), { 'type' : mimeString });
	}

	function activateLoadingScreen () {
		$('#phroper-title').hide();
		$('#phroper-subtitle').hide();
		$('#phroper-upload-button').hide();
		$('#phroper-loading-caption').show();
		$('#loading-bar').show().focus();
	}

	function deactivateLoadingScreen () {
		$('#phroper-loading-caption').hide();
		$('#loading-bar').hide();	
	}

}());
