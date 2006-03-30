

package net.sourceforge.fenixedu.domain.cms.infrastructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CmsConfiguration extends CmsConfiguration_Base
{
	private static String SMTP_SERVER_ADDRESS_DEFAULT = "mail.adm";
	private static Boolean FILTER_NON_TEXTUAL_ATTACHMENTS_DEFAULT = true;
	private static String MAILING_LIST_HOST_DEFAULT = "lists.fenix.ist.utl.pt";
	private static String CONVERSATION_REPLY_MARKERS_DEFAULT = "Re:,Res:,Reply:,Resposta:";
	private static Integer MAX_ATTACHMENT_SIZE_DEFAULT = 10000000;
	private static Integer MAX_MESSAGE_SIZE_DEFAULT =    10500000;	

	public CmsConfiguration()
	{
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		this.setSmtpServerAddress(SMTP_SERVER_ADDRESS_DEFAULT);
		this.setFilterNonTextualAttachments(FILTER_NON_TEXTUAL_ATTACHMENTS_DEFAULT);
		this.setMailingListsHost(MAILING_LIST_HOST_DEFAULT);
		this.setConversationReplyMarkers(CONVERSATION_REPLY_MARKERS_DEFAULT);
		this.setMaxAttachmentSize(MAX_ATTACHMENT_SIZE_DEFAULT);
		this.setMaxMessageSize(MAX_MESSAGE_SIZE_DEFAULT);
	}

	public String getSmtpServerAddressToUse()
	{
		return this.getSmtpServerAddress() == null ? SMTP_SERVER_ADDRESS_DEFAULT
				: this.getSmtpServerAddress();
	}
	
	public boolean getFilterNonTextualAttachmentsToUse()
	{
		return this.getFilterNonTextualAttachments() == null ? FILTER_NON_TEXTUAL_ATTACHMENTS_DEFAULT
				: this.getFilterNonTextualAttachments();
	}
		
	public String getMailingListsHostToUse()
	{
		return this.getMailingListsHost() == null? MAILING_LIST_HOST_DEFAULT: this.getMailingListsHost();
	}
	
	public String[] getConversationReplyMarkersToUse()
	{
		return this.getConversationReplyMarkers() == null? CONVERSATION_REPLY_MARKERS_DEFAULT.split(","): this.getConversationReplyMarkers().split(",");
	}

	public Integer getMaxAttachmentSizeToUse()
	{
		return this.getMaxAttachmentSize() == null? MAX_ATTACHMENT_SIZE_DEFAULT: this.getMaxAttachmentSize();
	}

	public Integer getMaxMessageSizeToUse()
	{
		return this.getMaxMessageSize() == null? MAX_MESSAGE_SIZE_DEFAULT:this.getMaxMessageSize();
	}
		
	public void delete()
	{
            CmsConfiguration.remove(this, this.getCms());
            removeRootDomainObject();
        super.deleteDomainObject();
	}		
}
