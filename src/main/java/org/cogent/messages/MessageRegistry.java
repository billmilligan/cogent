package org.cogent.messages;

import java.text.MessageFormat ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.stream.Collectors ;

import org.cogent.validation.TemplateType ;
import org.cogent.validation.ValidationCode ;

public class MessageRegistry {

	public static MessageRegistry INSTANCE = new MessageRegistry ( ) ;

	private Map <MessageTemplateKey, MessageTemplate> contents = new HashMap <> ( ) ;

	protected MessageRegistry ( ) { ; }

	public void clear ( ) {
		contents.clear ( ) ;
	}

	public void register ( Code c, MessageTemplateType type, String template, int paramCount ) {
		MessageTemplateKey k = new MessageTemplateKey ( c, type ) ;
		MessageTemplate v = new MessageTemplate ( new ReadableMessageFormat ( template ), paramCount ) ;
		contents.put ( k, v ) ;
	}

	public String format ( Code c, MessageTemplateType type, Object ... params ) {
		MessageTemplate template = templateFor ( c, type ) ;
		String [ ] quoted = quoteEach ( params ) ;
		if ( template == null ) {
			StringBuilder sb = new StringBuilder ( c.name ( ) ) ;
			sb.append ( ":" ) ;
			sb.append ( type ) ;
			sb.append ( ":  " ) ;
			sb.append ( Arrays.asList ( quoted ).stream ( ).collect ( Collectors.joining ( ", " ) ) ) ;
			return sb.toString ( ) ;
		} else {
			if ( template.paramCount != quoted.length ) {
				throw new IllegalArgumentException ( "Wrong number of params for message " + c + ":" + type + ".  Should be " + template.paramCount + " but was " + Arrays.asList ( params ) ) ;
			}
			return template.template.format ( quoted ) ;
		}
	}

	protected MessageTemplate templateFor ( Code c, MessageTemplateType type ) {
		return contents.get ( new MessageTemplateKey ( c, type ) ) ;
	}

	public int messageCountFor ( ValidationCode cd, TemplateType context ) {
		MessageTemplate mt = templateFor ( cd, context ) ;
		return mt == null ? 0 : mt.paramCount ;
	}

	public MessageFormat formatFor ( Code c, MessageTemplateType type ) {
		MessageTemplate mt = templateFor ( c, type ) ;
		if ( mt == null ) {
			return null ;
		} else {
			return mt.template ( ) ;
		}
	}

	private String [ ] quoteEach ( Object [ ] params ) {
		return Arrays.asList ( params ).stream ( ).map ( this::quote ).toList ( ).toArray ( new String [ params.length ] ) ;
	}

	private String quote ( Object s ) {
		return s == null ? "null" : ( "\"" + s + "\"" ) ;
	}

	private static record MessageTemplateKey ( Code code, MessageTemplateType type ) { ; }

	private static record MessageTemplate ( MessageFormat template, int paramCount ) { ; }

	private static class ReadableMessageFormat extends MessageFormat {
		private static final long serialVersionUID = 4001907808780312107L ;
		private String template ;

		public ReadableMessageFormat ( String template ) {
			super ( template ) ;
			this.template = template ;
		}

		@Override
		public String toString ( ) {
			return template ;
		}
	}
}
