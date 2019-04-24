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

import br.com.senai.model.Categoria;

@Repository
public class CategoriaDAO {
	
	private Connection conexao;

	@Autowired
	public CategoriaDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Categoria categ) {
		String sql = "insert into categoria(descricao) values(?)";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, categ.getDescricao());

			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Categoria> listar() {
		try {
			List<Categoria> lista = new ArrayList<Categoria>();
			PreparedStatement stmt = this.conexao
					.prepareStatement("select * from categoria");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Categoria categ = new Categoria();
				categ.setId(rs.getInt("id"));
				categ.setDescricao(rs.getString("descricao"));

				lista.add(categ);
			}
			rs.close();
			stmt.close();
			
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
