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

import br.com.senai.model.Equipamento;

@Repository
public class EquipamentoDAO {
	
	private Connection conexao;

	@Autowired
	public EquipamentoDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Equipamento equip) {
		String sql = "insert into equipamento(nome, descricao) values(?, ?)";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, equip.getNome());
			stmt.setString(2, equip.getDescricao());

			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void alterar(Equipamento equip) {
		try {
			PreparedStatement stmt = conexao.prepareStatement("update equipamento set nome=?, descricao=?");
			stmt.setString(1, equip.getNome());
			stmt.setString(2, equip.getDescricao());
			
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void remover(Equipamento equip) {
		try {
			PreparedStatement stmt = conexao
					.prepareStatement("delete from equipamento where id=?");
			stmt.setLong(1, equip.getId());
			
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Equipamento> listar() {
		try {
			List<Equipamento> lista = new ArrayList<Equipamento>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from equipamento");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Equipamento equip = new Equipamento();
				equip.setId(rs.getInt("id"));
				equip.setNome(rs.getString("nome"));
				equip.setDescricao(rs.getString("descricao"));

				lista.add(equip);
			}
			rs.close();
			stmt.close();
			
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Equipamento buscaId(int id) {
		Equipamento equip = null;
		String sql = "select * from equipamento where id = ?";
		try {
			PreparedStatement stmt = this.conexao
					.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				equip = new Equipamento();
				equip.setId(rs.getInt("id"));
				equip.setNome(rs.getString("nome"));
				equip.setDescricao(rs.getString("descricao"));
			}
			stmt.close();
			
		} catch (Exception e) {
			
		}
		return equip;	
	}
}
