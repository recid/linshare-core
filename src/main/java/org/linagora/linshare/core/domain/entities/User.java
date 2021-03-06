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
package org.linagora.linshare.core.domain.entities;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.linagora.linshare.core.domain.constants.AccountType;
import org.linagora.linshare.core.domain.constants.Role;
import org.linagora.linshare.webservice.dto.UserDto;

public abstract class User extends Account {

	protected Set<ThreadMember> myThreads = new java.util.HashSet<ThreadMember>();

	protected String firstName;

	protected String lastName;

	protected String mail;

	// NOT IMPLEMENTED YET
	protected Date notAfter;

	// NOT IMPLEMENTED YET
	protected Date notBefore;

	protected String ldapUid;

	protected boolean canUpload;

	protected TechnicalAccountPermission technicalAccountPermission;

	/** If the user is allowed to create guest */
	protected boolean canCreateGuest;

	public User() {
		this.firstName = null;
		this.lastName = null;
		this.mail = null;

		this.lsUuid = null;
		this.creationDate = new Date();
		this.modificationDate = new Date();
		this.role = Role.SIMPLE;
		this.enable = true;
		this.destroyed = false;
		this.canUpload = true;
		this.canCreateGuest = true;
		this.externalMailLocale = "en";
	}

	public User(String firstName, String lastName, String mail) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.lsUuid = null;
		this.creationDate = new Date();
		this.modificationDate = new Date();
		this.role = Role.SIMPLE;
		this.enable = true;
		this.destroyed = false;
		this.canUpload = true;
		this.canCreateGuest = true;
		this.externalMailLocale = "en";
	}

	public User(UserDto userDto) {
		this.lsUuid = userDto.getUuid();
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.mail = userDto.getMail();
		this.role = Role.valueOf(userDto.getRole());
		this.canUpload = userDto.getCanUpload();
		this.canCreateGuest = userDto.getCanCreateGuest();
		this.externalMailLocale = userDto.getLocale();
	}

	public void setFirstName(String value) {
		this.firstName = StringUtils.capitalize(value);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String value) {
		this.lastName = StringUtils.capitalize(value);
	}

	public String getLastName() {
		return lastName;
	}

	public void setMail(String value) {
		this.mail = value;
	}

	public String getMail() {
		return mail;
	}

	public String getLogin() {
		return mail;
	}

	public void setNotAfter(Date value) {
		this.notAfter = value;
	}

	public Date getNotAfter() {
		return notAfter;
	}

	public void setNotBefore(Date value) {
		this.notBefore = value;
	}

	public Date getNotBefore() {
		return notBefore;
	}

	public String getLdapUid() {
		return ldapUid;
	}

	public void setLdapUid(String ldapUid) {
		this.ldapUid = ldapUid;
	}

	public void setCanUpload(boolean value) {
		this.canUpload = value;
	}

	public boolean getCanUpload() {
		return canUpload;
	}

	public boolean isRestricted() {
		return false;
	}

	public void setTechnicalAccountPermission(TechnicalAccountPermission value) {
		this.technicalAccountPermission = value;
	}

	public TechnicalAccountPermission getTechnicalAccountPermission() {
		return technicalAccountPermission;
	}

	public Set<ThreadMember> getMyThreads() {
		return myThreads;
	}

	public void setMyThreads(Set<ThreadMember> myThreads) {
		this.myThreads = myThreads;
	}

	public boolean getCanCreateGuest() {
		return canCreateGuest;
	}

	public void setCanCreateGuest(Boolean canCreateGuest) {
		this.canCreateGuest = canCreateGuest;
	}

	/*
	 * Helpers
	 */

	public boolean isInternal() {
		return this.getAccountType().equals(AccountType.INTERNAL);
	}

	public boolean isGuest() {
		return this.getAccountType().equals(AccountType.GUEST);
	}
}
