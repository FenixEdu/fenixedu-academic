package net.sourceforge.fenixedu.util;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum ContentType {

    JPG, PNG;

    public String getMimeType() {

	ContentType type = valueOf(name());

	switch (type) {
	case JPG:
	    return "image/jpeg";
	case PNG:
	    return "image/png";
	default:
	    return "*/*";
	}

    }

    public String getFileExtention() {
	return name().toLowerCase();
    }

    public static ContentType getContentType(String httpContentType) {

	String contentTypeInLowerCase = httpContentType.toLowerCase();

	if (contentTypeInLowerCase.equals("image/jpeg") || contentTypeInLowerCase.equals("image/jpg")
		|| contentTypeInLowerCase.equals("image/pjpeg")) {
	    return JPG;
	}
	if (contentTypeInLowerCase.equals("image/png") || contentTypeInLowerCase.equals("image/x-png")) {
	    return PNG;
	}

	return null;
    }

}
