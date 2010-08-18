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
 * Interface Extension that adds extra features to the JSR 289 SipSession interface.</br>
 * It adds the following capabilities : 
 * 
 * <ul> 		
 * 		<li>
 * 			Allows for applications to set the outbound interface based on SipURI, to allow routing based on transport protocol as well.
 * 		</li>
 * 		<li>
 * 			Allows for applications to schedule work asynchronously against a SipSession in a thread-safe manner if used in conjunction with the Mobicents Concurrency Control Mechanism
 * 		</li>
 * </ul>
 * 
 * Here is some sample code to show how the asynchronous work can be used :
 * 
 * ((SipSessionExt)sipSession).scheduleAsynchronousWork(new SipSessionAsynchronousWork() {
 * 		private static final long serialVersionUID = 1L;
 * 
 * 		public void doAsynchronousWork(SipSession sipSession) {				
 * 			
 * 			String textMessageToSend = (String) sipSession.getAttribute("textMessageToSend"); 					
 * 			
 *			try {
 *				SipServletRequest sipServletRequest = sipFactory.createRequest(
 *						sipSession.getSipApplicationSession(), 
 *						"MESSAGE", 
 *						"sip:sender@sip-servlets.com", 
 *						"sip:receiver@sip-servlets.com");
 *				SipURI sipUri = sipFactory.createSipURI("receiver", "127.0.0.1:5060");
 *				sipServletRequest.setRequestURI(sipUri);
 *				sipServletRequest.setContentLength(content.length());
 *				sipServletRequest.setContent(content, "text/plain;charset=UTF-8");
 *				sipServletRequest.send();
 *			} catch (ServletParseException e) {
 *				logger.error("Exception occured while parsing the addresses",e);
 *			} catch (IOException e) {
 *				logger.error("Exception occured while sending the request",e);			
 *			}
 * 		}
 * 	});
 * 
 * 
 * @author jean.deruelle@gmail.com
 * @since 1.4
 */
public interface SipSessionExt {
	/**
     * In multi-homed environment this method can be used to select the outbound interface to use when sending requests for this SipSession. 
     * The specified address must be the address of one of the configured outbound interfaces. The set of SipURI objects which represent the supported outbound interfaces can be obtained from the servlet context attribute named javax.servlet.sip.outboundInterfaces.
     * Invocation of this method also impacts the system headers generated by the container for this message, such as the the Via and the Contact header. 
     * The supplied IP address, port and transport are used to construct these system headers.
     * @param outboundInterface the sip uri representing the outbound interface to use when sending requests out  
     * @throws NullPointerException on null sip uri
     * @throws IllegalArgumentException if the sip uri is not understood by the container as one of its outbound interface 
     */
    void setOutboundInterface(SipURI outboundInterface);
    /**
	 * This method allows an application to access a SipSession in an asynchronous manner. 
	 * This method is useful for accessing the SipSession from Web or EJB modules in a converged application 
	 * or from unmanaged threads started by the application itself.
	 * 
     * When this API is used in conjunction with the Mobicents Concurrency Control in SipSession mode, 
     * the container guarantees that the business logic contained within the SipSessionAsynchronousWork
     * will be executed in a thread-safe manner. 
     * 
     * It has to be noted that the work may never execute if the session gets invalidated in the meantime
     * and the work will be executed locally on the node on a cluster.
     * 
	 * @param work the work to be performed on this SipSession. 
	 */
    void scheduleAsynchronousWork(SipSessionAsynchronousWork work);
}
