package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

public enum StorkAttributeType {
	STORK_EIDENTIFIER("eIdentificador"),

	STORK_NAME("NomeProprio"), STORK_SURNAME("NomeApelido"), STORK_FAMILY_NAME("NomeFamilia"), STORK_ADOPTED_FAMILY_NAME(
			"NomeAdoptado"),

	STORK_GENDER("Sexo"), STORK_BIRTHDATE("DataNascimento"),

	STORK_COUNTRY_OF_BIRTH("Naturalidade"), STORK_NATIONALITY("Nacionalidade"),

	STORK_RESIDENCE_ADDRESS("Morada"), STORK_TEXT_RESIDENCE_ADDRESS("TextoMorada"), STORK_CANONICAL_ADDRESS("MoradaCanonica"),

	STORK_EMAIL("CorreioElectronico"), STORK_PHONE_CONTACT("Contactos"),

	STORK_RETURN_CODE("returnCode"), STORK_ERROR_CODE("errorCode"), STORK_ERROR_MESSAGE("errorMessage");

	private final String storkName;

	StorkAttributeType(String name) {
		this.storkName = name;
	}

	public String getStorkName() {
		return this.storkName;
	}

	public static StorkAttributeType getTypeFromStorkName(String value) {
		for (StorkAttributeType type : StorkAttributeType.values()) {
			if (value.equals(type.getStorkName())) {
				return type;
			}
		}

		return null;
	}

}
