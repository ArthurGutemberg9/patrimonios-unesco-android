# Patrimônios UNESCO

O Patrimônios UNESCO é um aplicativo Android criado para apresentar, de forma simples e visual, lugares históricos, culturais e naturais reconhecidos pela UNESCO. A ideia é transformar a pesquisa sobre patrimônio mundial em uma experiência de descoberta: a pessoa entra, explora os locais, consulta informações básicas e pode salvar os destinos que mais despertarem interesse.

A proposta foi pensada para unir tecnologia e educação patrimonial. Em vez de ser apenas uma lista, o aplicativo organiza cada local por país, categoria, ano de reconhecimento e uma breve descrição. Assim, o usuário consegue conhecer diferentes histórias e perceber por que esses espaços precisam ser preservados.

## Funcionalidades

A aplicação possui telas de cadastro e login usando Firebase Authentication. Depois de entrar, o usuário encontra um catálogo com busca por nome, país ou categoria, cards com imagens dos locais, uma tela de detalhes e uma área de favoritos. O catálogo é lido do Cloud Firestore e conta com dados demonstrativos para que a navegação continue funcionando durante a configuração inicial do projeto Firebase.

## Tecnologias

O projeto foi desenvolvido como um aplicativo Android nativo em Kotlin. A interface foi construída integralmente com Jetpack Compose e Material 3. A navegação utiliza Navigation Compose, as imagens são carregadas com Coil e a camada de dados utiliza Firebase Authentication e Cloud Firestore.

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Kotlin |
| Interface | Jetpack Compose + Material 3 |
| Autenticação | Firebase Authentication |
| Backend | Cloud Firestore |
| Navegação | Navigation Compose |
| Imagens | Coil |
| IDE recomendada | Android Studio Ladybug ou superior |

## Como executar

Abra a pasta do projeto no Android Studio e aguarde a sincronização do Gradle. No console do Firebase, crie um projeto, registre um aplicativo Android com o identificador `com.arthur.patrimoniosunesco` e faça o download do arquivo `google-services.json`. Coloque esse arquivo dentro da pasta `app/` do projeto.

No Firebase Authentication, ative o provedor de e-mail e senha. Em seguida, crie o Cloud Firestore em modo de teste durante o desenvolvimento. Para utilizar dados próprios, crie uma coleção chamada `sites`; cada documento pode conter os campos `name`, `country`, `category`, `year`, `description` e `imageUrl`.

Depois disso, basta executar o aplicativo em um emulador ou dispositivo Android com Android 8.0 ou superior. O arquivo `google-services.json` não deve ser publicado caso contenha configurações específicas do projeto; por isso, ele deve ser adicionado localmente pelo responsável pelo Firebase.

## Organização do projeto

A tela principal está em `MainActivity.kt`, reunindo o fluxo de autenticação, catálogo, favoritos e detalhes. A identidade visual está separada em `ui/theme`, enquanto as configurações de compilação ficam no módulo `app`.

## Objetivo acadêmico

Este projeto atende à proposta de criar uma aplicação Android desde o início, usando uma interface moderna com Jetpack Compose, autenticação em um serviço real e comunicação com um backend para disponibilizar os dados do catálogo. O tema Patrimônios UNESCO foi escolhido por permitir uma aplicação útil, visual e relacionada à valorização da memória cultural e natural do mundo.
