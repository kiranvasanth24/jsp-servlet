package com.hubino.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hubino.usermanagement.dao.UserDAO;
import com.hubino.usermanagement.model.TableUserInfo;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDAO userDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
    	this.userDAO = new UserDAO();
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		switch(action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				showEditForm(request, response);
			} catch (SQLException | IOException | ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateUser(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			try {
				listUser(request, response);
			} catch (SQLException | IOException | ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private void showNewForm(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
		requestDispatcher.forward(request, response);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		TableUserInfo tableUserInfo = new TableUserInfo(name, email, country);
		userDAO.insertUser(tableUserInfo);
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
		int id =  Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("list");
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
		int id =  Integer.parseInt(request.getParameter("id"));
		TableUserInfo existingUserInfo = userDAO.selectUser(id);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUserInfo);
		requestDispatcher.forward(request, response);
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id =  Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		
		TableUserInfo tableUserInfo = new TableUserInfo(id, name, email, country);
		userDAO.updateUser(tableUserInfo);
		response.sendRedirect("list");
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
		List<TableUserInfo> tableListUserInfo = userDAO.selectAllUser();
		request.setAttribute("listUser", tableListUserInfo);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-list.jsp");
		requestDispatcher.forward(request, response);
	}
}
