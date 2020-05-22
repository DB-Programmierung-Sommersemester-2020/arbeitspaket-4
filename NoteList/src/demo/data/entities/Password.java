package demo.data.entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Passwords")
public class Password {
	
	@Id
	private int id;
	
	@Lob
	@Column(name="pwdhash")
	private byte[] pwdHash;
	
	@Lob
	@Column(name="salt")
	private byte[] salt;

	public Password() {
		super();
	}
	
	public Password(int id, byte[] pwdHash, byte[] salt) {
		super();
		this.id = id;
		this.pwdHash = pwdHash;
		this.salt = salt;
	}
	
	public Password(byte[] pwdHash, byte[] salt) {
		super();
		this.pwdHash = pwdHash;
		this.salt = salt;
	}

	public int getId() {
		return id;
	}
	public byte[] getPwdHash() {
		return pwdHash;
	}

	public byte[] getSalt() {
		return salt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(pwdHash);
		result = prime * result + Arrays.hashCode(salt);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Password other = (Password) obj;
		if (id != other.id)
			return false;
		if (!Arrays.equals(pwdHash, other.pwdHash))
			return false;
		if (!Arrays.equals(salt, other.salt))
			return false;
		return true;
	}

	
}
