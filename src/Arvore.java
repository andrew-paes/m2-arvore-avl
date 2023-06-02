import java.util.*;

/*
* Andrew Paes da Silva
* As chaves devem ser todas inteiras
* Você não precisa colocar os dados que os nós da árvore indexam, cada nó pode armazenar apenas a chave do nó, o fator de balanceamento e os ponteiros para as sub-árvores direita e esquerda
* As entradas do programa devem ser realizadas via teclado (ou seja, solicite ao usuário que digite a operação desejada e o valor a ser inserido). Por exemplo: “insert 10” pode ser uma forma de indicar a inserção da chave 10 na árvore
* A saída do programa também pode ser realizada com impressões, desde que seja possível ver o estado da árvore ao final de cada operação. Você pode utilizar o formato de impressão que preferir
* A cada operação de rotação realizada, imprima na tela a operação realizada (rotação simples, dupla, a esquerda ou direita) e o motivo (ou seja, qual chave estava desbalanceada)
*/
public class Arvore {
    public static volatile boolean finalizado = false;
    public static Scanner _scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            List<Integer> chaves = new ArrayList<>();
            AVL arvore = new AVL();

            do {
                // As entradas do programa devem ser realizadas via teclado
                System.out.println("# QUIT, para finalizar o programa.");
                System.out.println("# RESET, para reiniciar o programa.");
                System.out.println("# DEL, para excluir uma chave.");
                System.out.println("# FIND, para procurar uma chave.");
                System.out.println("Para INSERIR, digite um numero inteiro:");
                String input = _scanner.nextLine();

                if (input != null) {
                    if (input.trim().equalsIgnoreCase("QUIT")) {
                        finalizado = true;
                    } else if (input.trim().equalsIgnoreCase("RESET")) {
                        chaves = new ArrayList<>();
                        arvore = new AVL();
                    } else if (input.trim().equalsIgnoreCase("FIND")) {
                        try {
                            System.out.println("Para ENCONTRAR, digite um numero inteiro:");
                            input = _scanner.nextLine();

                            int chave = Integer.parseInt(input); // As chaves devem ser todas inteiras

                            No no = arvore.buscar(arvore.raiz, chave);

                            if (no != null) {
                                System.out.println("Nó encontrado.");
                                System.out.println("Chave: " + no.chave);
                                System.out.println("Altura: " + no.altura);
                                System.out.println("Fator: " + no.fator);
                            } else {
                                System.out.println("Nó não foi encontrado.");
                            }

                            System.out.println("##########################################");
                        } catch (NumberFormatException ex) {
                            System.out.println("Digite um numero valido.");
                        }
                    } else if (input.trim().equalsIgnoreCase("DEL")) {
                        try {
                            System.out.println("Para EXCLUIR, digite um numero inteiro:");
                            input = _scanner.nextLine();

                            int chave = Integer.parseInt(input); // As chaves devem ser todas inteiras
                            chaves.removeIf(item -> item.equals(chave));

                            // A saída do programa também pode ser realizada com impressões, desde que seja
                            // possível ver o estado da árvore ao final de cada operação. Você pode utilizar
                            // o formato de impressão que preferir
                            System.out.print("Chaves: [");
                            chaves.forEach(item -> System.out.print(item + ","));
                            System.out.println("]");

                            arvore.raiz = arvore.excluir(arvore.raiz, chave);
                            arvore.imprimir();

                            System.out.println("##########################################");
                        } catch (NumberFormatException ex) {
                            System.out.println("Digite um numero valido.");
                        }
                    } else {
                        try {
                            int chave = Integer.parseInt(input); // As chaves devem ser todas inteiras
                            chaves.add(chave);

                            // A saída do programa também pode ser realizada com impressões, desde que seja
                            // possível ver o estado da árvore ao final de cada operação. Você pode utilizar
                            // o formato de impressão que preferir
                            System.out.print("Chaves: [");
                            chaves.forEach(item -> System.out.print(item + ","));
                            System.out.println("]");

                            arvore.raiz = arvore.inserir(arvore.raiz, chave);
                            // arvore.raiz = arvore.excluir(arvore.raiz, numero);
                            arvore.imprimir();

                            System.out.println("##########################################");
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

// Você não precisa colocar os dados que os nós da árvore indexam,
// cada nó pode armazenar apenas a chave do nó, o fator de balanceamento e os
// ponteiros para as sub-árvores direita e esquerda
class No {
    int chave; // chave do nó
    int altura;
    int fator; // o fator de balanceamento
    No subArvoreDireita;
    No subArvoreEsquerda; // os ponteiros para as sub-árvores direita e esquerda

    No(int chave) {
        this.chave = chave;
        this.altura = 1; // Inicializa a altura do nó como 1 (nó folha)
    }
}

class AVL {
    No raiz;

    // inserção
    No inserir(No no, int novaChave) {
        if (no == null)
            return (new No(novaChave)); // Cria um novo nó se o nó atual for nulo

        if (novaChave < no.chave) // Se o valor informado é menor que o valor do nó
            no.subArvoreEsquerda = inserir(no.subArvoreEsquerda, novaChave); // Insere o valor na subárvore esquerda
        else if (novaChave > no.chave)
            no.subArvoreDireita = inserir(no.subArvoreDireita, novaChave); // Insere o valor na subárvore direita
        else
            return no; // Retorna o nó atual se o valor já existir na árvore

        no.altura = 1 + Math.max(calcularAltura(no.subArvoreEsquerda), calcularAltura(no.subArvoreDireita)); // Atualiza
                                                                                                             // a altura
                                                                                                             // do nó de
                                                                                                             // referência
        no.fator = calcularFatorBalanceamento(no); // Calcula o fator de balanceamento do nó de referência

        // A cada operação de rotação realizada, imprima na tela a operação realizada
        // (rotação simples, dupla, a esquerda ou direita)
        // e o motivo (ou seja, qual chave estava desbalanceada)
        // Realiza as rotações necessárias para manter o balanceamento da árvore
        if (no.fator > 1 && novaChave < no.subArvoreEsquerda.chave) {
            System.out.println("Realizada rotação a direita para a chave: " + no.chave);

            return rotacaoDireita(no);
        }

        if (no.fator < -1 && novaChave > no.subArvoreDireita.chave) {
            System.out.println("Realizada rotação a esquerda para a chave: " + no.chave);

            return rotacaoEsquerda(no);
        }

        if (no.fator > 1 && novaChave > no.subArvoreEsquerda.chave) {
            no.subArvoreEsquerda = rotacaoEsquerda(no.subArvoreEsquerda);

            System.out.println("Realizada rotação dupla -> esquerda para " + no.chave + " e direita para : "
                    + no.subArvoreEsquerda.chave);

            return rotacaoDireita(no);
        }

        if (no.fator < -1 && novaChave < no.subArvoreDireita.chave) {
            no.subArvoreDireita = rotacaoDireita(no.subArvoreDireita);

            System.out.println("Realizada rotação dupla -> direita para " + no.chave + " e esquerda para : "
                    + no.subArvoreDireita.chave);

            return rotacaoEsquerda(no);
        }

        return no;
    }

    // exclusão
    No excluir(No no, int chave) {
        if (no == null)
            return null; // Retorna nulo se o nó atual for nulo (árvore vazia ou valor não encontrado)

        if (chave < no.chave) {
            no.subArvoreEsquerda = excluir(no.subArvoreEsquerda, chave); // Exclui o valor na subárvore esquerda
        } else if (chave > no.chave) {
            no.subArvoreDireita = excluir(no.subArvoreDireita, chave); // Exclui o valor na subárvore direita
        } else {
            // O nó atual é o nó a ser excluído
            if (no.subArvoreEsquerda == null || no.subArvoreDireita == null) {
                // Caso em que o nó tem 0 ou 1 filho
                No filho = (no.subArvoreEsquerda != null) ? no.subArvoreEsquerda : no.subArvoreDireita;

                if (filho == null) {
                    // Caso em que o nó não tem filhos
                    no = null;
                } else {
                    // Caso em que o nó tem um único filho
                    no = filho;
                }
            } else {
                No atual = no.subArvoreDireita;

                // Percorre a subárvore esquerda para encontrar o nó mais à esquerda
                while (atual.subArvoreEsquerda != null) {
                    atual = atual.subArvoreDireita;
                }

                // Caso em que o nó tem 2 filhos
                No sucessor = atual; // Encontra o sucessor do nó atual

                no.chave = sucessor.chave;
                no.subArvoreDireita = excluir(no.subArvoreDireita, sucessor.chave);
            }
        }

        if (no != null) {
            // Atualiza a altura do nó atual
            no.altura = 1 + Math.max(calcularAltura(no.subArvoreEsquerda), calcularAltura(no.subArvoreDireita));

            // Verifica o fator de balanceamento do nó atual
            int fator = calcularFatorBalanceamento(no);

            // Realiza as rotações necessárias para manter o balanceamento da árvore
            if (fator > 1 && calcularFatorBalanceamento(no.subArvoreEsquerda) >= 0) {

                return rotacaoDireita(no);
            }

            if (fator > 1 && calcularFatorBalanceamento(no.subArvoreEsquerda) < 0) {

                no.subArvoreEsquerda = rotacaoEsquerda(no.subArvoreEsquerda);
                return rotacaoDireita(no);
            }

            if (fator < -1 && calcularFatorBalanceamento(no.subArvoreDireita) <= 0) {

                return rotacaoEsquerda(no);
            }

            if (fator < -1 && calcularFatorBalanceamento(no.subArvoreDireita) > 0) {
                no.subArvoreDireita = rotacaoDireita(no.subArvoreDireita);

                return rotacaoEsquerda(no);
            }
        }

        return no; // Retorna o nó atual após a exclusão (ou sem alterações)
    }

    // busca
    No buscar(No no, int chave) {
        if (no == null || no.chave == chave)
            return no;

        if (chave < no.chave) {
            return buscar(no.subArvoreEsquerda, chave);
        } else {
            return buscar(no.subArvoreDireita, chave);
        }
    }

    int calcularAltura(No no) {
        if (no == null)
            return 0; // Retorna 0 se o nó for nulo (altura de uma árvore vazia)

        return no.altura;
    }

    int calcularNumeroNosSubArvore(No no) {
        if (no == null) {
            return 0; // Retorna 0 se o nó for nulo (árvore vazia)
        }

        int numNosEsquerda = calcularNumeroNosSubArvore(no.subArvoreEsquerda); // Calcula o número de nós na subárvore
                                                                               // esquerda
        int numNosDireita = calcularNumeroNosSubArvore(no.subArvoreDireita); // Calcula o número de nós na subárvore
                                                                             // direita

        return 1 + numNosEsquerda + numNosDireita; // Retorna o número total de nós na árvore
    }

    // Calcula a diferença de nós entre cada lado
    int calcularFatorBalanceamento(No no) {
        if (no == null)
            return 0; // Retorna 0 se o nó for nulo (árvore vazia)

        return calcularAltura(no.subArvoreEsquerda) - calcularAltura(no.subArvoreDireita); // Calcula o fator de
                                                                                           // balanceamento do nó
    }

    No rotacaoDireita(No y) {
        No refEsquerda = y.subArvoreEsquerda; // Armazena a referência para o nó à esquerda de y
        No refDireita = refEsquerda.subArvoreDireita; // Armazena a referência para o nó à direita de x

        refEsquerda.subArvoreDireita = y; // Faz a rotação para a direita
        y.subArvoreEsquerda = refDireita;

        y.altura = Math.max(calcularAltura(y.subArvoreEsquerda), calcularAltura(y.subArvoreDireita)) + 1; // Atualiza a
                                                                                                          // altura de y
        // pegando o maior valor entre
        // esquerda e direita
        refEsquerda.altura = Math.max(calcularAltura(refEsquerda.subArvoreEsquerda),
                calcularAltura(refEsquerda.subArvoreDireita)) + 1;

        return refEsquerda;
    }

    No rotacaoEsquerda(No x) {
        No refDireita = x.subArvoreDireita; // Armazena a referência para o nó à direita de x
        No refEsquerda = refDireita.subArvoreEsquerda; // Armazena a referência para o nó à esquerda de y

        refDireita.subArvoreEsquerda = x; // Faz a rotação esquerda
        x.subArvoreDireita = refEsquerda;

        x.altura = Math.max(calcularAltura(x.subArvoreEsquerda), calcularAltura(x.subArvoreDireita)) + 1;
        refDireita.altura = Math.max(calcularAltura(refDireita.subArvoreEsquerda),
                calcularAltura(refDireita.subArvoreDireita)) + 1;

        return refDireita;
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
            System.out.println(no.chave);

            // Verifica se o nó tem filhos
            if (no.subArvoreEsquerda != null || no.subArvoreDireita != null) {
                imprimirSubArvore(no.subArvoreEsquerda, indentador, false); // Imprime lado esquerdo se houver
                imprimirSubArvore(no.subArvoreDireita, indentador, true); // Imprime lado direito se houver
            }
        }
    }
}