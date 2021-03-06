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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.linagora.linshare.core.domain.entities.Functionality;

import com.google.common.collect.Ordering;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

// This field is added to the JSON object by the angular interface for internationalization. 
@JsonIgnoreProperties({"name"})
@XmlRootElement(name = "Functionality")
@ApiModel(value = "Functionality", description = "Functionalities are used to configure the application")
public class FunctionalityDto implements Comparable<FunctionalityDto> {

	@ApiModelProperty(value = "Identifier")
	protected String identifier;

	@XmlTransient
	@ApiModelProperty(value = "Name")
	private String name;

	@ApiModelProperty(value = "Domain")
	protected String domain;

	@ApiModelProperty(value = "Type")
	protected String type;

	@ApiModelProperty(value = "ActivationPolicy")
	protected PolicyDto activationPolicy;

	@ApiModelProperty(value = "ConfigurationPolicy")
	protected PolicyDto configurationPolicy;

	@ApiModelProperty(value = "DelegationPolicy")
	protected PolicyDto delegationPolicy;

	// This field is designed to indicate if the parent functionality allow you to update the parameters.
	@ApiModelProperty(value = "ParentAllowParametersUpdate")
	protected boolean parentAllowParametersUpdate;

	@ApiModelProperty(value = "Parameters")
	protected List<ParameterDto> parameters;

	@ApiModelProperty(value = "parentIdentifier")
	protected String parentIdentifier;

	@ApiModelProperty(value = "functionalities")
	protected List<FunctionalityDto> functionalities;

	@ApiModelProperty(value = "displayable")
	protected boolean displayable;

	public FunctionalityDto() {
		super();
	}

	public FunctionalityDto(Functionality f,
			boolean parentAllowAPUpdate,
			boolean parentAllowCPUpdate,
			boolean parentAllowDPUpdate,
			boolean parentAllowParametersUpdate) {
		super();
		this.domain = f.getDomain().getIdentifier();
		this.identifier = f.getIdentifier();
		// Activation policy
		this.activationPolicy = new PolicyDto(f.getActivationPolicy());
		this.activationPolicy.setParentAllowUpdate(parentAllowAPUpdate);
		// Configuration policy
		this.configurationPolicy = new PolicyDto(f.getConfigurationPolicy());
		this.configurationPolicy.setParentAllowUpdate(parentAllowCPUpdate);
		// Delegation policy
		if (f.getDelegationPolicy() != null) {
			this.delegationPolicy = new PolicyDto(f.getDelegationPolicy());
			this.delegationPolicy.setParentAllowUpdate(parentAllowDPUpdate);
		}
		// Parameters
		this.parentAllowParametersUpdate = parentAllowParametersUpdate;
		this.parameters = f.getParameters();
		this.type = f.getType().toString();
		this.parentIdentifier = f.getParentIdentifier();
		functionalities = new ArrayList<FunctionalityDto>();
		this.displayable = true;

		if (!parentAllowAPUpdate && f.getActivationPolicy().isForbidden()) {
			// No modification allowed, functionality is forbidden.
			// You will never be able to perform any modification on the functionality
			this.displayable = false;
		}
		if (!parentAllowAPUpdate && f.getActivationPolicy().isMandatory()) {
			// No modification allowed for AP, functionality is mandatory.
			if(!parentAllowCPUpdate && f.getConfigurationPolicy().isForbidden()) {
				// No modification allowed for CP, functionality configuration is forbidden.
				// You will never be able to perform any modification on the functionality
				this.displayable = false;
			}
		}
		if(parameters.size() == 0)
			this.parentAllowParametersUpdate = false;
		if (!parentAllowAPUpdate && !parentAllowCPUpdate && !this.parentAllowParametersUpdate) {
			this.displayable = false;
		}
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public PolicyDto getActivationPolicy() {
		return activationPolicy;
	}

	public void setActivationPolicy(PolicyDto activationPolicy) {
		this.activationPolicy = activationPolicy;
	}

	public PolicyDto getConfigurationPolicy() {
		return configurationPolicy;
	}

	public void setConfigurationPolicy(PolicyDto configurationPolicy) {
		this.configurationPolicy = configurationPolicy;
	}

	public PolicyDto getDelegationPolicy() {
		return delegationPolicy;
	}

	public void setDelegationPolicy(PolicyDto delegationPolicy) {
		this.delegationPolicy = delegationPolicy;
	}

	public boolean isParentAllowParametersUpdate() {
		return parentAllowParametersUpdate;
	}

	public void setParentAllowParametersUpdate(boolean parentAllowParametersUpdate) {
		this.parentAllowParametersUpdate = parentAllowParametersUpdate;
	}

	public List<ParameterDto> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterDto> parameters) {
		this.parameters = parameters;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentIdentifier() {
		return parentIdentifier;
	}

	public void setParentIdentifier(String parentIdentifier) {
		this.parentIdentifier = parentIdentifier;
	}

	public List<FunctionalityDto> getFunctionalities() {
		return Ordering.natural().immutableSortedCopy(functionalities);
	}

	public void setFunctionalities(List<FunctionalityDto> functionalities) {
		this.functionalities = functionalities;
	}

	public void addFunctionalities(FunctionalityDto functionality) {
		if (functionality != null) {
			this.functionalities.add(functionality);
			if(functionality.isDisplayable()) {
				this.displayable = true;
			}
		}
	}

	public boolean isDisplayable() {
		return displayable;
	}

	@Override
	public int compareTo(FunctionalityDto o) {
		return this.identifier.compareTo(o.getIdentifier());
	}

	@Override
	public String toString() {
		return "FunctionalityDto [identifier=" + identifier + ", domain="
				+ domain + "]";
	}
}
