/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package net.java.slee.resource.diameter.sh.events.avp.userdata;

import java.util.List;

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TExtension;
import org.w3c.dom.Element;

/**
 * <p>Interface for tHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Header" type="{}tString"/>
 *         &lt;element name="Content" type="{}tString" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tExtension" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface Header {

  /**
   * Gets the value of the header property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getHeader();

  /**
   * Sets the value of the header property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setHeader(String value);

  /**
   * Gets the value of the content property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getContent();

  /**
   * Sets the value of the content property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setContent(String value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TExtension }
   *     
   */
  public abstract Extension getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TExtension }
   *     
   */
  public abstract void setExtension(Extension value);

  /**
   * Gets the value of the any property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the any property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAny().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Object }
   * {@link Element }
   * 
   * 
   */
  public abstract List<Object> getAny();

}