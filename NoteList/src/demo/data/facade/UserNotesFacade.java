package demo.data.facade;

import java.util.List;

import demo.data.entities.Note;
import demo.data.entities.User;
import demo.data.entities.Password;
import demo.data.repositories.implementations.NotesRepository;
import demo.data.repositories.implementations.UsersRepository;
import demo.data.repositories.implementations.PasswordRepository;

public class UserNotesFacade {
	
	private UsersRepository usersRepository = null;
	private NotesRepository notesRepository = null;
	private PasswordRepository passwordRepository = null;
	private static UserNotesFacade instance = null;
	
	private UserNotesFacade() {
		super();
		this.usersRepository = new UsersRepository();
		this.notesRepository = new NotesRepository();
		this.passwordRepository = new PasswordRepository();
	}
	
	public static UserNotesFacade getInstance() {
		return (instance == null) ? new UserNotesFacade() : instance;
	}
	
	public List<User> getAllUsers(){
		return usersRepository.getAll();
	}
	
	public List<Note> getAllNotes(){
		return notesRepository.getAll();
	}
	
	public User getUserById(int id) {
		return usersRepository.getById(id);
	}
	
	public Note getNoteById(int id) {
		return notesRepository.getById(id);
	}
	
	public boolean createUser(User user) {
		return usersRepository.create(user);
	}
	
	public boolean createNote(Note note) {
		return notesRepository.create(note);
	}
	
	public boolean updateUser(User user) {
		return usersRepository.update(user);
	}
	
	public boolean updateNote(Note note) {
		return notesRepository.update(note);
	}
	
	public boolean deleteUser(User user) {
		return usersRepository.delete(user);
	}
	
	public boolean deleteNote(Note note) {
		return notesRepository.delete(note);
	}
	
	public byte[] getPasswordHashByUser(User user) {
		return passwordRepository.getById(user.getId()).getPwdHash();
	}
	
	public byte[] getSaltByUser(User user) {
		return passwordRepository.getById(user.getId()).getSalt();
	}
	
	public boolean createPassword(Password password) {
		return passwordRepository.create(password);
	}
	
	public boolean updatePassword(Password password) {
		return passwordRepository.create(password);
	}
	
	 public boolean deletePassword(Password password) {
		 return passwordRepository.delete(password);
	 }
	 
	 public List<Password> getAllPasswords(){
		 return passwordRepository.getAll();
	 }
	 
	 public Password getPasswordById(int id) {
		 return passwordRepository.getById(id);
	 }
	
}
