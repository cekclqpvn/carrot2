/*
 * Carrot2 Project
 * Copyright (C) 2002-2005, Dawid Weiss
 * All rights reserved.
 *
 * Refer to full text of the license "carrot2.LICENSE" in the root folder
 * of CVS checkout or at:
 * http://www.cs.put.poznan.pl/dweiss/carrot2.LICENSE
 */

package com.dawidweiss.carrot.filter;


import com.dawidweiss.carrot.util.AbstractRequestProcessor;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A filter request processor class. This is an abstract scheleton class which should be overriden
 * to include some processing. <code>processFilterRequest</code> gets called in response to user
 * query, override it as needed. Convenience methods for serializing input data to JDOM's object
 * structure also exist. Please note that for optimum performance, you should override
 * <code>processFilterRequest</code> and start processing as soon as possible, not waiting for the
 * full XML to be available.
 */
public abstract class FilterRequestProcessor
    extends AbstractRequestProcessor
{
    /**
     * In this method, the carrot standard request stream must be parsed, and some XML result
     * should be returned to the output.
     *
     * @param carrotData An Carrot<sup>2</sup> data stream as passed in the POST request.
     * @param request Http request object.
     * @param response Http response object.
     * @param paramsBeforeData A Map object containing parameter names and their values (always
     *        embedded in Lists), sent in the POST stream before Carrot data. This may be used to
     *        customize processing, however is not a standard extension.
     */
    public abstract void processFilterRequest(
        InputStream carrotData, HttpServletRequest request, HttpServletResponse response,
        Map paramsBeforeData
    )
        throws Exception;


    // ------------------------------------------------------- protected section

    /**
     * Convenience method for parsing XML stream.
     */
    protected Element parseXmlStream(InputStream stream, String encoding)
        throws IOException, JDOMException, UnsupportedEncodingException
    {
        SAXBuilder builder = new SAXBuilder();

        return builder.build(new InputStreamReader(stream, encoding)).getRootElement();
    }


    /**
     * Convenience method for serializing XML stream.
     */
    protected void serializeXmlStream(Element root, OutputStream stream, String encoding)
        throws JDOMException, IOException
    {
        Format format = Format.getRawFormat();
        format.setEncoding(encoding);
        XMLOutputter out = new XMLOutputter(format);
        out.output(root, stream);
    }
}
