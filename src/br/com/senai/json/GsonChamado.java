package br.com.senai.json;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import br.com.senai.dao.ChamadoDAO;
import br.com.senai.dao.FuncionarioDAO;
import br.com.senai.model.Chamado;
import br.com.senai.model.Funcionario;

@RestController
@RequestMapping("services/chamado")
public class GsonChamado {
	@Autowired
	private ChamadoDAO dao;

	@Autowired
	private FuncionarioDAO daoF;

	@RequestMapping(value = "/listarChamados", method = RequestMethod.GET, headers = "accept=application/json;charset=utf-8")
	public List<Chamado> lista(Model model) {
		model.addAttribute("chamado", dao.listar());
		return dao.listar();
	}
	
	@RequestMapping(value = "/listarChamados/{login}", method = RequestMethod.GET, headers = "accept=application/json;charset=utf-8")
	public List<Chamado> listaTecnico(@PathVariable(value = "login") String login) {
		//Chamado c = dao.buscaLogin(login);
		return dao.listarTecnico(login);
	}

	@RequestMapping(value = "/chamado_id/{id}", method = RequestMethod.GET, headers = "accept=application/json;charset=utf-8")
	public Chamado buscaPorid(@PathVariable(value = "id") int id) {
		Chamado c = dao.buscaId(id);
		dao.finalizarAgora(c);
		return c;
	}

	@RequestMapping(value = "/login/{login}/{senha}", method = RequestMethod.GET, headers = "accept=application/json;charset=utf-8")
	public Funcionario login(@PathVariable(value = "login") String login, @PathVariable(value = "senha") String senha) {
		Funcionario ff = daoF.login(login, senha);
		return ff;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "accept=application/json;charset=utf-8", consumes = "application/json;charset=UTF-8")
	public String logar(@RequestBody String jsonString, Funcionario f) {
		String retorno;
		
		JSONObject json = new JSONObject(jsonString);
		
		f = new Funcionario();
		if (daoF.login2(f) != null) {
			System.out.println(jsonString);
		}
		
		String login = json.getString("login");
		String senha = json.getString("senha");
		
		if (daoF.login(login, senha) != null) {
			retorno = "Logado com Sucesso!";
		} else {
			retorno = "Erro Encontrado";
		}
		return retorno;
	}
	
	@RequestMapping(value = "/foto", method = RequestMethod.POST, headers = "accept=application/json;charset=utf-8", consumes = "application/json;charset=UTF-8")
	public String foto(@RequestBody String jsonString) {

		System.out.println(jsonString);
		JSONObject json = new JSONObject(jsonString);
		
		
		
		byte[] foto = Base64.decode(json.getString("foto"));
		Chamado c = new Chamado();
		c.setId(json.getInt("idChamado"));
		c.setFoto(foto);
		
		dao.finalizarAgoraWS2(c);
		
		return "Finalizado";
	}
}
