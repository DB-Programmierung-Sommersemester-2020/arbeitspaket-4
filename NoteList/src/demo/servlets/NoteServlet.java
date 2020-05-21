package demo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import demo.controllers.UserController;
import demo.data.entities.Note;
import demo.data.entities.User;
import demo.data.facade.UserNotesFacade;

@WebServlet("/Notes")
public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserController userController = null;
	private UserNotesFacade facade = null;
	private Gson gson = new Gson();

	public NoteServlet() {
		super();
		this.userController = UserController.getInstance();
		this.facade = UserNotesFacade.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);

		if (user.isPresent()) {
			List<Note> notes = user.get().getNotes();

			String jsonString = gson.toJson(notes);
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			out.print(jsonString);
			out.flush();
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			error(request, response, "Kein Benutzer gefunden");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);

		String subject = request.getParameter("subject");
		String content = request.getParameter("content");

		if (subject == null || content == null) {
			error(request, response, "Keine Daten für Notizen erhalten");
			return;
		}

		if (user.isPresent()) {
			user.get().getNotes().add(new Note(subject, content));
			facade.updateUser(user.get());
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			error(request, response, "Kein Benutzer gefunden");
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);
		if (user.isPresent()) {
			// user.get().removeAllNotes();
			user.get().getNotes().clear();
			facade.updateUser(user.get());
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			error(request, response, "Kein Benutzer gefunden");
		}
	}

	private Optional<User> checkIfUserIsLoggedInAndGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");

		if (user == null) {
			error(request, response, "Kein Benutzer gefunden");
			return Optional.empty();
		} else if (!this.userController.lookupUser(user.getName()).isPresent()) {
			error(request, response, "Benutzername nicht gefunden");
			return Optional.empty();
		}

		return Optional.of(user);
	}

	private void error(HttpServletRequest request, HttpServletResponse response, String errorMessage)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
		request.setAttribute("errorMessage", errorMessage);
		dispatcher.forward(request, response);
	}
}
