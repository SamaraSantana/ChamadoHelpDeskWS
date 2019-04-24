package br.com.senai.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.senai.model.Categoria;
import br.com.senai.model.Chamado;
import br.com.senai.model.Equipamento;
import br.com.senai.model.Funcionario;
import br.com.senai.model.Problema;
import br.com.senai.model.Usuario;

@Repository
public class ChamadoDAO {

	private Connection conexao;

	@Autowired
	public ChamadoDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void adicionar(Chamado ch) {
		String sql = "insert into chamado(descricao, usuario, serie, dataAbertura, problema, status, categoria, tecnico, equipamento) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, ch.getDescricao());
			stmt.setString(2, ch.getUsuario());
			stmt.setString(3, ch.getSerie());
			stmt.setDate(4, new Date(Calendar.getInstance().getTimeInMillis()));
			stmt.setString(5, ch.getProblema());
			stmt.setString(6, ch.getStatus());
			stmt.setString(7, ch.getCategoria());
			stmt.setString(8, ch.getTecnico());
			stmt.setString(9, ch.getEquipamento());

			stmt.execute();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void finalizarAgoraWS(Chamado ch) {
		String sql = "update chamado set dataFinalizacao=?, status=?, foto=? where id=?";
		PreparedStatement stmt;
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
			stmt.setString(2, "Fechado");
			stmt.setBlob(3, (ch.getFoto() == null) ? null : new ByteArrayInputStream(ch.getFoto()));
			stmt.setInt(4, ch.getId());

			stmt.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void finalizarAgoraWS2(Chamado ch) {
		String sql = "update chamado set status=? where id=?";
		PreparedStatement stmt;
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, "Fechado");
			stmt.setInt(2, ch.getId());

			stmt.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void finalizarAgora(Chamado ch) {
		String sql = "update chamado set dataFinalizacao=?, status=? where id=?";
		PreparedStatement stmt;
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
			stmt.setString(2, "Fechado");
			stmt.setInt(3, ch.getId());

			stmt.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void escolherTecnico(Chamado ch) {
		String sql = "update chamado set tecnico=? where id=?";
		
		PreparedStatement stmt;
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, ch.getTecnico());
			stmt.setInt(2, ch.getId());

			stmt.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

/*	private String pegarDataAtual() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}	*/

	public List<Chamado> listar() {
		try {
			List<Chamado> lista = new ArrayList<Chamado>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from chamado");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chamado ch = new Chamado();
				ch.setId(rs.getInt("id"));
				ch.setDescricao(rs.getString("descricao"));
				
				Date data = rs.getDate("dataAbertura");
				if (data != null) {
					Calendar dataAbertura = Calendar.getInstance();
					dataAbertura.setTime(data);
					ch.setDataAbertura(dataAbertura);
				}
				
				Date data2 = rs.getDate("dataFinalizacao");
				if (data2 != null) {
					Calendar dataFinalizacao = Calendar.getInstance();
					dataFinalizacao.setTime(data2);
					ch.setDataFinalizacao(dataFinalizacao);
				}
				
				ch.setCategoria(rs.getString("categoria"));
				ch.setEquipamento(rs.getString("equipamento"));
				ch.setProblema(rs.getString("problema"));
				ch.setSerie(rs.getString("serie"));
				ch.setStatus(rs.getString("status"));
				ch.setTecnico(rs.getString("tecnico"));
				ch.setUsuario(rs.getString("usuario"));

				lista.add(ch);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Chamado> listarTecnico(String login) {
		try {
			List<Chamado> lista = new ArrayList<Chamado>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from chamado where tecnico=?");
			stmt.setString(1, login);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chamado ch = new Chamado();
				ch.setId(rs.getInt("id"));
				ch.setDescricao(rs.getString("descricao"));
				
				Date data = rs.getDate("dataAbertura");
				if (data != null) {
					Calendar dataAbertura = Calendar.getInstance();
					dataAbertura.setTime(data);
					ch.setDataAbertura(dataAbertura);
				}
				
				Date data2 = rs.getDate("dataFinalizacao");
				if (data2 != null) {
					Calendar dataFinalizacao = Calendar.getInstance();
					dataFinalizacao.setTime(data2);
					ch.setDataFinalizacao(dataFinalizacao);
				}
				
				ch.setCategoria(rs.getString("categoria"));
				ch.setEquipamento(rs.getString("equipamento"));
				ch.setProblema(rs.getString("problema"));
				ch.setSerie(rs.getString("serie"));
				ch.setStatus(rs.getString("status"));
				ch.setTecnico(rs.getString("tecnico"));
				ch.setUsuario(rs.getString("usuario"));

				lista.add(ch);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Equipamento> listarEquipamentos() {
		try {
			List<Equipamento> lista = new ArrayList<Equipamento>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from equipamento");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Equipamento e = new Equipamento();
				e.setId(rs.getInt("id"));
				e.setNome(rs.getString("nome"));
				e.setDescricao(rs.getString("descricao"));

				lista.add(e);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Usuario> listarUsuarios() {
		try {
			List<Usuario> lista = new ArrayList<Usuario>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from usuario");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNome(rs.getString("nome"));

				lista.add(u);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Categoria> listarCategorias() {
		try {
			List<Categoria> lista = new ArrayList<Categoria>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from categoria");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Categoria c = new Categoria();
				c.setId(rs.getInt("id"));
				c.setDescricao(rs.getString("descricao"));

				lista.add(c);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Problema> listarProblemas() {
		try {
			List<Problema> lista = new ArrayList<Problema>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from problema");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Problema c = new Problema();
				c.setId(rs.getInt("id"));
				c.setDescricao(rs.getString("descricao"));

				lista.add(c);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Funcionario> listarTecnicos() {
		try {
			List<Funcionario> lista = new ArrayList<Funcionario>();
			PreparedStatement stmt = this.conexao.prepareStatement("select * from funcionario");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Funcionario c = new Funcionario();
				c.setId(rs.getInt("id"));
				c.setNome(rs.getString("nome"));
				c.setLogin(rs.getString("login"));

				lista.add(c);
			}
			rs.close();
			stmt.close();

			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Chamado buscaId(int id) {
		Chamado chamado = null;
		String sql = "select * from chamado where id = ?";
		try {
			PreparedStatement stmt = this.conexao
					.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				chamado = new Chamado();
				chamado.setId(rs.getInt("id"));
				chamado.setProblema(rs.getString("problema"));
				chamado.setCategoria(rs.getString("categoria"));
				
				Date data = rs.getDate("dataAbertura");
				if (data != null) {
					Calendar dataAbertura = Calendar.getInstance();
					dataAbertura.setTime(data);
					chamado.setDataAbertura(dataAbertura);
				}
				
				Date data2 = rs.getDate("dataFinalizacao");
				if (data2 != null) {
					Calendar dataFinalizacao = Calendar.getInstance();
					dataFinalizacao.setTime(data2);
					chamado.setDataFinalizacao(dataFinalizacao);
				}
				
				chamado.setDescricao(rs.getString("descricao"));
				chamado.setEquipamento(rs.getString("equipamento"));
				chamado.setSerie(rs.getString("serie"));
				chamado.setStatus(rs.getString("status"));
				chamado.setTecnico(rs.getString("tecnico"));
				chamado.setUsuario(rs.getString("usuario"));
			}
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return chamado;	
	}
	
	public Chamado buscaLogin(String login) {
		Chamado chamado = null;
		Funcionario f = null;
		String sql = "select chamado.id, funcionario.id, funcionario.login, chamado.problema, chamado.status from chamado, funcionario where funcionario.login = ?";
		try {
			PreparedStatement stmt = this.conexao
					.prepareStatement(sql);
			stmt.setString(1, login);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				chamado = new Chamado();
				chamado.setId(rs.getInt("chamado.id"));
				chamado.setProblema(rs.getString("problema"));
				
				chamado.setStatus(rs.getString("status"));
				
				f = new Funcionario();
				f.setLogin(rs.getString("funcionario.login"));
				f.setId(rs.getInt("funcionario.id"));
				
				chamado.setTecnico(f.getLogin());
			}
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return chamado;	
	}
}
