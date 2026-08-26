# Patrimônios UNESCO

O **Patrimônios UNESCO** é um aplicativo Android criado para ajudar a conhecer lugares históricos, culturais e naturais reconhecidos pela UNESCO.

A proposta é simples: entrar, explorar o catálogo, pesquisar por nome ou categoria e salvar os patrimônios favoritos. Cada local apresenta uma imagem, país, tipo, ano de reconhecimento e uma breve descrição.

## Telas do aplicativo

### Login

![Tela de login](assets/tela-login.png)

### Catálogo

![Catálogo de patrimônios](assets/tela-catalogo.png)

### Detalhes e favoritos

![Detalhes do patrimônio](assets/tela-detalhes.png)

## Tecnologias

O projeto foi desenvolvido como um aplicativo Android nativo em **Kotlin**, com a interface feita integralmente em **Jetpack Compose** e Material 3. A autenticação usa Firebase Authentication e os dados do catálogo ficam preparados para o Cloud Firestore.

## Como executar

Abra o projeto no Android Studio. Para ativar o Firebase, crie um projeto no Firebase, registre o aplicativo com o identificador `com.arthur.patrimoniosunesco`, ative o login por e-mail e senha, baixe o arquivo `google-services.json` e coloque-o dentro da pasta `app/`.

Depois, sincronize o Gradle e execute o aplicativo em um emulador ou dispositivo Android.

## Autor

Arthur Gutemberg Costa

[Repositório no GitHub](https://github.com/ArthurGutemberg9/patrimonios-unesco-android)

[Documentação do Jetpack Compose](https://developer.android.com/jetpack/compose)

[Documentação do Firebase Authentication](https://firebase.google.com/docs/auth)

[Documentação do Cloud Firestore](https://firebase.google.com/docs/firestore)

[UNESCO World Heritage](https://whc.unesco.org/)

> As imagens deste README são mockups visuais das telas propostas para o aplicativo. Os arquivos do projeto continuam disponíveis na pasta `app/`.
