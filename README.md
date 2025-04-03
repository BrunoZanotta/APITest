## APITest

Este projeto é uma suíte de testes automatizados para APIs REST, desenvolvida em Java utilizando Maven como ferramenta de gerenciamento de dependências. Os principais frameworks e bibliotecas integrados incluem:

### 🛠️ **Tecnologias Utilizadas**

✅ **RestAssured** – Facilita a escrita e validação de testes de API.        
✅ **TestNG** – Estruturação e execução dos testes automatizados.    
✅ **Hamcrest** – Matchers flexíveis e legíveis para asserções.     
✅ **JavaFaker** – Geração de dados fictícios realistas para os testes.    
✅ **Lombok** – Redução da verbosidade do código através da geração automática de métodos comuns.   
✅ **Allure Report** – Geração de relatórios detalhados e interativos para análise dos testes.

### 🔧 **Configuração**

**Clone o repositório:**  
> git clone https://github.com/BrunoZanotta/APITest.git

**Importe o projeto em sua IDE favorita:**  
Abra o IntelliJ IDEA, Eclipse ou VS Code e importe como um projeto Maven.

**Instale as dependências do projeto:**     
> mvn clean install

### Executando os Testes

#### Executar todos os testes
> mvn test

#### Executar um teste específico
> mvn -Dtest=FunctionalDeleteBooksTest test

### Executar testes com Allure Report

#### Execute os testes com o comando
> mvn clean test

#### Gere o relatório do Allure
> mvn allure:serve


### 📊 Relatórios de Testes

#### Os relatórios de execução são gerados nos diretórios:

📂 target/surefire-reports – Relatórios padrão do TestNG  
📂 target/allure-results – Relatórios do Allure Report