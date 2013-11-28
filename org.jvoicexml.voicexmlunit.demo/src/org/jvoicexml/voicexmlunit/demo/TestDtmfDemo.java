package org.jvoicexml.voicexmlunit.demo;

import java.io.File;
import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvoicexml.event.JVoiceXMLEvent;
import org.jvoicexml.voicexmlunit.Call;

/**
 * A demo that tests DTMF input.
 * @author Raphael Groner
 * @author Dirk Schnelle-Walka
 * @since 0.7.6
 *
 */
public final class TestDtmfDemo {
    /** The call to JVoiceXML. */
    private Call call;
    /** URI of the application to call. */
    private URI uri;

    /**
     * Set up the test environment.
     * @throws Exception
     *        error setting up the test environment
     */
    @Before
    public void setUp() throws Exception {
        uri = new File("etc/dtmf.vxml").toURI();
        call = new Call();
    }

    /**
     * Tear down the test environment.
     * @exception JVoiceXMLEvent
     *            error tearing down
     */
    @After
    public void tearDown() throws JVoiceXMLEvent {
        call.hangup();
    }

    /**
     * Runs a test with the option '1'.
     */
    @Test(timeout = 5000)
    public void testInputYes() {
        call.call(uri);
        call.hears(
               "Do you like this example? Please enter 1 for yes or 2 for no");
        call.enter("1");
        call.hears("You like this example.");
    }

    /**
     * Runs a test with the option '2'.
     */
    @Test(timeout = 5000)
    public void testInputNo() {
        call.call(uri);
        call.hears(
               "Do you like this example? Please enter 1 for yes or 2 for no");
        call.enter("2");
        call.hears("You do not like this example.");
    }
}
