package dev.rhcehd123

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.rhcehd123.ui.main.CurrencyInfo
import dev.rhcehd123.ui.main.MainUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun main_changeRecipient() {
        composeTestRule.setContent {
            val uiState = MainUiState()
            val recipientList = listOf("a", "b", "c")
            CurrencyInfo(
                uiState = uiState,
                onChangeRecipientCountry = {},
                onChangeRemittance = {},
                recipientList = recipientList
            )
        }
        //delay(2000)
        composeTestRule.onAllNodesWithContentDescription("listItemPicker")[1].performTouchInput {
            this.swipeUp()
        }
        //delay(2000)
        Thread.sleep(4000)
    }

    @Test
    fun main_changeRemittance() {
        composeTestRule.setContent {
            val uiState = MainUiState()
            val recipientList = listOf("a", "b", "c")
            CurrencyInfo(
                uiState = uiState,
                onChangeRecipientCountry = {},
                onChangeRemittance = {},
                recipientList = recipientList
            )
        }
    }

    @Test
    fun scrollTest() {
        composeTestRule.setContent {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) {
                    Text(text = "$it")
                }
            }
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToIndex(50)
        Thread.sleep(4000)
    }
}