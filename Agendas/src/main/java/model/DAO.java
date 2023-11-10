package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.JavaBeans;

public class DAO {
	/**
	 * Modulo de conexão
	 */
	// Parametros de conexão
	
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String passaword = "4338464@$Sa";

	JavaBeans beans = new JavaBeans();

	// Método de conexão
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, passaword);

			return con;
		}

		catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	// Teste de Conexão
	public void TesteConexao() {
		try {
			Connection con = conectar();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);

		}
	}
	

	/* CRUD CREATE */

	public void inserirContato(JavaBeans beans) {
		String craete = "insert into contatos" + " (nome , fone , email) values  (?,  ?, ?)";

		try {
			// abrir conexão
			Connection con = conectar();

			// Preparar a query para execuçõa no banco de dados

			PreparedStatement statement = con.prepareStatement(craete);

			// Substituir os pararmetros pelo conteudo das variveis JavaBeans

			statement.setString(1, beans.getNome());
			statement.setString(2, beans.getFone());
			statement.setString(3, beans.getEmail());

			// Executar a query
			statement.executeUpdate();
			// Encerrar a conexão com o banco de dados
			statement.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/* CRUD READ */

	public ArrayList<JavaBeans> listarContatos() {
		// Criando objeto para acessar a classe javabeans

		ArrayList<JavaBeans> contatos = new ArrayList<>();
		String read = "select * from contatos order by nome";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			// O laço a baixo será executado enquanto houver Contatos

			while (rs.next()) {

				// variaveis de apoio que recebem os dados do banco
				String idcon = rs.getString(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);
				// populando o ArrayList
				contatos.add(new JavaBeans(idcon, nome, fone, email));

			}
			con.close();

			return contatos;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/* CRUD Update */

	// Selecionar contato

	public void selecionarcontato(JavaBeans contato) {
		String red2 = "select * from contatos where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(red2);
			pst.setString(1, contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				contato.setIdcon(rs.getString(1));
				;
				contato.setNome((rs.getString(2)));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
				;
			}
			con.close();
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}

	/* Ediatr Contato */
	public void alterarContato(JavaBeans contato) {
		String create = "update contatos set nome=? , fone=? , email=? where idcon=?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setString(4, contato.getIdcon());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/* CRUD DELETE */
	public void deletarContato(JavaBeans contato) {
		String delete = "delete from contatos where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(delete);
			pst.setString(1, contato.getIdcon());
			pst.executeUpdate();
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
