package org.cogent.startup;

import org.cogent.model.Code ;
import org.cogent.model.MessageRegistry ;
import org.cogent.model.MessageTemplateType ;

public final class StarterContext {

	public MessageRegistry messages ( ) {
		return MessageRegistry.INSTANCE ;
	}

	public void registerMessage ( Code c, MessageTemplateType type, String template, int paramCount ) {
		messages ( ).register ( c, type, template, paramCount ) ;
	}
}
