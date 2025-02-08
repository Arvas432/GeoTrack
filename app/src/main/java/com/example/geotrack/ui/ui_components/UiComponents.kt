package com.example.geotrack.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import com.example.geotrack.BottomNavigationItem
import com.example.geotrack.ui.PlaceholderTransformation
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.Black22
import com.example.geotrack.ui.theme.BlueE7
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.theme.GrayEB

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {
        BottomNavigationItem().bottomNavigationItems()
            .forEachIndexed { index, bottomNavigationItem ->
                NavigationBarItem(
                    selected = index == navigationSelectedItem,
                    colors = NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedIconColor = GrayB4,
                        unselectedTextColor = GrayB4,
                        disabledIconColor = GrayEB,
                        disabledTextColor = GrayEB
                    ),
                    label = {
                        Text(
                            bottomNavigationItem.label,
                            fontFamily = robotoFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(
                            bottomNavigationItem.icon,
                            contentDescription = bottomNavigationItem.label,
                            modifier = Modifier.height(32.dp)
                        )
                    },
                    onClick = {
                        navigationSelectedItem = index
                        navController.navigate(bottomNavigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
    }
}


@Composable
fun ScreenHeader(headerText: String, modifier: Modifier = Modifier) {
    Text(
        text = headerText,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 22.sp,
        modifier = modifier.padding(vertical = 19.dp)
    )
}

@Composable
fun HintedTextField(
    text: String,
    hintText: String,
    topHint: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val localTextColor = if (text.isEmpty())
        MaterialTheme.colorScheme.onPrimary.copy(ContentAlpha.medium)
    else
        LocalContentColor.current.copy(LocalContentAlpha.current)

    val textStyle = if (text.isEmpty())
        LocalTextStyle.current.merge(
            color = Black22,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Normal
        )
    else
        LocalTextStyle.current

    OutlinedTextField(
        value = text,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = BlueE7,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            focusedLabelColor = BlueE7,
            unfocusedLabelColor = GrayB4,
            unfocusedTextColor = localTextColor,
        ),
        textStyle = textStyle,
        visualTransformation = if (text.isEmpty())
            PlaceholderTransformation(hintText)
        else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                topHint,
                fontFamily = robotoFamily,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .offset(y = (0).dp)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = onTextChanged,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
            .height(65.dp)
    )
}

@Composable
fun DefaultButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = GrayB4
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp), onClick = onClick
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}