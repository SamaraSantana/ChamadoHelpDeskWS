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
import br.com.senai.model.Problema;

@Repository
public class ProblemaDAO {
	
	private Connection conexao;

	@Autowired
	public ProblemaDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Problema p) {
		String sql = "insert into problema(descricao, equipamento_id) values(?, ?)";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, p.getDescricao());
			stmt.setString(2, p.getEquipamento());
			
			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Equipamento> listarEquipamentos() {
		try {
			List<Equipamento> lista = new ArrayList<Equipamento>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from equipamento");
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
}
