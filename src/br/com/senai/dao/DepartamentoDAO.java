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

@Repository
public class DepartamentoDAO {
	
	private Connection conexao;

	@Autowired
	public DepartamentoDAO(DataSource data) {
		try {
			this.conexao = data.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void adicionar(Departamento dep) {
		String sql = "insert into departamento(nome) values(?)";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, dep.getNome());

			stmt.execute();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Departamento> listar() {
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
}
