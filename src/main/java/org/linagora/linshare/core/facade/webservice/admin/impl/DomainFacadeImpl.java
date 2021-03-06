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
 * and free version of LinShare™, powered by Linagora © 2009–2014. Contribute to
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
package org.linagora.linshare.core.facade.webservice.admin.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.linagora.linshare.core.domain.constants.DomainType;
import org.linagora.linshare.core.domain.constants.Role;
import org.linagora.linshare.core.domain.entities.AbstractDomain;
import org.linagora.linshare.core.domain.entities.DomainPattern;
import org.linagora.linshare.core.domain.entities.DomainPolicy;
import org.linagora.linshare.core.domain.entities.GuestDomain;
import org.linagora.linshare.core.domain.entities.LDAPConnection;
import org.linagora.linshare.core.domain.entities.LdapUserProvider;
import org.linagora.linshare.core.domain.entities.MailConfig;
import org.linagora.linshare.core.domain.entities.MimePolicy;
import org.linagora.linshare.core.domain.entities.SubDomain;
import org.linagora.linshare.core.domain.entities.TopDomain;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.webservice.admin.DomainFacade;
import org.linagora.linshare.core.service.AbstractDomainService;
import org.linagora.linshare.core.service.AccountService;
import org.linagora.linshare.core.service.DomainPolicyService;
import org.linagora.linshare.core.service.UserAndDomainMultiService;
import org.linagora.linshare.core.service.UserProviderService;
import org.linagora.linshare.webservice.dto.DomainDto;

import com.google.common.collect.Sets;

public class DomainFacadeImpl extends AdminGenericFacadeImpl implements
		DomainFacade {

	private final AbstractDomainService abstractDomainService;

	private final UserProviderService userProviderService;

	private final DomainPolicyService domainPolicyService;

	private final UserAndDomainMultiService userAndDomainMultiService;

	public DomainFacadeImpl(final AccountService accountService,
			final AbstractDomainService abstractDomainService,
			final UserProviderService userProviderService,
			final DomainPolicyService domainPolicyService,
			final UserAndDomainMultiService userAndDomainMultiService) {
		super(accountService);
		this.abstractDomainService = abstractDomainService;
		this.userProviderService = userProviderService;
		this.domainPolicyService = domainPolicyService;
		this.userAndDomainMultiService = userAndDomainMultiService;
	}

	@Override
	public Set<DomainDto> findAll() throws BusinessException {
		User actor = checkAuthentication(Role.ADMIN);
		Set<DomainDto> domainDtoList = Sets.newHashSet();
		List<AbstractDomain> entities = abstractDomainService.findAll(actor);
		for (AbstractDomain abstractDomain : entities) {
			domainDtoList.add(DomainDto.getFull(abstractDomain));
		}
		return domainDtoList;
	}

	@Override
	public DomainDto find(String domain, boolean tree)
			throws BusinessException {
		User actor = checkAuthentication(Role.ADMIN);
		Validate.notEmpty(domain, "domain identifier must be set.");
		AbstractDomain entity = abstractDomainService.retrieveDomain(domain);
		if (entity == null) {
			throw new BusinessException(BusinessErrorCode.NO_SUCH_ELEMENT,
					"the curent domain was not found : " + domain);
		}
		if (tree) {
			if (actor.hasSuperAdminRole()) {
				return DomainDto.getFullTree(entity);
			}
			if (entity.isManagedBy(actor)) {
				return DomainDto.getSimpleTree(entity);
			}
		} else {
			if (entity.isManagedBy(actor)) {
				return DomainDto.getSimple(entity);
			}
		}
		throw new BusinessException(BusinessErrorCode.NO_SUCH_ELEMENT,
				"the curent domain was not found : " + domain);
	}

	@Override
	public DomainDto create(DomainDto domainDto) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		AbstractDomain domain = getDomain(domainDto);
		switch (domain.getDomainType()) {
		case TOPDOMAIN:
			return DomainDto.getFull(abstractDomainService.createTopDomain(actor, (TopDomain) domain));
		case SUBDOMAIN:
			return DomainDto.getFull(abstractDomainService.createSubDomain(actor, (SubDomain) domain));
		case GUESTDOMAIN:
			return DomainDto.getFull(abstractDomainService.createGuestDomain(actor, (GuestDomain) domain));
		default:
			throw new BusinessException(BusinessErrorCode.DOMAIN_INVALID_TYPE,
					"Try to create a root domain");
		}
	}

	@Override
	public DomainDto update(DomainDto domainDto) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(domainDto.getIdentifier(),
				"domain identifier must be set.");
		AbstractDomain domain = getDomain(domainDto);
		return DomainDto.getFull(abstractDomainService.updateDomain(actor, domain));
	}

	@Override
	public void delete(DomainDto domainDto) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(domainDto.getIdentifier(),
				"domain identifier must be set.");
		userAndDomainMultiService.deleteDomainAndUsers(actor, domainDto.getIdentifier());
	}

	private AbstractDomain getDomain(DomainDto domainDto)
			throws BusinessException {
		checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(domainDto.getIdentifier(),
				"domain identifier must be set.");
		Validate.notNull(domainDto.getPolicy(), "domain policy must be set.");
		Validate.notEmpty(domainDto.getPolicy().getIdentifier(),
				"domain policy identifier must be set.");

		DomainType domainType = DomainType.valueOf(domainDto.getType());
		AbstractDomain parent = abstractDomainService.retrieveDomain(domainDto
				.getParent());
		AbstractDomain domain = domainType.getDomain(domainDto, parent);
		DomainPolicy policy = domainPolicyService
				.find(domainDto.getPolicy().getIdentifier());
		domain.setPolicy(policy);


		if (domainDto.getMailConfigUuid() != null) {
			MailConfig mailConfig = new MailConfig();
			mailConfig.setUuid(domainDto.getMailConfigUuid());
			domain.setCurrentMailConfiguration(mailConfig);
		}
		if (domainDto.getMimePolicyUuid() != null) {
			MimePolicy mimePolicy = new MimePolicy();
			mimePolicy.setUuid(domainDto.getMimePolicyUuid());
			domain.setMimePolicy(mimePolicy);
		}

		if (!domainDto.getProviders().isEmpty()) {
			String baseDn = domainDto.getProviders().get(0).getBaseDn();
			Validate.notEmpty(baseDn, "ldap base dn must be set.");

			String domainPatternId = domainDto.getProviders().get(0)
					.getDomainPatternId();
			Validate.notEmpty(domainPatternId,
					"domain pattern identifier must be set.");

			String ldapConnectionId = domainDto.getProviders().get(0)
					.getLdapConnectionId();
			Validate.notEmpty(ldapConnectionId,
					"ldap connection identifier must be set.");

			LDAPConnection ldapConnection = userProviderService
					.retrieveLDAPConnection(ldapConnectionId);
			DomainPattern domainPattern = userProviderService
					.retrieveDomainPattern(domainPatternId);
			domain.setUserProvider(new LdapUserProvider(baseDn, ldapConnection,
					domainPattern));
		}
		return domain;
	}
}
