/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2014 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2013. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */
package org.linagora.linshare.webservice.test.soap;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.7.0
 * 2013-01-17T17:17:27.997+01:00
 * Generated source version: 2.7.0
 */

@WebFault(name = "BusinessException", targetNamespace = "http://webservice.linshare.linagora.org/")
public class BusinessException_Exception extends Exception {
    
    private org.linagora.linshare.webservice.test.soap.BusinessException businessException;

    public BusinessException_Exception() {
        super();
    }
    
    public BusinessException_Exception(String message) {
        super(message);
    }
    
    public BusinessException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException_Exception(String message, org.linagora.linshare.webservice.test.soap.BusinessException businessException) {
        super(message);
        this.businessException = businessException;
    }

    public BusinessException_Exception(String message, org.linagora.linshare.webservice.test.soap.BusinessException businessException, Throwable cause) {
        super(message, cause);
        this.businessException = businessException;
    }

    public org.linagora.linshare.webservice.test.soap.BusinessException getFaultInfo() {
        return this.businessException;
    }
}
