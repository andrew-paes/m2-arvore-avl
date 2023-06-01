import java.util.*;

/*
* Andrew Paes da Silva
*/
public class Arvore {
    public static volatile boolean finalizado = false;
    public static Scanner _scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            List<Integer> itens = new ArrayList<>();
            AVL arvore = new AVL();

            do {
                System.out.println(
                        "Digite SAIR, para finalizar o programa. Se quiser continuar digite um numero inteiro:");
                String input = _scanner.nextLine();

                if (input != null) {
                    if (input.trim().equalsIgnoreCase("SAIR")) {
                        finalizado = true;
                    } else {
                        try {
                            int numero = Integer.parseInt(input);
                            arvore.raiz = arvore.inserir(arvore.raiz, numero);

                            itens.add(numero);

                            System.out.println("##########################################");
                            System.out.println("AVL impressa:");
                            System.out.print("Nós: [");
                            itens.forEach(item -> System.out.print(item + ","));
                            System.out.println("]");

                            arvore.imprimir();
                        } catch (NumberFormatException ex) {
                            System.out.println("Digite um numero valido.");
                        }
                    }
                }
            } while (!finalizado);

            _scanner.close();
        } catch (Exception e) {
            System.out.println("Erro:");
            e.printStackTrace();
        }
    }
}

class No {
    int valor;
    int altura;
    No esquerda;
    No direita;

    No(int valor) {
        this.valor = valor;
        this.altura = 1; // Inicializa a altura do nó como 1 (nó folha)
    }
}

class AVL {
    No raiz;

    int altura(No no) {
        if (no == null)
            return 0; // Retorna 0 se o nó for nulo (altura de uma árvore vazia)

        return no.altura;
    }

    // Calcula a diferença de nós entre cada lado
    int calcularBalanceamento(No no) {
        if (no == null)
            return 0; // Retorna 0 se o nó for nulo (árvore vazia)

        return altura(no.esquerda) - altura(no.direita); // Calcula o fator de balanceamento do nó
    }

    No rotacaoDireita(No y) {
        No refEsquerda = y.esquerda; // Armazena a referência para o nó à esquerda de y
        No refDireita = refEsquerda.direita; // Armazena a referência para o nó à direita de x

        refEsquerda.direita = y; // Faz a rotação para a direita
        y.esquerda = refDireita;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1; // Atualiza a altura de y pegando o maior valor
                                                                        // entre esquerda e direita
        refEsquerda.altura = Math.max(altura(refEsquerda.esquerda), altura(refEsquerda.direita)) + 1;

        return refEsquerda;
    }

    No rotacaoEsquerda(No x) {
        No refDireita = x.direita; // Armazena a referência para o nó à direita de x
        No refEsquerda = refDireita.esquerda; // Armazena a referência para o nó à esquerda de y

        refDireita.esquerda = x; // Faz a rotação esquerda
        x.direita = refEsquerda;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        refDireita.altura = Math.max(altura(refDireita.esquerda), altura(refDireita.direita)) + 1;

        return refDireita;
    }

    No inserir(No no, int valor) {
        if (no == null)
            return (new No(valor)); // Cria um novo nó se o nó atual for nulo

        if (valor < no.valor) // Se o valor informado é menor que o valor do nó
            no.esquerda = inserir(no.esquerda, valor); // Insere o valor na subárvore esquerda
        else if (valor > no.valor)
            no.direita = inserir(no.direita, valor); // Insere o valor na subárvore direita
        else
            return no; // Retorna o nó atual se o valor já existir na árvore

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita)); // Atualiza a altura do nó

        int fator = calcularBalanceamento(no); // Calcula o fator de balanceamento do nó

        // Realiza as rotações necessárias para manter o balanceamento da árvore
        if (fator > 1 && valor < no.esquerda.valor)
            return rotacaoDireita(no);

        if (fator < -1 && valor > no.direita.valor)
            return rotacaoEsquerda(no);

        if (fator > 1 && valor > no.esquerda.valor) {
            no.esquerda = rotacaoEsquerda(no.esquerda);

            return rotacaoDireita(no);
        }

        if (fator < -1 && valor < no.direita.valor) {
            no.direita = rotacaoDireita(no.direita);

            return rotacaoEsquerda(no);
        }

        return no;
    }

    int calcularNumeroNosSubArvore(No no) {
        if (no == null) {
            return 0; // Retorna 0 se o nó for nulo (árvore vazia)
        }

        int numNosEsquerda = calcularNumeroNosSubArvore(no.esquerda); // Calcula o número de nós na subárvore esquerda
        int numNosDireita = calcularNumeroNosSubArvore(no.direita); // Calcula o número de nós na subárvore direita

        return 1 + numNosEsquerda + numNosDireita; // Retorna o número total de nós na árvore
    }

    void imprimir() {
        int numeroNos = calcularNumeroNosSubArvore(raiz);
        System.out.println("A árvore contém " + numeroNos + " nós.");

        imprimirSubArvore(this.raiz, "", true);
    }

    void imprimirSubArvore(No no, String indentador, boolean ultimo) {

        if (no != null) {
            // Verifica se o nó atual é o último filho do nó pai
            if (ultimo) {
                System.out.print(indentador + "└── ");
                indentador += "    "; // Aumenta a indentação para o último filho
            } else {
                int numNos = calcularNumeroNosSubArvore(no);
                
                if (numNos > 1)
                    System.out.print(indentador + "├── ");
                else
                    System.out.print(indentador + "└── ");

                indentador += "│   "; // Aumenta a indentação para outros filhos
            }

            // Imprime o valor do nó atual
            System.out.println(no.valor);

            // Verifica se o nó tem filhos
            if (no.esquerda != null || no.direita != null) {
                imprimirSubArvore(no.esquerda, indentador, false); // Imprime lado esquerdo se houver
                imprimirSubArvore(no.direita, indentador, true); // Imprime lado direito se houver
            }
        }
    }
}