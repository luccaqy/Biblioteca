package controller;

import model.Livro;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "livroServlet", value = "/livros") // Define a URL de acesso ao servlet
public class LivroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Livro> livros; // Lista local de livros (não compartilhada com o Bean)

    @Override
    public void init() {
        // Inicializa a lista ao iniciar o servlet
        livros = new ArrayList<>();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtém o livroBean do contexto da aplicação ou cria um novo se não existir
        LivroBean livroBean = (LivroBean) request.getServletContext().getAttribute("livroBean");
        if (livroBean == null) {
            livroBean = new LivroBean();
            request.getServletContext().setAttribute("livroBean", livroBean);
        }

        // Envia a lista para a página de listagem
        request.setAttribute("livros", livros);
        request.getRequestDispatcher("/listagem.xhtml").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtém o livroBean do contexto ou cria um novo
        LivroBean livroBean = (LivroBean) request.getServletContext().getAttribute("livroBean");
        if (livroBean == null) {
            livroBean = new LivroBean();
            request.getServletContext().setAttribute("livroBean", livroBean);
        }

        // Recupera os parâmetros do formulário
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String anoStr = request.getParameter("anoPublicacao");
        String isbn = request.getParameter("isbn");
        try {
            int anoPublicacao = Integer.parseInt(anoStr);
            Livro livro = new Livro(titulo, autor, anoPublicacao, isbn);
            livros.add(livro);
            response.sendRedirect("livros"); // Redireciona para a listagem
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Ano inválido. Digite um número válido.");
            request.getRequestDispatcher("/cadastro.xhtml").forward(request, response);
            return;
        }
    }
}