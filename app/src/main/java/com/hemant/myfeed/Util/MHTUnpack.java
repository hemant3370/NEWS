/*
Copyright (c) 2010 Brad Hanken

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.hemant.myfeed.Util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 * A simple utility for un-packing MHT files.
 * 
 * This library depends on the JavaMail library to parse Mime encoded MHT files.
 * It was tested with javamail version 1.4.3, but probably works with other
 * versions as well.
 * 
 * @author Brad Hanken
 */
public class MHTUnpack
{
	/**
	 * Invoke the static unpack method to unpack an mht file.
	 */
    private MHTUnpack() {
    }
    
    /**
     * Unpack an MHT file and return the constituent parts.
     * 
     * The returned MimeBodyPart objects can easily be written to disk using the
     * saveFile method, or accessed directly via the object's DataHandler.
     * 
     * @param data InputStream containing the MHT file to be unpacked
     * @return Collection of MimeBodyPart objects containing the packed parts
     * 
     * @throws MessagingException
     * @throws IOException
     */
    public static Collection<Attachment> unpack(InputStream data) throws MessagingException, IOException {
    	Session s = Session.getDefaultInstance(new Properties());
        MimeMessage msg = new MimeMessage(s, data);
        return extractMimePart(msg.getContent());
    }
    
    /**
     * Unpack an MHT file and return the constituent parts.
     * 
     * The returned MimeBodyPart objects can easily be written to disk using the
     * saveFile method, or accessed directly via the object's DataHandler.
     * 
     * @param data byte array containing the MHT file to be unpacked
     * @return Collection of MimeBodyPart objects containing the packed parts
     * 
     * @throws MessagingException
     * @throws IOException
     */
    public static Collection<Attachment> unpack(byte[] data) throws MessagingException, IOException {
        return unpack(new ByteArrayInputStream(data));
    }
    
    /**
     * Unpack an MHT file and return the constituent parts.
     * 
     * The returned MimeBodyPart objects can easily be written to disk using the
     * saveFile method, or accessed directly via the object's DataHandler.
     * 
     * @param data MHT file to be unpacked
     * @return Collection of MimeBodyPart objects containing the packed parts
     * 
     * @throws MessagingException
     * @throws IOException
     */
    public static Collection<Attachment> unpack(File data) throws MessagingException, IOException {
        return unpack(new FileInputStream(data));
    }
    
    // Recursively extract all mime attachments
    
    private static Collection<Attachment> extractMimePart(Object o) throws MessagingException, IOException {
        if (o instanceof Multipart) {
            return handleMultipart((Multipart) o);
        } else if (o instanceof MimeBodyPart) {
            return handleMimeBodyPart((MimeBodyPart) o);
        } else {
            return null;
        }
    }
    
    private static Collection<Attachment> handleMultipart(Multipart m) throws MessagingException, IOException {
        Collection<Attachment> bodyParts = new ArrayList<Attachment>();
        for (int i = 0; i < m.getCount(); i++) {
            Collection<Attachment> c = extractMimePart(m.getBodyPart(i));
            if (c != null) {
                bodyParts.addAll(c);
            }
        }
        return bodyParts;
    }
    
    private static Collection<Attachment> handleMimeBodyPart(MimeBodyPart mbp) throws MessagingException, IOException {
        if (mbp.getContent() instanceof Multipart) {
            return handleMultipart((Multipart) mbp.getContent());
        } else {
            Collection<Attachment> bodyParts = new ArrayList<Attachment>();
            bodyParts.add(new Attachment(mbp));
            return bodyParts;
        }
    }
}
