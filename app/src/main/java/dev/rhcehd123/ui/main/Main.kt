package dev.rhcehd123.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.ListItemPicker

@Composable
fun Main(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Main(
        modifier = modifier,
        uiState = uiState,
        onChangeRemittance = { viewModel.setRemittance(it) },
        onChangeRecipientCountry = {viewModel.setRecipientCountry(it) },
        recipientList = viewModel.recipientList
    )
}

@Composable
fun Main(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onChangeRemittance: (String) -> Unit,
    onChangeRecipientCountry: (String) -> Unit,
    recipientList: List<String>
) {
    Column {
        MainTitle()
        CurrencyInfo(
            uiState = uiState,
            onChangeRemittance = onChangeRemittance,
            onChangeRecipientCountry = onChangeRecipientCountry,
            recipientList = recipientList
        )
    }
}

@Composable
fun MainTitle() {
    Text(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 32.dp),
        text = "환율 계산",
        fontSize = TextUnit(50f, TextUnitType.Sp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun CurrencyInfo(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onChangeRecipientCountry: (String) -> Unit,
    onChangeRemittance: (String) -> Unit,
    recipientList: List<String>
) {
    Column {
        ContentLine(
            contentTitle = "송금국가",
            content = uiState.remittanceCountry
        )
        ContentLine(
            contentTitle = "수취국가",
            content = uiState.recipientCountry
        )
        ContentLine(
            contentTitle = "환율",
            content = uiState.currencyRate
        )
        ContentLine(
            contentTitle = "조회시간",
            content = uiState.lookupTime
        )
        ContentLine(
            contentTitle = "송금액",
            contentSuffix = " USD",
            customContentEnabled = true
        ) {
            val focusManager = LocalFocusManager.current
            BasicTextField(
                modifier = Modifier.width(60.dp),
                value = uiState.remittance,
                onValueChange = onChangeRemittance,
                textStyle = TextStyle(textAlign = TextAlign.End),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 56.dp, bottom = 120.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "수취금액은 "
            )
            Text(
                uiState.recipient
            )
            Text(
                text = " 입니다."
            )
        }
        ListItemPicker(
            modifier = Modifier.fillMaxWidth()
                .semantics { contentDescription = "listItemPicker" },
            value = uiState.recipientCountry,
            onValueChange = onChangeRecipientCountry,
            list = recipientList,
            dividersColor = Color.Gray
        )
    }
}

@Composable
fun ContentLine(
    contentTitle: String = "",
    content: String = "",
    contentSuffix: String = "",
    customContentEnabled: Boolean = false,
    customContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            modifier = Modifier.width(120.dp),
            text = "$contentTitle : ",
            textAlign = TextAlign.End
        )
        if(customContentEnabled) {
            customContent()
        } else {
            Text(
                text = content
            )
        }
        if(contentSuffix.isNotEmpty()) {
            Text(
                text = contentSuffix
            )
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    val uiState = MainUiState(
        "미국(USD)",
        "한국(KRW)",
        "",
        "0 KRW / USD",
        "0.00 KRW",
        "1999-09-09 09:09"
    )
    val emptyOnValueChange = { _: String -> }
    val emptyList = listOf("a", "b", "c")

    Main(
        uiState = uiState,
        onChangeRemittance = emptyOnValueChange,
        onChangeRecipientCountry = emptyOnValueChange,
        recipientList = emptyList
    )
}

