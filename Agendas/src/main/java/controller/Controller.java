package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());

		String action = request.getServletPath();

		System.out.println(action);

		if (action.equals("/main")) {
			Contatos(request, response);

		}

		else if (action.equals("/insert")) {
			novocontato(request, response);
		}

		else if (action.equals("/select")) {
			listarcontato(request, response);
		}

		else if (action.equals("/update")) {
			Editarcontato(request, response);
		}

		else if (action.equals("/delete")) {
			removercontato(request, response);
		}

		else if (action.equals("/report")) {
			gerarRelatoriocontato(request, response);
		}

		else {
			response.sendRedirect("index.html");
		}

	}

	// Listar Contatos
	protected void Contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Criando um objeto que ira receber os dados Javabeans
		ArrayList<JavaBeans> lista = dao.listarContatos();

		// Ecaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);

		/*
		 * //Teste de recebimento da Lista //for( int i = 0 ; i<lista.size(); i++) {
		 * //System.out.println(lista.get(i).getIdcon());
		 * //System.out.println(lista.get(i).getNome());
		 * //System.out.println(lista.get(i).getFone());
		 * //System.out.println(lista.get(i).getEmail());
		 * 
		 * }
		 */
	}

	// Novo contato
	protected void novocontato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Teste de Recebimento dos dados do formulario

		/*
		 * System.out.println(request.getParameter("nome"));
		 * //System.out.println(request.getParameter("fone"));
		 * //System.out.println(request.getParameter("email"));
		 */

		// setar as variaveis javaBeans

		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));

		// Invocar o metodo inserir contato Passando objeto contato
		dao.inserirContato(contato);

		// redirecionar para documento agenda.jsp
		response.sendRedirect("main");

	}

	// Editar Conatato
	private void listarcontato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Teste Recebimento do id do contato que será editado
		String idcon = request.getParameter("idcon");

		// Setar Variavel JavaBeans
		contato.setIdcon(idcon);

		// Executar o metodo selecionar contato (DAO)
		dao.selecionarcontato(contato);

		// Testet de recebimento
		// System.out.println(contato.getIdcon());
		// System.out.println(contato.getNome());
		// System.out.println(contato.getFone());
		// System.out.println(contato.getEmail());

		// Setar os atributos do formulario com conteudo JavaBeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());

		// Encaminhar ao documento editar.jsp

		RequestDispatcher rd = request.getRequestDispatcher("Editar.jsp");
		rd.forward(request, response);
	}

	private void Editarcontato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Teste Recebimento do id do contato que será editado

		// System.out.println( request.getParameter("idcon"));
		// System.out.println( request.getParameter("nome"));
		// System.out.println( request.getParameter("fone"));
		// System.out.println( request.getParameter("email"));

		// Setar as variaveis JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));

		// Executar Metodo Alterar Contato
		dao.alterarContato(contato);
		// redirecionar para o documento agenda.jsp(Atualizando as alterações)
		response.sendRedirect("main");
	}

	private void removercontato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idcon = request.getParameter("idcon");

		// Teste do recebimento do contato a ser excluido(validador.js)

		// System.out.println(idcon);
		contato.setIdcon(idcon);          

		/// executar o metodo deletarContato (DAO) passando o objeto contato
		dao.deletarContato(contato);

		// redirecionar para o documento agenda.jsp(Atualizando as alterações)
		response.sendRedirect("main");
	}

//Gerar relatorio
	protected void gerarRelatoriocontato(HttpServletRequest request, HttpServletResponse response) {
		
		//documento
		
          Document documento = new Document();			
					

		try {
			// Tipo de documento
			response.setContentType("apllication/pdf");

			// nome do documento
			response.reset();
			response.addHeader("Content-Disposition", "inline ; filename=" + "contatos.pdf");

			// criar documento

			PdfWriter.getInstance((com.itextpdf.text.Document) documento, response.getOutputStream());

			// Abrir o documento -> conteúdo

			documento.open();
			documento.add(new Paragraph("Lista de contatos:"));
			documento.add(new Paragraph("  "));

			// Criar uma tabela
			PdfPTable tabela = new PdfPTable(3);// esse numero indica o numero de coluns que a tabela vai ter

			// Cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));

			PdfPCell col2 = new PdfPCell(new Paragraph("fone"));

			PdfPCell col3 = new PdfPCell(new Paragraph("email"));

			tabela.addCell(col1);

			tabela.addCell(col2);

			tabela.addCell(col3);

			// popular a tabela com os contatos

			ArrayList<JavaBeans> lista = dao.listarContatos();

			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}

			documento.add(tabela);

			documento.close();

		} catch (Exception e) {

			System.out.println(e);
			documento.close();

		}

	}

}