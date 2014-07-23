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

import java.util.List;

import org.linagora.linshare.core.domain.entities.*;
import org.linagora.linshare.core.domain.vo.ShareDocumentVo;
import org.linagora.linshare.core.exception.BusinessException;

public interface MailBuildingService {

	MailContainerWithRecipient buildAnonymousDownload(
			AnonymousShareEntry shareEntry) throws BusinessException;

	MailContainerWithRecipient buildRegisteredDownload(ShareEntry shareEntry)
			throws BusinessException;

	MailContainerWithRecipient buildNewGuest(User sender, User recipient,
			String password) throws BusinessException;

	MailContainerWithRecipient buildResetPassword(Guest recipient,
			String password) throws BusinessException;

	MailContainerWithRecipient buildSharedDocUpdated(Entry shareEntry,
			String oldDocName, String fileSizeTxt) throws BusinessException;

	MailContainerWithRecipient buildSharedDocDeleted(Account actor,
			Entry shareEntry) throws BusinessException;

	MailContainerWithRecipient buildSharedDocUpcomingOutdated(
			Entry shareEntry, Integer days) throws BusinessException;

	MailContainerWithRecipient buildDocUpcomingOutdated(DocumentEntry document,
			Integer days) throws BusinessException;

	MailContainerWithRecipient buildNewSharing(User sender,
			MailContainer inputMailContainer, User recipient,
			List<ShareDocumentVo> shares) throws BusinessException;

	MailContainerWithRecipient buildNewSharingProtected(User sender,
			MailContainer inputMailContainer, AnonymousUrl anonymousUrl)
			throws BusinessException;

	MailContainerWithRecipient buildNewSharingCyphered(User sender,
			MailContainer inputMailContainer, User recipient,
			List<ShareDocumentVo> shares) throws BusinessException;

	MailContainerWithRecipient buildNewSharingCypheredProtected(User sender,
			MailContainer inputMailContainer, AnonymousUrl anonymousUrl)
			throws BusinessException;

	MailContainerWithRecipient buildCreateUploadProposition(User recipient,
															MailContainer inputMailContainer, UploadProposition proposition)
			throws BusinessException;

	MailContainerWithRecipient buildRejectUploadProposition(User sender,
															MailContainer inputMailContainer, UploadProposition proposition)
			throws BusinessException;

	MailContainerWithRecipient buildUpdateUploadRequest(User sender,
														MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildActivateUploadRequest(User sender,
														  MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildFilterUploadRequest(User recipient,
														MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildCreateUploadRequest(User sender,
														MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildAckUploadRequest(User recipient,
													 MailContainer inputMailContainer, UploadRequestUrl request, UploadRequestEntry entry)
			throws BusinessException;

	MailContainerWithRecipient buildRemindUploadRequest(User sender,
														MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildUploadRequestBeforeExpiryWarnOwner(User recipient,
																	   MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildUploadRequestBeforeExpiryWarnRecipient(User sender,
																		   MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildUploadRequestExpiryWarnOwner(User recipient,
																 MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildUploadRequestExpiryWarnRecipient(User sender,
																	 MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildCloseUploadRequestByRecipient(User recipient,
																  MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildCloseUploadRequestByOwner(User sender,
															  MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildDeleteUploadRequestByOwner(User sender,
															   MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildErrorUploadRequestNoSpaceLeft(User recipient,
																  MailContainer inputMailContainer, UploadRequestUrl request)
			throws BusinessException;

	MailContainerWithRecipient buildNewUploadRequest(User sender,
			MailContainer inputMailContainer, UploadRequestUrl requestUrl) throws BusinessException;

}
