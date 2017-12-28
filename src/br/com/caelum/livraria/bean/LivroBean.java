package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;

@ManagedBean
@ViewScoped
public class LivroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Livro livro = new Livro();
    private Integer autorId;

    
    public void setAutorId(Integer autorId) {
        this.autorId = autorId;
    }

    public Integer getAutorId() {
        return autorId;
    }

    public Livro getLivro() {
        return livro;
    }

    public List<Livro> getLivros() {
        return new DAO<Livro>(Livro.class).listaTodos();
    }

    public List<Autor> getAutores() {
        return new DAO<Autor>(Autor.class).listaTodos();
    }

    public List<Autor> getAutoresDoLivro() {
        return this.livro.getAutores();
    }

    public void gravarAutor() {
        Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
        this.livro.adicionaAutor(autor);
    }

    /*
     * FacesContext.getCurrentInstance() - obtem a referência do contexto JSF no momento da chamada do método
     * addMessage(String , FacesMessage) - Adiciona a mensagem no contexto
     * 		O primeiro parâmetro  é o id que definimos no XHTML do componente
     * 		O segundo parâmetro é um FacesMessage, que recebe no seu construtor a mensagem que queremos mostrar
     */
    public void gravar() {
        System.out.println("Gravando livro " + this.livro.getTitulo());

        if (livro.getAutores().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("autor",  new FacesMessage("Livro deve ter pelo menos um Autor"));
            return;
        } else {
            new DAO<Livro>(Livro.class).adiciona(this.livro);
            this.livro = new Livro(); //limpar os campos da tela
        }
    }
    
    /*
     * FacesContext - um objeto que permite obter informações da view processada no momento;
     * UIComponent - referencia ao componente que esta sendo validado;
     * Object - valor digitado pelo usuário;
     * ValidatorException - é a exceção que sinaliza para o JSF que algo deu errado.
     */
    public void validaISBNComecaComDigitoUm(FacesContext fc, UIComponent component, Object valorDigitado) 
    		throws ValidatorException {
    	
    	String valor = valorDigitado.toString();
    	if(!valor.startsWith("1"))
    		throw new ValidatorException(new FacesMessage("ISBN deve começar com 1"));
    }
}
