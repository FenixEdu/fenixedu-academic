/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.sourceforge.fenixedu.util.ByteArray;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

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
