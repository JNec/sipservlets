/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.javax.servlet.sip;

import javax.servlet.sip.SipURI;

/**
 * Interface Extension that adds extra features to the JSR 289 Proxy interface.</br>
 * It adds the following capabilities : 
 * 
 * <ul>
 * 		<li>
 * 			Allows for applications to set a timeout on 1xx responses as JSR 289 defines a timeout only for final responses.
 * 		</li>
 * 		<li>
 * 			Allows for applications to set the outbound interface based on SipURI, to allow routing based on transport protocol as well.
 * 		</li>
 * </ul>
 * 
 * Here is some sample code to show how it can be used :
 * 
 * <pre>
 * 	import java.io.IOException;
 * 	import java.util.ArrayList;
 * 	import javax.servlet.ServletException;
 * 	import javax.servlet.sip.Proxy;
 * 	import javax.servlet.sip.ProxyBranch;
 * 	import javax.servlet.sip.SipFactory;
 * 	import javax.servlet.sip.SipServlet;
 * 	import javax.servlet.sip.SipServletRequest;
 * 	import javax.servlet.sip.URI;
 * 	
 * 	import org.mobicents.javax.servlet.sip.ProxyBranchListener;
 * 	import org.mobicents.javax.servlet.sip.ProxyExt;
 * 	import org.mobicents.javax.servlet.sip.ResponseType;
 * 
 * 	public class ProxySipServlet extends SipServlet implements ProxyBranchListener {
 *  
 * 	protected void doInvite(SipServletRequest request) throws ServletException,
 * 			IOException {
 * 
 * 		if(!request.isInitial()){
 * 			return;
 * 		}
 * 			
 * 		SipFactory sipFactory = (SipFactory) getServletContext().getAttribute(SIP_FACTORY);
 * 		Proxy proxy = request.getProxy();
 * 		proxy.setParallel(false);
 * 		// set the timeout for receiving a final response
 * 		proxy.setProxyTimeout(5);
 * 		// set the timeout for receiving a 1xx response
 * 		((ProxyExt)proxy).setProxy1xxTimeout(1);				
 * 		proxy.setRecordRoute(true);
 * 		ArrayList<URI> uris = new ArrayList<URI>();
 * 		URI uri1 = sipFactory.createAddress("sip:receiver@127.0.0.1:5057").getURI();		
 * 		URI uri2 = sipFactory.createAddress("sip:second-receiver@127.0.0.1:5056").getURI();
 * 		uris.add(uri2);
 * 		uris.add(uri1);
 *  
 * 		proxy.proxyTo(uris);		
 *  }
 * 	
 * 	/**
 * 	 * Called if no 1xx and no final response has been received with a response type of INFORMATIONAL
 * 	 * Called if no 2xx response has been received with a response type of FINAL
 * 	 *\/
 * 	public void onProxyBranchResponseTimeout(ResponseType responseType,
 * 			ProxyBranch proxyBranch) {
 * 		logger.info("onProxyBranchResponseTimeout callback was called. responseType = " + responseType + " , branch = " + proxyBranch + ", request " + proxyBranch.getRequest() + ", response " + proxyBranch.getResponse());
 * 	}
 * </pre>
 * @author jean.deruelle@gmail.com
 * @since 1.3
 */
public interface ProxyExt {
	/**
	 * This is the amount of time, in seconds, the container waits for an informational response when proxying.</br>
	 * <ul>
	 * 	<li> 
	 * 		If the proxy is sequential, when the timer expires and no 1xx response nor final response has been received,
	 * 		the container CANCELs the current branch and proxies to the next element in the target set.
	 * 	</li>
	 *  <li> 
	 * 		If the proxy is parallel, then this acts as the upper limit for the entire proxy operation resulting in equivalent of invoking cancel()
	 * 		if the the proxy did not complete during this time, which means that neither an informational response nor a final response was not sent upstream. 
	 * 	</li>
	 * </ul>
	 * @param timeout new search 1xx timeout in seconds
	 * @throws IllegalArgumentException if the container cannot set the value as requested because it is too high, too low or negative
	 * @since 1.3
	 */
	public void setProxy1xxTimeout(int timeout);
	/**
	 * The current value of the overall proxy 1xx timeout value. This is measured in seconds.
	 * @return current value of proxy timeout in seconds.
	 * @since 1.3
	 */
	public int getProxy1xxTimeout();
	/**
	 * In multi-homed environment this method can be used to select the outbound interface and port number and transport to use for proxy branches. 
     * The specified address must be the address of one of the configured outbound interfaces. 
     * The set of SipURI objects which represent the supported outbound interfaces can be obtained from the servlet context attribute named javax.servlet.sip.outboundInterfaces.
     * 
     * The port is interpreted as an advice by the app to the container.  If the port of the socket address has a non-zero value, the container will make a best-effort attempt to use it as the source port number for UDP packets, 
     * or as a source port number for TCP connections it originates. 
     * If the port is not available, the container will use its default port allocation scheme.
     * 
     * Invocation of this method also impacts the system headers generated by the container for this Proxy, 
     * such as the Record-Route header (getRecordRouteURI()), the Via and the Contact header. 
     * The IP address, port and transport parts of the SipURI are used to construct these system headers.
     * @param outboundInterface the sip uri representing the outbound interface to use when forwarding requests with this proxy 
     * @throws NullPointerException on null sip uri
     * @throws IllegalArgumentException if the sip uri is not understood by the container as one of its outbound interface 
	 * @since 1.4
	 */
	void setOutboundInterface(SipURI outboundInterface);
}
