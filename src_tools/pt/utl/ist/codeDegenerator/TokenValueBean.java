package pt.utl.ist.codeDegenerator;

public class TokenValueBean {

	private String token;
	private String value;

	public TokenValueBean(final String token, final String value) {
		this.token = token;
		this.value = value;
	}

	public String getToken() {
		return token;
	}
	public String getValue() {
		return value;
	}

}
