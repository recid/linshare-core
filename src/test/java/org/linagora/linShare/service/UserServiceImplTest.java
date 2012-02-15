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

import java.security.acl.Owner;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linagora.linShare.common.service.LinShareMessageHandler;
import org.linagora.linShare.core.domain.constants.FunctionalityNames;
import org.linagora.linShare.core.domain.constants.Language;
import org.linagora.linShare.core.domain.constants.LinShareTestConstants;
import org.linagora.linShare.core.domain.constants.Policies;
import org.linagora.linShare.core.domain.constants.UserType;
import org.linagora.linShare.core.domain.entities.AbstractDomain;
import org.linagora.linShare.core.domain.entities.AllowedContact;
import org.linagora.linShare.core.domain.entities.DomainAccessPolicy;
import org.linagora.linShare.core.domain.entities.DomainPolicy;
import org.linagora.linShare.core.domain.entities.Functionality;
import org.linagora.linShare.core.domain.entities.Guest;
import org.linagora.linShare.core.domain.entities.GuestDomain;
import org.linagora.linShare.core.domain.entities.Internal;
import org.linagora.linShare.core.domain.entities.MailContainer;
import org.linagora.linShare.core.domain.entities.Policy;
import org.linagora.linShare.core.domain.entities.Role;
import org.linagora.linShare.core.domain.entities.User;
import org.linagora.linShare.core.domain.vo.UserVo;
import org.linagora.linShare.core.exception.BusinessException;
import org.linagora.linShare.core.exception.TechnicalException;
import org.linagora.linShare.core.repository.AbstractDomainRepository;
import org.linagora.linShare.core.repository.AllowedContactRepository;
import org.linagora.linShare.core.repository.FunctionalityRepository;
import org.linagora.linShare.core.repository.GuestRepository;
import org.linagora.linShare.core.repository.UserRepository;
import org.linagora.linShare.core.service.UserService;
import org.linagora.linShare.core.utils.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.AssertThrows;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.subethamail.smtp.server.SMTPServer;

@ContextConfiguration(locations = { 
		"classpath:springContext-datasource.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-service.xml",
		"classpath:springContext-dao.xml",
		"classpath:springContext-facade.xml",
		"classpath:springContext-startopends.xml",
		"classpath:springContext-jackRabbit.xml",
		"classpath:springContext-test.xml"
		})
public class UserServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);
	
	@Autowired
	private UserService userService;
	
	@Qualifier("userRepository")
	@Autowired
	private UserRepository userRepository;
	
	@Qualifier("guestRepository")
	@Autowired
	private GuestRepository guestRepository;
	
	
	@Autowired
	private AllowedContactRepository allowedContactRepository;
	
	@Autowired
	private FunctionalityRepository functionalityRepository;
	
	@Autowired
	private AbstractDomainRepository abstractDomainRepository;
	
	
	private SMTPServer wiser;
	
	public UserServiceImplTest() {
		super();
		wiser = new SMTPServer(new LinShareMessageHandler());
		wiser.setPort(2525);
	}
	
	
	@Before
	public void setUp() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_SETUP);
		wiser.start();

		
		logger.debug(LinShareTestConstants.END_SETUP);
	}

	@After
	public void tearDown() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_TEARDOWN);
		wiser.stop();

		logger.debug(LinShareTestConstants.END_TEARDOWN);
	}
	
	
	
	@Test
	public void testCreateGuest() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		
		Functionality fonc = new Functionality(FunctionalityNames.GUESTS,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				domain);
		
		functionalityRepository.create(fonc);
		domain.addFunctionality(fonc);
		

		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user.setDomain(domain);
		user.setCanCreateGuest(true);
		userService.saveOrUpdateUser(user);
		
		try{
		userService.createGuest("guest1@linpki.org", "Guest", "Doe", "guest1@linpki.org", true, false, "", new MailContainer("blabla", Language.FRENCH), user.getLogin(), user.getDomainId());
		}catch(TechnicalException e){
			logger.info("Impossible to send mail, normal in test environment");
		}
		Assert.assertNotNull(userRepository.findByMail("guest1@linpki.org"));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testFindUserInDB(){
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user.setDomain(domain);
		logger.info("Save user in DB");
		userService.saveOrUpdateUser(user);
		
		Assert.assertNotNull(userService.findUserInDB(LoadingServiceTestDatas.sqlSubDomain, "user1@linpki.org"));
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testFindUnkownUserInDB(){
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user.setDomain(domain);
		logger.info("Save user in DB");
		userService.saveOrUpdateUser(user);
		
		Assert.assertTrue(userService.findUnkownUserInDB("user1@linpki.org").equals(user));

		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testFindUsersInDB(){
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user.setDomain(domain);
		logger.info("Save user in DB");
		userService.saveOrUpdateUser(user);
		
		Assert.assertTrue(userService.findUsersInDB(LoadingServiceTestDatas.sqlSubDomain).contains(user));
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testGeneratePassword(){
		logger.info(LinShareTestConstants.BEGIN_TEST);
		Assert.assertNotNull(userService.generatePassword());
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testDeleteUser() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.ADMIN);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		
		logger.info("Save users in DB");
		userService.saveOrUpdateUser(user1);
		userService.saveOrUpdateUser(user2);
		
		logger.info("John Doe trying to delete Jane Smith");
		userService.deleteUser("user2@linpki.org", user1);
		
		Assert.assertNull(userService.findUserInDB(LoadingServiceTestDatas.sqlSubDomain, "user2@linpki.org"));
		Assert.assertNotNull(userService.findUserInDB(LoadingServiceTestDatas.sqlRootDomain, "user1@linpki.org"));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testDeleteAllUsersFromDomain() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.ADMIN);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		Internal user3 = new Internal("user3@linpki.org","Foo","Bar","user3@linpki.org");
		user3.setDomain(subDomain);
		
		logger.info("Save users in DB");
		userService.saveOrUpdateUser(user1);
		userService.saveOrUpdateUser(user2);
		userService.saveOrUpdateUser(user3);
		
		logger.info("John Doe trying to delete Jane Smith");
		userService.deleteAllUsersFromDomain(user1, subDomain.getIdentifier());
		
		Assert.assertNull(userService.findUserInDB(LoadingServiceTestDatas.sqlSubDomain, "user2@linpki.org"));
		Assert.assertNull(userService.findUserInDB(LoadingServiceTestDatas.sqlSubDomain, "user3@linpki.org"));

		Assert.assertNotNull(userService.findUserInDB(LoadingServiceTestDatas.sqlRootDomain, "user1@linpki.org"));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testIsAdminForThisUser(){
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.ADMIN);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		
		Assert.assertTrue(userService.isAdminForThisUser(user1, user2));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testCleanExpiredGuestAcccounts() throws IllegalArgumentException, BusinessException, ParseException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		
		Functionality fonc = new Functionality(FunctionalityNames.GUESTS,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				domain);
		
		functionalityRepository.create(fonc);
		domain.addFunctionality(fonc);

		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user.setDomain(domain);
		user.setCanCreateGuest(true);
		userService.saveOrUpdateUser(user);
		
		Guest guest = userService.createGuest("guest1@linpki.org", "Guest", "Doe", "guest1@linpki.org", true, false, "", new MailContainer("blabla", Language.FRENCH), user.getLogin(), user.getDomainId());
	
		Assert.assertNotNull(userRepository.findByMail("guest1@linpki.org"));
		
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		Date date = dfm.parse("2007-02-26 20:15:00");

		guest.setExpiryDate(date);
		
		userService.cleanExpiredGuestAcccounts();
		
		Assert.assertNull(userRepository.findByMail("guest1@linpki.org"));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testSearchUser() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.ADMIN);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		
		Assert.assertTrue(userService.searchUser(user2.getMail(), user2.getFirstName(), user2.getLastName(), UserType.INTERNAL, user1).contains(user2));
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testSearchUserForRestrictedGuestEditionForm() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Functionality fonc = new Functionality(FunctionalityNames.GUESTS,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				subDomain);
		
		functionalityRepository.create(fonc);
		subDomain.addFunctionality(fonc);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		user2.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user2);
		Guest guest = userService.createGuest("guest1@linpki.org", "Guest", "Doe", "guest1@linpki.org", true, false, "", new MailContainer("blabla", Language.FRENCH), user2.getLogin(), user2.getDomainId());
	
		Assert.assertTrue(userService.searchUserForRestrictedGuestEditionForm("user2@linpki.org","Jane","Smith", guest).contains(user2));
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testUpdateGuest()throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Functionality fonc = new Functionality(FunctionalityNames.GUESTS,
				false,
				new Policy(Policies.ALLOWED, true),
				new Policy(Policies.ALLOWED, true),
				subDomain);
		
		functionalityRepository.create(fonc);
		subDomain.addFunctionality(fonc);
		
		User user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		user2.setCanCreateGuest(true);
		user2.setRole(Role.SYSTEM);
			
		userService.saveOrUpdateUser(user2);
		
		UserVo userVo2 = new UserVo(user2);
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user2);
		
		guestRepository.create(guest);
				
		userService.updateGuest(LoadingServiceTestDatas.sqlGuestDomain, "user3@linpki.org","Foo","Bar", false, false, userVo2);
		
		Assert.assertFalse(guest.getCanCreateGuest());
		
		userService.updateGuest(LoadingServiceTestDatas.sqlGuestDomain, "user3@linpki.org","Foo","Bar", true, true, userVo2);

		Assert.assertTrue(guest.getCanCreateGuest());
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testUpdateUserRole() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.SYSTEM);
		UserVo userVo = new UserVo(user1);
		
		
		User user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		user2.setCanCreateGuest(true);
		user2.setRole(Role.SIMPLE);
		
		userService.saveOrUpdateUser(user1);
		userService.saveOrUpdateUser(user2);

		Assert.assertTrue(user2.getRole()==Role.SIMPLE);
		userService.updateUserRole(LoadingServiceTestDatas.sqlSubDomain, "user2@linpki.org", Role.ADMIN, userVo);
		
		Assert.assertTrue(user2.getRole()==Role.ADMIN);
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testChangePassword() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		
		String oldPassword = new String("password");
		
		user1.setPassword(
				HashUtils.hashSha1withBase64(oldPassword.getBytes()));
		
		userService.saveOrUpdateUser(user1);
		
		String newPassword = new String("newPassword");

		Assert.assertTrue(user1.getPassword().equals(HashUtils.hashSha1withBase64(oldPassword.getBytes())));
		
		userService.changePassword("user1@linpki.org", oldPassword, newPassword);
		
		Assert.assertTrue(user1.getPassword().equals(HashUtils.hashSha1withBase64(newPassword.getBytes())));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testResetPassword() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		String oldPassword = new String("password222");
		
		guest.setPassword(
				HashUtils.hashSha1withBase64(oldPassword.getBytes()));
		
		guestRepository.create(guest);
		
		userService.resetPassword("user3@linpki.org", new MailContainer("", Language.FRENCH));
		
		Assert.assertFalse(guest.getPassword().equals(HashUtils.hashSha1withBase64(oldPassword.getBytes())));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testRemoveGuestContactRestriction() throws IllegalArgumentException, BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		guest.setRestricted(true);
		
		guestRepository.create(guest);
		
		Assert.assertTrue(guest.isRestricted());
		
		userService.removeGuestContactRestriction("user3@linpki.org");
		
		Assert.assertFalse(guest.isRestricted());
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testaddGuestContactRestriction() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest2 = new Guest("user2@linpki.org","Jane","Smith","user2@linpki.org");
		guest2.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest2.setOwner(user1);
		
		guestRepository.create(guest2);
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		guest.setRestricted(true);
		
		guestRepository.create(guest);
		
		userService.addGuestContactRestriction(guest.getLogin(), guest2.getLogin());
		
		List<AllowedContact> listAllowedContact= allowedContactRepository.findByOwner(guest);
		
		boolean test = false;
		
		for (AllowedContact allowedContact : listAllowedContact) {
			
			if(allowedContact.getContact().getLogin() == guest2.getLogin()){
				test=true;
			}	
		}
		
		Assert.assertTrue(test);
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testSetGuestContactRestriction() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest2 = new Guest("user2@linpki.org","Jane","Smith","user2@linpki.org");
		guest2.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest2.setOwner(user1);
		
		guestRepository.create(guest2);
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		guest.setRestricted(true);
		
		guestRepository.create(guest);
		
		List<String> mailContacts = new ArrayList<String>();
		
		mailContacts.add(guest2.getMail());
		
		userService.setGuestContactRestriction(guest.getLogin(), mailContacts);
		
		List<AllowedContact> listAllowedContact= allowedContactRepository.findByOwner(guest);
		
		boolean test = false;
		
		for (AllowedContact allowedContact : listAllowedContact) {
			
			if(allowedContact.getContact().getLogin() == guest2.getLogin()){
				test=true;
			}	
		}
		Assert.assertTrue(test);
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testFetchGuestContacts() throws IllegalArgumentException, BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest2 = new Guest("user2@linpki.org","Jane","Smith","user2@linpki.org");
		guest2.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest2.setOwner(user1);
		
		guestRepository.create(guest2);
		
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		guest.setRestricted(true);
		
		guestRepository.create(guest);
		
		
		userService.addGuestContactRestriction(guest.getLogin(), guest2.getLogin());

		
		List<User> guestContacts = userService.fetchGuestContacts(guest.getLogin());
		
		Assert.assertTrue(guestContacts.contains(guest2));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	
	@Test
	public void testGetGuestEmailContacts() throws IllegalArgumentException, BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setCanCreateGuest(true);
		
		userService.saveOrUpdateUser(user1);
		
		//create guest
		Guest guest2 = new Guest("user2@linpki.org","Jane","Smith","user2@linpki.org");
		guest2.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest2.setOwner(user1);
		
		guestRepository.create(guest2);
		
		
		//create guest
		Guest guest = new Guest("user3@linpki.org","Foo","Bar","user3@linpki.org");
		guest.setDomain(abstractDomainRepository.findById(LoadingServiceTestDatas.sqlGuestDomain));
		
		guest.setOwner(user1);
		
		guest.setRestricted(true);
		
		guestRepository.create(guest);
		
		
		userService.addGuestContactRestriction(guest.getLogin(), guest2.getLogin());

		
		List<String> guestEmailContacts = userService.getGuestEmailContacts(guest.getLogin());
		
		Assert.assertTrue(guestEmailContacts.contains(guest2.getMail()));
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testUpdateUserDomain() throws BusinessException{
		logger.info(LinShareTestConstants.BEGIN_TEST);
		
		AbstractDomain rootDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlRootDomain);
		Internal user1 = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		user1.setDomain(rootDomain);
		user1.setRole(Role.SYSTEM);
		
		userService.saveOrUpdateUser(user1);
		UserVo userVo = new UserVo(user1);
		
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		Internal user2 = new Internal("user2@linpki.org","Jane","Smith","user2@linpki.org");
		user2.setDomain(subDomain);
		
		userService.saveOrUpdateUser(user2);
		
		userService.updateUserDomain(user2.getLogin(), LoadingServiceTestDatas.sqlGuestDomain, userVo);
		
		logger.debug(LinShareTestConstants.END_TEST);
	}
	/*
	@Test
	public void testFindOrCreateUserWithDomainPolicies(){
		new DomainPolicy("DomainPolicy", new DomainAccessPolicy());
		
		
		domainPolicyRepository.create(defaultPolicy);
	}
	*/
	@Test
	@Rollback(true)
	public void testSaveOrUpdateUser() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		AbstractDomain domain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		
		Internal user = new Internal("user1@linpki.org","John","Doe","user1@linpki.org");
		
		logger.info("Trying to create a user without domain : should fail.");
		try {
			userService.saveOrUpdateUser(user);
			Assert.fail("It should fail before this message.");
		} catch (TechnicalException e) {
			logger.debug("TechnicalException raise as planned.");
		}
		logger.info("Trying to create a user : .");
		logger.debug("user id : " + user.getId());
		user.setDomain(domain);
		userService.saveOrUpdateUser(user);
		logger.debug("user id : " + user.getId());
		Assert.assertTrue(user.getCanUpload());
		user.setCanUpload(false);
		userService.saveOrUpdateUser(user);
		Assert.assertFalse(user.getCanUpload());
		logger.debug(LinShareTestConstants.END_TEST);
	}
	
	@Test
	public void testFindOrCreateUser() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		long id_user=0;
		
		try {
			logger.debug("Trying to find john doe: should create this user");
			User user = userService.findOrCreateUserWithDomainPolicies("user1@linpki.org", LoadingServiceTestDatas.sqlSubDomain);
			Assert.assertEquals("John", user.getFirstName());
			Assert.assertEquals("Doe", user.getLastName());
			id_user= user.getId();
			logger.debug("the user created was " + user.getFirstName() + " " + user.getLastName() +" (id=" + user.getId() + ")");
		} catch (BusinessException e) {
			logger.error("userService can not create an user ");
			logger.error(e.toString());
		}
		
		try {
			logger.debug("Trying to find john doe: should return this user");
			User user = userService.findOrCreateUser("user1@linpki.org", LoadingServiceTestDatas.sqlSubDomain);
			Assert.assertEquals("John", user.getFirstName());
			Assert.assertEquals("Doe", user.getLastName());
			// Should be the same user, not a new one.
			Assert.assertEquals(id_user, user.getId());
			logger.debug("the user created was " + user.getFirstName() + " " + user.getLastName() +" (id=" + user.getId() + ")");
			
		} catch (BusinessException e) {
			logger.error("userService can not find an user ");
			logger.error(e.toString());
		}
		
		try {
			logger.debug("Trying to find john doe: should return this user");
			User user = userService.findOrCreateUser("user1@linpki.org", LoadingServiceTestDatas.sqlSubDomain);
			Assert.assertEquals("John", user.getFirstName());
			Assert.assertEquals("Doe", user.getLastName());
			// Should be the same user, not a new one.
			Assert.assertEquals(id_user, user.getId());
			logger.debug("the user created was " + user.getFirstName() + " " + user.getLastName() +" (id=" + user.getId() + ")");
			
		} catch (BusinessException e) {
			logger.error("userService can not find an user ");
			logger.error(e.toString());
		}
		logger.debug(LinShareTestConstants.END_TEST);
	}
		
	
	
	
}
