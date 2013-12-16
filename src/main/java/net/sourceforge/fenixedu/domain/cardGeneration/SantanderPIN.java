package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;

import com.google.common.io.BaseEncoding;

public class SantanderPIN extends SantanderPIN_Base {

    private SantanderPIN() {
        super();
    }

    public SantanderPIN(Person person, byte[] encryptedPIN) {
        this();
        setPerson(person);
        setEncryptedPIN(encryptedPIN);
    }

    public SantanderPIN(Person person, String encryptedPIN) {
        this();
        setPerson(person);
        setEncryptedPIN(encryptedPIN);
    }

    public void delete() {
        setPerson(null);
        deleteDomainObject();
    }

    public void setEncryptedPIN(byte[] encryptedBytes) {
        setEncryptedPIN(BaseEncoding.base64().encode(encryptedBytes));
    }

    public byte[] getEncryptedPINAsByteArray() {
        return BaseEncoding.base64().decode(getEncryptedPIN());
    }
}
