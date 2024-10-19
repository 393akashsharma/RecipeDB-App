package com.astecnology.recipe_project

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.astecnology.recipe_project.ui.theme.Recipe_ProjectTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.astecnology.recipe_project.Data.Category
import com.astecnology.recipe_project.Data.Meal
import com.astecnology.recipe_project.Data.MealDetail


class MainActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Recipe_ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            CategoryDetailScreen(categoryName = categoryName ?: "", navController)
        }
        composable("ingredient/{idMeal}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("idMeal")
            MealDetailScreen(mealId = mealId ?: "")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Food Recipe",
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "make your own food at home",
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }


                },

            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(65.dp))
                SearchBar()
                Text(
                    text = "Categories",
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                CategoriesList(navController)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Color.LightGray, // Directly specifying the background color
                shape = RoundedCornerShape(15.dp)
            )
            .clickable { /* Handle click event here */ },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp),
                tint = Color.Black
            )
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {
                    Text(
                        text = "Search any recipe",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}


@Composable
fun CategoriesList(navController: NavController,viewModel: MainViewModel = viewModel()) {
    viewModel.fetchCategories()
    val categoriesState by viewModel.categories.observeAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(categoriesState) { category ->
            CategoryCard(category = category , navController = navController)
        }
    }
}



@Composable
fun CategoryCard(
    modifier: Modifier = Modifier, category: Category, navController: NavController
) {
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(110.dp)
            .clickable {
                navController.navigate("category/${category.strCategory}")
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box {

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = ImageRequest.Builder(context).data(category.strCategoryThumb).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x4D000000))
                    .align(Alignment.BottomCenter)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = category.strCategory,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CategoryDetailScreen(categoryName: String, navController: NavController, viewModel: MainViewModel = viewModel()) {
    viewModel.fetchFilteredMeals(categoryName)
    val meals by viewModel.meals.observeAsState(initial = emptyList())
    Column {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 15.dp)
                .height(70.dp) // Increased height for better visibility
                .clickable {
                    // Navigate to meal details if needed
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.Blue,
            ),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(8.dp), // Slightly larger corner radius for a smoother look
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), // Fill the card space
                contentAlignment = Alignment.Center // Center the content inside the box
            ) {
                Text(
                    text = categoryName+" Menu List",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(meals) { meal ->
                MealCard(meal = meal, navController = navController)
            }
        }
    }

}

@Composable
fun MealCard(
    modifier: Modifier = Modifier
    , meal: Meal, navController: NavController
) {
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(180.dp) // Increased height for better visibility
            .clickable {
                navController.navigate("ingredient/${meal.idMeal}")
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(8.dp), // Slightly larger corner radius for a smoother look
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Set height for the image to leave space for the text
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)), // Clip to match the card's shape
                model = ImageRequest.Builder(context).data(meal.strMealThumb).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = meal.strMeal,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(mealId: String, viewModel: MainViewModel = viewModel()) {
    LaunchedEffect(mealId) {
        viewModel.fetchMealDetail(mealId)
    }
    val mealDetail by viewModel.mealDetail.observeAsState()

    mealDetail?.let { meal ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = meal.strMeal,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${meal.strCategory} | ${meal.strArea ?: ""}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle back navigation */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            },
            content = {
                RecipeDetailContent(meal)
            }
        )
    }
}

@Composable
fun RecipeDetailContent(mealDetail: MealDetail) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.White)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mealDetail.strMealThumb)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .align(Alignment.CenterEnd)
                    .border(2.dp, Color.White, CircleShape)
            )

            Card(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(start = 20.dp, top = 150.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        // Action buttons (Watch on YouTube, Share Recipe, Source)
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            mealDetail.strYoutube?.let { youtubeUrl ->
                ActionButton(
                    text = "Watch On YouTube",
                    icon = R.drawable.ic_play,
                    backgroundColor = Color(0xFFFFE1E1),
                    textColor = Color(0xFFEC2D2D),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            ActionButton(
                text = "Share Recipe",
                icon = R.drawable.ic_share,
                backgroundColor = Color(0xFFFFDA9C),
                textColor = Color(0xFFA66B08),
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "${mealDetail.strMeal}\n\n${mealDetail.strInstructions}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Recipe via"))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            mealDetail.strSource?.let { sourceUrl ->
                ActionButton(
                    text = "Source",
                    icon = R.drawable.ic_link,
                    backgroundColor = Color(0xFFC1F8CE),
                    textColor = Color(0xFF2A7E2F),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Ingredients and instructions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFA000))
                .padding(16.dp)


        ) {
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val ingredients = listOf(
                mealDetail.strIngredient1,
                mealDetail.strIngredient2,
                mealDetail.strIngredient3,
                mealDetail.strIngredient4,
                mealDetail.strIngredient5,
                mealDetail.strIngredient6,
                mealDetail.strIngredient7,
                mealDetail.strIngredient8,
                mealDetail.strIngredient9,
                mealDetail.strIngredient10,
                mealDetail.strIngredient11,
                mealDetail.strIngredient12,
                mealDetail.strIngredient13,
                mealDetail.strIngredient14,
                mealDetail.strIngredient15,
                mealDetail.strIngredient16,
                mealDetail.strIngredient17,
                mealDetail.strIngredient18,
                mealDetail.strIngredient19,
                mealDetail.strIngredient20
            ).filterNotNull().filter { it.isNotBlank() }

            val measures = listOf(
                mealDetail.strMeasure1,
                mealDetail.strMeasure2,
                mealDetail.strMeasure3,
                mealDetail.strMeasure4,
                mealDetail.strMeasure5,
                mealDetail.strMeasure6,
                mealDetail.strMeasure7,
                mealDetail.strMeasure8,
                mealDetail.strMeasure9,
                mealDetail.strMeasure10,
                mealDetail.strMeasure11,
                mealDetail.strMeasure12,
                mealDetail.strMeasure13,
                mealDetail.strMeasure14,
                mealDetail.strMeasure15,
                mealDetail.strMeasure16,
                mealDetail.strMeasure17,
                mealDetail.strMeasure18,
                mealDetail.strMeasure19,
                mealDetail.strMeasure20
            ).filterNotNull().filter { it.isNotBlank() }

            ingredients.zip(measures).forEach { (ingredient, measure) ->
                Text(
                    text = "$ingredient: $measure",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = mealDetail.strInstructions,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun ActionButton(text: String,
                 icon: Int, backgroundColor: Color,
                 textColor: Color,
                 onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)

        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = textColor,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Recipe_ProjectTheme {
        MainScreen()
    }
}