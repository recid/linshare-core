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
package org.linagora.linshare.view.tapestry.pages.uploadrequest;

import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.linagora.linshare.core.domain.vo.UploadRequestVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.FunctionalityFacade;
import org.linagora.linshare.core.facade.UploadRequestFacade;
import org.linagora.linshare.view.tapestry.beans.ShareSessionObjects;
import org.linagora.linshare.view.tapestry.enums.BusinessUserMessageType;
import org.linagora.linshare.view.tapestry.objects.BusinessUserMessage;
import org.linagora.linshare.view.tapestry.objects.MessageSeverity;
import org.linagora.linshare.view.tapestry.services.BusinessMessagesManagementService;
import org.slf4j.Logger;

public class Detail {

	@SessionState
	@Property
	private ShareSessionObjects shareSessionObjects;

	@SessionState
	@Property
	private UserVo userVo;

	@Property
	@Persist
	private UploadRequestVo selected;

	@InjectPage
	private Edit edit;

	@InjectPage
	private History history;

	@Inject
	private PersistentLocale persistentLocale;

	@Inject
	private Logger logger;

	@Inject
	private Messages messages;

	@Inject
	private FunctionalityFacade functionalityFacade;

	@Inject
	private UploadRequestFacade uploadRequestFacade;

	@Inject
	private BusinessMessagesManagementService businessMessagesManagementService;

	public Object onActivate(String uuid) {
		try {
			this.selected = uploadRequestFacade.findRequestByUuid(userVo, uuid);
		} catch (BusinessException e) {
			businessMessagesManagementService.notify(new BusinessUserMessage(
					BusinessUserMessageType.UPLOAD_REQUEST_NOT_FOUND,
					MessageSeverity.ERROR));
			return Index.class;
		}
		return null;
	}

	public Object onActivate() {
		if (!functionalityFacade.isEnableUploadRequest(userVo
				.getDomainIdentifier())) {
			return Index.class;
		}
		if (selected == null) {
			logger.info("No upload request selected, abort");
			return Index.class;
		}
		if (!selected.getOwner().businessEquals(userVo)
				|| !selected.isVisible()) {
			logger.info("Unauthorized");
			businessMessagesManagementService.notify(new BusinessUserMessage(
					BusinessUserMessageType.UPLOAD_REQUEST_NOT_FOUND,
					MessageSeverity.ERROR));
			return Index.class;
		}
		return null;
	}

	public Object onPassivate() {
		return selected.getUuid();
	}

	@Log
	public Object onActionFromBack() throws BusinessException {
		return Content.class;
	}

	@Log
	public void onActionFromClose() throws BusinessException {
		selected = uploadRequestFacade.closeRequest(userVo, selected);
	}

	@Log
	public Object onActionFromDelete() throws BusinessException {
		selected = uploadRequestFacade.archiveRequest(userVo, selected);
		return Index.class;
	}

	@Log
	public Object onActionFromEdit() throws BusinessException {
		edit.setMySelected(selected);
		return edit;
	}

	@Log
	public Object onActionFromHistory() throws BusinessException {
		history.setMySelected(selected);
		return history;
	}

	public void setMySelected(UploadRequestVo selected) {
		this.selected = selected;
	}

	/*
	 * TODO: ugly
	 */
	public String getFileSize(int bytes) {
		boolean si = !persistentLocale.get().equals(Locale.ENGLISH);
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/*
	 * Exception Handling
	 */

	Object onException(Throwable cause) {
		shareSessionObjects.addError(messages.get("global.exception.message"));
		logger.error(cause.getMessage());
		cause.printStackTrace();
		return this;
	}
}
