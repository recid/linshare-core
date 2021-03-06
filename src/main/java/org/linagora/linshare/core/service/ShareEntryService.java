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
package org.linagora.linshare.core.service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.DocumentEntry;
import org.linagora.linshare.core.domain.entities.ShareEntry;
import org.linagora.linshare.core.domain.entities.SystemAccount;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.domain.objects.SuccessesAndFailsItems;
import org.linagora.linshare.core.exception.BusinessException;

public interface ShareEntryService {
	
	public ShareEntry findByUuid(User actor, String uuid)  throws BusinessException ;

	public ShareEntry createShare(DocumentEntry documentEntry, User sender, User recipient, Calendar expirationDate) throws BusinessException;

	public SuccessesAndFailsItems<ShareEntry> createShare(DocumentEntry documentEntry, User sender, List<User> recipients, Calendar expirationDate) ;
	
	public SuccessesAndFailsItems<ShareEntry> createShare(List<DocumentEntry> documentEntries, User sender, List<User> recipients, Calendar expirationDate);

	public void deleteShare(Account actor, String shareUuid) throws BusinessException;
	
	public void deleteShare(Account actor, ShareEntry share) throws BusinessException;
	
	/**
	 * no mail will be sent.
	 * @param actor
	 * @param share
	 * @throws BusinessException
	 */
	public void deleteShare(SystemAccount actor, ShareEntry share) throws BusinessException;

	public DocumentEntry copyDocumentFromShare(String shareUuid, User actor) throws BusinessException; 

	public void updateShareComment(User actor, String uuid, String comment) throws BusinessException;
	
	public boolean shareHasThumbnail(User actor, String shareEntryUuid);
	
	public InputStream getShareThumbnailStream(User actor, String shareEntryUuid) throws BusinessException;
	
	public InputStream getShareStream(User actor, String shareEntryUuid) throws BusinessException;
	
	public void sendDocumentEntryUpdateNotification(ShareEntry shareEntry, String friendlySize, String originalFileName);

	public List<ShareEntry> findAllMyShareEntries(Account actor, User owner);
	
	void sendUpcomingOutdatedShareEntryNotification(SystemAccount actor, ShareEntry shareEntry, Integer days);
	
}
