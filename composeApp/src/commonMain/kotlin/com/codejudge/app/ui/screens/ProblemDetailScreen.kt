package com.codejudge.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.codejudge.app.data.api.ApiClient
import com.codejudge.app.data.api.getBaseUrl
import com.codejudge.app.data.model.Language
import com.codejudge.app.data.model.Problem
import com.codejudge.app.data.model.SubmissionRequest
import com.codejudge.app.data.model.SubmissionResponse
import com.codejudge.app.ui.theme.AppColors
import kotlinx.coroutines.launch

data class ProblemDetailScreen(val problemId: Long) : Screen {
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val apiClient = remember { ApiClient(getBaseUrl()) }
        
        var problem by remember { mutableStateOf<Problem?>(null) }
        var sourceCode by remember { mutableStateOf("# Write your solution here\n") }
        var selectedLanguage by remember { mutableStateOf(Language.PYTHON) }
        var isLoading by remember { mutableStateOf(true) }
        var isSubmitting by remember { mutableStateOf(false) }
        var submissionResult by remember { mutableStateOf<SubmissionResponse?>(null) }
        var error by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(problemId) {
            try {
                problem = apiClient.getProblem(problemId)
                isLoading = false
            } catch (e: Exception) {
                error = e.message ?: "Failed to load problem"
                isLoading = false
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(AppColors.Background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppColors.Primary)
            }
            return
        }
        
        problem?.let { prob ->
        BoxWithConstraints {
            val isMobile = maxWidth < 800.dp
            
            if (isMobile) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.Background)
                        .verticalScroll(rememberScrollState())
                ) {
                    DescriptionPanel(
                        prob = prob,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        onBack = { navigator.pop() }
                    )
                    EditorPanel(
                        problemId = problemId,
                        apiClient = apiClient,
                        sourceCode = sourceCode,
                        onSourceCodeChange = { sourceCode = it },
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { selectedLanguage = it },
                        isSubmitting = isSubmitting,
                        onSubmittingChange = { isSubmitting = it },
                        submissionResult = submissionResult,
                        onResultChange = { submissionResult = it },
                        error = error,
                        onErrorChange = { error = it },
                        modifier = Modifier.fillMaxWidth().background(AppColors.Surface).padding(16.dp),
                        scope = scope
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.Background)
                ) {
                    DescriptionPanel(
                        prob = prob,
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp).verticalScroll(rememberScrollState()),
                        onBack = { navigator.pop() }
                    )
                    EditorPanel(
                        problemId = problemId,
                        apiClient = apiClient,
                        sourceCode = sourceCode,
                        onSourceCodeChange = { sourceCode = it },
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { selectedLanguage = it },
                        isSubmitting = isSubmitting,
                        onSubmittingChange = { isSubmitting = it },
                        submissionResult = submissionResult,
                        onResultChange = { submissionResult = it },
                        error = error,
                        onErrorChange = { error = it },
                        modifier = Modifier.weight(1f).fillMaxHeight().background(AppColors.Surface).padding(16.dp).verticalScroll(rememberScrollState()),
                        scope = scope
                    )
                }
            }
        }
        }
    }
}

@Composable
private fun DescriptionPanel(
    prob: Problem,
    modifier: Modifier,
    onBack: () -> Unit
) {
    Column(modifier = modifier) {
        // Back Button
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = AppColors.TextSecondary
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = prob.title,
                color = AppColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            DifficultyBadge(prob.difficulty)
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        SectionHeader("Problem Statement")
        Text(
            text = "Time: ${prob.timeLimitMs}ms • Memory: ${prob.memoryLimitKb}KB",
            color = AppColors.TextMuted,
            fontSize = 12.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = prob.description,
            color = AppColors.TextSecondary,
            fontSize = 15.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (prob.inputFormat?.isNotEmpty() == true) {
            SectionHeader("Input Format")
            Text(prob.inputFormat ?: "", color = AppColors.TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        if (prob.outputFormat?.isNotEmpty() == true) {
            SectionHeader("Output Format")
            Text(prob.outputFormat ?: "", color = AppColors.TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        if (prob.sampleTestCases.isNotEmpty()) {
            SectionHeader("Example")
            prob.sampleTestCases.forEach { tc ->
                CodeBlock("Input:", tc.input)
                Spacer(modifier = Modifier.height(8.dp))
                CodeBlock("Output:", tc.expectedOutput)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun EditorPanel(
    problemId: Long,
    apiClient: ApiClient,
    sourceCode: String,
    onSourceCodeChange: (String) -> Unit,
    selectedLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    isSubmitting: Boolean,
    onSubmittingChange: (Boolean) -> Unit,
    submissionResult: SubmissionResponse?,
    onResultChange: (SubmissionResponse?) -> Unit,
    error: String?,
    onErrorChange: (String?) -> Unit,
    modifier: Modifier,
    scope: kotlinx.coroutines.CoroutineScope
) {
    Column(modifier = modifier) {
        // Language Selector + Submit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageSelector(
                selected = selectedLanguage,
                onSelect = onLanguageChange
            )
            
            Button(
                onClick = {
                    scope.launch {
                        onSubmittingChange(true)
                        onResultChange(null)
                        try {
                            val response = apiClient.submitCode(
                                SubmissionRequest(
                                    problemId = problemId,
                                    sourceCode = sourceCode,
                                    languageId = selectedLanguage.id
                                )
                            )
                            onResultChange(response)
                        } catch (e: Exception) {
                            onErrorChange(e.message)
                        }
                        onSubmittingChange(false)
                    }
                },
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = AppColors.Background,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("▶ Submit", color = AppColors.Background)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Code Editor
        OutlinedTextField(
            value = sourceCode,
            onValueChange = onSourceCodeChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedContainerColor = AppColors.SurfaceVariant,
                unfocusedContainerColor = AppColors.SurfaceVariant,
                focusedBorderColor = AppColors.Primary,
                unfocusedBorderColor = AppColors.CardBorder
            ),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        )
        
        // Submission Result
        submissionResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            SubmissionResultCard(result)
        }
        
        error?.let { err ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = err, color = AppColors.Error)
        }
    }
}


@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = AppColors.Primary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty) {
        "Easy" -> AppColors.EasyBadge
        "Medium" -> AppColors.MediumBadge
        else -> AppColors.HardBadge
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(difficulty.uppercase(), color = AppColors.Background, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CodeBlock(label: String, code: String) {
    Column {
        Text(label, color = AppColors.TextMuted, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(AppColors.SurfaceVariant)
                .padding(12.dp)
        ) {
            Text(
                text = code,
                color = AppColors.Primary,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSelector(selected: Language, onSelect: (Language) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected.displayName,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().width(160.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedContainerColor = AppColors.SurfaceVariant,
                unfocusedContainerColor = AppColors.SurfaceVariant,
                focusedBorderColor = AppColors.Primary,
                unfocusedBorderColor = AppColors.CardBorder
            )
        )
        
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Language.entries.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang.displayName) },
                    onClick = {
                        onSelect(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SubmissionResultCard(result: SubmissionResponse) {
    val verdictColor = when (result.verdict) {
        "ACCEPTED" -> AppColors.Success
        "WRONG_ANSWER" -> AppColors.Error
        "TIME_LIMIT_EXCEEDED", "MEMORY_LIMIT_EXCEEDED" -> AppColors.Warning
        "RUNTIME_ERROR", "COMPILATION_ERROR", "INTERNAL_ERROR" -> AppColors.Error
        else -> AppColors.TextMuted
    }
    
    val formattedVerdict = result.verdict.replace("_", " ")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, verdictColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .background(verdictColor.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(verdictColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(formattedVerdict, color = AppColors.Background, fontWeight = FontWeight.Bold)
            }
            Text(
                text = "Time: ${result.executionTimeMs?.toInt() ?: 0}ms • Memory: ${result.memoryUsedKb ?: 0}KB",
                color = AppColors.TextSecondary,
                fontSize = 14.sp
            )
        }
        
        // Compile Error
        if (result.compileOutput != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Compilation Error", color = AppColors.Error, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.Background)
                    .padding(8.dp)
            ) {
                Text(
                    text = (result.compileOutput),
                    color = AppColors.Error,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }
        
        // Runtime Error
        if (result.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Error", color = AppColors.Error, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.Background)
                    .padding(8.dp)
            ) {
                Text(
                    text = (result.errorMessage),
                    color = AppColors.Error,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }
        
        // Test Cases
        if (result.testCaseResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Test Cases", color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            
            result.testCaseResults.forEach { tc ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusColor = if (tc.passed) AppColors.Success else AppColors.Error
                    Text(
                        text = if (tc.passed) "✓" else "✗",
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Test Case ${tc.testCaseNumber}${if (tc.hidden) " (Hidden)" else ""}",
                            color = AppColors.TextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Time: ${tc.executionTimeMs?.toInt() ?: 0}ms • Memory: ${tc.memoryUsedKb ?: 0}KB",
                            color = AppColors.TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
