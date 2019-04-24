package br.com.senai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.senai.model.Funcionario;

@Repository
public class FuncionarioDAO {

	private Connection conexao;

	@Autowired
	public FuncionarioDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void adicionar(Funcionario funcionario) {
		String sql = "insert into funcionario(nome, login, senha, email, telefone, tipo) values(?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, funcionario.getNome());
			stmt.setString(2, funcionario.getLogin());
			stmt.setString(3, funcionario.getSenha());
			stmt.setString(4, funcionario.getEmail());
			stmt.setString(5, funcionario.getTelefone());
			stmt.setString(6, funcionario.getTipo());

			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void alterar(Funcionario funcionario) {
		try {
			PreparedStatement stmt = conexao.prepareStatement("update funcionario set nome=?, login=? where id=?");
			stmt.setString(1, funcionario.getNome());
			stmt.setString(2, funcionario.getLogin());
			stmt.setInt(3, funcionario.getId());

			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void remover(Funcionario funcionario) {
		try {
			PreparedStatement stmt = conexao.prepareStatement("delete from funcionario where id=?");
			stmt.setLong(1, funcionario.getId());

			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Funcionario> listar() {
		try {
			List<Funcionario> lista = new ArrayList<Funcionario>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from funcionario");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Funcionario funcionario = new Funcionario();
				funcionario.setId(rs.getInt("id"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setLogin(rs.getString("login"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTipo(rs.getString("tipo"));

				lista.add(funcionario);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Funcionario buscaId(int id) {
		Funcionario funcionario = null;
		String sql = "select * from funcionario where id = ?";
		try {
			PreparedStatement stmt = this.conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				funcionario = new Funcionario();
				funcionario.setId(rs.getInt("id"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setLogin(rs.getString("login"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTipo(rs.getString("tipo"));
			}
			stmt.close();

		} catch (Exception e) {

		}
		return funcionario;
	}

	public Funcionario existeFuncionario(Funcionario funcionario) {
		if (funcionario == null) {
			throw new IllegalArgumentException("Funcionário não existe");
		}

		try {
			PreparedStatement stmt = conexao.prepareStatement("select * from funcionario where login=? and senha=? and tipo=?");
			stmt.setString(1, funcionario.getLogin());
			stmt.setString(2, funcionario.getSenha());
			stmt.setString(3, funcionario.getTipo());

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				funcionario.setId(rs.getInt("id"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setLogin(rs.getString("login"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTipo(rs.getString("tipo"));
			} else {
				funcionario = null;
			}
			rs.close();
			stmt.close();
			
			return funcionario;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Funcionario login2(Funcionario funcionario) {
		if (funcionario == null) {
			throw new IllegalArgumentException("Funcionário não existe");
		}

		try {
			PreparedStatement stmt = conexao.prepareStatement("select * from funcionario where login=? and senha=?");
			stmt.setString(1, funcionario.getLogin());
			stmt.setString(2, funcionario.getSenha());
			
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				funcionario.setId(rs.getInt("id"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setLogin(rs.getString("login"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setTelefone(rs.getString("telefone"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setTipo(rs.getString("tipo"));
			} else {
				funcionario = null;
			}
			rs.close();
			stmt.close();
			
			return funcionario;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Funcionario login(String login, String senha) {
		Funcionario f = null;
		
		try {
			PreparedStatement stmt = conexao.prepareStatement("select * from funcionario where login=? and senha=?");
			stmt.setString(1, login);
			stmt.setString(2, senha);

			ResultSet rs = stmt.executeQuery();
			
			
			if (rs.next()) {
				f = new Funcionario();
				f.setId(rs.getInt("id"));
				f.setNome(rs.getString("nome"));
				f.setLogin(rs.getString("login"));
				f.setSenha(rs.getString("senha"));
				f.setTelefone(rs.getString("telefone"));
				f.setEmail(rs.getString("email"));
				f.setTipo(rs.getString("tipo"));
			} else {
				f = null;
			}
			
			rs.close();
			stmt.close();
			
			return f;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
