package com.example.wishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.example.wishlistapp.Data.Wish
import kotlinx.coroutines.launch


@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val snackMessage = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (id != 0L) {
        val wish = viewModel.getAWishByID(id)
            .collectAsState(initial = Wish(0L, "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = { AppBarView(title =
        if (id != 0L) { stringResource(id = R.string.update_wish) }
        else { stringResource(id = R.string.add_wish) }
        ) {
            navController.navigateUp()
        }
        }
    ) {
        Column(modifier = Modifier.padding(it)
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {
            Spacer(modifier = Modifier.height(10.dp))

            WishTextField(label = "Title",
                value = viewModel.wishTitleState,
                onValueChanged = { viewModel.onWishTitleChanged(it) })

            Spacer(modifier = Modifier.height(10.dp))

            WishTextField(label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChanged = { viewModel.onWishDescriptionChanged(it) })

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (viewModel.wishTitleState.isNotEmpty() &&
                    viewModel.wishDescriptionState.isNotEmpty()) {
                    if (id != 0L) {
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel
                                    .wishTitleState.trim(),
                                description = viewModel
                                    .wishDescriptionState.trim()
                            )
                        )
                    } else {
                        viewModel.addWish(
                            Wish(title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim())
                        )
                        snackMessage.value = "Wish has been created"
                    }
                } else {
                    snackMessage.value = "Enter fields to create a wish"
                }
                keyboardController?.hide()
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(snackMessage.value)
                    if (viewModel.wishTitleState.isNotEmpty() &&
                        viewModel.wishDescriptionState.isNotEmpty()) {
                        navController.navigateUp()
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFFDD1E5F))
                ) {
                Text(text =
                if (id != 0L) {stringResource(id = R.string.update_wish)}
                else {stringResource(id = R.string.add_wish)},
                color = Color.White)
            }
        }
    }
}


@Composable
fun WishTextField(label: String, value: String,
                  onValueChanged: (String) -> Unit
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChanged,
        label = { Text(label, color = Color.Black) },
        modifier = Modifier.fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = Color.Blue
        ))
}


@Preview(showBackground = true)
@Composable
fun AddEditDetailPreview() {
    WishTextField(label = "text",
        value = "text",
        onValueChanged = {})
}
