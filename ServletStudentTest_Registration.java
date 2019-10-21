package UnitTesting;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import controller.DbConnection;
import controller.ServletStudent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunWith(MockitoJUnitRunner.class)
class ServletStudentTest_Registration {

		@Mock
	    HttpServletRequest request;

	    @Mock
	    HttpServletResponse response;
	    
	    @Mock
	    HttpSession session;
	    
	    @BeforeEach
	    void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(request.getParameter("flag")).thenReturn("1");
		
		sw = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(sw));
		servletStudent = new ServletStudent();
		
		when(request.getSession()).thenReturn(session);
		
		conn = new DbConnection().getConn();
	    }

	    @AfterEach
	    void tearDown() throws Exception {
		servletStudent = null;
		sw.flush();
		
		conn.createStatement().execute("DELETE FROM user WHERE email like 'm.iannone10@studenti.unisa.it'");
		conn.commit();
	    }

	    @Test
	    void TC_02_01() {
		when(request.getParameter("name")).thenReturn("");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_02() {
		when(request.getParameter("name")).thenReturn("Defranceschinigiliberti");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_03() {
		when(request.getParameter("name")).thenReturn("Marc0@");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_04() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_05() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Pierfrancescorlandomenico");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_06() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannnon3@");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_07() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("m.iannone10@studenti.unisa.it");
		when(request.getParameter("sex")).thenReturn("");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Valore non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_08() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("a.bcd@studenti.unisa.it");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_09() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("g.peppe@dominio.it");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    /**
	     * Per questo test case è necessario inserire nel database un utente con email 'r.verdi98@studenti.unisa.it'
	     * 
	     * */
	    @Test
	    void TC_02_10() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("sex")).thenReturn("M");
		when(request.getParameter("email")).thenReturn("r.verdi98@studenti.unisa.it");
		assertDoesNotThrow(() -> servletStudent.doPost(request, response));
		try {
		    JSONObject obj = (JSONObject) new JSONParser().parse(sw.getBuffer().toString());
		    assertEquals("Utente gi&agrave; registrato.", obj.get("error"));
		} catch (ParseException e) {
		    fail("");
		}
	    }
	    
	   
	    
	    @Test
	    void TC_02_11() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("m.iannone10@studenti.unisa.it");
		when(request.getParameter("sex")).thenReturn("M");
		when(request.getParameter("password")).thenReturn("0000");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_12() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("m.iannone10@studenti.unisa.it");
		when(request.getParameter("sex")).thenReturn("M");
		when(request.getParameter("password")).thenReturn("ciaociao1");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Formato non corretto", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_13() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("m.iannone10@studenti.unisa.it");
		when(request.getParameter("sex")).thenReturn("M");
		when(request.getParameter("password")).thenReturn("Ciaociao10");
		when(request.getParameter("verifyPassword")).thenReturn("Ciaociao11");
		Exception e = assertThrows(IllegalArgumentException.class, () -> servletStudent.doPost(request, response));    
		assertEquals("Password non corrispondono", e.getMessage());
	    }
	    
	    @Test
	    void TC_02_14() {
		when(request.getParameter("name")).thenReturn("Marco");
		when(request.getParameter("surname")).thenReturn("Iannone");
		when(request.getParameter("email")).thenReturn("m.iannone10@studenti.unisa.it");
		when(request.getParameter("sex")).thenReturn("M");
		when(request.getParameter("password")).thenReturn("Ciaociaoo10");
		when(request.getParameter("verifyPassword")).thenReturn("Ciaociaoo10");
		assertDoesNotThrow(()-> servletStudent.doPost(request, response));
		
		try {
		    JSONObject obj = (JSONObject) new JSONParser().parse(sw.getBuffer().toString());
		    System.out.print((String) obj.get("content"));
		    assertTrue(((String) obj.get("content")).contains("Registrazione effettuata correttamente."));
		} catch (ParseException e) {
		    fail(e.getMessage());
		}
	    }
	    
	    private Connection conn;
	    private StringWriter sw;
	    private ServletStudent servletStudent;
	}



