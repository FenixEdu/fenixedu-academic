package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class SantanderSequenceNumberGenerator extends SantanderSequenceNumberGenerator_Base {

    private SantanderSequenceNumberGenerator() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSequenceNumber(0);
        setPhotoSequenceNumber(0);
    }

    private static SantanderSequenceNumberGenerator getInstance() {
        final List<SantanderSequenceNumberGenerator> instances =
                RootDomainObject.getInstance().getSantanderSequenceNumberGenerators();
        if (instances.size() > 1) {
            throw new DomainException("santanderSequenceNumberGenerator.more.than.one.instances.exist");
        }
        return instances.size() == 0 ? new SantanderSequenceNumberGenerator() : instances.iterator().next();
    }

    public static int getNewSequenceNumber() {
        Integer seqNumber = getInstance().getSequenceNumber();
        getInstance().setSequenceNumber(++seqNumber);
        return seqNumber;
    }

    public static int getNewPhotoSequenceNumber() {
        Integer seqNumber = getInstance().getPhotoSequenceNumber();
        getInstance().setPhotoSequenceNumber(++seqNumber);
        return seqNumber;
    }

    private static String generatePIN(int numberOfDigits) {
        int min = 1;
        int max = ((int) Math.pow(10, numberOfDigits)) - 1;
        int pin = min + (int) (Math.random() * ((max - min) + 1));
        return String.format("%0" + numberOfDigits + "d", pin);
    }

    @Service
    public static SantanderPIN generateSantanderPIN(Person person) {
        String pin = generatePIN(4);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            byte[] secret = PropertiesManager.getProperty("app.institution.AES128.secretKey").getBytes("UTF-8");
            SecretKey key = new SecretKeySpec(secret, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] cipheredPIN = cipher.doFinal(pin.getBytes("UTF-8"));
            return new SantanderPIN(person, cipheredPIN);
        } catch (Exception e) {
            throw new DomainException("santanderPINGenerator.errorCipheringPIN", e);
        }
    }

    public static String decodeSantanderPIN(SantanderPIN santanderPIN) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            byte[] secret = PropertiesManager.getProperty("app.institution.AES128.secretKey").getBytes("UTF-8");
            SecretKey key = new SecretKeySpec(secret, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] decipheredPIN = cipher.doFinal(santanderPIN.getEncryptedPINAsByteArray());
            return new String(decipheredPIN, "UTF-8");
        } catch (Exception e) {
            throw new DomainException("santanderPINGenerator.errorDecipheringPIN", e);
        }
    }

}
