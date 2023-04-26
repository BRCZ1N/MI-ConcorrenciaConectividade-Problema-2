<h2 align="center">Postos de Recarga inteligentes </h2>
 
# Índice

- [Desenvolvedor](#desenvolvedor)
- [Solução](#Solução)
- [Componentes](#Componentes)
   - [Posto](#Posto)
   - [Névoa](#Névoa)
   - [Cliente](#Cliente)
   - [Nuvem](#Nuvem)
   - [API Rest](#APIRest)
- [Considerações finais](#consideracoes)

# Desenvolvedor
<br /><a href="https://github.com/BRCZ1N">Bruno Campos</a>
<br /><a href="https://github.com/Oguelo">Alex Júnior</a>

# Introdução

Esse problema foi desenvolvido com a proposta de resolver dificuldades relacionadas ao recarregamento de veiculos eletricos, e os problemas de grandes filas no postos alem do alto tempo gasto pelos consumidores desse tipo de automovel. Os alunos de Engenharia da computação, da Universidade Estadual de Feira de Santana (UEFS), foram instruidos para a construção de um prototipo de sistema que ira analizar a quantidade de carros presentes em filas de postos proprios para recargas de veiculos eletricos em diferentes cantos das cidades.

O relatório presente pretende descrever de forma simples um projeto que se baseia em um sistema de comunicação que utiliza arquitetura em rede desenvolvido na linguagem de programação Java.

O presente projeto foi desenvolvido a partir do uso da comunicação MQTT em uma das aplicações (posto) e do uso de requisições HTTP(Carro) para a obtenção de dados, provendo uma comunicação TCP entre os carros e os servidores da névoa dos quais ele se conectarão alem disso, haverá a conexão mqtt entre posto e névoa, onde o posto publicara dados de quantidade de sua fila e os servidores da névoa irão receber esses dados após a inscrição deles no topico referente a obtenção desse dados, Assim esses servidores irão armazenar a quantidade de carros nas filas destes postos, além calcular qual a melhor fila por tamanho, localização, além de fazer um apanhado de todos os postos presentes na determinada aréa da qual esse servidor coexiste. Os automoveis terão que cuidar do acompanhamento do consumo de energia da bateria, alem de alertar sobre os niveis criticos e mostrar os postoso proximos, com menores filas, juntamente com o apanhado de todos os postos para o usuario. Com relação à parte de teste de execução desse protótipo, utilizou-se de contêineres docker para virtualizar e executar a aplicação em um ambiente de rede, e tambem foi utilizado o software mosquitto para ser o broker de recebimento e roteamento de mensagens para seus destinos. 

# Fundamentação teórica

<li>O servidor pode ser definido como uma entidade que provê serviços para serem utilizados por clientes, seja ele um usuário ou um sistema. 
<li>O cliente pode ser determinado como uma entidade que utiliza dados providos de servidores.
<li>Relacionamento cliente-servidor, consiste basicamente em uma entidade que requisita serviços, isto é, um cliente e uma entidade que processa e envia respostas ao cliente, isto é, provendo-o, conhecido como servidor.
<li>API REST (Representational State Transfer) é um estilo de arquitetura de software para sistemas distribuídos que utiliza o protocolo HTTP para a comunicação entre diferentes sistemas. É um conjunto de padrões e princípios que definem como as requisições e respostas devem ser feitas, permitindo a integração de diferentes aplicações e sistemas. Em uma arquitetura RESTful, cada recurso é representado por uma URL, e as operações que podem ser realizadas nesses recursos são definidas pelos verbos HTTP (GET, POST, PUT, DELETE, entre outros).
<li>Contêineres são uma forma de virtualização de aplicativos que permite a criação de ambientes isolados e independentes, nos quais é possível executar aplicativos e serviços sem afetar o sistema operacional ou outras aplicações que estejam sendo executadas no mesmo servidor ou máquina.
<li>Docker é uma plataforma de código aberto que permite a criação, distribuição e execução de aplicativos em contêineres.
<li>Mosquitto é um broker MQTT, ele fornece um serviço de mensagens que permite que os dispositivos se comuniquem entre si usando o protocolo MQTT.
<li>MQTT (Message Queuing Telemetry Transport) é amplamente utilizado em aplicações de IoT para coletar e enviar dados de sensores, monitorar dispositivos remotos e controlar dispositivos conectados.
<li>TCP (Transmission Control Protocol) é um protocolo de comunicação de rede que oferece uma transmissão confiável de dados entre dois dispositivos em uma rede, estabelecendo uma conexão antes da transferência de dados, além de verificar a entrega dos dados. 
<li> Nuvem tem como principal vantagem, a flexibilidade e escalabilidade que ela oferece, permitindo que as empresas aumentem ou reduzam seus recursos de computação de acordo com suas necessidades em tempo real. Esses recursos são gerenciados e mantidos por provedores de serviços em nuvem e são distribuídos em diferentes locais ao redor do mundo, permitindo que os usuários acessem e utilizem esses serviços a partir de qualquer dispositivo conectado à internet.
<li> Nevoa, mais conhecido como computação de borda, refere-se a uma arquitetura de rede que processa e armazena dados localmente, perto dos dispositivos de origem ou dos usuários finais, em vez de enviá-los para processamento em servidores remotos em nuvem.
<li> Bridge (ponte em português) é um dispositivo de rede que conecta dois ou mais segmentos de rede local (LAN) para permitir a comunicação entre eles.

 # Metodologia geral

Para realização deste projeto a prori pensou-se no emprego de um tipo de arquitetura que poderia se adequar ao projeto, e ficou acordado que a arquitetura utilizada seria de névoa, afinal considerou-se que esse tipo arquitetural poderia fornecer um melhor desempenho com relação ao excesso de dados enviados, tendo em vista que o problema considera que o cenário proposto é na casa dos milhares de veículos, e portanto se sua concentração fosse na núvem isso poderia gerar diversas problemáticas de desempenho na aplicação com relação a quantidade de dados.

Com relação a comunicação entre os nós da rede ficou estabelicido da seguinte forma, cada carro envia requisições HTTP via API REST a uma névoa regional que estivesse dentro dos limites de sua localização. A névoa regional como mencionado anteriormente se comunica com o carro e retorna resposta para essas requisições, além disso ela se comunica via protocolo de comunicação MQTT com os postos de recarga para receber os dados dos postos da região, e portanto receber os dados de localização dos postos, dados de fila, nome do posto e até mesmo o seu identificador, com finalidade de obter e processar os dados que devem ser utilizados pelos usuarios finais, isto é, os carros, por fim a mesma envia os dados do melhor posto de sua região para a núvem via MQTT através de uma bridge MQTT. Finalmente tem-se a núvem que recebe os dados da névoa e armazena-os para retornar as névoas regionais uma lista dos melhores postos de cada região.

Com relação as névoas regionais a sua implementação ocorreu da seguinte forma, utilizou-se de uma aplicação spring para que a comunicação HTTP fosse efetuada, isto é, precisou de controladores, para enviar os dados para os usuários finais e para auxiliar os mesmos foram criados serviços com finalidade de processar algumas informações que foram armazenadas na névoa advindo dos postos e da núvem. Após receber os dados via MQTT eles foram processados e armazenados para que ficassem disponível aos usuários, além disso precisou-se de callbacks para a comunicação MQTT e threads, os callbacks foram utilizados para receber as mensagens via MQTT entre o posto e névoa, e entre névoa e núvem, além disso nessa comunicação névoa-nuvem utilizou-se de uma bridge mqtt, isto é, uma ponte de comunicação entre o broker regional e o broker global, para que fossem trocados os dados entre a névoa regional e o broker global

A aplicação que corresponde aos carros foram desenvolvidos com uma interface que utiliza o método generateBatteryCar para gerar o nível inicial da bateria do automóvel, juntamente com o método generateCurrentDischargeLevel, que geraria o nível de descarga que o carro teria. Além disso, o método generatePosUser é usado para definir a posição relativa aleatória do automóvel. Esses métodos gerariam números aleatórios para cada automóvel que existisse no sistema, então 3 threads são criadas com o objetivo de reduzir o nível de bateria do carro, monitorar o nível de bateria para gerar alertas ao motorista e a thread que irá definir qual estação de rádio o carro se comunicará, dependendo da latitude e longitude gerada automaticamente anteriormente. Finalmente, o motorista tem acesso a um menu que exibe o nível de carga da bateria do carro e também possui um menu com opções para buscar postos com as filas mais curtas na região, buscar os postos mais próximos do carro e até mesmo buscar postos em outras regiões que tenham as melhores condições de acesso.

Com o objetivo de rotear informações para as diferentes nevoas de cada região, uma nuvem foi construída com uma estrutura que permite o recebimento de dados enviados por cada nevoa no formato MQTT e o envio posterior desses dados para outras nevoas presentes no sistema. Para fazer isso, foram utilizadas duas threads, uma para receber dados de todas as nevoas, que possuem um identificador único para evitar a duplicação de dados, e outra para enviar esses dados para cada nevoa.É importante destacar que apenas os dados dos melhores postos de cada região são trocados pelas nevoas. Esses dados são compartilhados para eventual necessidade de acesso a um posto presente em uma região diferente.
 
 O posto de recarga utiliza a geolocalização para determinar sua posição relativa e se conectar à nevoa presente na área. Além disso, ele possui uma interface que permite ao usuário inserir o nome do estabelecimento. Após a inserção do nome, as threads são iniciadas para configurar a conexão MQTT, atualizar a quantidade de carros na fila e enviar esses dados para a nevoa presente na região.
# Solução para os requisitos principais 

<h2>Requisitos principais do projeto</h2>

   1. Interface para gerenciamento de postos disponiveis;
   2. Acompanhamento do consumo de energia do automovel;
   
<h2>1. Interface para gerenciamento de postos disponiveis.</h2>
   
&emsp;Para esse requisito foi desenvolvido uma interface simples que possui uma entrada para a definição da região onde o carro esta presente, após isso um menu para as consultas do nivel de bateria e para os menus onde estão postos o metodos de busca de postos para recarga do carro, dos quais estão os postos com menor fila, postos mais proximos do veiculo e para buscar todos os postos na proximidade, assim uma requisição em formato JSON pode ser feita e enviada a nevoa. 

<h2>2. Acompanhamento do consumo de energia do automovel.</h2>

&emsp;Objetivando o acompanhamento do consumo de energia, e assim avisar ao motorista quando o automovel estiver com a bateria em situação critica e necessite de abastecimento, este acompanhamento é feito por meio de uma interface que possui uma variavel que recebe um valor para simular a quantidade de bateria, com o passar do tempo a variavel é decrementada, do qual ela pode ser decrementada a partir de três fases, dentre elas alta, media e baixa.

# Componentes do projeto

<h2>- Servidor Nevoa</h2>
<p2> O servidor processa as requisições, realiza o tratamento das mesmas, e por fim retorna a resposta da requisição, ainda se considera a sua função de gerar threads para cada tipo de componente no servidor, isto é, seja posto(MQTT) ou usuário(TCP).</p2>
 <h2>- Servidor Nuvem</h2>
<p2> O servidor faz o papel de roteamento das informações que foram geradas por todos os postos presentes nas nevoas e enviados para ele.</p2>

<h2>- Interface do usuário</h2>
<p2> O usuário se conecta ao servidor da nevoa e aos serviços através da API Rest. O mesmo, por conseguinte, apresenta as seguintes funcionalidades:</p2>
 <ul>
  <li>1. Visualizar nivel de carga de energia</li>
  <li>2. Buscar posto com menor fila </li>
  <li>3. Buscar posto mais proximo </li>
  <li>4. Buscar melhores postos de outras regiões</li>
  <li>5. Desconectar</li>
</ul>

<h2>- Interface do posto </h2>
<p2> O posto realiza o envio dos dados ao servidor  da nevoa de forma continua</p2>
 <ul>
  <li>1. Envia a quantidade na fila e os dados necessários para armazenar a quantidade no servidor são eles: quantidade na fila, sua identificação e sição relativa</li>
  <li>2. Altera o a quantidade de carros na fila de forma randomica</li>
</ul>
 
# Utilização do projeto
O ip de conexão das aplicações medidor e cliente estão setados para máquina 3 do laboratório de IP: 172.16.103.3, visto que a execução foi feita tendo o servidor na máquina 3 e as demais aplicações da maquina 4 a 6, se quiser alterar a maquina do servidor, basta alterar manualmente o ip setados nas aplicações.
 
Segue a descrição para a alteração manual:
<li>Passo 1 - Deverá ir nessa classe MI-ConcorrenciaConectividade-Problema-1/src/client/Client.java no método "main" e apenas alterar o IP de 172.16.103.3   para a sua preferência e depois gerar o .jar e a imagem, ou se preferir executar localmente basta alterar o ip para localhost e executar na IDE.
<li>Passo 2 - Deverá ir nessa classe MI-ConcorrenciaConectividade-Problema-1/src/measurer/MeasurerClient.java no método "execMeasurer" e  no metodo "startMeasurer" e alterar o IP de 172.16.103.3 para a sua preferência e depois gerar o .jar e a imagem, ou se preferir executar localmente basta alterar o ip  para localhost e executar na IDE.
<li>Obs: Mas se já quiser testar direto na maquina 3 do larsid basta pegar as imagens que estão no dockerhub através desse link https://hub.docker.com/u/brcz1n ou então executar os docker-compose que estão no projeto na aba stack do portainer
<li> Obs2: Os usuários de testes podem ter suas senhas mostrada na classe de serviço para usuarios e o login é o id do mesmo que é sequencial na mesma ordem que estão setados.
<h2> Exemplo de requisição para um usuário :</h2>
   <li>Para o usuário de id:0 e senha:Test1 para a maquina 3 do larsid no insomnia:</p2>
   <li> 172.16.103.3:8000/user/statusConsumption/0 - Pega o alerta de consumo para o usuário de id = 0;
   <li> 172.16.103.3:8000/consumption/historic/0 - Pega o histórico de consumo para o usuário de id = 0;
   <li> 172.16.103.3:8000/invoice/newInvoice/0 - Gera uma nova fatura para o usuário de id = 0;
   <li> 172.16.103.3:8000/invoice/0 - Pega a fatura que possui possui id = 0;
   <li> 172.16.103.3:8000/invoice/all/0 - Pega todas as faturas para o usuario de id:0.
 
 # Considerações finais 
<p2> &emsp; O projeto consegue realizar tudo dentro das obrigações mínimas. Na implementação surgiu desafios com relação à compreensão da API Rest, o mesmo também possibilitou aprofundamento em questões relacionadas no que diz respeito ao que é uma API Restful e a tipos de conexões(TCP e UDP). Em versões posteriores, poderiam ser adicionado um melhoramento entre as trocas de dados das conexões. Apesar disso, tem-se que o estado atual é apenas de um protótipo simples e que não deve refletir em um possível produto
