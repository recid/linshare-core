/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2013 LINAGORA
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
package org.linagora.linshare.webservice.admin.impl;

import java.net.IDN;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.admin.WebServiceDomainFacade;
import org.linagora.linshare.core.facade.admin.WebServiceFunctionalityFacade;
import org.linagora.linshare.webservice.admin.DomainRestService;
import org.linagora.linshare.webservice.dto.DomainDto;
import org.linagora.linshare.webservice.dto.FunctionalityDto;
import org.linagora.linshare.webservice.impl.WebserviceBase;

public class DomainRestServiceImpl extends WebserviceBase implements
		DomainRestService {

	private final WebServiceDomainFacade webServiceDomainFacade;
	private final WebServiceFunctionalityFacade webServiceFunctionalityFacade;

	public DomainRestServiceImpl(
			final WebServiceDomainFacade webServiceDomainFacade,
			final WebServiceFunctionalityFacade webServiceFunctionalityFacade) {
		this.webServiceDomainFacade = webServiceDomainFacade;
		this.webServiceFunctionalityFacade = webServiceFunctionalityFacade;
	}

	@Path("/")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public DomainDto getDomains() throws BusinessException {
		try {
			webServiceDomainFacade.checkAuthentication();
			return webServiceDomainFacade.getDomains();
		} catch (Exception e) {
			throw analyseFault(e);
		}
	}

	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public void createDomain(DomainDto domain) throws BusinessException {
		try {
			webServiceDomainFacade.checkAuthentication();
			webServiceDomainFacade.createDomain(domain);
		} catch (Exception e) {
			throw analyseFault(e);
		}
	}

	@Path("/")
	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public void updateDomain(DomainDto domain) throws BusinessException {
		try {
			webServiceDomainFacade.checkAuthentication();
			webServiceDomainFacade.updateDomain(domain);
		} catch (Exception e) {
			throw analyseFault(e);
		}
	}

	@Path("/")
	@DELETE
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public void deleteDomain(DomainDto domain) throws BusinessException {
		try {
			webServiceDomainFacade.checkAuthentication();
			webServiceDomainFacade.deleteDomain(domain);
		} catch (Exception e) {
			throw analyseFault(e);
		}
	}

	/*
	 * Functionalities
	 */

	@Path("{domain}/functionality")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public List<FunctionalityDto> getDomainFunctionalities(String domain)
			throws BusinessException {
		try {
			webServiceFunctionalityFacade.checkAuthentication();
			return webServiceFunctionalityFacade.getAll(domain);
		} catch (BusinessException e) {
			throw analyseFault(e);
		}
	}

	@Path("{domain}/functionality/{identifier}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public FunctionalityDto getDomainFunctionality(String domain,
			String identifier) throws BusinessException {
		try {
			webServiceFunctionalityFacade.checkAuthentication();
			return webServiceFunctionalityFacade.get(domain, identifier);
		} catch (BusinessException e) {
			throw analyseFault(e);
		}
	}

	@Path("{domain}/functionality")
	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public void updateDomainFunctionality(String domain, FunctionalityDto func)
			throws BusinessException {
		try {
			webServiceFunctionalityFacade.checkAuthentication();
			webServiceFunctionalityFacade.update(domain, func);
		} catch (BusinessException e) {
			throw analyseFault(e);
		}
	}
}