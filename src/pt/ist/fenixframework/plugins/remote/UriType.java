package pt.ist.fenixframework.plugins.remote;

public enum UriType {

	OBJECT, CLASS;

	public String type() {
		return name().toLowerCase();
	}

}
