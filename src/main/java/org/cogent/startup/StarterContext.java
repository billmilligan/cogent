package org.cogent.startup;

import org.cogent.messages.Code ;
import org.cogent.messages.MessageRegistry ;
import org.cogent.messages.MessageTemplateType ;

public final class StarterContext {

	public MessageRegistry messages ( ) {
		return MessageRegistry.INSTANCE ;
	}

	public void registerMessage ( Code c, MessageTemplateType type, String template, int paramCount ) {
		messages ( ).register ( c, type, template, paramCount ) ;
	}
}
