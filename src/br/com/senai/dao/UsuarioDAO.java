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

import br.com.senai.model.Departamento;
import br.com.senai.model.Usuario;

@Repository
public class UsuarioDAO {
	
	private Connection conexao;
	
	@Autowired
	public UsuarioDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Usuario usuario) {
		String sql = "insert into usuario(nome, login, senha, email, telefone,departamento_id) values(?, ?, ?, ?, ?,?)";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getLogin());
			stmt.setString(3, usuario.getSenha());
			stmt.setString(4, usuario.getEmail());
			stmt.setString(5, usuario.getTelefone());
			stmt.setString(6, usuario.getDepartamento());
			

			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void alterar(Usuario usuario) {
		try {
			PreparedStatement stmt = conexao.prepareStatement("update usuario set nome=?, login=? where id=?");
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getLogin());
			stmt.setInt(3, usuario.getId());
			
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void remover(Usuario usuario) {
		try {
			PreparedStatement stmt = conexao
					.prepareStatement("delete from usuario where id=?");
			stmt.setLong(1, usuario.getId());
			
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Usuario> listar() {
		try {
			List<Usuario> lista = new ArrayList<Usuario>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from usuario");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setEmail(rs.getString("email"));

				lista.add(usuario);
			}
			rs.close();
			stmt.close();
			
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Departamento> listarDepartamento() {
		try {
			List<Departamento> lista = new ArrayList<Departamento>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from departamento");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Departamento dep = new Departamento();
				dep.setId(rs.getInt("id"));
				dep.setNome(rs.getString("nome"));

				lista.add(dep);
			}
			rs.close();
			stmt.close();
			
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Usuario buscaId(int id) {
		Usuario usuario = null;
		String sql = "select * from usuario where id = ?";
		try {
			PreparedStatement stmt = this.conexao
					.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setEmail(rs.getString("email"));
			}
			stmt.close();
			
		} catch (Exception e) {
			
		}
		return usuario;	
	}
	
	public Usuario existeUsuario(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuário não existe");
		} 

		try {
			PreparedStatement stmt = conexao.prepareStatement("select * from usuario where login=? and senha=?");
			stmt.setString(1, usuario.getLogin());
			stmt.setString(2, usuario.getSenha());
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setEmail(rs.getString("email"));
			} else {
				usuario = null;
			}
			rs.close();
			stmt.close();
			
			return usuario;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
