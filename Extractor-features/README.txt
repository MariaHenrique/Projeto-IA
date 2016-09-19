- extract-words-more-frequent-SPAMS.py:
	- realiza um preprocessamento nos emals, identificar palavras iguais escritas no singular e/ou plural. Verifica quais sao as N palavras mais frequentes nos SPAMS de treinamento
	- para usar, basta seguir o seguinte comando:
		- python extract-words-more-frequent-SPAMS.py <CAMINHO-DA-PASTA-SPAM> <numero-das-N-palavras-mais-frequentes> <ARQUIVO-QUE-SERÁ-SALVO-AS-N-PALAVRAS>


- extract-features.py:
	- Computa os histogramas com base na palavras detectadas no script anterior.
	- para usar, é preciso ter as palavras salvas no disco, ou seja, ter rodado o script acima. Em seguida, basta seguir o seguinte comando:
		- python extract-features.py <CAMINHO-DA-PASTA-HAM> <CAMINHO-DA-PASTA-SPAM> <NOME-DO-ARQUIVO-DE-SAIDA-DOS-HISTOGRAMAS> <treino/teste>
			- 'treino': se as pastas que contém os spams e hams são do treinamento. 'teste', caso contrário
