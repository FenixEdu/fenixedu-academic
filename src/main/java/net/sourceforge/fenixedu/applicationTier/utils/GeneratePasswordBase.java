package net.sourceforge.fenixedu.applicationTier.utils;

import java.security.SecureRandom;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;

import com.Ostermiller.util.PasswordVerifier;
import com.Ostermiller.util.RandPass;

public class GeneratePasswordBase implements IGeneratePassword {

    protected RandPass randPass;

    public GeneratePasswordBase() {
        inicializeRandandomPassGenerator();
    }

    private void inicializeRandandomPassGenerator() {
        try {
            randPass = new RandPass(SecureRandom.getInstance("SHA1PRNG"));
            randPass.setMaxRepetition(1);
            randPass.addVerifier(new PasswordVerifier() {
                @Override
                public boolean verify(char[] password) {
                    return PasswordVerifierUtil.has3ClassesOfCharacters(password);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generatePassword(Person person) {

        return randPass.getPass(PropertiesManager.getIntegerProperty("passSize"));

    }
}
