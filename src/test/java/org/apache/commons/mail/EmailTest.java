package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail; 

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Rule;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;



public class EmailTest {

	private static final String[] TEST_EMAILS = {"ab@bc.com", "a.b@c.org", "alkjshdfkjsdhf@skjdhfksjdhf.com.bd"};
	private static final String[] NULL_EMAILS = null;
	private static final String TEST_EMAIL = "ab@bc.com";
	
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception {
		
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//AddBCC Test Cases
	@Test
	public void testAddBcc() throws Exception {
		email.addBcc(TEST_EMAILS);

		//Check to ensure 3 BCC emails in the list.
		assertEquals(3, email.getBccAddresses().size());
	}
	
	@Test
	public void testNullAddBcc() throws Exception {
		//Call for null and pass on exception throw.
		thrown.expect(EmailException.class);
		thrown.expectMessage("Address List provided was invalid");
		email.addBcc(NULL_EMAILS);
	}
	
	//AddCC Test Cases
	@Test
	public void testAddCc() throws Exception {
		email.addCc(TEST_EMAIL);
		//Ensure the CC list now contains an address.
		assertEquals(1, email.getCcAddresses().size());
	}
	
	//addHeader Test Cases
	@Test
	public void testAddHeader() throws Exception {
		String testName = "name";
		String testValue = "value";
		
		email.addHeader(testName, testValue);
		//Check to ensure the headers list contains a new header.
		assertEquals(1, email.headers.size());
	}
	
	@Test
	public void testNullNameAddHeader() throws Exception {
		String testName = null;
		String testValue = "value";
		//Test that exception is thrown for a null name in the header.
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("name can not be null or empty");
		email.addHeader(testName, testValue);
	}
	
	@Test
	public void testNullValueAddHeader() throws Exception {
		String testName = "name";
		String testValue = null;
		//Test that exception is thrown for null value in the header.
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("value can not be null or empty");
		email.addHeader(testName, testValue);
	}
	
	//addReplyTo Test Cases
	@Test
	public void testAddReplyTo() throws Exception {
		String testName = "name";
		
		email.addReplyTo(TEST_EMAIL, testName);
		//Test that the reply list contains the new reply.
		assertEquals(1, email.replyList.size());
	}
	
	//buildMimeMessage Test Cases
	@Test
	public void testBuildMimeMessage() throws Exception {
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.buildMimeMessage();
				 
		//Check that message object was created and that it contains the recipient.
		assertEquals(1, email.message.getRecipients(Message.RecipientType.TO).length);
	}
	
	@Test
	public void testCCBuildMimeMessage() throws Exception {
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.addCc(TEST_EMAIL);
		email.buildMimeMessage();

		//Check that message object was created and that it contains the a CC recipient.
		assertEquals(1, email.message.getRecipients(Message.RecipientType.CC).length);
	}
	
	@Test
	public void testBCCBuildMimeMessage() throws Exception {
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.addBcc(TEST_EMAIL);
		email.buildMimeMessage();

		//Check that message object was created and that it contains the a BCC recipient.
		assertEquals(1, email.message.getRecipients(Message.RecipientType.BCC).length);
	}
	
	@Test
	public void testSubjectBuildMimeMessage() throws Exception {
		String testSubject = "Email subject";
		
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.setSubject(testSubject);
		email.buildMimeMessage();
		//Tests that the message object was constructed by comparing the subject value of the email source to the subject of the constructed message.
		//These should be the same if it worked correctly, and it would prove that message was built.
		assertEquals(testSubject, email.message.getSubject());
	}
	
	@Test
	public void testSubjectAndCharBuildMimeMessage() throws Exception {
		String testSubject = "Email subject";
		String testCharset = "UTF-8";
		
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.setSubject(testSubject);
		email.setCharset(testCharset);
		email.buildMimeMessage();
		//Test message construction using a defined subject and charset by comparing back to the email's subject.
		assertEquals(testSubject, email.message.getSubject());
	}
	
	@Test
	public void testReplyBuildMimeMessage() throws Exception {
		
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.addReplyTo(TEST_EMAIL);
		email.buildMimeMessage();
		//Test message construction with data in the replyTo list. Check to make sure the list contains our entry.
		assertEquals(1, email.message.getReplyTo().length);
	}
	
	@Test
	public void testContentBuildMimeMessage() throws Exception {
		String testContent = "Email Content";
		
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.setContent(testContent, "text/plain");
		email.buildMimeMessage();
		email.getMimeMessage().getContent();
		//Test message construction with defined content. Ensure that our testContent matches the content stored in the message.
		assertEquals(testContent, email.message.getContent());
	}
	
	@Test
	public void testContentWithCharsetBuildMimeMessage() throws Exception {
		String testContent = "Email Content";
		
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.setContent(testContent, "UTF-8");
		email.buildMimeMessage();
		email.getMimeMessage().getContent();
		//Test message construction with defined content and charset. Ensure that our testContent matches the content stored in the message.
		assertEquals(testContent, email.message.getContent());
	}
	
	@Test
	public void testBodyBuildMimeMessage() throws Exception {
		MimeMultipart testBody = new MimeMultipart();

		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.setContent(testBody);
		email.buildMimeMessage();
		//Test message construction with defined emailBody. Ensure that our testBody matches the content stored in the message.
		assertEquals(testBody, email.message.getContent());
	}
	
	@Test
	public void testAlreadyBuiltBuildMimeMessage() throws Exception {
		email.setHostName("hostname");
		email.setFrom(TEST_EMAIL);
		email.addTo(TEST_EMAIL);
		email.buildMimeMessage();
		//Check to ensure that if a message was already built, an exception is thrown if a the function is called again.
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("The MimeMessage is already built.");
		email.buildMimeMessage();
	}
	
	//getHostName Test Cases
	@Test
	public void testGetHostName() throws Exception {
		String testHostName = "hostname";
		email.setHostName(testHostName);
		//Test to ensure the testHostName matches the return of getHostName.
		assertEquals(testHostName, email.getHostName());
	}
	
	@Test
	public void testNullNameGetHostName() throws Exception {
		String testHostName = null;
		//Test to ensure the testHostName matches the return of getHostName if it is null.
		assertEquals(testHostName, email.getHostName());
	}
	
	@Test
	public void testSessionGetHostName() throws Exception {
		String testHostName = "hostname";
		
		email.setHostName(testHostName);
		email.getMailSession();
		//Test to ensure the testHostName matches the return of getHostName when a mail session exists.
		assertEquals(testHostName, email.getHostName());
	}
	
	//getMailSession Test Cases
	@Test
	public void testGetMailSession() throws Exception {
		String testHostName = "hostname";
		
		email.setHostName(testHostName);
		Session testSession = email.getMailSession();
		//Test to ensure that the session stored in email matches the return of getMailSession.
		assertEquals(testSession, email.getMailSession());
	}
	
	@Test
	public void testSSLGetMailSession() throws Exception {
		String testHostName = "hostname";
		
		email.setHostName(testHostName);
		email.ssl = true;
		Session testSession = email.getMailSession();
		//Test to ensure that the session stored in email matches the return of getMailSession when ssl is true.
		assertEquals(true, email.isSSLOnConnect());
		assertEquals(testSession, email.getMailSession());
	}
	
	//getSentDate Test Cases
	@Test
	public void testGetSentDate() throws Exception {
		Date testDate = new Date();
		email.setSentDate(testDate);
		//Test to ensure getSentDate return matches our testDate.
		assertEquals(testDate, email.getSentDate());
	}
	
	@Test
	public void testNullGetSentDate() throws Exception {
		Date testDate = new Date();
		//Test to ensure getSentDate return matches our testDate when its null.
		assertEquals(testDate, email.getSentDate());
	}
	
	//getSocketConnectionTimeout Test Cases
	@Test
	public void testGetSocketConnectionTimeout() throws Exception {
		int testTimeout = 4;
		email.setSocketConnectionTimeout(testTimeout);
		//Test to ensure getSocketConnectionTimeout matches our testTimeout.
		assertEquals(testTimeout, email.getSocketConnectionTimeout());
	}
	
	//setFrom Test Cases
	@Test
	public void TestSetFrom() throws Exception {
		String testFrom = "ab@bc.com";
		email.setFrom(testFrom);
		
		//If the from address doesn't get set by setFrom then this test case will fail.
		if(email.fromAddress == null) {
			fail();
		}
		//address will resolve to null, but as long as it's created it will pass.
		assertEquals(null, email.fromAddress.getPersonal());
	}
}
