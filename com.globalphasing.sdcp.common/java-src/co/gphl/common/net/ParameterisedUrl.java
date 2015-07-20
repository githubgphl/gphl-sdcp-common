/*
 * Copyright © 2015 by Global Phasing Ltd. All rights reserved
 *
 * This software is proprietary to and embodies the confidential
 * technology of Global Phasing Limited (GPhL).
 *
 * Any possession or use (including but not limited to duplication, reproduction
 * and dissemination) of this software (in either source or compiled form) is
 * forbidden except where an agreement with GPhL that permits such possession or
 * use is in force.
 *
 */

package co.gphl.common.net;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common superclass for URLs that are used to specify a beamline service to be
 * loaded. Services should define a subclass with methods to extract the
 * parameters needed to create a beamline service instance.
 * 
 * <p>
 * Note that we use a {@link URI} rather than a {@link java.net.URL URL} as the
 * parameter for the constructor. This is a (possibly temporary) device to avoid
 * having to register URL protocols that we invent: URI schemes don't have to be
 * registered and can be invented on an ad-hoc basis. Conceptually it is still a
 * URL, since it is used to locate something, not merely to identify it.
 * </p>
 * <p>
 * In the future, we may change this so that true {@link java.net.URL URL}
 * instances are used as the constructor parameter, and register a set of custom
 * protocols for them, if it can be done in a tractable way.
 * </p>
 * 
 * @author pkeller
 * 
 */
public abstract class ParameterisedUrl {

    protected URI uri;
    private final Map<String, List<String>> params;
    
    /**
     * Service URL constructor.
     * See {@linkplain ParameterisedUrl javaodoc of this class} for an explanation
     * of why the {@code url} parameter is of type {@code URI}.
     * 
     * @param url
     * @throws URISyntaxException
     */
    protected ParameterisedUrl(URI url, String scheme) throws URISyntaxException {
        
        // Basic sanity check
        if ( ! url.getScheme().toLowerCase().equals(scheme) )
            throw new IllegalArgumentException("Expecting URI with scheme " 
                    + scheme + ", got " + url.toASCIIString() );
        
        this.uri = url;
        
        // TODO: if any service URLs start to use fragments, extract them here too.
        
        // This handling of queries is inspired by http://stackoverflow.com/a/13592567/1866402
        
        this.params = new HashMap<String, List<String>>();
        
        // We use getRawQuery() rather than getQuery(), so that we can cope with query parameter
        // names and values containing %-encoded '&' and '=' characters.
        String query = this.uri.getRawQuery();
        if ( query != null && query.length() > 0 ) {

            try {

                for ( String q: query.split("&") ) {

                    String[] p = q.split("=", 2);
                    
                    /* The Javadoc for URLDecoder.decode states that encodings
                     * other than UTF-8 go against WWWC recommendations and
                     * may introduce incompatibilities */
                    String name = URLDecoder.decode(p[0], "UTF-8");
                    List<String> vals = this.params.get(name);

                    if ( vals == null ) {
                        vals = new ArrayList<String>();
                        this.params.put(name, vals);
                    }

                    if ( p.length == 1 )
                        vals.add(null);
                    else 
                        vals.add( URLDecoder.decode(p[1], "UTF-8") );

                }

            }
            catch ( UnsupportedEncodingException e ) {
                throw new RuntimeException("Serious configuration problem: UTF-8 should be supported by any JVM!", e);
            }

        }
    }
    
    protected String getNamedParam( String paramName, boolean required ) {

        List<String> vals = this.params == null ? null : this.params.get(paramName);

        String retval = null;
        if ( vals != null ) {
            if ( vals.size() > 1 )
                throw new IllegalStateException(
                    String.format("%d values defined for parameter '%s': we can only handle 1",
                        vals.size(), paramName) );
            else if ( vals.size() == 1 )
                retval = vals.get(0);
        }

        if ( required && ( retval == null || retval.length() == 0 ) )
            throw new IllegalStateException(
                String.format("No value for parameter '%s' defined. URI is: %s",
                    paramName, this.uri.toString()));

        return retval;

    }
    
    public String toString() {
        return this.uri.toString();
    }
    
}
