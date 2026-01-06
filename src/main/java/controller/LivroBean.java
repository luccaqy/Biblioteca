package controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import model.Livro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("livroBean") // Define o nome do bean para uso nas páginas JSF
@SessionScoped // Escopo de sessão: o bean mantém os dados enquanto a sessão do usuário estiver ativa
public class LivroBean implements Serializable {

    private Livro livro = new Livro(); // Objeto atual do livro a ser cadastrado
    private static List<Livro> livros = new ArrayList<>(); // Lista de livros cadastrados

    // Adiciona o livro à lista e reinicia o objeto
    public void adicionarLivro() {
        livros.add(livro);
        livro = new Livro();
    }

    // Remove o livro da lista com base no ISBN
    public void excluir(String isbn) {
        livros.removeIf(l -> l.getIsbn().equals(isbn));
    }

    // Getters e setters
    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    // Salva o livro após validações
    public void salvar() {
        // Verifica se todos os campos foram preenchidos corretamente
        if (livro.getTitulo() == null || livro.getTitulo().equals("") || livro.getTitulo().trim().isEmpty() ||
                livro.getAutor() == null || livro.getAutor().equals("") || livro.getAutor().trim().isEmpty() ||
                livro.getIsbn() == null || livro.getIsbn().equals("") || livro.getIsbn().trim().isEmpty() ||
                livro.getAno() == null || livro.getAno() <= 0) {

            // Exibe mensagem de erro caso algum campo esteja inválido
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos corretamente.", null));
            return;
        } else if (livro.getAno() < 0 || livro.getAno() > 2100) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ano inválido", null));
            return;
        }  else if (!livro.getIsbn().matches("\\d{1,13}")) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "ISBN inválido. Use de 1 a 13 digitos númericos.", null));
            return;
        }

        // Verifica se já existe livro com o mesmo título ou ISBN
        boolean duplicado = livros.stream()
                .anyMatch(l -> l.getTitulo().equalsIgnoreCase(livro.getTitulo()) || l.getIsbn().equalsIgnoreCase(livro.getIsbn()));

        if (duplicado) {
            // Exibe mensagem de erro caso seja duplicado
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Livro ou ISBN já cadastrado.", null));
            return;
        }

        // Adiciona o livro à lista se passou pelas validações
        adicionarLivro();
    }
}