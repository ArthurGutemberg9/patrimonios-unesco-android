package com.arthur.patrimoniosunesco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.arthur.patrimoniosunesco.ui.theme.PatrimoniosUNESCTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PatrimoniosUNESCTheme { App() } }
    }
}

data class HeritageSite(
    val id: String = "",
    val name: String = "",
    val country: String = "",
    val category: String = "",
    val year: Int = 0,
    val description: String = "",
    val imageUrl: String = ""
)

class HeritageRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val fallback = listOf(
        HeritageSite("1", "Centro Histórico de Ouro Preto", "Brasil", "Cultural", 1980, "Cidade histórica mineira marcada pela arquitetura barroca, pelas ladeiras e pela memória do ciclo do ouro.", "https://images.unsplash.com/photo-1544986581-efac024faf62?w=900"),
        HeritageSite("2", "Parque Nacional de Machu Picchu", "Peru", "Cultural e natural", 1983, "Santuário histórico andino que reúne paisagem de montanha, engenharia inca e uma das experiências culturais mais conhecidas do mundo.", "https://images.unsplash.com/photo-1587595431973-160d0d94add1?w=900"),
        HeritageSite("3", "Acrópole de Atenas", "Grécia", "Cultural", 1987, "Conjunto monumental que representa a influência da civilização grega na arte, na arquitetura e na história ocidental.", "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=900"),
        HeritageSite("4", "Cidade Antiga de Jerusalém", "Jerusalém", "Cultural", 1981, "Lugar de encontro entre diferentes tradições religiosas, com uma história milenar preservada em seus espaços e monumentos.", "https://images.unsplash.com/photo-1548013146-72479768bada?w=900"),
        HeritageSite("5", "Ilhas Galápagos", "Equador", "Natural", 1978, "Arquipélago de biodiversidade extraordinária, conhecido por suas espécies endêmicas e por sua importância para a ciência.", "https://images.unsplash.com/photo-1516690561799-46d8f74f9abf?w=900")
    )

    fun observeSites(onResult: (List<HeritageSite>) -> Unit) {
        firestore.collection("sites").get().addOnSuccessListener { snapshot ->
            val sites = snapshot.documents.mapNotNull { it.toObject(HeritageSite::class.java)?.copy(id = it.id) }
            onResult(if (sites.isEmpty()) fallback else sites)
        }.addOnFailureListener { onResult(fallback) }
    }

    fun signIn(email: String, password: String, onResult: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(if (task.isSuccessful) null else task.exception?.localizedMessage ?: "Não foi possível entrar")
        }
    }

    fun signUp(email: String, password: String, onResult: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(if (task.isSuccessful) null else task.exception?.localizedMessage ?: "Não foi possível criar a conta")
        }
    }

    fun signOut() = auth.signOut()
    fun isLoggedIn() = auth.currentUser != null
}

class HeritageViewModel : ViewModel() {
    private val repository = HeritageRepository()
    var sites by mutableStateOf<List<HeritageSite>>(emptyList())
    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)
    var favorites by mutableStateOf<Set<String>>(emptySet())

    init { loadSites() }

    fun loadSites() {
        loading = true
        repository.observeSites { result -> sites = result; loading = false }
    }

    fun toggleFavorite(id: String) { favorites = if (id in favorites) favorites - id else favorites + id }
    fun login(email: String, password: String, onDone: (Boolean, String?) -> Unit) = repository.signIn(email, password) { onDone(it == null, it) }
    fun register(email: String, password: String, onDone: (Boolean, String?) -> Unit) = repository.signUp(email, password) { onDone(it == null, it) }
    fun logout() = repository.signOut()
    fun loggedIn() = repository.isLoggedIn()
}

@Composable
fun App(viewModel: HeritageViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if (viewModel.loggedIn()) "home" else "auth") {
        composable("auth") { AuthScreen(viewModel) { navController.navigate("home") { popUpTo("auth") { inclusive = true } } } }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("detail/{siteId}") { entry ->
            val site = viewModel.sites.firstOrNull { it.id == entry.arguments?.getString("siteId") }
            if (site != null) DetailScreen(navController, site, viewModel)
        }
    }
}

@Composable
fun AuthScreen(viewModel: HeritageViewModel, onSuccess: () -> Unit) {
    var register by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(28.dp), verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Explore, null, Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(18.dp))
        Text("Patrimônios UNESCO", style = MaterialTheme.typography.headlineLarge)
        Text("Descubra histórias que pertencem ao mundo.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(32.dp))
        Text(if (register) "Crie sua conta" else "Bem-vindo de volta", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("E-mail") }, singleLine = true)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        message?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 10.dp)) }
        Spacer(Modifier.height(18.dp))
        Button(onClick = { busy = true; if (register) viewModel.register(email, password) { ok, error -> busy = false; message = error; if (ok) onSuccess() } else viewModel.login(email, password) { ok, error -> busy = false; message = error; if (ok) onSuccess() } }, Modifier.fillMaxWidth(), enabled = !busy && email.isNotBlank() && password.length >= 6) { if (busy) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White) else Text(if (register) "Criar conta" else "Entrar") }
        TextButtonLike(if (register) "Já tenho uma conta" else "Ainda não tenho cadastro") { register = !register; message = null }
    }
}

@Composable
fun TextButtonLike(text: String, onClick: () -> Unit) { Text(text, Modifier.padding(top = 18.dp).clickable { onClick() }, color = MaterialTheme.colorScheme.primary) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HeritageViewModel) {
    var query by remember { mutableStateOf("") }
    var tab by remember { mutableStateOf(0) }
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filtered = viewModel.sites.filter { it.name.contains(query, true) || it.country.contains(query, true) || it.category.contains(query, true) }.let { if (tab == 1) it.filter { site -> site.id in viewModel.favorites } else it }
    Scaffold(topBar = { TopAppBar(title = { Text(if (tab == 0) "Explorar patrimônios" else "Meus favoritos") }, actions = { IconButton({ viewModel.logout(); navController.navigate("auth") { popUpTo(0) } }) { Icon(Icons.Default.Logout, "Sair") } }) }, bottomBar = { NavigationBar { NavigationBarItem(tab == 0, { tab = 0 }, { Icon(Icons.Default.Explore, "Explorar") }, label = { Text("Explorar") }); NavigationBarItem(tab == 1, { tab = 1 }, { Icon(Icons.Default.Bookmark, "Favoritos") }, label = { Text("Favoritos") }) } }, snackbarHost = { SnackbarHost(snack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp)) {
            OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth().padding(top = 12.dp), placeholder = { Text("Busque por nome ou país") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
            Spacer(Modifier.height(16.dp))
            if (viewModel.loading) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } else LazyColumn(contentPadding = PaddingValues(bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { items(filtered) { site -> SiteCard(site, site.id in viewModel.favorites) { viewModel.toggleFavorite(site.id); scope.launch { snack.showSnackbar(if (site.id in viewModel.favorites) "Adicionado aos favoritos" else "Removido dos favoritos") }; navController.navigate("detail/${site.id}") } } }
        }
    }
}

@Composable
fun SiteCard(site: HeritageSite, favorite: Boolean, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column {
            AsyncImage(site.imageUrl, site.name, Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)), contentScale = ContentScale.Crop)
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(site.name, style = MaterialTheme.typography.titleLarge); Text("${site.country} · ${site.category}", color = MaterialTheme.colorScheme.secondary) }; Icon(if (favorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null, tint = MaterialTheme.colorScheme.primary) }
                Spacer(Modifier.height(8.dp)); Text(site.description, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, site: HeritageSite, viewModel: HeritageViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Detalhes") }, navigationIcon = { IconButton({ navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Voltar") } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) { item { AsyncImage(site.imageUrl, site.name, Modifier.fillMaxWidth().height(250.dp), contentScale = ContentScale.Crop); Column(Modifier.padding(22.dp)) { Text(site.name, style = MaterialTheme.typography.headlineLarge); Spacer(Modifier.height(8.dp)); Text("${site.country} · ${site.category} · inscrito em ${site.year}", color = MaterialTheme.colorScheme.secondary); Spacer(Modifier.height(22.dp)); Text(site.description, style = MaterialTheme.typography.bodyLarge); Spacer(Modifier.height(24.dp)); OutlinedButton({ viewModel.toggleFavorite(site.id) }, Modifier.fillMaxWidth()) { Icon(if (site.id in viewModel.favorites) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null); Spacer(Modifier.width(8.dp)); Text(if (site.id in viewModel.favorites) "Remover dos favoritos" else "Salvar nos favoritos") }; Spacer(Modifier.height(22.dp)); Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Visibility, null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(8.dp)); Text("Conheça, respeite e preserve este patrimônio.") } } } }
    }
}
