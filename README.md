📱 GitHub Explorer

Aplicativo Android desenvolvido em Kotlin utilizando Jetpack Compose que consome a API pública do GitHub para listar repositórios de usuários, exibir detalhes e permitir o gerenciamento de favoritos com suporte offline.

O projeto foi estruturado seguindo a arquitetura MVVM, com separação clara de responsabilidades entre UI, ViewModel, Repository e camadas de dados. A aplicação utiliza Room como fonte única de verdade (Single Source of Truth), garantindo funcionamento mesmo sem conexão com a internet, enquanto a sincronização com a API é feita via Retrofit.

A injeção de dependência é realizada com Hilt, promovendo desacoplamento e facilitando a manutenção e testabilidade do código. Além disso, o app implementa tratamento completo de estados de interface (loading, erro e vazio), atualização reativa com Flow/StateFlow e persistência local de dados.

🚀 Principais funcionalidades
🔍 Busca de repositórios por usuário do GitHub
📄 Visualização de detalhes dos repositórios
⭐ Sistema de favoritos com persistência local
📶 Suporte offline com cache via Room
🔄 Sincronização automática com a API
⚠️ Tratamento de erros (rede, servidor, usuário inexistente)
