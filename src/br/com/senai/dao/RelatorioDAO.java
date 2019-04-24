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

import br.com.senai.model.Relatorio;

@Repository
public class RelatorioDAO {
	
	private Connection conexao;

	@Autowired
	public RelatorioDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Relatorio rel) {
		String sql = "insert into relatorio(detalhe, chamado_id, status_id) values(?, ?, ?)";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, rel.getDetalhe());
			stmt.setString(2, rel.getChamado());
			stmt.setString(3, rel.getStatus());
			
			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Relatorio> listar() {
		try {
			List<Relatorio> lista = new ArrayList<Relatorio>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from relatorio");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Relatorio rel = new Relatorio();
				rel.setId(rs.getInt("id"));
				rel.setDetalhe(rs.getString("detalhe"));
				
				lista.add(rel);
			}
			rs.close();
			stmt.close();
			
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
