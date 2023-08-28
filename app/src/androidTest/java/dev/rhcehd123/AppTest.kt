package dev.rhcehd123

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.rhcehd123.ui.MainActivity
import dev.rhcehd123.ui.main.Main
import dev.rhcehd123.ui.theme.CurrencyInfoTheme
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_launch() {
        /*composeTestRule.setContent {
            CurrencyInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Main()
                }
            }
        }*/
    }
}