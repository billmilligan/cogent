package org.cogent.model;

import java.text.MessageFormat ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.stream.Collectors ;

public class MessageRegistry {

	public static MessageRegistry INSTANCE = new MessageRegistry ( ) ;

	private Map <MessageTemplateKey, MessageTemplate> contents = new HashMap <> ( ) ;

	private MessageRegistry ( ) { ; }

	public void register ( Code c, MessageTemplateType type, String template, int paramCount ) {
		MessageTemplateKey k = new MessageTemplateKey ( c, type ) ;
		MessageTemplate v = new MessageTemplate ( new ReadableMessageFormat ( template ), paramCount ) ;
		contents.put ( k, v ) ;
	}

	public String format ( Code c, MessageTemplateType type, Object ... params ) {
		MessageTemplate template = contents.get ( new MessageTemplateKey ( c, type ) ) ;
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
				throw new IllegalArgumentException ( "Wrong number of pararms for message " + c + ":" + type + ".  Should be " + template.paramCount + " but was " + Arrays.asList ( params ) ) ;
			}
			return template.template.format ( quoted ) ;
		}
	}

	private String [ ] quoteEach ( Object [ ] params ) {
		return Arrays.asList ( params ).stream ( ).map ( this::quote ).toList ( ).toArray ( new String [ 0 ] ) ;
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
