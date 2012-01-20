/*
 *    This file is part of Linshare.
 *
 *   Linshare is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   Linshare is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with Foobar.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
 */
package org.linagora.linShare.service;

import org.linagora.linShare.core.domain.constants.FileSizeUnit;
import org.linagora.linShare.core.domain.constants.Policies;
import org.linagora.linShare.core.domain.entities.AbstractDomain;
import org.linagora.linShare.core.domain.entities.DomainAccessPolicy;
import org.linagora.linShare.core.domain.entities.DomainPolicy;
import org.linagora.linShare.core.domain.entities.FileSizeUnitClass;
import org.linagora.linShare.core.domain.entities.Functionality;
import org.linagora.linShare.core.domain.entities.Policy;
import org.linagora.linShare.core.domain.entities.RootDomain;
import org.linagora.linShare.core.domain.entities.StringValueFunctionality;
import org.linagora.linShare.core.domain.entities.SubDomain;
import org.linagora.linShare.core.domain.entities.TopDomain;
import org.linagora.linShare.core.domain.entities.UnitValueFunctionality;
import org.linagora.linShare.core.exception.BusinessException;
import org.linagora.linShare.core.repository.AbstractDomainRepository;
import org.linagora.linShare.core.repository.DomainPolicyRepository;
import org.linagora.linShare.core.repository.FunctionalityRepository;
import org.linagora.linShare.core.service.impl.FunctionalityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingServiceTestDatas {
	
	protected Logger logger = LoggerFactory.getLogger(FunctionalityServiceImpl.class);
	
	private FunctionalityRepository functionalityRepository;
	private AbstractDomainRepository abstractDomainRepository;
	private DomainPolicyRepository domainPolicyRepository;
	
	public static String rootDomaineName = "TEST_Domain-0";
	public static String topDomaineName = "TEST_Domain-0-1";
	public static String topDomaineName2 = "TEST_Domain-0-2";
	public static String subDomaineName1 = "TEST_Domain-0-1-1";
	public static String subDomaineName2 = "TEST_Domain-0-1-2";
	
	public static String TEST_TIME_STAMPING="TEST_TIME_STAMPING";
	public static String FILESIZE_MAX="TEST_FILESIZE_MAX";
	public static String QUOTA_USER="TEST_QUOTA_USER";
	public static String QUOTA_GLOBAL="TEST_QUOTA_GLOBAL";
	public static String GUEST="GUEST";
	public static String FUNC1="TEST_FUNC1";
	public static String FUNC2="TEST_FUNC2";
	public static String FUNC3="TEST_FUNC3";
	public static String FUNC4="TEST_FUNC4";
	public static String FUNC5="TEST_FUNC5";
	
	private static String domainePolicyName0 = "TestAccessPolicy0";
	
	public static int TOTAL_COUNT_FUNC=10;
	public static String timeStampingUrl = "http://server/service";
	
	private RootDomain rootDomain;
	private DomainPolicy defaultPolicy;
	

	public LoadingServiceTestDatas(
			FunctionalityRepository functionalityRepository,
			AbstractDomainRepository abstractDomainRepository,
			DomainPolicyRepository domainPolicyRepository) {
		super();
		this.functionalityRepository = functionalityRepository;
		this.abstractDomainRepository = abstractDomainRepository;
		this.domainPolicyRepository = domainPolicyRepository;
	}

	public RootDomain getRootDomain() {
		return rootDomain;
	}

	public  void deleteDatas() throws BusinessException {
		abstractDomainRepository.delete(getRootDomain());
		domainPolicyRepository.delete(defaultPolicy);
	}
		
		
	public  void loadDatas() throws BusinessException {
		
		defaultPolicy = new DomainPolicy(domainePolicyName0, new DomainAccessPolicy());
		domainPolicyRepository.create(defaultPolicy);
		
		rootDomain= new RootDomain(rootDomaineName,rootDomaineName);
		rootDomain.setPolicy(defaultPolicy);
		abstractDomainRepository.create(rootDomain);
		logger.debug("Current AbstractDomain object: " + rootDomain.toString());
		
		TopDomain topDomain = new TopDomain(topDomaineName,topDomaineName,rootDomain);
		topDomain.setPolicy(defaultPolicy);
		abstractDomainRepository.create(topDomain);
		rootDomain.addSubdomain(topDomain);
		abstractDomainRepository.update(rootDomain);
		logger.debug("Current TopDomain object: " + topDomain.toString());

		SubDomain subDomain1 = new SubDomain(subDomaineName1,subDomaineName1,topDomain);
		subDomain1.setPolicy(defaultPolicy);
		abstractDomainRepository.create(subDomain1);
		topDomain.addSubdomain(subDomain1);
		abstractDomainRepository.update(topDomain);
		logger.debug("Current SubDomain object: " + subDomain1.toString());
		
		SubDomain subDomain2 = new SubDomain(subDomaineName2,subDomaineName2,topDomain);
		subDomain2.setPolicy(defaultPolicy);
		abstractDomainRepository.create(subDomain2);
		topDomain.addSubdomain(subDomain2);
		abstractDomainRepository.update(topDomain);
		logger.debug("Current SubDomain object: " + subDomain2.toString());
		
		createTimeStampingFunctionality(rootDomain);
		createMaxFileSizeFunctionality(rootDomain,200);
		createQuotaUserFunctionality(rootDomain,500);
		createQuotaGlobalFunctionality(rootDomain);
		createGuestFunctionality(rootDomain);
		createFunc1Functionality(rootDomain);
		createFunc2Functionality(rootDomain);
		createFunc3Functionality(rootDomain);
		createFunc4Functionality(rootDomain);
		createFunc5Functionality(rootDomain);
		
		
		createMaxFileSizeFunctionality(topDomain,100);
		createQuotaUserFunctionality(topDomain,250);
		
		createMaxFileSizeFunctionality(subDomain1,50);
		
		createQuotaUserFunctionality(subDomain2,125);
		
		
		
		
		
		TopDomain topDomain2 = new TopDomain(topDomaineName2,topDomaineName2,rootDomain);
		topDomain2.setPolicy(defaultPolicy);
		abstractDomainRepository.create(topDomain2);
		rootDomain.addSubdomain(topDomain2);
		abstractDomainRepository.update(rootDomain);
		logger.debug("Current topDomain2 object: " + topDomain2.toString());
		
		
	}

	
	
	private void createTimeStampingFunctionality(AbstractDomain currentDomain) throws BusinessException {
		
		
		Functionality fonc = new StringValueFunctionality(TEST_TIME_STAMPING,
						false,
						new Policy(Policies.ALLOWED, true),
						new Policy(Policies.ALLOWED, true),
						currentDomain,
						timeStampingUrl);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createGuestFunctionality(AbstractDomain currentDomain) throws BusinessException {
		
		Functionality fonc = new Functionality(GUEST,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createMaxFileSizeFunctionality(AbstractDomain currentDomain,Integer value) throws BusinessException{
		
		Functionality fonc = new UnitValueFunctionality(FILESIZE_MAX,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value,
				new FileSizeUnitClass(FileSizeUnit.MEGA)
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	private void createQuotaUserFunctionality(AbstractDomain currentDomain,Integer value) throws BusinessException{
		
		Functionality fonc = new UnitValueFunctionality(QUOTA_USER,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value,
				new FileSizeUnitClass(FileSizeUnit.MEGA)
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	private void createQuotaGlobalFunctionality(AbstractDomain currentDomain) throws BusinessException{
		
		Integer value = 1;
		Functionality fonc = new UnitValueFunctionality(QUOTA_GLOBAL,
				true,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value,
				new FileSizeUnitClass(FileSizeUnit.GIGA)
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createFunc1Functionality(AbstractDomain currentDomain) throws BusinessException{
		
		String value = "blabla";
		Functionality fonc = new StringValueFunctionality(FUNC1,
				false,
				new Policy(Policies.FORBIDDEN, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createFunc2Functionality(AbstractDomain currentDomain) throws BusinessException{
		
		String value = "blabla";
		Functionality fonc = new StringValueFunctionality(FUNC2,
				false,
				new Policy(Policies.FORBIDDEN, true),
				new Policy(Policies.FORBIDDEN, true),
				currentDomain,
				value
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}

	private void createFunc3Functionality(AbstractDomain currentDomain) throws BusinessException{
	
		String value = "blabla";
		Functionality fonc = new StringValueFunctionality(FUNC3,
				false,
				new Policy(Policies.MANDATORY, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value
				);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createFunc4Functionality(AbstractDomain currentDomain) throws BusinessException{
		
		String value = "blabla";
		Functionality fonc = new StringValueFunctionality(FUNC4,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				currentDomain,
				value
				);
		
		fonc.getConfigurationPolicy().setSystem(true);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	private void createFunc5Functionality(AbstractDomain currentDomain) throws BusinessException{
		
		String value = "blabla";
		Functionality fonc = new StringValueFunctionality(FUNC5,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, false),
				currentDomain,
				value
				);
		fonc.getConfigurationPolicy().setSystem(true);
		
		functionalityRepository.create(fonc);
		logger.debug("Current object: " + fonc.toString());
	}
	
	
	public int getAvailableFunctionalitiesForTopDomain2() {
		// three have their activation policy dedicated to the root domain (forbidden or mandatory): -3 : (FUNC1,FUNC2,FUNC3)
		return TOTAL_COUNT_FUNC -3 ;
	}
	
	public int getAlterableFunctionalitiesForTopDomain2() {
		// two have their activation policy set to forbidden , so they can't be active nor configurable : -2 : (FUNC1,FUNC2)
		// two have their configuration policy set to system : -2 (FUNC4,FUNC5)
		return TOTAL_COUNT_FUNC -2 -2;
	}
	
	public int getEditableFunctionalitiesForTopDomain2() {
		// two are dedicated to the system : -1 : (QUOTA_GLOBAL)
		// two have their activation policy set to forbidden , so they can't be active nor configurable nor usable : -2 : (FUNC1,FUNC2)
		// one have its configuration policy status set to false : -1 (FUNC5)
		return TOTAL_COUNT_FUNC -1 -2 -1 ;
	}
	
}
