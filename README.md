## Sobre o projeto

O **Fiscaliza-e** permite que cidadãos registrem reclamações relacionadas a serviços públicos — iluminação, pavimentação, saneamento, transporte coletivo, espaços públicos, órgãos públicos e trânsito — e acompanhem o andamento de cada solicitação diretamente pelo celular.

Desenvolvido como trabalho acadêmico da disciplina de **Arquitetura de Software — IFAL, campus Arapiraca**.

**Equipe:**
| Integrante | 
|---|
| Alice Julia | 
| Divaldo Verçosa | 
| Emanuel Vilela | 
| Ludmila Barbosa | 
| Vitoria Cabral |

---

## Funcionalidades

- **Autenticação** — cadastro e login de cidadãos com validação de CPF e hash de senha (SHA-256)
- **Reclamações** — registro com título, descrição, categoria, endereço e até 3 fotos
- **Geolocalização** — preenchimento automático do endereço via GPS (Nominatim + fallback Android Geocoder)
- **Busca por CEP** — autopreenchimento de endereço via ViaCEP
- **Detalhe da reclamação** — visualização completa com mapa (OpenStreetMap), galeria de fotos e timeline de status
- **Lista com filtros** — filtragem e ordenação por status, categoria e data
- **Perfil** — edição de nome e endereço, upload de foto da galeria e exclusão de conta
- **FAQ** — perguntas frequentes com busca por palavra-chave
- **Funcionamento offline** — dados armazenados localmente via Room; sincronização com API quando disponível

---

## Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)** com arquitetura em camadas, conforme documentado na Etapa 1:

```
View (Jetpack Compose)
       │ eventos / ações
       ▼
ViewModel (StateFlow / UiState selado)
       │ chamadas de repositório
       ▼
Repository (Single Source of Truth)
       │                    │
       ▼                    ▼
API remota (Retrofit)   Banco local (Room / SQLite)
```

**Principais tecnologias:**

| Camada | Tecnologia |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose (com animações de transição) |
| Assincronismo | Kotlin Coroutines + StateFlow |
| Banco local | Room (SQLite) |
| HTTP / APIs | Retrofit 2 + Gson |
| Mapas | OSMDroid (OpenStreetMap) |
| Geocodificação | Nominatim API + Android Geocoder |
| Imagens | Coil |
| Sessão | SharedPreferences |

---

## Pré-requisitos

- **Android Studio** Hedgehog (2023.1.1) ou superior
- **JDK 11**
- **Android SDK** — mínimo API 24 (Android 7.0), alvo API 36

---

## Como rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/fiscaliza-e/FiscalizaApp.git
cd FiscalizaApp
```

### 2. Abrir no Android Studio

1. **File → Open** e selecione a pasta `FiscalizaApp`
2. Aguarde o Gradle sincronizar as dependências (pode levar alguns minutos na primeira vez)

### 3. Executar

**Emulador:**
1. Abra o **Device Manager** (`View → Tool Windows → Device Manager`)
2. Crie um dispositivo virtual — recomendado: **Pixel 6, API 34**
3. Clique em ▶ **Run** ou `Shift + F10`

**Dispositivo físico:**
1. Ative as **Opções do desenvolvedor** no Android (`Configurações → Sobre o dispositivo → toque 7x em "Número da versão"`)
2. Habilite a **Depuração USB**
3. Conecte o cabo, autorize o acesso e clique em ▶ **Run**

---

## Conta de teste

Um usuário é pré-carregado automaticamente no banco local. Use as credenciais abaixo para entrar sem precisar se cadastrar:

| Campo | Valor |
|---|---|
| E-mail | `teste@gmail.com` |
| Senha | `123456` |

Ou crie uma nova conta pela tela de cadastro. O CEP deve começar com `573` (Arapiraca — AL).

---

## Permissões utilizadas

| Permissão | Finalidade |
|---|---|
| `INTERNET` | Comunicação com APIs (ViaCEP, Nominatim, API fake) |
| `ACCESS_FINE_LOCATION` | GPS para autopreenchimento do endereço da reclamação |
| `ACCESS_COARSE_LOCATION` | Localização aproximada como fallback do GPS |
| `READ_MEDIA_IMAGES` (API 33+) | Seleção de foto de perfil da galeria |
| `READ_EXTERNAL_STORAGE` (API < 33) | Seleção de foto de perfil em versões antigas |
| `ACCESS_NETWORK_STATE` | Verificação de conectividade |

---

## Estrutura de diretórios

```
app/src/main/java/br/edu/ifal/fiscalizaapp/
│
├── composables/          # Componentes reutilizáveis de UI
│   ├── accordion/        # Acordeão para FAQ
│   ├── button/           # Botão com variantes (Primary, Danger…)
│   ├── card/             # Cards e itens de categoria
│   ├── header/           # AppHeader (tela principal e tela interna)
│   ├── input/            # Input com máscara, validação e tipos
│   ├── protocolcard/     # Card de reclamação com status colorido
│   ├── protocollist/     # Lista de reclamações com estado vazio
│   ├── searchfilter/     # Barra de filtros
│   └── statustag/        # Tag de status (Pendente, Visualizada…)
│
├── data/
│   ├── api/              # Interfaces Retrofit, DTOs e helpers
│   │   ├── cep/          # API ViaCEP
│   │   └── nominatim/    # API Nominatim (geocodificação reversa)
│   ├── db/               # Room: entities, DAOs, dados iniciais
│   ├── repository/       # Repositories (Single Source of Truth)
│   └── session/          # SessionManager (SharedPreferences)
│
├── model/                # Modelos de domínio
├── navigation/
│   ├── routes/           # AppNavHost e funções de rota por tela
│   └── screens/          # Telas do aplicativo
│       ├── categories/   # Lista de categorias
│       ├── faq/          # FAQ com busca
│       ├── home/         # Tela inicial
│       ├── login/        # Login
│       ├── profile/      # Perfil, edição e escolha de avatar
│       ├── protocols/    # Lista, nova reclamação e detalhe
│       ├── register/     # Cadastro
│       └── welcome/      # Boas-vindas
│
├── ui/
│   ├── state/            # UiState selado (Loading, Success, Error)
│   ├── theme/            # Cores, tipografia e tema Material 3
│   └── viewmodels/       # ViewModels + ViewModelFactory
│
└── utils/
    └── LocationHelper.kt # Geocodificação via Nominatim e Geocoder
```
