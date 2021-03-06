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

package org.linagora.linshare.core.service.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.service.AntiSamyService;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiSamyServiceImpl implements AntiSamyService {

	private static final Logger logger = LoggerFactory
			.getLogger(AntiSamyServiceImpl.class);
	
	private final Policy policy;

	public AntiSamyServiceImpl(final Policy policy) {
		this.policy = policy;
	}
	
	@Override
	public String clean(String value)
			throws BusinessException {
		if (value == null)
			return null;

		try {
			CleanResults cr = new AntiSamy().scan(value, policy);

			if (cr.getNumberOfErrors() > 0)
				logger.warn("Striped invalid chracters in : " + value);

			return StringEscapeUtils.unescapeHtml(cr.getCleanHTML().trim());
		} catch (ScanException e) {
			throw new BusinessException(
				BusinessErrorCode.XSSFILTER_SCAN_FAILED,
				"Antisamy is not able to scan the field");
		} catch (PolicyException e) {
			throw new BusinessException(
				BusinessErrorCode.XSSFILTER_SCAN_FAILED,
				"Antisamy is not able to scan the field");
		}
	}
}
