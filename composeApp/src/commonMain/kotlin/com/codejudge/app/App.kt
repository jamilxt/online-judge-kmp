package com.codejudge.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.codejudge.app.ui.screens.ProblemListScreen
import com.codejudge.app.ui.theme.CodeJudgeTheme

@Composable
fun App() {
    CodeJudgeTheme {
        Navigator(ProblemListScreen())
    }
}
