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

package org.linagora.linshare.core.facade.webservice.user.impl;

import org.linagora.linshare.core.domain.entities.AllowedContact;
import org.linagora.linshare.core.domain.entities.Guest;
import org.linagora.linshare.core.domain.entities.Internal;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.webservice.user.GuestFacade;
import org.linagora.linshare.core.service.AccountService;
import org.linagora.linshare.core.service.GuestService;
import org.linagora.linshare.webservice.dto.UserDto;

public class GuestFacadeImpl extends GenericFacadeImpl implements GuestFacade {

	private final GuestService guestService;

	public GuestFacadeImpl(final AccountService accountService,
			final GuestService guestService) {
		super(accountService);
		this.guestService = guestService;
	}

	@Override
	public UserDto find(String lsUuid) throws BusinessException {
		User actor = checkAuthentication();
		return UserDto.getFull(guestService.findByLsUuid(actor, lsUuid));
	}

	@Override
	public UserDto create(UserDto guestDto, String ownerLsUuid)
			throws BusinessException {
		User actor = checkAuthentication();
		Guest guest = retreiveGuest(guestDto);
		return UserDto.getFull(guestService.create(actor, guest, ownerLsUuid));
	}

	@Override
	public UserDto create(UserDto guestDto) throws BusinessException {
		User actor = checkAuthentication();
		Guest guest = retreiveGuest(guestDto);
		return UserDto.getFull(guestService.create(actor, guest,
				actor.getLsUuid()));
	}

	@Override
	public UserDto update(UserDto guestDto) throws BusinessException {
		User actor = checkAuthentication();
		return UserDto.getFull(guestService.update(actor, new Guest(guestDto),
				guestDto.getOwner().getUuid()));
	}

	@Override
	public void delete(UserDto guestDto) throws BusinessException {
		User actor = checkAuthentication();
		guestService.delete(actor, guestDto.getUuid());
	}

	@Override
	public void delete(String lsUuid) throws BusinessException {
		User actor = checkAuthentication();
		guestService.delete(actor, lsUuid);
	}

	/**
	 * HELPERS
	 */

	private Guest retreiveGuest(UserDto guestDto) {
		Guest guest = new Guest(guestDto);
		if (guest.isRestricted()) {
			for (UserDto contact : guestDto.getRestrictedContacts()) {
				guest.addContact(new AllowedContact(guest,
						new Internal(contact)));
			}
		}
		return guest;
	}
}
