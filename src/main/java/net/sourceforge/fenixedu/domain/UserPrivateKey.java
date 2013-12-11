package net.sourceforge.fenixedu.domain;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.sourceforge.fenixedu.util.ByteArray;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class UserPrivateKey extends UserPrivateKey_Base {

    public UserPrivateKey() {
        super();
    }

    private void generateNewKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        setPrivateKey(new ByteArray(raw));
        setPrivateKeyCreation(new DateTime());
        setPrivateKeyValidity(getPrivateKeyCreation().plusYears(1));
    }

    @Atomic
    public static void generateNewKeyForUser(User user) throws Exception {
        if (user.getPrivateKey() == null) {
            user.setPrivateKey(new UserPrivateKey());
        }
        user.getPrivateKey().generateNewKey();
    }

}
