/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $Date$
 * Author:  $LastChangedBy$
 *
 * JVoiceXML - A free VoiceXML implementation.
 *
 * Copyright (C) 2007-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.mock.implementation;

import java.util.List;

import org.jvoicexml.CallControl;
import org.jvoicexml.CallControlProperties;
import org.jvoicexml.DtmfInput;
import org.jvoicexml.DocumentServer;
import org.jvoicexml.ImplementationPlatform;
import org.jvoicexml.Session;
import org.jvoicexml.SpeakableText;
import org.jvoicexml.SystemOutput;
import org.jvoicexml.UserInput;
import org.jvoicexml.event.EventBus;
import org.jvoicexml.event.error.BadFetchError;
import org.jvoicexml.event.error.NoresourceError;
import org.jvoicexml.event.plain.ConnectionDisconnectHangupEvent;
import org.jvoicexml.implementation.SynthesizedOutputListener;

/**
 * This class provides a dummy {@link ImplementationPlatform} for testing
 * purposes.
 *
 * @author Dirk Schnelle-Walka
 * @version $Revision$
 * @since 0.6
 */
public final class MockImplementationPlatform
        implements ImplementationPlatform {
    /** Borrowed user input. */
    private UserInput input;

    /** Borrowed system output. */
    private SystemOutput output;

    /** Output listener to add once the system output is obtained. */
    private SynthesizedOutputListener outputListener;

    /** Borrowed call control. */
    private CallControl call;

    /** The queued prompts. */
    private List<SpeakableText> prompts;

    /**
     * {@inheritDoc}
     */
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    public CallControl getCallControl() throws NoresourceError {
        if (call == null) {
            call = new MockCallControl();
        }
        return call;
    }

    /**
     * {@inheritDoc}
     */
    public CallControl getBorrowedCallControl() {
        return call;
    }

    /**
     * {@inheritDoc}
     */
    public DtmfInput getCharacterInput() throws NoresourceError {
        return null;
    }

    /**
     * Sets the output listener to add once the system output is obtained.
     * @param listener the listener.
     */
    public void setSystemOutputListener(
            final SynthesizedOutputListener listener) {
        outputListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    public SystemOutput getSystemOutput() throws NoresourceError {
        if (output == null) {
            MockSystemOutput mock = new MockSystemOutput();
            if (outputListener != null) {
                mock.addListener(outputListener);
            }
            output = mock;
        }
        return output;
    }

    /**
     * {@inheritDoc}
     */
    public void waitOutputQueueEmpty() {
    }

    /**
     * {@inheritDoc}
     */
    public UserInput getUserInput() throws NoresourceError {
        if (input == null) {
            input = new MockUserInput();
        }
        return input;
    }

    /**
     * {@inheritDoc}
     */
    public void setEventBus(final EventBus bus) {
    }

    /**
     * {@inheritDoc}
     */
    public UserInput getBorrowedUserInput() {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    public void setSession(final Session currentSession) {
    }

    /**
     * {@inheritDoc}
     */
    public void waitNonBargeInPlayed() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserInputActive() {
        return input != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPromptTimeout(final long timeout) {
        prompts = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queuePrompt(final SpeakableText speakable) {
        if (prompts == null) {
            prompts = new java.util.ArrayList<SpeakableText>();
        }
        prompts.add(speakable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderPrompts(final String sessionId,
            final DocumentServer server, final CallControlProperties callProps)
            throws BadFetchError, NoresourceError,
                ConnectionDisconnectHangupEvent {
        if (prompts == null) {
            return;
        }
        final SystemOutput out = getSystemOutput();
        for (SpeakableText speakable : prompts) {
            out.queueSpeakable(speakable, sessionId, server);
        }
        prompts = null;
    }
}
