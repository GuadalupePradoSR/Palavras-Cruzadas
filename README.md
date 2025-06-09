# Palavras-Cruzadas
Jogo de palavras cruzadas desenvolvido em Java tanto no back end como no front end, para o front foi utilizado java swing.
# Funcionalidade do Sistema
Inicialmente o desenvolvedor se identifica, depois ele é responsável por criar o tamanho da matriz e inserir as palavras informando a direção, a coluna e a linha que a palavra deve ser inserida.

Em seguida, o sistema muda para a tela do jogador, no qual ele vai se identificar e depois vai visualizar a matriz que contém as palavras inseridas, simulando um jogo de palavras cruzadas, o jogador deve apenas digitar na caixa de texto as palavras que ele encontrou na matriz, quando as palavras que ele encontrou estão corretas com as palavras presentes na matriz, essas palavras e suas respectivas posições e direções são salvas em um campo de visualização no qual é constantemente atualiazado a medida que o jogador vai achando as palavras.

Além disso, as palavras inseridas pelo desenvolvedor são salvas em um arquivo CSV, no qual é manipulado para inserir as palavras na matriz. Todas as palavras que o jogador encontra também são salvas em outro arquivo CSV.
# POO e Try-Catch
O projeto engloba os principais conceitos de Programação Orientada a Objetos (POO), como encapsulamento, herança, polimorfismo e uso de interfaces. As classes foram organizadas para separar as responsabilidades, facilitando a manutenção e a escalabilidade do código. 

Além disso, o sistema conta com tratamento de exceções usando blocos try-catch, garantindo que erros como entradas inválidas, falhas na leitura ou escrita de arquivos CSV, e problemas na manipulação da matriz sejam tratados de forma adequada, proporcionando uma experiência mais robusta e segura ao usuário. 
