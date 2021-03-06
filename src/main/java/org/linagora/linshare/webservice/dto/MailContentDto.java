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
package org.linagora.linshare.webservice.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.linagora.linshare.core.domain.constants.Language;
import org.linagora.linshare.core.domain.constants.MailContentType;
import org.linagora.linshare.core.domain.entities.MailContent;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "MailContent")
@ApiModel(value = "MailContent", description = "")
public class MailContentDto {

	@ApiModelProperty(value = "Name")
	private String name;

	@ApiModelProperty(value = "Domain")
	private String domain;

	@ApiModelProperty(value = "Visible")
	private boolean visible;

	@ApiModelProperty(value = "MailContentType")
	private String mailContentType;

	@ApiModelProperty(value = "Language")
	private Language language;

	@ApiModelProperty(value = "Subject")
	private String subject;

	@ApiModelProperty(value = "Greetings")
	private String greetings;

	@ApiModelProperty(value = "Body")
	private String body;

	@ApiModelProperty(value = "CreationDate")
	private Date creationDate;

	@ApiModelProperty(value = "ModificationDate")
	private Date modificationDate;

	@ApiModelProperty(value = "Uuid")
	private String uuid;

	@ApiModelProperty(value = "Plaintext")
	private boolean plaintext;

	public MailContentDto() {
	}

	public MailContentDto(MailContent cont) {
		this.uuid = cont.getUuid();
		this.domain = cont.getDomain().getIdentifier();
		this.name = cont.getName();
		this.body = cont.getBody();
		this.subject = cont.getSubject();
		this.greetings = cont.getGreetings();
		this.language = Language.fromInt(cont.getLanguage());
		this.plaintext = cont.isPlaintext();
		this.visible = cont.isVisible();
		this.mailContentType = MailContentType.fromInt(
				cont.getMailContentType()).toString();
		this.creationDate = new Date(cont.getCreationDate().getTime());
		this.modificationDate = new Date(cont.getModificationDate().getTime());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getMailContentType() {
		return mailContentType;
	}

	public void setMailContentType(String mailContentType) {
		this.mailContentType = mailContentType;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getGreetings() {
		return greetings;
	}

	public void setGreetings(String greetings) {
		this.greetings = greetings;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isPlaintext() {
		return plaintext;
	}

	public void setPlaintext(boolean plaintext) {
		this.plaintext = plaintext;
	}
}
