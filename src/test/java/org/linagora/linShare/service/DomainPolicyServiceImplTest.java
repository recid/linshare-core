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

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linagora.linShare.core.domain.entities.AbstractDomain;
import org.linagora.linShare.core.domain.entities.AllowAllDomain;
import org.linagora.linShare.core.domain.entities.AllowDomain;
import org.linagora.linShare.core.domain.entities.DenyAllDomain;
import org.linagora.linShare.core.domain.entities.DomainAccessPolicy;
import org.linagora.linShare.core.domain.entities.DomainPolicy;
import org.linagora.linShare.core.domain.entities.GuestDomain;
import org.linagora.linShare.core.domain.entities.RootDomain;
import org.linagora.linShare.core.domain.entities.SubDomain;
import org.linagora.linShare.core.domain.entities.TopDomain;
import org.linagora.linShare.core.service.DomainPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = { 
		"classpath:springContext-test.xml",
		"classpath:springContext-datasource.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-service-test.xml"		
		})
public class DomainPolicyServiceImplTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private DomainPolicyService domainPolicyService;
	
	private AbstractDomain rootDomain;
	
	private TopDomain t1 ;
	private TopDomain t2 ;
	private SubDomain s1 ;
	private SubDomain s4 ;
	
	private int CST_TOTAL_DOMAIN=9;
	
	
	@Before
	@Transactional (propagation=Propagation.REQUIRED)
	public void setUp() throws Exception {
		logger.debug("Begin setUp");
		
		DomainPolicy domainePolicy1 = new DomainPolicy("TestAccessPolicy0", new DomainAccessPolicy());
		domainePolicy1.getDomainAccessPolicy().addRule(new AllowAllDomain());
		
		
		
		
		rootDomain = new RootDomain("id_root","root domain");
		rootDomain.setPolicy(domainePolicy1);
		
		t1 = new TopDomain("id_top1", "top1", (RootDomain)rootDomain);
		rootDomain.addSubdomain(t1);
		t1.setPolicy(domainePolicy1);
		
		s1 = new SubDomain("id_sub1","id_sub1",t1);
		t1.addSubdomain(s1);
		s1.setPolicy(domainePolicy1);
		
		SubDomain s2 = new SubDomain("id_sub2","id_sub2",t1);
		t1.addSubdomain(s2);
		s2.setPolicy(domainePolicy1);
		
		GuestDomain s3 = new GuestDomain("id_sub3","id_sub3");
		s3.setParentDomain(t1);
		t1.addSubdomain(s3);
		s3.setPolicy(domainePolicy1);
		
		
		
		
		t2 = new TopDomain("id_top2", "top2", (RootDomain)rootDomain);
		rootDomain.addSubdomain(t2);
		DomainPolicy domainePolicy2 = new DomainPolicy("TestAccessPolicy1", new DomainAccessPolicy());
		domainePolicy2.getDomainAccessPolicy().addRule(new AllowDomain(t2));
		domainePolicy2.getDomainAccessPolicy().addRule(new DenyAllDomain());
		t2.setPolicy(domainePolicy2);
		
		s4 = new SubDomain("id_sub4","id_sub4",t2);
		t2.addSubdomain(s4);
		s4.setPolicy(domainePolicy2);
		
		SubDomain s5 = new SubDomain("id_sub5","id_sub5",t2);
		t2.addSubdomain(s5);
		s5.setPolicy(domainePolicy2);
		
		SubDomain s6 = new SubDomain("id_sub6","id_sub6",t2);
		t2.addSubdomain(s6);
		s6.setPolicy(domainePolicy2);
		
		
		logger.debug("End setUp");
	}

	@After
	@Transactional (propagation=Propagation.REQUIRED)
	public void tearDown() throws Exception {
		logger.debug("Begin tearDown");
		logger.debug("End tearDown");
	}
	
	
	private void printAuthorizedDomain(List<AbstractDomain> authorizedSubDomain, AbstractDomain src) {
		logger.debug("Begin print : " + src);
		for (AbstractDomain abstractDomain : authorizedSubDomain) {
			logger.debug("domain : " + abstractDomain);
		}
		logger.debug("End print : " + src);
	}

	@Test
	public void testAuthorizedSubDomain() {
		List<AbstractDomain> authorizedSubDomain = domainPolicyService.getAuthorizedSubDomain(rootDomain);
		printAuthorizedDomain(authorizedSubDomain,rootDomain);
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSubDomain(t1);
		printAuthorizedDomain(authorizedSubDomain,t1);
		// 3 sub domains.
		Assert.assertEquals(3, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSubDomain(s1);
		printAuthorizedDomain(authorizedSubDomain,s1);
		// no sub domains
		Assert.assertEquals(0, authorizedSubDomain.size());

		authorizedSubDomain = domainPolicyService.getAuthorizedSubDomain(t2);
		printAuthorizedDomain(authorizedSubDomain,t2);
		// sub domain communication not allowed : 0
		Assert.assertEquals(0, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSubDomain(s4);
		printAuthorizedDomain(authorizedSubDomain,s4);
		// no sub domains
		Assert.assertEquals(0, authorizedSubDomain.size());
		
	}
	
	@Test
	public void testIsAuthorizedToCommunicateWithItSelf() {
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItSelf(t1));
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItSelf(s1));
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItSelf(t2));
		Assert.assertFalse(domainPolicyService.isAuthorizedToCommunicateWithItSelf(s4));
	}
	
	@Test
	public void testIsAuthorizedToCommunicateWithItsParent() {
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItsParent(t1));
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItsParent(s1));
		Assert.assertFalse(domainPolicyService.isAuthorizedToCommunicateWithItsParent(t2));
		Assert.assertTrue(domainPolicyService.isAuthorizedToCommunicateWithItsParent(s4));
	}
	
	
	@Test
	public void testAuthorizedSiblingDomain1() {
		List<AbstractDomain> authorizedSubDomain = domainPolicyService.getAuthorizedSibblingDomain(rootDomain);
		printAuthorizedDomain(authorizedSubDomain,rootDomain);
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSibblingDomain(t1);
		printAuthorizedDomain(authorizedSubDomain,t1);
		// 2 siblings.
		Assert.assertEquals(2, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSibblingDomain(s1);
		printAuthorizedDomain(authorizedSubDomain,s1);
		// 3 siblings
		Assert.assertEquals(3, authorizedSubDomain.size());

	}
	
	@Test
	public void testAuthorizedSiblingDomain2() {
		List<AbstractDomain> authorizedSubDomain;

		authorizedSubDomain = domainPolicyService.getAuthorizedSibblingDomain(t2);
		printAuthorizedDomain(authorizedSubDomain,t2);
		// 2 siblings, but communication with itself only : 1
		Assert.assertEquals(1, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAuthorizedSibblingDomain(s4);
		printAuthorizedDomain(authorizedSubDomain,s4);
		// 3 siblings, but communication with only top domain t2: 0
		Assert.assertEquals(0, authorizedSubDomain.size());
		
	}
	
	@Test
	public void testGetAllAuthorizedDomain () {
		List<AbstractDomain> authorizedSubDomain;
		
		authorizedSubDomain = domainPolicyService.getAllAuthorizedDomain(rootDomain);
		Assert.assertEquals(CST_TOTAL_DOMAIN, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAllAuthorizedDomain(t1);
		Assert.assertEquals(CST_TOTAL_DOMAIN, authorizedSubDomain.size());
		

		authorizedSubDomain = domainPolicyService.getAllAuthorizedDomain(s1);
		Assert.assertEquals(CST_TOTAL_DOMAIN, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAllAuthorizedDomain(t2);
		Assert.assertEquals(1, authorizedSubDomain.size());
		
		authorizedSubDomain = domainPolicyService.getAllAuthorizedDomain(s4);
		Assert.assertEquals(1, authorizedSubDomain.size());
	}
}
