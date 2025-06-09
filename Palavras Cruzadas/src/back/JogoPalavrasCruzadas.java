package back;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class JogoPalavrasCruzadas {

    protected Dev dev;
    protected Tabuleiro tabuleiro;
    protected GerenciadorPalavras gerenciador;
    protected ArrayList<PalavraPosicionada> palavrasComPosicao = new ArrayList<>();
    

    public void setDev(Dev dev) {
    this.dev = dev;
    }
    public Dev getDev() {
        return dev;
    }


    public void setTabuleiro(Tabuleiro tabuleiro) {
    this.tabuleiro = tabuleiro;
    }
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }


    public void setGerenciador(GerenciadorPalavras gerenciador) {
    this.gerenciador = gerenciador;
    }
    public GerenciadorPalavras getGerenciador() {
        return gerenciador;
    }


    public ArrayList<PalavraPosicionada> getPalavrasComPosicao() {
        return palavrasComPosicao;
    }

    public void jogar() {
        Scanner sc = new Scanner(System.in);

        obterDev(sc);
        criarTabuleiro(sc);
        inserirPalavras(sc);
        tabuleiro.preencherAleatorio();
        inserirPalavrasNoTabuleiro();

        jogarComoJogador(sc);

        sc.close();
    }

    // Lógica para o Dev: obter nome e validar
    private void obterDev(Scanner sc) {
        dev = null;
        while (dev == null) {
            try {
                System.out.println("Dev, digite seu nome: ");
                String nomeDev = sc.nextLine().trim();
                dev = new Dev(nomeDev);

                if (nomeDev.isEmpty() || nomeDev.isBlank()) {
                    throw new Exception("nome não pode ser vazio");
                }
                if (!dev.validar(nomeDev)) {
                    throw new Exception("nome deve conter apenas letras e acento");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                dev = null; // força repetir
            }
        }
    }

    // Lógica para criar o tabuleiro
    private void criarTabuleiro(Scanner sc) {
        int linhas = 0, colunas = 0;
        while (linhas < 2) {
            try {
                System.out.print(dev.nome + ", escolha o número de linhas do tabuleiro (mínimo 2): ");
                String entradaLinhas = sc.nextLine().trim();
                linhas = Integer.parseInt(entradaLinhas);
                if (linhas < 2) {
                    System.out.println("O número de linhas deve ser maior ou igual a dois");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas um número inteiro para as linhas");
                linhas = 0; // força repetir
            }
        }
        while (colunas < 2) {
            try {
                System.out.print(dev.nome + ", escolha o número de colunas do tabuleiro (mínimo 2): ");
                String entradaColunas = sc.nextLine().trim();
                colunas = Integer.parseInt(entradaColunas);
                if (colunas < 2) {
                    System.out.println("O número de colunas deve ser maior ou igual a dois");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas um número inteiro para as colunas");
                colunas = 0; // força repetir
            }
        }
        tabuleiro = new Tabuleiro(linhas, colunas);
    }

    // Lógica para inserir palavras do Dev
    private void inserirPalavras(Scanner sc) {
        gerenciador = new GerenciadorPalavras();
        String palavraDev;
        do {
            try {
                System.out.print("Digite uma palavra para adicionar (ou 'sair' para finalizar): ");
                palavraDev = sc.nextLine().trim();
                if (palavraDev.isEmpty()) {
                    throw new Exception("Palavra não pode ser vazia");
                }
                if (!palavraDev.equalsIgnoreCase("sair")) {
                    if (palavraDev.length() > tabuleiro.getColunas() && palavraDev.length() > tabuleiro.getLinhas()) {
                        throw new Exception("Palavra maior que o permitido, insira uma palavra de tamanho menor");
                    }
                    PalavraDev p = new PalavraDev(palavraDev);
                    if (p.validar()) {
                        boolean palavraInserida = false;
                        while (!palavraInserida) {
                            // Pergunta a direção
                            Direcao direcao = null;
                            while (direcao == null) {
                                try {
                                    System.out.print("Escolha a direção (1-HORIZONTAL, 2-VERTICAL): ");
                                    String entradaDirecao = sc.nextLine().trim();
                                    int opcao = Integer.parseInt(entradaDirecao);
                                    switch (opcao) {
                                        case 1: direcao = Direcao.HORIZONTAL; break;
                                        case 2: direcao = Direcao.VERTICAL; break;
                                        default: System.out.println("Opção inválida!"); break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Digite apenas o número da opção!");
                                }
                            }
                            // Pergunta a linha e coluna de início
                            int linha = -1, coluna = -1;
                            boolean posicaoValida = false;
                            while (!posicaoValida) {
                                // Linha
                                while (linha < 0 || linha >= tabuleiro.getLinhas()) {
                                    try {
                                        System.out.print("Digite a linha inicial (0 a " + (tabuleiro.getLinhas()-1) + "): ");
                                        String entradaLinha = sc.nextLine().trim();
                                        linha = Integer.parseInt(entradaLinha);
                                        if (linha < 0 || linha >= tabuleiro.getLinhas()) {
                                            System.out.println("Linha fora do intervalo!");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Digite apenas um número para a linha!");
                                    }
                                }
                                // Coluna
                                while (coluna < 0 || coluna >= tabuleiro.getColunas()) {
                                    try {
                                        System.out.print("Digite a coluna inicial (0 a " + (tabuleiro.getColunas()-1) + "): ");
                                        String entradaColuna = sc.nextLine().trim();
                                        coluna = Integer.parseInt(entradaColuna);
                                        if (coluna < 0 || coluna >= tabuleiro.getColunas()) {
                                            System.out.println("Coluna fora do intervalo!");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Digite apenas um número para a coluna!");
                                    }
                                }
                                // Validação se a palavra cabe na direção escolhida
                                int tam = p.getTexto().length();
                                boolean cabe = false;
                                switch (direcao) {
                                    case HORIZONTAL:
                                        cabe = (coluna + tam <= tabuleiro.getColunas());
                                        break;
                                    case VERTICAL:
                                        cabe = (linha + tam <= tabuleiro.getLinhas());
                                        break;
                                }
                                if (!cabe) {
                                    System.out.println("A palavra não cabe na posição e direção escolhidas!");
                                    int opcao = 0;
                                    while (opcao != 1 && opcao != 2) {
                                        System.out.print("Deseja tentar outra direção e posição para a mesma palavra? (1-SIM / 2-NÃO): ");
                                        String resposta = sc.nextLine().trim();
                                        try {
                                            opcao = Integer.parseInt(resposta);
                                            if (opcao != 1 && opcao != 2) {
                                                System.out.println("Digite apenas 1 para SIM ou 2 para NÃO.");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Digite apenas 1 para SIM ou 2 para NÃO.");
                                        }
                                    }
                                    if (opcao == 1) {
                                        
                                        direcao = null;
                                        linha = -1;
                                        coluna = -1;
                                        break;
                                    } else {
                                    
                                        palavraInserida = true;
                                        p = null;
                                        break;
                                    }
                                } else {
                                    posicaoValida = true;
                                    palavrasComPosicao.add(new PalavraPosicionada(p, linha, coluna, direcao));
                                    gerenciador.adicionar(p);
                                    palavraInserida = true;
                                }
                            }
                        }
                    } else {
                        System.out.println("Palavra inválida, por favor insira apenas palavras sem acentos");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                palavraDev = ""; // força repetir caso erro
            }
        } while (!palavraDev.equalsIgnoreCase("sair"));

        // verifica se o ArrayList de palavras está vazio
        try {
            if (gerenciador.getPalavras().isEmpty()) {
                throw new Exception("Nenhuma palavra foi inserida, jogo encerrado.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        // salvar palavras em CSV
        gerenciador.salvarEmCSV("palavrasinseridas.csv");
        System.out.println("palavras salvas em palavrasinseridas.csv");
    }

    // inserir as palavras no tabuleiro
    private void inserirPalavrasNoTabuleiro() {
        GerenciadorPalavras gerenciadorJogo = new GerenciadorPalavras();
        gerenciadorJogo.carregarDeCSV("palavrasinseridas.csv");

        for (PalavraPosicionada pp : palavrasComPosicao) {
            if (pp.palavra.getTexto().length() <= tabuleiro.getColunas()) {
                tabuleiro.inserirPalavra(pp.palavra.getTexto().toUpperCase(), pp.linha, pp.coluna, pp.direcao);
            }
        }

        // mostrar tabuleiro
        System.out.println("\n=== TABULEIRO DE PALAVRAS CRUZADAS ===");
        char[][] matriz = tabuleiro.getMatriz();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    // jogador jogar com o tabuleiro formado
    private void jogarComoJogador(Scanner sc) {
        GerenciadorPalavras gerenciadorJogo = new GerenciadorPalavras();
        gerenciadorJogo.carregarDeCSV("palavrasinseridas.csv");

        String nome = "";
        while(nome.isEmpty()){
            System.out.print("Digite seu nome para jogar: ");
            nome = sc.nextLine().trim();
            Jogador jogadorTemp = new Jogador(nome);
            if (!jogadorTemp.validar(nome) || nome.isEmpty()) {
                System.out.println("Nome inválido! Digite apenas letras e acentos.");
                nome = ""; // força repetir
            }
        }

        Jogador jogador = new Jogador(nome);

        String tentativa;
        do {
            System.out.print("Digite uma palavra encontrada (ou 'sair'): ");
            tentativa = sc.nextLine().trim();

            if (!tentativa.equalsIgnoreCase("sair")) {
                try {
                    // só aceita letras sem acento
                    if (!tentativa.matches("^[a-zA-Z]+$")) {
                        throw new Exception("Não pode inserir números, acentos ou caracteres especiais!");
                    }
                    boolean achou = false;
                    for (PalavraDev palavra : gerenciadorJogo.getPalavras()) {
                        if (palavra.getTexto().equalsIgnoreCase(tentativa)) {
                            // encontra a PalavraPosicionada correspondente à tentativa
                            PalavraPosicionada encontrada = null;
                            for (PalavraPosicionada pp : palavrasComPosicao) {
                                if (pp.palavra.getTexto().equalsIgnoreCase(tentativa)) {
                                    encontrada = pp;
                                    break;
                                }
                            }
                            if (encontrada != null) {
                                jogador.adicionarPalavra(encontrada);
                                System.out.println("Correto!");
                            } else {
                                System.out.println("Erro interno: Palavra não encontrada na lista de posições.");
                            }
                            achou = true;
                            break;
                        }
                    }
                    if (!achou) {
                        System.out.println("Palavra não encontrada!");
                    }
                    // verifica se encontrou todas as palavras
                    if (jogador.getPalavrasEncontradas().size() == gerenciadorJogo.getPalavras().size()) {
                        System.out.println("Parabéns! Você encontrou todas as palavras");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (!tentativa.equalsIgnoreCase("sair"));

        // salvar palavras encontradas pelo jogador
        System.out.println("Salvando palavras encontradas...");
        try {
            FileWriter writer = new FileWriter("palavrasachadas.csv", true);
            for (PalavraPosicionada palavraPosicionada : jogador.getPalavrasEncontradas()) {
                writer.write(jogador.getNome() + ";" + palavraPosicionada.palavra.getTexto() + "\n");
            }
            writer.close();
            System.out.println("palavras salvas em palavrasachadas.csv");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }

        System.out.println("Fim de jogo!");
    }
}
