package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EmployeeDao;
import dao.UserDAO;
import entity.Employee;
import entity.User;

public class ActionServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// response.setContentType("text/html;charset=utf-8");
		// PrintWriter out = response.getWriter();
		// 获得请求资源路径
		String uri = request.getRequestURI();
		System.out.println(uri);
		// 分析请求资源路径
		String action = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		System.out.println("action" + action);
		if ("/list".equals(action)) {
			// 访问数据库，获取所有员工信息
			EmployeeDao dao = new EmployeeDao();
			try {
				List<Employee> employees = dao.findAll();
				// 依据查询到的员工信息，生成表格
				// 因为servlet不擅长生成页面，我们使用转发交给jsp来生成页面
				// step1.将处理结果绑定到request
				request.setAttribute("employees", employees);
				// step2.获得转发器
				RequestDispatcher rd = request.getRequestDispatcher("listEmp.jsp");
				// step3.转发
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				// 将异常抛给容器
				throw new ServletException(e);
			}
		} else if ("/add".equals(action)) {
			String name = request.getParameter("uname");
			String age = request.getParameter("age");
			String salary = request.getParameter("salary");
			// 一般来说，服务器端应该对请求参数值作一些合法性检查，比如检查姓名是否为空，这儿暂时不做
			// 将员工信息插入到数据库
			EmployeeDao dao = new EmployeeDao();
			Employee e = new Employee();
			e.setName(name);
			e.setSalary(Double.parseDouble(salary));
			e.setAge(Integer.parseInt(age));
			try {
				dao.save(e);
				// 重定向
				// 重定向地址是任意的；
				// 重定向之后浏览器地址栏的地址会发生改变

				/*
				 * 重定向之前，容器会先清空response对象上存放的所有的数据
				 * 所以out.println("插入成功");这句话是没有任何价值的
				 */
				// out.println("插入成功");
				response.sendRedirect("list.do");
				System.out.println("重定向之后的地址");
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new ServletException(e1);
			}
		} else if ("/del".equals(action)) {
			String id = request.getParameter("id");
			EmployeeDao dao = new EmployeeDao();
			try {
				dao.delById(Integer.parseInt(id));
				response.sendRedirect("list.do");
				// out.println("删除成功");
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ServletException(e2);
			}
		} else if ("/load".equals(action)) {
			String id = request.getParameter("id");
			// 依据id访问数据库，获取员工信息
			EmployeeDao dao = new EmployeeDao();
			try {
				Employee e = dao.findById(Integer.parseInt(id));
				// 依据查询到的员工信息，生成表单
				request.setAttribute("e", e);
				RequestDispatcher rd = request.getRequestDispatcher("updateEmp.jsp");
				rd.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		} else if ("/upd".equals(action)) {
			EmployeeDao dao = new EmployeeDao();
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			Double salary = Double.parseDouble(request.getParameter("salary"));
			int age = Integer.parseInt(request.getParameter("age"));
			Employee e = new Employee();
			e.setId(id);
			e.setName(name);
			e.setSalary(salary);
			e.setAge(age);
			try {
				dao.update(e);
				response.sendRedirect("list.do");
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ServletException(e2);
			}
		} else if("/regist".equals(action)) {
			UserDAO dao = new UserDAO();
			String userName = request.getParameter("username");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String gender = request.getParameter("sex");
			try {
				User us = dao.findByUserName(userName);
				if(us != null) {
					request.setAttribute("errMsg", "用户名已存在");
					request.getRequestDispatcher("regist.jsp").forward(request, response);
				} else {
					us = new User();
					us.setName(name);
					us.setUserName(userName);
					us.setGender(gender);
					us.setPwd(pwd);
					dao.save(us);
					response.sendRedirect("login.jsp");
				}	
			} catch(Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		} else if ("/login".equals(action)) {
			String userName = request.getParameter("name");
			String pwd = request.getParameter("pwd");
			UserDAO dao = new UserDAO();
			try {
				User us = dao.findByUserName(userName);
				if(us == null || !us.getPwd().equals(pwd)) {
					request.setAttribute("errMsg", "用户名或者密码错误");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else {
					response.sendRedirect("list.do");
				}
			} catch(Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
			
		}
	}
}
