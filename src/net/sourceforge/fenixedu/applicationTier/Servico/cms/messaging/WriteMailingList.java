package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailQueue;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:27:21,18/Out/2005
 * @version $Id$
 */
public class WriteMailingList extends CmsService {
    public MailingList run(String name, String description, String address,
            Collection<String> aliasAdresses, Group mailingListGroup, boolean membersOnly,
            boolean replyToList, Person owner) throws FenixServiceException, ExcepcaoPersistencia {
        MailingList mailingList = new MailingList();
        MailQueue queue = new MailQueue();
        mailingList.setName(name);
        mailingList.setDescription(description);
        mailingList.setAddress(address);
        mailingList.setClosed(false);
        mailingList.setMembersOnly(membersOnly);
        mailingList.setReplyToList(replyToList);

        mailingList.setCreator(owner);
        mailingList.setQueue(queue);
        Collection<MailAddressAlias> aliases = new ArrayList<MailAddressAlias>();
        for (String aliasAddress : aliasAdresses) {
            MailAddressAlias existingAlias = MailAddressAlias.readByAddress(aliasAddress);
            if (existingAlias == null) {
                existingAlias = new MailAddressAlias();
                existingAlias.setAddress(address);
            }
            aliases.add(existingAlias);
        }
        for (MailAddressAlias alias : aliases) {
            mailingList.addAliases(alias);
        }

        mailingList.setGroup(mailingListGroup);

        this.updateRootObjectReferences(mailingList);
        return mailingList;
    }

    protected void updateRootObjectReferences(MailingList mailingList) throws ExcepcaoPersistencia {
        this.readFenixCMS().addUsers(mailingList.getCreator());
        this.readFenixCMS().addContents(mailingList);
    }
}
