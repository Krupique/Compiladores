# Compilador
Trabalho prático para a disciplina de Compiladores do curso de Ciência da Computação. <br>
Este trabalho foi desenvolvido por Henrique Krupck Secchi.
O Projeto foi desenvolvido utilizando a linguagem Java com auxilio de CSS e da biblioteca JFoenix para interface gráfica. Foi utilizado o Netbeans como IDE para o desenvolvimento.
<br><br>
A intenção deste projeto foi desenvolver uma linguagem de programação bem como o seu compilador para fins de aprendizado. <br>
O projeto foi dividido em etapas, sendo cada etapa pré-requisito para a próxima e assim sucessivamente. <br>
Uma linguagem de programação pode ser definida pela sua BNF (Backus Naur Form) e pela suas regras de First e Follow. <br>
Um compilador por sua vez possui seis etapas, sendo elas:
* Análise Léxica;
* Análise Sintática;
* Análise Semântica;
* Geração de Códigos Intermediários;
* Otimização de Códigos Intermediários;
* Geração de código de máquina.
<br>
Este projeto foi implementado até a etapa cinco do compilação, e são descritas a seguir.<br>
A BNF desta linguagem e seu first e follow estão em arquivos .txt na raiz deste projeto.

### Etapa 1: Definição da gramática
* Definição da linguagem utilizando a BNF (Backus Naur Form) para definir sua gramática e seus comandos.
* Definição das regras First e Follow da linguagem para ajudar/auxiliar na implementação das fases seguintes.

### Etapa 2: Análise Léxica
* Transformação e separação do texto de entrada em tokens, onde cada comando da linguagem tem seu respectivo token.
* Os sub-textos que não são comandos da linguagem foram separados em identificadores ou valores de acordo com sua característica.

### Etapa 3: Análise Sintática
* Validação dos tokens e verificação se os tokens estão de acordo com a BNF da linguagem, caso contrário a linguagem gerá um erro sintático. Exemplo: Se depois de um 'if' há um '(' e depois um 'identificador' ou 'valor', e assim por diante.
* A abordagem utilizada foi o método recursivo.

### Etapa 4: Análise Semântica
* Validação de tipos. Exemplo: Se um valor 'String' foi atribuido para uma variável 'int' gerá um erro semântico.
* Verificação de inicialização: Exemplo: Utilizar uma variável que ainda não possui valor também gerá um erro semântico.
* Verificação de não utilização: Exemplo: Se uma variável foi declarada mas não possui valor irá gerar um aviso.

### Etapa 5: Geração de Código Intermediário
* Essa etapa existe principalmente para transformar a linguagem de alto nível que é complexa para o montador e mal otimizada e transformar em algo mais simples para que possa ser aplicado regras de otimização, além de facilitar na hora de converter para código de máquina.

### Etapa 6: Otimização de Código Intermediário
* Otimização do código intermediário a fim de deixá-lo mais limpo e simples para a próxima etapa.

### Etapa 7: Geração de Código de Máquina
* Ainda não foi implementado.

## Imagens
### Interface
<img src="/imagens/01_interface.PNG" width="700">

### Exemplo de linguagem reconhecida
<img src="/imagens/02_exemplo01.PNG" width="700">

### Análise léxica
<img src="/imagens/03_lexico.PNG" width="550">

### Exemplo de Análise sintática de uma linguagem não reconhecida
<img src="/imagens/04_sintatico.PNG" width="700">

### Análise semântica
<img src="/imagens/05_semantico.PNG" width="550">

### Código intermediário já otimizado
<img src="/imagens/06_codigo_itermed.PNG" width="550">

### Tabela de tokens 1
<img src="/imagens/07_tabela_tokens1.PNG" width="300">

### Tabela de tokens 2
<img src="/imagens/07_tabela_tokens2.PNG" width="300">
