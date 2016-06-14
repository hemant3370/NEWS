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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * This is a simple container for MimeBodyPart attachments in MHT files.
 * 
 * This class provides convenience methods for common tasks like retrieving
 * the file name, MIME type, and accessing the attachment contents.  If
 * complete details of the attachment are required, they can still be
 * accessed via the getMimeBodyPart method, which will return the parsed
 * object returned by the JavaMail API. 
 * 
 * @author Brad Hanken
 */
public class Attachment
{
    private static final String CONTENT_LOCATION = "Content-Location";

    private MimeBodyPart mimeBodyPart;

    Attachment(MimeBodyPart mimeBodyPart)
    {
        this.mimeBodyPart = mimeBodyPart;
    }

    public MimeBodyPart getMimeBodyPart()
    {
        return mimeBodyPart;
    }

    /**
     * Obtain the likely filename of an attachment by checking the content headers
     * 
     * @return likely filename
     * @throws MessagingException
     */
    public String getFileName() throws MessagingException
    {
        String fileName = mimeBodyPart.getFileName();
        if (fileName == null) {
            String[] contentLocationHeader = mimeBodyPart.getHeader(CONTENT_LOCATION);
            if (contentLocationHeader != null && contentLocationHeader.length > 0) {
                String contentLocation = mimeBodyPart.getHeader(CONTENT_LOCATION)[0];
                try {
                    URL url = new URL(contentLocation);
                    fileName = url.getFile();
                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                } catch (MalformedURLException e) {
                    // Couldn't parse URL - probably wasn't one.
                }
            }
        }
        return fileName;
    }

    /**
     * Obtain the MIME type of an attachment by checking the Content-Type header
     * 
     * @return MIME type
     * @throws MessagingException
     */
    public String getMimeType() throws MessagingException
    {
        String contentType = mimeBodyPart.getContentType();
        int endPos = contentType.indexOf(';');
        if (endPos != -1) {
            return contentType.substring(0, endPos);
        } else {
            return contentType;
        }
    }

    /**
     * Return the attachment contents as a byte array
     * 
     * @return attachment contents
     * @throws IOException
     * @throws MessagingException
     */
    public byte[] toByteArray() throws IOException, MessagingException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mimeBodyPart.getDataHandler().writeTo(bos);
        return bos.toByteArray();
    }

    /**
     * Save the attachment to the specified file
     * 
     * @param file to save to
     * @throws IOException
     * @throws MessagingException
     */
    public void saveFile(File file) throws IOException, MessagingException
    {
        mimeBodyPart.saveFile(file);
    }

    /**
     * Save the attachment to the specified file
     * 
     * @param file to save to
     * @throws IOException
     * @throws MessagingException
     */
    public void saveFile(String file) throws IOException, MessagingException
    {
        mimeBodyPart.saveFile(file);
    }
}
