package demo.controllers;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import demo.data.entities.Password;
import demo.data.entities.User;
import demo.data.facade.UserNotesFacade;

public class UserController {
	private static UserController instance = new UserController();
	private UserNotesFacade facade = UserNotesFacade.getInstance();

	public static UserController getInstance() {
		return UserController.instance;
	}

	private Map<String, User> users = new HashMap<>();

	private UserController() {
		super();
		fillUsersMap();
	}

	private void fillUsersMap() {
		facade.getAllUsers().forEach(user -> users.put(user.getName(), user));
	}

	private static byte[] generateSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[8];
			random.nextBytes(salt);
			return salt;
		} catch (NoSuchAlgorithmException exce) {
			exce.printStackTrace();
			return "42".getBytes();
		}
	}

	private static byte[] generatePassword(String password, byte[] salt) {
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 10000, 160);
			SecretKey s = f.generateSecret(ks);
			Key k = new SecretKeySpec(s.getEncoded(), "AES");
			return k.getEncoded();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException exce) {
			exce.printStackTrace();
			return password.getBytes();
		}
	}

	public boolean checkPassword(User user, String passwd) {
		byte[] pwd = facade.getPasswordHashByUser(user);
		byte[] salt = facade.getSaltByUser(user);
		byte[] pwdToTest = generatePassword(passwd, salt);
		boolean compare = true;
		if (pwd.length != pwdToTest.length)
			return false;
		for (int i = 0; i < pwd.length; i++) {
			compare &= (pwd[i] == pwdToTest[i]);
		}
		return compare;
	}

	public Optional<User> lookupUser(String username) {
		return this.users.values().stream().filter(user -> user.getName().equals(username)).findFirst();
	}

	public User register(String username, String password, String email) {
		if (this.lookupUser(username).isPresent()) {
			RuntimeException exce = new RuntimeException("User " + username + " exists");
			throw exce;
		} else {
			byte[] salt = generateSalt();
			byte[] pwdHash = generatePassword(password, salt);
			
			
			User user = new User(username, password, email);
			this.users.put(username, user);
			facade.createUser(user);
			facade.createPassword(new Password(user.getId(),pwdHash,salt));
			return user;
		}
	}
}
