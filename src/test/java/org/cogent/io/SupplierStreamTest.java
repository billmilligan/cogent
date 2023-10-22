package org.cogent.io;

import static org.junit.jupiter.api.Assertions.assertEquals ;

import org.junit.jupiter.api.Test ;

public class SupplierStreamTest {

	@Test
	public void testReadSingle ( ) throws Exception {
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			assertEquals ( 'H', ss.read ( ) ) ;
			assertEquals ( 'e', ss.read ( ) ) ;
			assertEquals ( 'l', ss.read ( ) ) ;
			assertEquals ( 'l', ss.read ( ) ) ;
			assertEquals ( 'o', ss.read ( ) ) ;
			assertEquals ( '!', ss.read ( ) ) ;
			assertEquals ( -1, ss.read ( ) ) ;
		}
	}

	@Test
	public void testReadMultiple ( ) throws Exception {
		byte [ ] bs = new byte [ 10 ] ;
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			int read = ss.read ( bs, 1, 3 ) ;
			assertEquals ( 3, read, "Read the count asked" ) ;
			int i = 0 ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 10, i, "Read them all!" ) ;

			read = ss.read ( bs, 5, 4 ) ;
			assertEquals ( 3, read, "Read only what was left" ) ;
			i = 0 ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 10, i, "Read them all!" ) ;
		}
	}

	@Test
	public void testReadAllBufferstyle ( ) throws Exception {
		byte [ ] bs = new byte [ 10 ] ;
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			int read = ss.read ( bs ) ;
			assertEquals ( 6, read ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 10, i, "Read them all!" ) ;
		}

		bs = new byte [ 3 ] ;
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			int read = ss.read ( bs ) ;
			assertEquals ( 3, read, "Can only read the size of the available space" ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 3, i, "Read them all!" ) ;
		}
	}

	@Test
	public void testReadAll ( ) throws Exception {
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			byte [ ] bs = ss.readAllBytes ( ) ;
			assertEquals ( 6, bs.length ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 6, i, "Read them all!" ) ;
		}
	}

	@Test
	public void testReadN ( ) throws Exception {
		// Equal
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			byte [ ] bs = ss.readNBytes ( 6 ) ;
			assertEquals ( 6, bs.length ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 6, i, "Read them all!" ) ;
		}

		// Less
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			byte [ ] bs = ss.readNBytes ( 3 ) ;
			assertEquals ( 3, bs.length ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 3, i, "Read them all!" ) ;
		}

		// More
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			byte [ ] bs = ss.readNBytes ( 10 ) ;
			assertEquals ( 6, bs.length ) ;
			int i = 0 ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 6, i, "Read them all!" ) ;
		}
	}

	// Subtle differences described: https://www.baeldung.com/convert-input-stream-to-array-of-bytes#:~:text=The%20difference%20between%20these%20two,the%20requested%20number%20of%20bytes.
	@Test
	public void readNSubtle ( ) throws Throwable {
		// Too subtle for me, I'm afraid.  The baeldung article describes
		// a situation where the read() should return -1 but readNBytes doesn't.
		// I can't seem to replicate that here and neither one returns -1!
		byte [ ] bs = new byte [ 10 ] ;
		try ( SupplierInputStream ss = new SupplierInputStream ( ( ) -> "Hello!" ) ) {
			int read = ss.readNBytes ( bs, 1, 3 ) ;
			assertEquals ( 3, read, "Read the count asked" ) ;
			int i = 0 ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 10, i, "Read them all!" ) ;

			read = ss.read ( bs, 5, 4 ) ;
			assertEquals ( 3, read, "Read only what was left" ) ;
			i = 0 ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'H', bs [ i++ ] ) ;
			assertEquals ( 'e', bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 'l', bs [ i++ ] ) ;
			assertEquals ( 'o', bs [ i++ ] ) ;
			assertEquals ( '!', bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 0, bs [ i++ ] ) ;
			assertEquals ( 10, i, "Read them all!" ) ;
		}
	}
}
