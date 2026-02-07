package com.codejudge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.codejudge.app.data.api.ApiClient
import com.codejudge.app.data.api.getBaseUrl
import com.codejudge.app.data.model.Problem
import com.codejudge.app.ui.theme.AppColors
import kotlinx.coroutines.launch

class ProblemListScreen : Screen {
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val apiClient = remember { ApiClient(getBaseUrl()) }
        
        var problems by remember { mutableStateOf<List<Problem>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(Unit) {
            try {
                problems = apiClient.getProblems()
                isLoading = false
            } catch (e: Exception) {
                error = e.message ?: "Failed to load problems"
                isLoading = false
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Background)
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "⚡ CodeJudge",
                color = AppColors.Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Stats Row
            BoxWithConstraints {
                val isMobile = maxWidth < 600.dp
                if (isMobile) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard("Total Problems", problems.size.toString(), AppColors.Primary, Modifier.fillMaxWidth())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard("Easy", problems.count { it.difficulty == "Easy" }.toString(), AppColors.EasyBadge, Modifier.weight(1f))
                            StatCard("Medium", problems.count { it.difficulty == "Medium" }.toString(), AppColors.MediumBadge, Modifier.weight(1f))
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard("Total Problems", problems.size.toString(), AppColors.Primary, Modifier.weight(1f))
                        StatCard("Easy", problems.count { it.difficulty == "Easy" }.toString(), AppColors.EasyBadge, Modifier.weight(1f))
                        StatCard("Medium", problems.count { it.difficulty == "Medium" }.toString(), AppColors.MediumBadge, Modifier.weight(1f))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Problem Set",
                color = AppColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.Primary)
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error!!, color = AppColors.Error)
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(problems) { problem ->
                            ProblemCard(
                                problem = problem,
                                index = problems.indexOf(problem) + 1,
                                onClick = { navigator.push(ProblemDetailScreen(problem.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.Surface)
            .border(1.dp, AppColors.CardBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = value,
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = AppColors.TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProblemCard(problem: Problem, index: Int, onClick: () -> Unit) {
    val difficultyColor = when (problem.difficulty) {
        "Easy" -> AppColors.EasyBadge
        "Medium" -> AppColors.MediumBadge
        else -> AppColors.HardBadge
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.Surface)
            .border(1.dp, AppColors.CardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#$index",
                    color = AppColors.TextMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.width(40.dp)
                )
                Column {
                    Text(
                        text = problem.title,
                        color = AppColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Time: ${problem.timeLimitMs}ms • Memory: ${problem.memoryLimitKb}KB",
                        color = AppColors.TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(difficultyColor.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = problem.difficulty.uppercase(),
                    color = difficultyColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
