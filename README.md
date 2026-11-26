# 📚 Biblioteca Pessoal

Aplicativo Android nativo para organizar a sua coleção de livros, registrar o que você já leu e acompanhar o seu progresso de leitura. Funciona offline e os dados ficam só no seu celular.

![Plataforma](https://img.shields.io/badge/plataforma-Android-3DDC84?logo=android&logoColor=white)
![Linguagem](https://img.shields.io/badge/linguagem-Java%2017-ED8B00?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-24%20(Android%207.0)-1F5C5A)
![UI](https://img.shields.io/badge/UI-Material%203-B8741A)
![Banco](https://img.shields.io/badge/banco-SQLite-003B57?logo=sqlite&logoColor=white)

---

## ✨ Funcionalidades

- **CRUD completo de livros:** cadastrar, listar, editar e excluir.
- **Dados de cada livro:** título, autor, ano de publicação, nota pessoal de 0 a 10 (opcional) e status lido/não lido.
- **Validação de formulário:** campos obrigatórios, ano entre 1450 e o ano atual, nota entre 0 e 10. Todos os erros aparecem de uma só vez.
- **Deslizar para excluir com "Desfazer":** o livro some com um gesto e pode voltar com um toque. O toque longo com confirmação também continua disponível.
- **Painel de progresso:** anel animado com o percentual de livros lidos, além dos totais de livros lidos e não lidos.
- **Modo escuro automático:** o app acompanha o tema do sistema.
- **Estado vazio guiado:** na primeira abertura, o app convida a cadastrar o primeiro livro.
- **100% offline e privado:** sem login, sem internet, sem coleta de dados.

## 📱 Telas

| Tela | O que faz |
|---|---|
| **Meus Livros** | Lista em ordem alfabética, com cartões que mostram título, autor, ano, nota e uma faixa colorida de status (verde = lido, terracota = não lido). Toque para editar; deslize ou segure para excluir. |
| **Cadastrar / Editar** | A mesma tela serve para os dois modos. Em edição, o formulário já vem preenchido e a barra superior ganha uma seta de voltar. |
| **Resumo** | Anel de progresso com o % de livros lidos e cartões com total, lidos e não lidos. |

A navegação entre as telas é feita por um menu inferior fixo (`BottomNavigationView`).

## 🛠️ Tecnologias

- **Java 17** e **Android SDK 34** (minSdk 24)
- **AndroidX:** AppCompat, ConstraintLayout, RecyclerView, Activity (EdgeToEdge, OnBackPressedDispatcher)
- **Material Components 1.12** com tema **Material 3 DayNight**
- **SQLite** nativo, via `SQLiteOpenHelper`
- **Gradle** (Android Gradle Plugin 8.5)

## 🏛️ Arquitetura

O projeto é organizado em camadas com responsabilidades bem separadas:

```
app/src/main/java/com/example/bibliotecapessoal/
├── model/          Livro.java              → entidade (POJO Serializable)
├── persistencia/   ILivroDAO.java          → contrato do acesso a dados
│                   LivroDAO.java           → implementação com SQLite
│                   DbHelper.java           → criação e migração do banco
├── adapter/        AdapterLivro.java       → liga a lista de livros à RecyclerView
├── view/           ListaLivrosActivity     → tela "Meus Livros" (inicial)
│                   CadastroLivroActivity   → cadastro e edição
│                   ResumoActivity          → painel de progresso
└── util/           NavegacaoMenu.java      → navegação centralizada do menu inferior
```

**Decisões de projeto**

- **Padrão DAO com interface:** as telas dependem de `ILivroDAO`, e não da implementação. Dá para trocar o SQLite por Room, uma API ou sincronização em nuvem sem reescrever as telas.
- **Consultas parametrizadas** (`WHERE id = ?`) em todas as operações, contra SQL Injection.
- **Contagens direto no banco** (`SELECT COUNT(*)`) na tela de Resumo, sem carregar todos os registros.
- **Migração de banco versionada:** `onUpgrade` adiciona a coluna `nota` sem apagar os livros de quem já tinha o app.
- **ViewHolder + callbacks:** o Adapter só avisa a tela sobre cliques (interface `OnLivroClickListener`) e não toma decisões de negócio.
- **Design system em recursos:** cores (`colors.xml` e `values-night/colors.xml`), medidas (`dimens.xml`), estilos e tipografia (`themes.xml`) e textos (`strings.xml`) centralizados. Nenhum valor visual ou texto fica "fixo" no código.
- **Código comentado** em todas as classes e layouts, explicando o *porquê* de cada trecho.

## 🗄️ Banco de dados

Tabela `livros` (arquivo `biblioteca.db`, versão 2):

| Coluna | Tipo | Regra |
|---|---|---|
| `id` | INTEGER | chave primária, autoincremento |
| `titulo` | TEXT | obrigatório |
| `autor` | TEXT | obrigatório |
| `ano` | INTEGER | obrigatório |
| `lido` | INTEGER | obrigatório (1 = lido, 0 = não lido) |
| `nota` | INTEGER | opcional (NULL = sem nota) |

## 🚀 Como executar

**Pré-requisitos:** Android Studio (Koala ou mais recente) com JDK 17 e Android SDK 34.

1. Clone o repositório:
   ```bash
   git clone https://github.com/Alexandremva/BibliotecaPessoal.git
   ```
2. Abra a pasta no Android Studio (**File → Open**) e aguarde a sincronização do Gradle.
3. Conecte um celular com Android 7.0 ou superior (ou crie um emulador) e clique em **Run ▶**.

## 🗺️ Próximos passos

- [ ] Busca por título e autor, com filtros por status
- [ ] Cadastro rápido por leitura do código de barras (ISBN)
- [ ] Capas dos livros
- [ ] Metas anuais de leitura
- [ ] Backup e sincronização em nuvem
- [ ] Testes automatizados (unitários para o DAO e instrumentados para as telas)

## 📄 Apresentação do produto

Uma apresentação comercial completa do app (propósito, guia das telas e pitch) está em [`APRESENTACAO.md`](APRESENTACAO.md), também disponível em [`APRESENTACAO.docx`](APRESENTACAO.docx).

## 👤 Autor

Desenvolvido por **[@Alexandremva](https://github.com/Alexandremva)**.

Gostou do projeto? Deixe uma ⭐ no repositório!
